package ru.hogwarts.school.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.exeption.StudentNotFoundException;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/students")
public class StudentController {
    private final StudentService studentService;
    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }
    @PostMapping
    public Optional<Student> createStudent(@RequestParam Long id, @RequestParam String name, @RequestParam int age) {
        return Optional.ofNullable(studentService.addStudent(id, name, age));
    }
    @GetMapping("/{id}")
    public Student getStudent(@PathVariable Long id) {
        return studentService.getStudent(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
    }
    @PutMapping("/{id}")
    public Student updateStudent(@PathVariable Long id, @RequestParam String name, @RequestParam int age) {
        Student updatedStudent = studentService.updateStudent(id, name, age);
        if (updatedStudent == null) {
            throw new StudentNotFoundException(id);
        }
        return updatedStudent;
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudent(@PathVariable Long id) {
        if (!studentService.deleteStudent(id)) {
            throw new StudentNotFoundException(id);
        }
    }
    @GetMapping("/filterByAge/{age}")
    public List<Student> filterStudentsByAge(@PathVariable int age) {
        return studentService.getAllStudents()
                .stream()
                .filter(student -> student.getAge() == age)
                .collect(Collectors.toList());
    }
    @GetMapping("/age")
    public List<Student> getStudentsByAgeBetween(@RequestParam int min, @RequestParam int max) {
        return studentService.getStudentsByAgeBetween(min, max);
    }
}