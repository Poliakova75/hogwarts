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
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private StudentService studentService;
    private Student testStudent;
    @BeforeEach
    public void setUp() {
        testStudent = new Student(1L, "Hurry", 11);
        studentService.addStudent(testStudent.getId(), testStudent.getName(), testStudent.getAge());
    }
    @Test
    public void createStudent() {
        Student newStudent = new Student(2L, "Ron", 12);
        ResponseEntity<Student> response = restTemplate.postForEntity("/students", newStudent, Student.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    public void getStudent() {
        ResponseEntity<Student> response = restTemplate.getForEntity("/students/1", Student.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(testStudent);
    }
    @Test
    public void updateStudent() {
        testStudent.setName("Hermione");
        ResponseEntity<Student> response = restTemplate.exchange("/students/1", HttpMethod.PUT, new HttpEntity<>(testStudent), Student.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo("Hermione");
    }
    @Test
    public void deleteStudent() {
        restTemplate.delete("/students/1");
        ResponseEntity<Student> response = restTemplate.getForEntity("/students/1", Student.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    @Test
    public void filterStudentsByAge() {
        ResponseEntity<List> response = restTemplate.getForEntity("/students/filterByAge/20", List.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains(testStudent);
    }
    @Test
    public void getStudentsByAgeBetween() {
        ResponseEntity<List> response = restTemplate.getForEntity("/students/age?min=18&max=25", List.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains(testStudent);
    }
}
