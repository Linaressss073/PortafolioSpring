package com.module.selene.auth.dto;

import java.util.UUID;

public record RegisterTenantResponse(
        String token,
        UUID tenantId,
        UUID userId,
        String email,
        String role
) {
}
