package com.francofral.jpastreams.mapper;

import com.francofral.jpastreams.domain.Course;
import com.francofral.jpastreams.dto.CourseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    CourseDto courseToDto(Course course);
    Course dtoToCourse(CourseDto dto);
}
