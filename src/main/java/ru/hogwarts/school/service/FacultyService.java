package ru.hogwarts.school.service;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;
import java.util.*;
@Service
public class FacultyService {
    private static final Logger logger = LoggerFactory.getLogger(FacultyService.class);
    private final FacultyRepository facultyRepository;
    @Autowired
    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }
    public List<Faculty> findByNameOrColorIgnoreCase(String filter) {
        logger.info("Was invoked method findByNameOrColorIgnoreCase with filter: {}", filter);
        return facultyRepository.findByNameOrColorIgnoreCase(filter);
    }
    public Faculty addFaculty(Long id, String name, String color) {
        logger.info("Was invoked method addFaculty with id: {}, name: {}, color: {}", id, name, color);
        Faculty faculty = new Faculty(id, name, color);
        return facultyRepository.save(faculty);
    }
    public Faculty getFaculty(Long id) {
        logger.info("Was invoked method getFaculty with id: {}", id);
        return facultyRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Faculty not found with id: {}", id);
                    return new EntityNotFoundException("Faculty not found with id: " + id);
                });
    }
    public Faculty updateFaculty(Long id, String name, String color) {
        logger.info("Was invoked method updateFaculty with id: {}", id);
        Optional<Faculty> facultyOpt = facultyRepository.findById(id);
        if (facultyOpt.isPresent()) {
            Faculty faculty = facultyOpt.get();
            faculty.setName(name);
            faculty.setColor(color);
            logger.info("Updated faculty with id: {}", id);
            return facultyRepository.save(faculty);
        }
        logger.warn("Attempted to update non-existing faculty with id: {}", id);
        return null;
    }
    public boolean deleteFaculty(Long id) {
        logger.info("Was invoked method deleteFaculty with id: {}", id);
        if (facultyRepository.existsById(id)) {
            facultyRepository.deleteById(id);
            logger.info("Deleted faculty with id: {}", id);
            return true;
        }
        logger.warn("Attempted to delete non-existing faculty with id: {}", id);
        return false;
    }
    public List<Faculty> getAllFaculties() {
        logger.info("Was invoked method getAllFaculties");
        return facultyRepository.findAll();
    }
    public String getLongestFacultyName() {
        logger.info("Fetching the longest faculty name");
        List<Faculty> faculties = facultyRepository.findAll();
        return faculties.stream()
                .map(Faculty::getName)
                .max(Comparator.comparingInt(String::length))
                .orElse("Нет факультетов");
    }
}