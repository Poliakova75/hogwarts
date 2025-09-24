package ru.hogwarts.school.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.model.Faculty;
import java.util.List;
@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    @Query("SELECT f FROM Faculty f WHERE LOWER(f.name) LIKE LOWER(CONCAT('%', :filter, '%')) OR LOWER(f.color) LIKE LOWER(CONCAT('%', :filter, '%'))")
    List<Faculty> findByNameOrColorIgnoreCase(@Param("filter") String filter);
}
