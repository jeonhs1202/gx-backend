package org.escape.gx.common.enums;

/**
 * GX 클래스 세션 상태.
 *
 * @author gx
 * @since 1.0
 */
public enum ClassStatus {
    /** 예약 시작 전 */
    BEFORE_RESV,
    /** 예약 진행 중 */
    ON_RESV,
    /** 정원 마감 */
    RESV_FULL,
    /** 수업 중 */
    IN_CLASS,
    /** 종료(출석 체크 완료) */
    TERMINATED
}
