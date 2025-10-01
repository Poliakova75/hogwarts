package ru.hogwarts.school.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);
    private final StudentRepository studentRepository;
    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }
    public Student addStudent(Long id, String name, int age){
    logger.info("Adding student with id: {}, name: {}, age: {}", id, name, age);
        Student student = new Student(id, name, age);
        return studentRepository.save(student);
    }
    public Optional<Student> getStudent(Long id){
        logger.info("Getting student with id: {}", id);
        return studentRepository.findById(id);
    }
    public Student updateStudent(Long id, String name, int age){
        logger.info("Updating student with id: {}", id);
        Optional<Student> studentOpt = studentRepository.findById(id);
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            student.setName(name);
            student.setAge(age);
            logger.info("Updated student with id: {}", id);
            return studentRepository.save(student);
        }
        logger.warn("Attempted to update non-existing student with id: {}", id);
        return null;
    }
    public boolean deleteStudent(Long id) {
        logger.info("Deleting student with id: {}", id);
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
            logger.info("Deleted student with id: {}", id);
            return true;
        }
        logger.warn("Attempted to delete non-existing student with id: {}", id);
        return false;
    }
    public List<Student> getAllStudents() {
        logger.info("Getting all students");
        return studentRepository.findAll();
    }
    @GetMapping("/age")
    public List<Student> getStudentsByAgeBetween(@RequestParam int min, @RequestParam int max) {
        logger.info("Getting students with age between {} and {}", min, max);
        return studentRepository.findByAgeBetween(min, max);
    }
    public Double getAverageAge() {
        logger.info("Calculating average age of students");
        return studentRepository.findAverageAge();
    }
    public List<Student> getLastFiveStudents() {
        logger.info("Getting last five students");
        return studentRepository.findTop5ByOrderByIdDesc();
    }
    public long getTotalStudents() {
        logger.info("Counting total number of students");
        return studentRepository.countAllStudents();
    }
    public List<String> getStudentNamesStartingWithA() {
        logger.info("Fetching student names starting with 'A'");
        List<Student> students = studentRepository.findAll();
        return students.stream()
                .map(Student::getName)
                .filter(name -> name.startsWith("A"))
                .map(String::toUpperCase)
                .sorted()
                .collect(Collectors.toList());
    }
    public double getAverageAgeOfStudents() {
        if (logger.isInfoEnabled()) {
            logger.info("Calculating average age of students");
        }
        List<Student> students = studentRepository.findAll();
        return students.stream()
                .mapToInt(Student::getAge)
                .average()
                .orElse(0);
    }
    public int calculateSum() {
        logger.info("Calculating sum of numbers from 1 to 1,000,000");
        int n = 1_000_000;
        return n * (n + 1) / 2;
    }
}

