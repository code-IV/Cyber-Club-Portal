package com.cyberclub.identity.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.cyberclub.identity.api.dtos.IsMemberResponse;

@Repository
public class MembershipRepo {
    
    private final JdbcTemplate jdbcTemplate;

    public MembershipRepo(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<IsMemberResponse> mapper = (rs, n) ->
        new IsMemberResponse(
            true,
            rs.getString("role")
        );

    public Optional<IsMemberResponse> findMembership(UUID id, String tenantKey){
        var sql = """
                SELECT m.role FROM identity.memberships m
                JOIN identity.tenants t ON m.tenant_id = t.id
                WHERE m.user_id = ? AND t.tenant_key = ?
                """;

        return jdbcTemplate
                .query(sql, mapper, id, tenantKey)
                .stream()
                .findFirst();
    }

    public boolean tenantExists (String tenantKey){
        var sql = """
                SELECT COUNT(*) FROM identity.tenants WHERE tenant_key = ?
                """;

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, tenantKey);
        return count != null && count > 0;
    }
}
