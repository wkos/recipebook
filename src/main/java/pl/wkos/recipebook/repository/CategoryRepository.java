package pl.wkos.recipebook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.wkos.recipebook.model.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category getCategoryById(Long id);

//    @Query("SELECT c FROM Category c")
//    List<Category> findAllCategories();
}
