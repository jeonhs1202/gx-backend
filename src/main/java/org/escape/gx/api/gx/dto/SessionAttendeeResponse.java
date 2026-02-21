package org.escape.gx.api.gx.dto;

import java.time.Instant;

/**
 * 세션 예약자·대기자 응답.
 *
 * @author gx
 * @since 1.0
 */
public record SessionAttendeeResponse(
        String userId,
        String name,
        String status,
        Instant createdAt
) {
}
