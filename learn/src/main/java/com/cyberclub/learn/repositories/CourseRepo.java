package com.cyberclub.learn.repositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import com.cyberclub.learn.dtos.Course;

@Repository
public class CourseRepo{

    private final JdbcTemplate jdbcTemplate;

    public CourseRepo(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Course> course = (rs, rowNum)->
            new Course(
                UUID.fromString(rs.getString("id")),
                rs.getString("title"),
                rs.getString("description"),
                rs.getTimestamp("created_at").toInstant()
            );

    public List<Course> courses(){
        var sql = """
                SELECT id, title, description, created_at 
                FROM courses
                ORDER BY created_at DESC
                """;

        return jdbcTemplate
                .query(sql, course);
    }

    public Optional<Course> course(UUID id){
        var sql = """
                SELECT id, title, description, created_at
                FROM courses
                WHERE id = ?
                """;

        return jdbcTemplate
                .query(sql, course, id)
                .stream()
                .findFirst();
    }

    public Course save(String title, String description){
        var sql = """
                INSERT INTO courses (title, description, created_at)
                VALUES (?, ? , now())
                RETURNING id, title, description, created_at
                """;
        return Objects.requireNonNull( jdbcTemplate
                .queryForObject(sql, course, title, description),
                "Save did not return a course"
        );
        
    }
}