package com.francofral.jpastreams.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
public class CourseDto {

    private Long id;
    private String name;
    private Short numberOfRegisteredStudents;
}
