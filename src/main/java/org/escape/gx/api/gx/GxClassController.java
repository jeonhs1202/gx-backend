package org.escape.gx.api.gx;

import jakarta.validation.Valid;
import org.escape.gx.api.gx.dto.GxClassRequest;
import org.escape.gx.api.gx.dto.GxClassResponse;
import org.escape.gx.domain.gx.GxClassInfo;
import org.escape.gx.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * 강의 기준정보 관리 API. 강사 전용 등록·수정, 전체 목록 조회.
 *
 * @author gx
 * @since 1.0
 */
@RestController
@RequestMapping("/api/gx/classes")
public class GxClassController {

    private static final String[] DAY_NAMES = {"", "월", "화", "수", "목", "금", "토", "일"};

    private final ReservationService reservationService;

    public GxClassController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    /**
     * 전체 강의 목록 조회.
     */
    @GetMapping
    public ResponseEntity<List<GxClassResponse>> listClasses() {
        List<GxClassInfo> classes = reservationService.findAllClasses();
        return ResponseEntity.ok(classes.stream().map(this::toResponse).toList());
    }

    /**
     * 내 강의 목록 조회 (강사용).
     */
    @GetMapping("/mine")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<List<GxClassResponse>> myClasses(Authentication auth) {
        String userId = (String) auth.getPrincipal();
        List<GxClassInfo> classes = reservationService.findClassesByInstructor(userId);
        return ResponseEntity.ok(classes.stream().map(this::toResponse).toList());
    }

    /**
     * 강의 등록 (강사 전용). 기간·요일에 따라 주간 세션 자동 생성.
     *
     * @param auth    인증 정보
     * @param request 강의 등록 요청
     * @return 생성된 강의 응답
     */
    @PostMapping
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<GxClassResponse> createClass(Authentication auth,
                                                        @Valid @RequestBody GxClassRequest request) {
        String userId = (String) auth.getPrincipal();
        GxClassInfo classInfo = reservationService.createClass(userId, request);
        return ResponseEntity.ok(toResponse(classInfo));
    }

    /**
     * 강의 수정 (강사 전용, 본인 강의만).
     *
     * @param auth    인증 정보
     * @param id      강의 기준정보 ID
     * @param request 수정 요청
     * @return 수정된 강의 응답
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<GxClassResponse> updateClass(Authentication auth,
                                                        @PathVariable String id,
                                                        @Valid @RequestBody GxClassRequest request) {
        String userId = (String) auth.getPrincipal();
        GxClassInfo classInfo = reservationService.updateClass(id, userId, request);
        return ResponseEntity.ok(toResponse(classInfo));
    }

    private GxClassResponse toResponse(GxClassInfo info) {
        Integer dow = info.getDayOfWeek();
        String dayName = (dow != null && dow >= 1 && dow <= 7) ? DAY_NAMES[dow] : "";
        LocalDate startDate = info.getSessionStartAt() != null ? info.getSessionStartAt().toLocalDate() : null;
        LocalDate endDate = info.getSessionEndAt() != null ? info.getSessionEndAt().toLocalDate() : null;
        String startTime = info.getSessionStartAt() != null
                ? info.getSessionStartAt().toLocalTime().toString() : null;
        String endTime = info.getSessionEndAt() != null
                ? info.getSessionEndAt().toLocalTime().toString() : null;
        int sessionCount = reservationService.countSessions(info.getGxClassInfoId());
        return new GxClassResponse(
                info.getGxClassInfoId(),
                info.getSessionName(),
                info.getInstructorUserId(),
                startDate,
                endDate,
                startTime,
                endTime,
                dow,
                dayName,
                info.getMaxCapacity(),
                info.getRequiredMembershipCount(),
                sessionCount
        );
    }
}
