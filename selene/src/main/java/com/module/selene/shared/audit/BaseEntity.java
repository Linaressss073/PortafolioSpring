package com.module.selene.shared.audit;

import com.module.selene.shared.security.TenantContext;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import java.util.UUID;

@MappedSuperclass
@FilterDef(name = "tenantFilter", parameters = @ParamDef(name = "tenantId", type = UUID.class))
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
@Getter
@Setter
public abstract class BaseEntity extends Auditable {

    @Column(nullable = false, updatable = false)
    private UUID tenantId;

    @PrePersist
    private void stampTenantId() {
        if (tenantId == null) {
            tenantId = TenantContext.get();
        }
    }
}