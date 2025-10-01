package ru.hogwarts.school.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/students")
public class StudentController {
    private final StudentService studentService;
    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }
    private synchronized void printStudentName(String name) {
        System.out.println(name);
    }
    @GetMapping("/print-synchronized")
    public void printStudentNamesSynchronized() {
        List<Student> students = studentService.getAllStudents();
        if (students.size() < 6) {
            System.out.println("Недостаточно студентов для выполнения задачи.");
            return;
        }
        printStudentName(students.get(0).getName());
        printStudentName(students.get(1).getName());

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> {
            printStudentName(students.get(2).getName());
            printStudentName(students.get(3).getName());
        }, executorService);

        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> {
            System.out.println(students.get(4).getName());
            System.out.println(students.get(5).getName());
        }, executorService);


        CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(future1, future2);
        combinedFuture.join();

        executorService.shutdown();
    }
    @GetMapping("/print-parallel")
    public void printStudentNamesInParallel() {
        List<Student> students = studentService.getAllStudents();
        if (students.size() < 6) {
            System.out.println("Недостаточно студентов для выполнения задачи.");
            return;
        }
        System.out.println(students.get(0).getName());
        System.out.println(students.get(1).getName());

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> {
            System.out.println(students.get(2).getName());
            System.out.println(students.get(3).getName());
        }, executorService);

        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> {
            System.out.println(students.get(4).getName());
            System.out.println(students.get(5).getName());
        }, executorService);

        CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(future1, future2);
        combinedFuture.join();

        executorService.shutdown();
    }
    @PostMapping
    public Optional<Student> createStudent(@RequestParam Long id, @RequestParam String name, @RequestParam int age) {
        return Optional.ofNullable(studentService.addStudent(id, name, age));
    }
    @GetMapping("/{id}")
    public Student getStudent(@PathVariable Long id) {
        return studentService.getStudent(id).orElse(null);
    }
    @PutMapping("/{id}")
    public Student updateStudent(@PathVariable Long id, @RequestParam String name, @RequestParam int age) {
        return studentService.updateStudent(id, name, age);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
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
    @GetMapping("/count")
    public long getTotalStudents() {
        return studentService.getTotalStudents();
    }
    @GetMapping("/average-age")
    public Double getAverageAge() {
        return studentService.getAverageAge();
    }
    @GetMapping("/last-five")
    public List<Student> getLastFiveStudents() {
        return studentService.getLastFiveStudents();
    }
}