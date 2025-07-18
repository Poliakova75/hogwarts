package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.*;
@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private StudentService studentService;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }
    public Student addStudent(Long id, String name, int age) {
        Student student = new Student(id, name, age);
        return studentRepository.save(student);
    }
    public Student getStudent(Long id) {
        Optional<Student> student = studentRepository.findById(id);
        return student.orElse(null);
    }
    public Student updateStudent(Long id, String name, int age) {
        Optional<Student> studentOpt = studentRepository.findById(id);
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            student.setName(name);
            student.setAge(age);
            return studentRepository.save(student);
        }
        return null;
    }
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
    @GetMapping("/age")
    public List<Student> getStudentsByAgeBetween(@RequestParam int min, @RequestParam int max) {
        return studentService.getStudentsByAgeBetween(min, max);
    }
}
