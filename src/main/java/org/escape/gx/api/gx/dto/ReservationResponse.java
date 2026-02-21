package org.escape.gx.api.gx.dto;

import java.time.Instant;

/**
 * 예약 응답.
 *
 * @author gx
 * @since 1.0
 */
public record ReservationResponse(
        String reservationId,
        String userId,
        String gxSessionId,
        String sessionName,
        java.time.LocalDateTime sessionStartAt,
        String status,
        Integer deductCount,
        Instant createdAt
) {
}
