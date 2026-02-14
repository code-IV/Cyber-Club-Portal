package com.cyberclub.learn.graphql;

import java.util.List;
import java.util.UUID;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import com.cyberclub.learn.dtos.Course;
import com.cyberclub.learn.dtos.Lesson;
import com.cyberclub.learn.services.LessonService;

@Controller
public class LessonQueryResolver {
    
    private final LessonService lessonService;

    public LessonQueryResolver(LessonService lessonService){
        this.lessonService = lessonService;
    }

    @SchemaMapping(typeName = "Course", field = "lessons")
    public List<Lesson> lessons(Course course){
        return lessonService.getLessonsForCourses(course.id());
    }

    @MutationMapping
    public Lesson createLesson(@Argument UUID courseId, @Argument String title, @Argument String content, @Argument int orderIndex){
        return lessonService.createLesson(courseId, title, content, orderIndex);
    }
}
