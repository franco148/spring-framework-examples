package com.francofral.jpastreams.repository;

import com.francofral.jpastreams.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
