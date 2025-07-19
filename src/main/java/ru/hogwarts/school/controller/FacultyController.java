package ru.hogwarts.school.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.List;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/faculty")
public class FacultyController {
    private final FacultyService facultyService;
    private final StudentService studentService;
    @Autowired
    public FacultyController(FacultyService facultyService, StudentService studentService) {
        this.facultyService = facultyService;
        this.studentService = studentService;
    }
    @Autowired
    private FacultyRepository facultyRepository;
    @PostMapping
    public Faculty createFaculty(@RequestParam Long id, @RequestParam String name, @RequestParam String color) {
        return facultyService.addFaculty(id, name, color);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Faculty> getFaculty(@PathVariable Long id) {
        Faculty faculty = facultyService.getFaculty(id);
        return faculty != null ? ResponseEntity.ok(faculty) : ResponseEntity.notFound().build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<Faculty> updateFaculty(@PathVariable Long id, @RequestParam String name, @RequestParam String color) {
        Faculty updatedFaculty = facultyService.updateFaculty(id, name, color);
        return updatedFaculty != null ? ResponseEntity.ok(updatedFaculty) : ResponseEntity.notFound().build();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFaculty(@PathVariable Long id) {
        facultyService.deleteFaculty(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/filterByColor/{color}")
    public List<Faculty> filterFacultiesByColor(@PathVariable String color) {
        return facultyService.getAllFaculties()
                .stream()
                .filter(faculty -> faculty.setColor("red").equalsIgnoreCase(color))
                .collect(Collectors.toList());
    }
    @GetMapping("/students/age")
    public List<Student> getStudentsByAgeBetween(@RequestParam int min, @RequestParam int max) {
        return studentService.getStudentsByAgeBetween(min, max);
    }
    @GetMapping("/search")
    public List<Faculty> searchFaculties(@RequestParam String filter) {
        return facultyService.findByNameOrColorIgnoreCase(filter);
    }
}
