package org.escape.gx.api.gx;

import org.escape.gx.api.gx.dto.GxSessionResponse;
import org.escape.gx.api.gx.dto.ReservationResponse;
import org.escape.gx.api.gx.dto.ReserveRequest;
import org.escape.gx.common.enums.ClassStatus;
import org.escape.gx.domain.gx.GxClassInfo;
import org.escape.gx.domain.gx.GxSession;
import org.escape.gx.domain.gx.Reservation;
import org.escape.gx.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.List;

/**
 * GX 세션 및 예약 API.
 *
 * @author gx
 * @since 1.0
 */
@RestController
@RequestMapping("/api/gx")
public class GxController {

    private final ReservationService reservationService;

    public GxController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    /**
     * 예약 가능한(시작 전) GX 세션 목록.
     */
    @GetMapping("/sessions/upcoming")
    public ResponseEntity<List<GxSessionResponse>> upcomingSessions() {
        List<GxSession> sessions = reservationService.findUpcomingSessions();
        List<GxSessionResponse> list = sessions.stream()
                .map(this::toSessionResponse)
                .toList();
        return ResponseEntity.ok(list);
    }

    /**
     * 내 예약 목록 (최신순).
     */
    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationResponse>> myReservations(Authentication auth) {
        String userId = (String) auth.getPrincipal();
        List<Reservation> reservations = reservationService.findByUserId(userId);
        List<ReservationResponse> list = reservations.stream()
                .map(this::toReservationResponse)
                .toList();
        return ResponseEntity.ok(list);
    }

    /**
     * GX 예약.
     */
    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> reserve(Authentication auth,
                                                         @Valid @RequestBody ReserveRequest request) {
        String userId = (String) auth.getPrincipal();
        Reservation reservation = reservationService.reserve(userId, request.gxSessionId());
        return ResponseEntity.ok(toReservationResponse(reservation));
    }

    /**
     * 예약 취소.
     */
    @PostMapping("/reservations/{reservationId}/cancel")
    public ResponseEntity<Void> cancel(Authentication auth, @PathVariable String reservationId) {
        String userId = (String) auth.getPrincipal();
        reservationService.cancel(userId, reservationId);
        return ResponseEntity.ok().build();
    }

    private GxSessionResponse toSessionResponse(GxSession s) {
        GxClassInfo info = s.getGxClassInfo();
        String name = info != null ? info.getSessionName() : null;
        // 세션에 직접 날짜가 있으면 우선 사용 (반복 강의), 없으면 기준정보 사용
        var start = s.getSessionStartAt() != null ? s.getSessionStartAt()
                : (info != null ? info.getSessionStartAt() : null);
        var end = info != null ? info.getSessionEndAt() : null;
        Integer max = info != null ? info.getMaxCapacity() : null;
        Integer required = info != null ? info.getRequiredMembershipCount() : null;
        ClassStatus status = s.getStatus();
        String instructorUserId = info != null ? info.getInstructorUserId() : null;
        return new GxSessionResponse(
                s.getGxSessionId(),
                s.getGxClassInfoId(),
                name,
                start,
                end,
                max,
                s.getReservedCount(),
                required,
                status != null ? status.name() : null,
                instructorUserId
        );
    }

    private ReservationResponse toReservationResponse(Reservation r) {
        GxSession session = r.getGxSession();
        String sessionName = null;
        java.time.LocalDateTime sessionStartAt = null;
        if (session != null) {
            sessionStartAt = session.getSessionStartAt();
            GxClassInfo info = session.getGxClassInfo();
            if (info != null) {
                sessionName = info.getSessionName();
                if (sessionStartAt == null) sessionStartAt = info.getSessionStartAt();
            }
        }
        return new ReservationResponse(
                r.getReservationId(),
                r.getUserId(),
                r.getGxSessionId(),
                sessionName,
                sessionStartAt,
                r.getStatus() != null ? r.getStatus().name() : null,
                r.getDeductCount(),
                r.getCreatedAt()
        );
    }
}
