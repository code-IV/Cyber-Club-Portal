package com.cyberclub.learn.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.cyberclub.learn.dtos.Lesson;
import com.cyberclub.learn.repositories.LessonRepo;

@Service
public class LessonService {
    
    private final LessonRepo lessonRepo;

    public LessonService(LessonRepo lessonRepo){
        this.lessonRepo = lessonRepo;
    }

    public List<Lesson> getLessonsForCourses(UUID id){
        return lessonRepo.findByCourseId(id);
    }

    public Lesson createLesson(UUID courseId, String title, String content, int orderIndex){
        return lessonRepo.save(courseId, title, content, orderIndex);
    }
}
