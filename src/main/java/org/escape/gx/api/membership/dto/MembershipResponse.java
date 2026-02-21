package org.escape.gx.api.membership.dto;

import java.time.Instant;
import java.time.LocalDate;

/**
 * 회원권 응답.
 *
 * @author gx
 * @since 1.0
 */
public record MembershipResponse(
        String membershipId,
        String userId,
        String status,
        LocalDate startDate,
        LocalDate endDate,
        Integer remainingCount,
        Integer initialCount,
        Instant issuedAt
) {
}
