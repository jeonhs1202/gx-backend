package org.escape.gx.api.gx.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * 강의 등록/수정 요청.
 *
 * @author gx
 * @since 1.0
 */
public record GxClassRequest(
        @NotBlank
        String sessionName,
        @NotNull
        LocalDate startDate,
        @NotNull
        LocalDate endDate,
        /** 강의 시작 시각 (HH:mm) */
        @NotBlank
        String startTime,
        /** 강의 종료 시각 (HH:mm) */
        @NotBlank
        String endTime,
        /** 요일 (1=월 ~ 7=일) */
        @NotNull @Min(1) @Max(7)
        Integer dayOfWeek,
        Integer maxCapacity,
        Integer requiredMembershipCount
) {
}
