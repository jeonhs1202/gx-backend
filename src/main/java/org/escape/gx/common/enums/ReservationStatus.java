package org.escape.gx.common.enums;

/**
 * 예약 상태.
 *
 * @author gx
 * @since 1.0
 */
public enum ReservationStatus {
    /** 예약됨 */
    RESERVED,
    /** 대기 중 (정원 마감 후 신청) */
    WAITING,
    /** 출석 완료 */
    COMPLETED,
    /** 취소됨 */
    CANCELLED
}
