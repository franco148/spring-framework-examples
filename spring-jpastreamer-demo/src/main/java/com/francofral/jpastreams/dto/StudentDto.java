package com.francofral.jpastreams.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class StudentDto {

    private Long id;
    private String name;
    private String lastName;
    private Short numberOfEnrolledCourses;
}
