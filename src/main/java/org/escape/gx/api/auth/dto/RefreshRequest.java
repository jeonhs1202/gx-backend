package org.escape.gx.api.auth.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * 리프레시 토큰 갱신 요청.
 *
 * @author gx
 * @since 1.0
 */
public record RefreshRequest(
        @NotBlank String refreshToken
) {
}
