package com.cyberclub.identity.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.cyberclub.identity.api.dtos.ServiceRecord;

@Repository
public class ServiceRepo {
    
    private final JdbcTemplate jdbcTemplate;

    public ServiceRepo(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<ServiceRecord> mapper = (rs, rowNum) ->
        new ServiceRecord(
            rs.getString("id") != null ? UUID.fromString(rs.getString("id")) : null,
            rs.getString("service_name"),
            rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null
        );
    
    public Optional<ServiceRecord> findByName (String serviceName){
        var sql = """
                SELECT * FROM identity.services WHERE service_name = ?
                """;
        
        return jdbcTemplate
                .query(sql, mapper, serviceName)
                .stream()
                .findFirst();
    }

}
