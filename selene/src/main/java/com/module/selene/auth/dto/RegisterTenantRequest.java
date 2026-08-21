package com.module.selene.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterTenantRequest(

        @NotBlank
        String tenantName,

        @NotBlank
        String tenantSlug,

        @NotBlank
        String fullName,

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Size(min = 8, message = "El password debe tener al menos 8 caracteres")
        String password
) {
}
