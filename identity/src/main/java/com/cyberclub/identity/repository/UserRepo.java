package com.cyberclub.identity.repository;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

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

}
