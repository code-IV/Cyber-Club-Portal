package com.cyberclub.identity.repository;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

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

    public void createDefaultMembership(UUID userId) {
        // 1. Get all service IDs
        List<UUID> serviceIds = jdbcTemplate.query(
            "SELECT id FROM identity.services", 
            (rs, rowNum) -> UUID.fromString(rs.getString("id"))
        );

        // 2. Batch insert using the logic from our previous step
        var sql = "INSERT INTO identity.memberships (id, user_id, service_id, role) VALUES (?, ?, ?, 'USER')";
        
        List<Object[]> batchArgs = serviceIds.stream()
            .map(sId -> new Object[] { UUID.randomUUID(), userId, sId })
            .toList();

        jdbcTemplate.batchUpdate(sql, batchArgs);
}

    private final RowMapper<IsMemberResponse> mapper = (rs, n) ->
        new IsMemberResponse(
            true,
            rs.getString("role")
        );

    public Optional<IsMemberResponse> findMembership(UUID id, String serviceName){
        var sql = """
                SELECT m.role FROM identity.memberships m
                JOIN identity.services s ON m.service_id = s.id
                WHERE m.user_id = ? AND s.service_name = ?
                """;

        return jdbcTemplate
                .query(sql, mapper, id, serviceName)
                .stream()
                .findFirst();
    }

    public boolean serviceExists (String serviceName){
        var sql = """
                SELECT COUNT(*) FROM identity.services WHERE service_name = ?
                """;

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, serviceName);
        return count != null && count > 0;
    }
}
