package org.escape.gx.service;

import org.escape.gx.common.enums.ClassStatus;
import org.escape.gx.common.enums.ReservationStatus;
import org.escape.gx.domain.gx.GxClassInfo;
import org.escape.gx.domain.gx.GxSession;
import org.escape.gx.domain.gx.Reservation;
import org.escape.gx.domain.membership.Membership;
import org.escape.gx.domain.gx.GxClassInfoRepository;
import org.escape.gx.domain.gx.GxSessionRepository;
import org.escape.gx.domain.gx.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * GX 예약 서비스. 예약·취소 시 회원권 차감·원복 및 원장 기록.
 *
 * @author gx
 * @since 1.0
 */
@Service
public class ReservationService {

    private final GxSessionRepository sessionRepository;
    private final GxClassInfoRepository classInfoRepository;
    private final ReservationRepository reservationRepository;
    private final MembershipService membershipService;

    public ReservationService(GxSessionRepository sessionRepository,
                               GxClassInfoRepository classInfoRepository,
                               ReservationRepository reservationRepository,
                               MembershipService membershipService) {
        this.sessionRepository = sessionRepository;
        this.classInfoRepository = classInfoRepository;
        this.reservationRepository = reservationRepository;
        this.membershipService = membershipService;
    }

    /**
     * 예약 가능한(시작 전) 세션 목록 조회.
     */
    @Transactional(readOnly = true)
    public List<GxSession> findUpcomingSessions() {
        return sessionRepository.findUpcomingWithClassInfo(LocalDateTime.now());
    }

    /**
     * 세션 예약. 회원권 차감 후 예약 생성.
     *
     * @param userId     사용자 ID
     * @param gxSessionId 세션 ID
     * @return 생성된 예약. 회원권 부족 또는 정원 마감 시 null
     */
    @Transactional
    public Reservation reserve(String userId, String gxSessionId) {
        GxSession session = sessionRepository.findByIdForUpdate(gxSessionId).orElse(null);
        if (session == null) {
            throw new IllegalArgumentException("세션을 찾을 수 없습니다.");
        }
        if (!session.canReserve(null)) {
            throw new IllegalStateException("예약 가능한 상태가 아닙니다.");
        }
        GxClassInfo info = classInfoRepository.findById(session.getGxClassInfoId()).orElse(null);
        int requiredCount = info != null ? info.getRequiredCount() : 1;
        int maxCapacity = info != null ? info.getMaxCapacity() : 20;
        if (session.getReservedCount() != null && session.getReservedCount() >= maxCapacity) {
            throw new IllegalStateException("정원이 마감되었습니다.");
        }
        if (reservationRepository.existsByUserIdAndGxSessionIdAndStatus(userId, gxSessionId, ReservationStatus.RESERVED)) {
            throw new IllegalStateException("이미 예약한 세션입니다.");
        }
        Membership membership = membershipService.deductForReservation(userId, requiredCount, gxSessionId, "GX 예약");
        if (membership == null) {
            throw new IllegalStateException("사용 가능한 회원권이 없습니다. 잔여 횟수와 유효기간을 확인해 주세요.");
        }
        session.incrementReservedCount();
        if (session.getReservedCount() >= maxCapacity) {
            session.setStatus(ClassStatus.RESV_FULL);
        } else if (session.getStatus() == ClassStatus.BEFORE_RESV) {
            session.setStatus(ClassStatus.ON_RESV);
        }
        sessionRepository.save(session);
        Reservation reservation = Reservation.builder()
                .reservationId(UUID.randomUUID().toString())
                .userId(userId)
                .gxSessionId(gxSessionId)
                .membershipId(membership.getMembershipId())
                .deductCount(requiredCount)
                .build();
        return reservationRepository.save(reservation);
    }

    /**
     * 예약 취소. 회원권 복구 후 예약 상태 취소.
     *
     * @param userId        사용자 ID
     * @param reservationId 예약 ID
     */
    @Transactional
    public void cancel(String userId, String reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));
        if (!reservation.getUserId().equals(userId)) {
            throw new IllegalArgumentException("본인 예약만 취소할 수 있습니다.");
        }
        if (!reservation.isReserved()) {
            throw new IllegalStateException("이미 취소되었거나 완료된 예약입니다.");
        }
        reservation.cancel();
        reservationRepository.save(reservation);
        membershipService.refundForCancellation(
                reservation.getMembershipId(),
                userId,
                reservation.getDeductCount() != null ? reservation.getDeductCount() : 1,
                reservation.getGxSessionId(),
                "GX 예약 취소"
        );
        GxSession session = sessionRepository.findByIdForUpdate(reservation.getGxSessionId()).orElse(null);
        if (session != null) {
            session.decrementReservedCount();
            if (session.getStatus() == ClassStatus.RESV_FULL) {
                session.setStatus(ClassStatus.ON_RESV);
            }
            sessionRepository.save(session);
        }
    }

    /**
     * 사용자 예약 목록 조회.
     */
    @Transactional(readOnly = true)
    public List<Reservation> findByUserId(String userId) {
        return reservationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    /**
     * 사용자 예약 목록 (예약됨만).
     */
    @Transactional(readOnly = true)
    public List<Reservation> findActiveByUserId(String userId) {
        return reservationRepository.findByUserIdAndStatus(userId, ReservationStatus.RESERVED);
    }
}
