package com.francofral.jpastreams.mapper;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { CourseMapper.class })
public interface StudentMapper {
}
