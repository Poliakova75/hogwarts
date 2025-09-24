package ru.hogwarts.school.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.model.Student;
import java.util.List;
@Repository
public interface StudentRepository extends JpaRepository<Student, Long>{
    List<Student> findByAgeBetween(int min, int max);
    @Query("SELECT COUNT(s) FROM Student s")
    long countAllStudents();
    @Query("SELECT AVG(s.age) FROM Student s")
    Double findAverageAge();
    @Query("SELECT s FROM Student s ORDER BY s.id DESC")
    List<Student> findTop5ByOrderByIdDesc();
}


