package com.module.selene.auth;

import com.module.selene.auth.dto.RegisterTenantRequest;
import com.module.selene.auth.dto.RegisterTenantResponse;
import com.module.selene.shared.security.JwtService;
import com.module.selene.shared.security.TenantContext;
import com.module.selene.tenant.Tenant;
import com.module.selene.tenant.TenantRepository;
import com.module.selene.user.Role;
import com.module.selene.user.User;
import com.module.selene.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public RegisterTenantResponse registerTenant(RegisterTenantRequest request) {
        if (tenantRepository.existsBySlug(request.tenantSlug())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un tenant con ese slug");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un usuario con ese email");
        }

        Tenant tenant = new Tenant();
        tenant.setName(request.tenantName());
        tenant.setSlug(request.tenantSlug());
        tenant.setActive(true);
        tenant = tenantRepository.save(tenant);

        // Todavia no hay JWT (se esta creando el primer usuario del tenant), asi
        // que se setea TenantContext a mano para que el @PrePersist de BaseEntity
        // complete el tenantId del User. Es el mismo mecanismo que usara el filtro
        // JWT en cada request normal.
        TenantContext.set(tenant.getId());
        try {
            User user = new User();
            user.setFullName(request.fullName());
            user.setEmail(request.email());
            user.setPassword(passwordEncoder.encode(request.password()));
            user.setRole(Role.ADMINISTRATOR);
            user.setActive(true);
            user = userRepository.save(user);

            String token = jwtService.generateToken(
                    user.getId(), user.getEmail(), tenant.getId(), user.getRole().name());

            return new RegisterTenantResponse(
                    token, tenant.getId(), user.getId(), user.getEmail(), user.getRole().name());
        } finally {
            TenantContext.clear();
        }
    }
}
