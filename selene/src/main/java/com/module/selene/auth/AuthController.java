package com.module.selene.auth;

import com.module.selene.auth.dto.RegisterTenantRequest;
import com.module.selene.auth.dto.RegisterTenantResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register-tenant")
    public ResponseEntity<RegisterTenantResponse> registerTenant(
            @Valid @RequestBody RegisterTenantRequest request) {
        RegisterTenantResponse response = authService.registerTenant(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
