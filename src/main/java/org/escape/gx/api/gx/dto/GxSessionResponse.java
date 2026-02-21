package org.escape.gx.api.gx.dto;

import java.time.Instant;
import java.time.LocalDateTime;

/**
 * GX 세션 응답 (목록/상세).
 *
 * @author gx
 * @since 1.0
 */
public record GxSessionResponse(
        String gxSessionId,
        String gxClassInfoId,
        String sessionName,
        LocalDateTime sessionStartAt,
        LocalDateTime sessionEndAt,
        Integer maxCapacity,
        Integer reservedCount,
        Integer requiredMembershipCount,
        String status
) {
}
