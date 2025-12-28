package com.cyberclub.identity.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.cyberclub.identity.api.dtos.TenantRecord;

@Repository
public class TenantRepo {
    
    private final JdbcTemplate jdbcTemplate;

    public TenantRepo(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<TenantRecord> mapper = (rs, rowNum) ->
        new TenantRecord(
            UUID.fromString(rs.getString("id")),
            rs.getString("tenantKey"),
            rs.getTimestamp("createdAt").toLocalDateTime()
        );
    
    public Optional<TenantRecord> findByKey (String tenantKey){
        var sql = """
                SELECT * FROM identity.tenants WHERE tenant_key = ?
                """;
        
        return jdbcTemplate
                .query(sql, mapper, tenantKey)
                .stream()
                .findFirst();
    }

}
