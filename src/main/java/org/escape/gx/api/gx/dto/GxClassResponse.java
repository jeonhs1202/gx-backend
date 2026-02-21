package org.escape.gx.api.gx.dto;

import java.time.LocalDate;

/**
 * 강의 응답.
 *
 * @author gx
 * @since 1.0
 */
public record GxClassResponse(
        String gxClassInfoId,
        String sessionName,
        String instructorUserId,
        LocalDate startDate,
        LocalDate endDate,
        String startTime,
        String endTime,
        Integer dayOfWeek,
        String dayOfWeekName,
        Integer maxCapacity,
        Integer requiredMembershipCount,
        int sessionCount
) {
}
