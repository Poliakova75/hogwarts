package ru.hogwarts.school;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
public class StudentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
    }
    @Test
    public void testPrintParallel() throws Exception {
        // Подготовка данных для теста
        when(studentService.getAllStudents()).thenReturn(generateMockStudents());

        mockMvc.perform(get("/students/print-parallel").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    public void testPrintSynchronized() throws Exception {
        // Подготовка данных для теста
        when(studentService.getAllStudents()).thenReturn(generateMockStudents());

        mockMvc.perform(get("/students/print-synchronized").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    private List<Student> generateMockStudents() {
        return List.of(
                new Student(1L, "Alice", 20),
                new Student(2L, "Bob", 21),
                new Student(3L, "Charlie", 22),
                new Student(4L, "David", 23),
                new Student(5L, "Eve", 24),
                new Student(6L, "Frank", 25)
        );
    }
}
