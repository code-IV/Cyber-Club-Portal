package com.cyberclub.learn.graphql;

import java.util.List;
import java.util.UUID;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.cyberclub.learn.dtos.Course;
import com.cyberclub.learn.services.CourseSerivce;

@Controller
public class CourseQueryResolver {

    private final CourseSerivce courseSerivce;

    public CourseQueryResolver(CourseSerivce courseSerivce){
        this.courseSerivce = courseSerivce;
    }

    @QueryMapping
    public List<Course> courses(){
        return courseSerivce.getCourses();
    }

    @QueryMapping
    public Course course(@Argument UUID id){
        return courseSerivce.getCourse(id);
    }

    @MutationMapping
    public Course createCourse(@Argument String title, @Argument String description){
        return courseSerivce.createCourse(title, description);
    }
    
}
