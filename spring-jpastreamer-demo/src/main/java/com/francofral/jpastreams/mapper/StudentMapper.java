package com.francofral.jpastreams.mapper;

import com.francofral.jpastreams.domain.Student;
import com.francofral.jpastreams.dto.StudentDto;
import com.francofral.jpastreams.dto.StudentFullDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { CourseMapper.class })
public interface StudentMapper {

    StudentFullDto studentToDto(Student student);
    Student dtoToStudent(StudentDto dto);
}
