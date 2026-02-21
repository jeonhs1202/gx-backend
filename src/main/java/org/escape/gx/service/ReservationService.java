package org.escape.gx.service;

import org.escape.gx.api.gx.dto.GxClassRequest;
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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
        LocalDateTime now = LocalDateTime.now();
        return sessionRepository.findUpcomingWithClassInfo(now, now.plusDays(7));
    }

    /**
     * 세션 예약. 회원권 차감 후 예약 생성.
     *
     * @param userId      사용자 ID
     * @param gxSessionId 세션 ID
     * @return 생성된 예약. 회원권 부족 또는 정원 마감 시 예외 발생
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
     * 사용자 예약 목록 조회 (세션·강의 정보 포함).
     *
     * @param userId 사용자 ID
     */
    @Transactional(readOnly = true)
    public List<Reservation> findByUserId(String userId) {
        return reservationRepository.findByUserIdWithDetails(userId);
    }

    /**
     * 사용자 예약 목록 (예약됨만).
     */
    @Transactional(readOnly = true)
    public List<Reservation> findActiveByUserId(String userId) {
        return reservationRepository.findByUserIdAndStatus(userId, ReservationStatus.RESERVED);
    }

    // ─── 강의 관리 ──────────────────────────────────────────────────────────────

    /**
     * 전체 강의 목록 조회.
     */
    @Transactional(readOnly = true)
    public List<GxClassInfo> findAllClasses() {
        return classInfoRepository.findAllByOrderBySessionStartAtAsc();
    }

    /**
     * 강사의 강의 목록 조회.
     *
     * @param instructorUserId 강사 사용자 ID
     */
    @Transactional(readOnly = true)
    public List<GxClassInfo> findClassesByInstructor(String instructorUserId) {
        return classInfoRepository.findByInstructorUserIdOrderBySessionStartAtAsc(instructorUserId);
    }

    /**
     * 강의 등록 및 주간 세션 자동 생성.
     *
     * @param instructorUserId 강사 사용자 ID
     * @param request          강의 등록 요청
     * @return 생성된 강의 기준정보
     */
    @Transactional
    public GxClassInfo createClass(String instructorUserId, GxClassRequest request) {
        LocalTime startTime = LocalTime.parse(request.startTime());
        LocalTime endTime = LocalTime.parse(request.endTime());
        LocalDateTime periodStart = LocalDateTime.of(request.startDate(), startTime);
        LocalDateTime periodEnd = LocalDateTime.of(request.endDate(), endTime);

        String classInfoId = UUID.randomUUID().toString();
        GxClassInfo classInfo = GxClassInfo.builder()
                .gxClassInfoId(classInfoId)
                .sessionName(request.sessionName())
                .instructorUserId(instructorUserId)
                .sessionStartAt(periodStart)
                .sessionEndAt(periodEnd)
                .maxCapacity(request.maxCapacity())
                .requiredMembershipCount(request.requiredMembershipCount())
                .dayOfWeek(request.dayOfWeek())
                .build();
        classInfoRepository.save(classInfo);
        generateWeeklySessions(classInfo);
        return classInfo;
    }

    /**
     * 강의 수정.
     *
     * @param classInfoId      강의 기준정보 ID
     * @param instructorUserId 요청 강사 ID (본인 강의인지 검증)
     * @param request          수정 요청
     * @return 수정된 강의 기준정보
     */
    @Transactional
    public GxClassInfo updateClass(String classInfoId, String instructorUserId, GxClassRequest request) {
        GxClassInfo classInfo = classInfoRepository.findById(classInfoId)
                .orElseThrow(() -> new IllegalArgumentException("강의를 찾을 수 없습니다."));
        if (!classInfo.getInstructorUserId().equals(instructorUserId)) {
            throw new IllegalArgumentException("본인의 강의만 수정할 수 있습니다.");
        }
        LocalTime startTime = LocalTime.parse(request.startTime());
        LocalTime endTime = LocalTime.parse(request.endTime());
        classInfo.update(
                request.sessionName(),
                LocalDateTime.of(request.startDate(), startTime),
                LocalDateTime.of(request.endDate(), endTime),
                request.maxCapacity(),
                request.dayOfWeek()
        );
        return classInfoRepository.save(classInfo);
    }

    /**
     * 강의에 속한 세션 수 조회.
     *
     * @param classInfoId 강의 기준정보 ID
     */
    @Transactional(readOnly = true)
    public int countSessions(String classInfoId) {
        return sessionRepository.findByGxClassInfoIdOrderByCreatedAtAsc(classInfoId).size();
    }

    /**
     * 강의 기간·요일에 따라 주간 세션을 자동 생성한다.
     *
     * @param classInfo 강의 기준정보
     */
    private void generateWeeklySessions(GxClassInfo classInfo) {
        LocalDate startDate = classInfo.getSessionStartAt().toLocalDate();
        LocalDate endDate = classInfo.getSessionEndAt().toLocalDate();
        LocalTime classTime = classInfo.getSessionStartAt().toLocalTime();
        DayOfWeek targetDay = DayOfWeek.of(classInfo.getDayOfWeek());

        LocalDate current = startDate;
        while (current.getDayOfWeek() != targetDay) {
            current = current.plusDays(1);
        }
        if (current.isAfter(endDate)) {
            return;
        }

        GxSession session = GxSession.builder()
                .gxSessionId(UUID.randomUUID().toString())
                .gxClassInfoId(classInfo.getGxClassInfoId())
                .sessionStartAt(LocalDateTime.of(current, classTime))
                .build();
        sessionRepository.save(session);
    }
}
