package pl.wkos.recipebook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.wkos.recipebook.model.Category;
import pl.wkos.recipebook.model.Recipe;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    Recipe getRecipeById(Long id);
    List<Recipe> getRecipeByCategory_Id(Long id);
    List<Recipe> findTop3ByOrderByNumberOfLikesDesc();
}
