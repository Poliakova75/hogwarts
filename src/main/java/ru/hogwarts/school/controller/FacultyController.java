package ru.hogwarts.school.controller;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/faculty")
public class FacultyController {
    private final FacultyService facultyService = new FacultyService();
    @PostMapping()
    public Faculty createFaculty(@RequestParam String name, @RequestParam String color) {
        return facultyService.addFaculty(name, color);
    }
    @GetMapping
    public Faculty getFaculty(@PathVariable Long id) {
        return facultyService.getFaculty(id);
    }
    @PutMapping()
    public Faculty updateFaculty(@PathVariable Long id, @RequestParam String name, @RequestParam String color) {
        return facultyService.updateFaculty(id, name, color);
    }
    @DeleteMapping("/{id}")
    public void deleteFaculty(@PathVariable Long id) {
        facultyService.deleteFaculty(id);
    }
    @GetMapping("/filterByColor/{color}")
    public List<Faculty> filterFacultiesByColor(@PathVariable String color) {
        return facultyService.getAllFaculties()
                .stream()
                .filter(faculty -> faculty.getColor().equalsIgnoreCase(color))
                .collect(Collectors.toList());
    }
}
