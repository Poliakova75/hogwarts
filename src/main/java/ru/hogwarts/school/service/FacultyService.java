package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.*;
@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;
    @Autowired
    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }
    public Faculty addFaculty(Long id,String name, String color) {
        Faculty faculty = new Faculty(id, name, color);
        return facultyRepository.save(faculty);
    }
    public Faculty getFaculty(Long id) {
        Optional<Faculty> faculty = facultyRepository.findById(id);
        return faculty.orElse(null);
    }
    public Faculty updateFaculty(Long id, String name, String color) {
        Optional<Faculty> facultyOpt = facultyRepository.findById(id);
        if (facultyOpt.isPresent()) {
            Faculty faculty = facultyOpt.get();
            faculty.setName(name);
            faculty.setColor(color);
            return facultyRepository.save(faculty);
        }
        return null;
    }
    public void deleteFaculty(Long id) {
        facultyRepository.deleteById(id);
    }
    public List<Faculty> getAllFaculties() {
        return facultyRepository.findAll();
    }
}