package org.escape.gx.api.gx.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * GX 예약 요청.
 *
 * @author gx
 * @since 1.0
 */
public record ReserveRequest(
        @NotBlank String gxSessionId
) {
}
