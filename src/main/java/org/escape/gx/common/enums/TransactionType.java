package org.escape.gx.common.enums;

/**
 * 회원권 원장 거래 유형.
 *
 * @author gx
 * @since 1.0
 */
public enum TransactionType {
    /** 회원권 발급 */
    GRANT,
    /** GX 예약 차감 */
    DEDUCT,
    /** 예약 취소 복구 */
    REFUND,
    /** 보너스 지급 */
    BONUS,
    /** 노쇼 패널티 차감 */
    PENALTY_DEDUCT,
    /** 관리자 증가 */
    ADMIN_INCREASE,
    /** 관리자 감소 */
    ADMIN_DECREASE,
    /** 유효기간 만료 소멸 */
    EXPIRE,
    /** 정합성 오류 수정 */
    ADJUST
}
