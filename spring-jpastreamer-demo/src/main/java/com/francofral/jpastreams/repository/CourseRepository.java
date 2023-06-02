package com.francofral.jpastreams.repository;

import com.francofral.jpastreams.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
