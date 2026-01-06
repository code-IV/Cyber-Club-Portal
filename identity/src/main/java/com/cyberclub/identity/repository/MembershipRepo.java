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
