package com.francofral.jpastreams;

import com.francofral.jpastreams.domain.Student;
import com.francofral.jpastreams.repository.StudentRepository;
import com.speedment.jpastreamer.application.JPAStreamer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/streams")
@AllArgsConstructor
public class JpaStreamsController {

    private final JPAStreamer jpaStreamer;
    private final StudentRepository studentRepository;

    @GetMapping("/students/{studentId}")
    public ResponseEntity<Student> findStudentById(@PathVariable("studentId") Long studentId) {

        Student student = jpaStreamer.stream(Student.class)
                .filter(Student$.id.equal(studentId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Student could not be found."));

        return ResponseEntity.ok(student);
    }

    @PostMapping("/students")
    public ResponseEntity<Void> saveStudent(@RequestBody Student student) {
        studentRepository.save(student);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
