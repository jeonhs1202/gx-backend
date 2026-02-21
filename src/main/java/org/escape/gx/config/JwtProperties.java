package org.escape.gx.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * JWT 설정 속성. validity 값은 밀리초 단위.
 *
 * @author gx
 * @since 1.0
 */
@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(
        String secret,
        long accessTokenValidity,
        long refreshTokenValidity
) {
}
