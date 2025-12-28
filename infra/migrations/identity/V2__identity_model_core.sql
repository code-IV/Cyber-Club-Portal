SET search_path TO identity;

-- ========= TENANTS =========
CREATE TABLE IF NOT EXISTS tenants (
    id UUID PRIMARY KEY,
    tenant_key VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- ========= MEMBERSHIPS =========
CREATE TABLE IF NOT EXISTS memberships (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    tenant_id UUID NOT NULL,
    role VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_membership_user
        FOREIGN KEY (user_id)
        REFERENCES users(id),

    CONSTRAINT fk_membership_tenant
        FOREIGN KEY (tenant_id)
        REFERENCES tenants(id),

    CONSTRAINT uq_membership UNIQUE (user_id, tenant_id)
);
