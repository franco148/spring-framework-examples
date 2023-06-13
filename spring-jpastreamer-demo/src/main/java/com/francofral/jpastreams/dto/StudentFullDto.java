package com.francofral.jpastreams.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class StudentFullDto {

    private Long id;
    private String name;
    private String lastName;

    private Set<CourseDto> courses;
}
