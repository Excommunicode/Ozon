package kz.ozon.repository;

import kz.ozon.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT CASE WHEN (COUNT(c)> 0) THEN TRUE ELSE FALSE END " +
            "FROM Category c " +
            "WHERE c.name = :name")
    boolean existsByName(String name);
}