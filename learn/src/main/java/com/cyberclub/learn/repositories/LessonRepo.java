package com.cyberclub.learn.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.cyberclub.learn.dtos.Lesson;

@Repository
public class LessonRepo{

    private final JdbcTemplate jdbcTemplate;

    public LessonRepo(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Lesson> lesson = (rs, rowNum) ->
            new Lesson(
                UUID.fromString(rs.getString("id")),
                UUID.fromString(rs.getString("course_id")),
                rs.getString("title"),
                rs.getString("content"),
                rs.getInt("order_index")
            );

    public List<Lesson> findByCourseId(UUID id){
        var sql = """
                SELECT id, course_id, title, content, order_index
                FROM lessons
                WHERE course_id=?
                ORDER BY order_index ASC
                """;
        
        return jdbcTemplate
                .query(sql, lesson, id);
    }

    public Lesson save(UUID courseId, String title, String content, int orderIndex){
        var sql = """
                INSERT INTO lessons (course_id, title, content, order_index, created_at)
                VALUES (?,?,?,?, now())
                RETURNING id, course_id, title, content, order_index, created_at
                """;

        return jdbcTemplate
                .queryForObject(sql, lesson, courseId, title, content, orderIndex);
    }
}