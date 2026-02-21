package org.escape.gx.api.auth.dto;

/**
 * 로그인/토큰 갱신 응답.
 *
 * @author gx
 * @since 1.0
 */
public record TokenResponse(
        String accessToken,
        String refreshToken,
        String userId,
        String email,
        String accountCode,
        String name
) {
}
