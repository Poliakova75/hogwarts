package ru.hogwarts.school;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;
import java.util.List;
import static java.lang.reflect.Array.get;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(StudentController.class)
public class StudentControllerTest {
    @Autowired
    private final MockMvc mockMvc;
    @Mock
    private StudentService studentService;
    private Student testStudent;
    public StudentControllerTest(MockMvc mockMvc, StudentService studentService) {
        this.mockMvc = mockMvc;
        this.studentService = studentService;
    }
    @BeforeEach
    public void setUp() {
        testStudent = new Student(1L, "Hurry", 11);
    }
    @Test
    public void createStudent() throws Exception {
        given(studentService.addStudent(testStudent.getId(), testStudent.getName(), testStudent.getAge())).willReturn(testStudent);
        mockMvc.perform(post("/students")
                        .param("id", String.valueOf(testStudent.getId()))
                        .param("name", testStudent.getName())
                        .param("age", String.valueOf(testStudent.getAge()))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.name").value(testStudent.getName()));
    }
    @Test
    public void getStudent() throws Exception {
        given(studentService.getStudent(testStudent.getId())).willReturn(testStudent);
        mockMvc.perform((RequestBuilder) get("/students/{id}", Math.toIntExact(testStudent.getId())))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.name").value(testStudent.getName()));
    }
    @Test
    public void updateStudent() throws Exception {
        testStudent.setName("Hurry");
        given(studentService.updateStudent(testStudent.getId(), testStudent.getName(), testStudent.getAge())).willReturn(testStudent);
        mockMvc.perform(put("/students/{id}", testStudent.getId())
                        .param("name", testStudent.getName())
                        .param("age", String.valueOf(testStudent.getAge()))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.name").value(testStudent.getName()));
    }
    @Test
    public void deleteStudent() throws Exception {
        mockMvc.perform(delete("/students/{id}", testStudent.getId()))
                .andExpect(status().isNoContent());

        verify(studentService).deleteStudent(testStudent.getId());
    }
    @Test
    public void filterStudentsByAge() throws Exception {
        given(studentService.getAllStudents()).willReturn(List.of(testStudent));
        mockMvc.perform((RequestBuilder) get("/students/filterByAge/{age}", testStudent.getAge()))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$[0].name").value(testStudent.getName()));
    }
    @Test
    public void getStudentsByAgeBetween() throws Exception {
        given(studentService.getStudentsByAgeBetween(10, 18)).willReturn(List.of(testStudent));
        mockMvc.perform(get("/students/age")
                        .param("min", "10")
                        .param("max", "18"))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$[0].name").value(testStudent.getName()));
    }
}