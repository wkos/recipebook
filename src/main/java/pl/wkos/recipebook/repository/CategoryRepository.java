package pl.wkos.recipebook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wkos.recipebook.model.Category;


public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category getCategoryById(Long id);
}
