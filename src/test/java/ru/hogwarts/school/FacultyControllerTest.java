package ru.hogwarts.school;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;
import java.util.List;
import static java.lang.reflect.Array.get;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(FacultyController.class)
public class FacultyControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Mock
    private FacultyService facultyService;
    @Mock
    private StudentService studentService;
    private Faculty testFaculty;

    public FacultyControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @BeforeEach
    public void setUp() {
        testFaculty = new Faculty(1L, "Gryffindor", "red");
    }
    @Test
    public void createFaculty() throws Exception {
        given(facultyService.addFaculty(Mockito.anyLong(), Mockito.anyString(), Mockito.anyString())).willReturn(testFaculty);
        mockMvc.perform(post("/faculty")
                        .param("id", String.valueOf(testFaculty.getId()))
                        .param("name", testFaculty.getName())
                        .param("color", testFaculty.getColor())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.name").value(testFaculty.getName()));
    }
    @Test
    public void getFaculty() throws Exception {
        given(facultyService.getFaculty(testFaculty.getId())).willReturn(testFaculty);
        mockMvc.perform((RequestBuilder) get("/faculty/{id}", Math.toIntExact(testFaculty.getId())))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.name").value(testFaculty.getName()));
    }
    @Test
    public void updateFaculty() throws Exception {
        testFaculty.setName("Slytherin");
        given(facultyService.updateFaculty(testFaculty.getId(), testFaculty.getName(), testFaculty.getColor())).willReturn(testFaculty);
        mockMvc.perform(put("/faculty/{id}", testFaculty.getId())
                        .param("name", testFaculty.getName())
                        .param("color", testFaculty.getColor())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.name").value(testFaculty.getName()));
    }
    @Test
    public void deleteFaculty() throws Exception {
        mockMvc.perform(delete("/faculty/{id}", testFaculty.getId()))
                .andExpect(status().isNoContent());
        Mockito.verify(facultyService).deleteFaculty(testFaculty.getId());
    }
    @Test
    public void filterFacultiesByColor() throws Exception {
        given(facultyService.getAllFaculties()).willReturn(List.of(testFaculty));
        mockMvc.perform((RequestBuilder) get("/faculty/filterByColor/{color}", Integer.parseInt(testFaculty.getColor())))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$[0].name").value(testFaculty.getName()));
    }
    @Test
    public void getStudentsByAgeBetween() throws Exception {
        List<Student> students = List.of(
                new Student(1L, "Hurry", 11),
                new Student(2L, "Ron", 11)
        );
        given(studentService.getStudentsByAgeBetween(10, 18)).willReturn(students);
        mockMvc.perform(get("/faculty/students/age")
                        .param("min", "10")
                        .param("max", "18"))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$", hasSize(2)))
                .andExpect((ResultMatcher) jsonPath("$[0].name").value("Hurry"))
                .andExpect((ResultMatcher) jsonPath("$[1].name").value("Ron"));
    }
    @Test
    public void searchFaculties(TestRestTemplate restTemplate) {
        ResponseEntity<List> response = restTemplate.getForEntity("/faculty/search?filter=Gryffindor", List.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isInstanceOf(List.class);
        assertThat(response.getBody()).contains(testFaculty);
    }
}

