package pl.wkos.recipebook.service;

import lombok.Getter;
import lombok.Setter;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.wkos.recipebook.model.Category;
import pl.wkos.recipebook.model.Recipe;
import pl.wkos.recipebook.repository.CategoryRepository;
import pl.wkos.recipebook.repository.RecipeRepository;

import java.io.*;
import java.util.List;

@Getter
@Setter
@Service
public class RecipeService {
    RecipeRepository recipeRepository;
    CategoryRepository categoryRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository, CategoryRepository categoryRepository) {
        this.recipeRepository = recipeRepository;
        this.categoryRepository = categoryRepository;
    }

    public void addLike(Long id){
        Recipe recipe = recipeRepository.getRecipeById(id);
        recipe.setNumberOfLikes(recipe.getNumberOfLikes()+1);
        recipeRepository.save(recipe);
    }

    public void addRecipe(MultipartFile file, Recipe recipe){
        try {
            File oFile = new File("src/main/resources/static/images/recipes/" + file.getOriginalFilename());
            OutputStream os = new FileOutputStream(oFile);
            InputStream inputStream = file.getInputStream();

            IOUtils.copy(inputStream, os); // 4
            recipe.setPictureFileName(file.getOriginalFilename());
            os.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        recipe.setNumberOfLikes(0L);
        recipeRepository.save(recipe);
    }

    public String getView(String name){
        switch (name){
            case "arabskie": return "redirect:/" + name;
            case "azjatyckie": return "redirect:/" + name;
            case "meksykanskie": return "redirect:/" + name;
            case "wszystkie": return "redirect:/" + name;
            default: return "redirect:/";
        }
    }

    public List<Recipe> getAllByCategory(String category){
        Long id = 0L;
        switch (category){
            case "arabskie": id = 1L; break;
            case "azjatyckie": id = 2L; break;
            case "meksykanskie": id = 3L; break;
            case "wszystkie": id = 0L; break;
//            default: id = 0L;
        }
        if(id == 0L){
            return getRecipeRepository().findAll();
        }else{
            return getRecipeRepository().getRecipeByCategory_Id(id);
        }
    }

    public List<Recipe> findTop3ByOrderByNumberOfLikesDesc(){
        return recipeRepository.findTop3ByOrderByNumberOfLikesDesc();
    }

//    public List<Category> getAllCategories(){
//        return recipeRepository.getAllCategories();
//    }
}
