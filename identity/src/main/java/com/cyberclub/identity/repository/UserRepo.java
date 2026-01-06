package com.cyberclub.identity.repository;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

import com.cyberclub.identity.api.dtos.MemberRecord;
import com.cyberclub.identity.api.dtos.UserRecord;

@Repository
public class UserRepo {

    private final JdbcTemplate jdbcTemplate;

    public UserRepo(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }
    
    private final RowMapper<UserRecord> mapper = (rs, rowNum) -> 
        new UserRecord(
            UUID.fromString(rs.getString("id")),
            rs.getString("username"),
            rs.getString("email"),
            rs.getTimestamp("created_at").toLocalDateTime()
        );

    public Optional<UserRecord> findById (UUID id){
        var sql = """
                    SELECT * FROM identity.users WHERE id = ?
                  """;
        
        return jdbcTemplate
                .query(sql, mapper, id)
                .stream()
                .findFirst();
    }

    public List<MemberRecord> findAll(String serviceName){
        var sql = """
                SELECT 
                    t.service_name,
                    u.username, 
                    u.email, 
                    m.role,
                    m.created_at
                FROM identity.memberships m
                JOIN identity.users u ON m.user_id = u.id
                JOIN identity.services t ON m.service_id = t.id
                WHERE t.service_name = ?
                """;

        RowMapper<MemberRecord> memberMapper = (rs, rowNum) -> 
            new MemberRecord(
                rs.getString("service_name"),
                rs.getString("username"),
                rs.getString("email"),
                rs.getString("role"),
                rs.getTimestamp("created_at").toLocalDateTime()
            );

        return jdbcTemplate
                .query(sql, memberMapper, serviceName);
    }

}
