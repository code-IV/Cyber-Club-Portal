package com.cyberclub.learn.dtos;

import java.util.UUID;

public record Lesson(
    UUID id,
    UUID courseId,
    String title,
    String content,
    int orderIndex

) {
}