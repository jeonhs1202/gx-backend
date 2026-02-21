package org.escape.gx.api.auth.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * 로그인 요청.
 *
 * @author gx
 * @since 1.0
 */
public record LoginRequest(
        @NotBlank String email,
        @NotBlank String password
) {
}
