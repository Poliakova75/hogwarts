package ru.hogwarts.school;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private FacultyService facultyService;
    @Autowired
    private StudentService studentService;
    private Faculty testFaculty;
    @BeforeEach
    public void setUp() {
        testFaculty = new Faculty(1L, "Gryffindor", "red");
        facultyService.addFaculty(testFaculty.getId(), testFaculty.getName(), testFaculty.setColor("red"));
    }
    @Test
    public void createFaculty() {
        Faculty newFaculty = new Faculty(2L, "Slytherin ", "green");
        ResponseEntity<Faculty> response = restTemplate.postForEntity("/faculty", newFaculty, Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    public void getFaculty() {
        ResponseEntity<Faculty> response = restTemplate.getForEntity("/faculty/1", Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(testFaculty);
    }
    @Test
    public void updateFaculty() {
        testFaculty.setName("Ravenclaw");
        ResponseEntity<Faculty> response = restTemplate.exchange("/faculty/1", HttpMethod.PUT, new HttpEntity<>(testFaculty), Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo("Ravenclaw");
    }
    @Test
    public void deleteFaculty() {
        restTemplate.delete("/faculty/1");
        ResponseEntity<Faculty> response = restTemplate.getForEntity("/faculty/1", Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    @Test
    public void filterFacultiesByColor() {
        ResponseEntity<List> response = restTemplate.getForEntity("/faculty/filterByColor/red", List.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains(testFaculty);
    }
    @Test
    public void getStudentsByAgeBetween() {
        ResponseEntity<List> response = restTemplate.getForEntity("/faculty/students/age?min=18&max=25", List.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    public void searchFaculties() {
        ResponseEntity<List> response = restTemplate.getForEntity("/faculty/search?filter=Gryffindor ", List.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isInstanceOf(List.class);
        assertThat(response.getBody()).contains(testFaculty);
    }
}
