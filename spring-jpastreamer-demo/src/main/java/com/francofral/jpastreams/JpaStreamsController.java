package com.francofral.jpastreams;

import com.francofral.jpastreams.domain.Course;
import com.francofral.jpastreams.domain.Course$;
import com.francofral.jpastreams.domain.Student;
import com.francofral.jpastreams.domain.Student$;
import com.francofral.jpastreams.dto.CourseDto;
import com.francofral.jpastreams.dto.FlatCourseDto;
import com.francofral.jpastreams.dto.StudentDto;
import com.francofral.jpastreams.mapper.CourseMapper;
import com.francofral.jpastreams.mapper.StudentMapper;
import com.francofral.jpastreams.repository.CourseRepository;
import com.francofral.jpastreams.repository.StudentRepository;
import com.speedment.jpastreamer.application.JPAStreamer;
import com.speedment.jpastreamer.projection.Projection;
import com.speedment.jpastreamer.streamconfiguration.StreamConfiguration;
import jakarta.persistence.Tuple;
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

import java.util.function.Function;

import static com.speedment.jpastreamer.streamconfiguration.StreamConfiguration.of;
import static java.util.stream.Collectors.toMap;

@Slf4j
@RestController
@RequestMapping("/api/v1/streams")
@AllArgsConstructor
public class JpaStreamsController {

    private final JPAStreamer jpaStreamer;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final StudentMapper studentMapper;
    private final CourseMapper courseMapper;

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

    @GetMapping("/courses/projection/{courseId}")
    public ResponseEntity<CourseDto> findCourseByIdProjection(@PathVariable("courseId") Long courseId) {

        StreamConfiguration<Course> courseConfiguration = StreamConfiguration.of(Course.class)
                .selecting(Projection.select(
                    Course$.id,
                    Course$.name
                ));

        CourseDto course = jpaStreamer.stream(courseConfiguration)
                .filter(Course$.id.equal(courseId))
                .findFirst()
                .map(c -> new CourseDto(c.getId(), c.getName(), null))
                .orElseThrow(() -> new RuntimeException("Course could not be found."));

        return ResponseEntity.ok(course);
    }

    @GetMapping("/courses/tuple/{courseId}")
    public ResponseEntity<CourseDto> findCourseByIdTuple(@PathVariable("courseId") Long courseId) {

        CourseDto courseDto = jpaStreamer.stream(Course.class)
                .filter(Course$.id.equal(courseId))
                .map(Projection.select(Course$.id, Course$.name))
                .findFirst()
                .map(mapTupleToCourseDto())
                .orElseThrow(() -> new RuntimeException("Course could not be found."));

        return ResponseEntity.ok(courseDto);
    }

    @GetMapping("/courses/flatprojection/{courseId}")
    public ResponseEntity<FlatCourseDto> findCourseByIdFlatProjection(@PathVariable("courseId") Long courseId) {

        FlatCourseDto courseFlatDto = jpaStreamer.stream(Course.class)
                .filter(Course$.id.equal(courseId))
                .map(FlatCourseDto::new)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Course could not be found."));

        return ResponseEntity.ok(courseFlatDto);
    }

    @PostMapping("/courses")
    public ResponseEntity<Void> saveCourse(@RequestBody Course course) {
        courseRepository.save(course);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/students/{studentId}/course/{courseId}")
    public ResponseEntity<Void> enrollToCourse(@PathVariable("studentId") Long studentId,
                                               @PathVariable("courseId") Long courseId) {
        Student student = jpaStreamer.stream(Student.class)
                .filter(Student$.id.equal(studentId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Student could not be found."));

        Course course = jpaStreamer.stream(Course.class)
                .filter(Course$.id.equal(courseId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Course could not be found."));

        student.addCourse(course);

        studentRepository.save(student);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/students/{studentId}/enrolled")
    public ResponseEntity<StudentDto> getStudentsWithAmountOfEnrolledCourses(@PathVariable("studentId") Long studentId) {

        StudentDto studentDto = jpaStreamer.stream(of(Student.class).joining(Student$.courses))
                .filter(Student$.id.equal(studentId))
                .collect(toMap(Function.identity(), student -> student.getCourses().size()))
                .entrySet()
                .stream()
                .map(entry -> StudentDto.builder()
                        .id(entry.getKey().getId())
                        .name(entry.getKey().getName())
                        .lastName(entry.getKey().getLastName())
                        .numberOfEnrolledCourses(entry.getValue().shortValue())
                        .build())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Student could not be found."));

        return ResponseEntity.ok(studentDto);
    }

    private Function<Tuple, CourseDto> mapTupleToCourseDto() {
        return tuple -> new CourseDto(tuple.get("id", Long.class), tuple.get("name", String.class), null);
    }
}
