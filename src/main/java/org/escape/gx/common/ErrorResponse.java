package org.escape.gx.common;

/**
 * API 오류 응답.
 *
 * @author gx
 * @since 1.0
 */
public record ErrorResponse(
        String message,
        String code
) {
}
