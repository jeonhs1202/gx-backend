package org.escape.gx.api.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 회원가입 요청.
 *
 * @author gx
 * @since 1.0
 */
public record RegisterRequest(
        @NotBlank @Email
        String email,
        @NotBlank @Size(min = 8)
        String password,
        String name,
        String phone
) {
}
