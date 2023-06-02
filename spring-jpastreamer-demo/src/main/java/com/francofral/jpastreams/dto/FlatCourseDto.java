package com.francofral.jpastreams.dto;

import com.francofral.jpastreams.domain.Course;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

//@Data
//@AllArgsConstructor
@Getter
public class FlatCourseDto {

    private final Long id;
    private final String name;

    public FlatCourseDto(Course course) {
        this.id = course.getId();
        this.name = course.getName();
    }
}
