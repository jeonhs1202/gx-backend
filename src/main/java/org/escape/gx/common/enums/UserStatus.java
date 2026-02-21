package org.escape.gx.common.enums;

/**
 * 회원 상태.
 *
 * @author gx
 * @since 1.0
 */
public enum UserStatus {
    /** 활성화 */
    ACTIVE,
    /** 90일 이상 미접속 휴면계정 */
    INACTIVE,
    /** 비밀번호 5회 연속 틀림 */
    LOCK,
    /** 탈퇴 */
    WITHDRAWN,
    /** 패널티 적용(정지) */
    SUSPENDED
}
