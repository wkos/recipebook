package pl.wkos.recipebook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.wkos.recipebook.model.Category;
import pl.wkos.recipebook.model.Recipe;
import pl.wkos.recipebook.service.RecipeService;

import java.io.*;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.List;

@Controller
public class RecipeController {
    RecipeService recipeService;
    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/")
    public String main(Model model){
//        List<Recipe> allRecipes = recipeService.getRecipeRepository().findAll();
        List<Recipe> best3WithLikes = recipeService.findTop3ByOrderByNumberOfLikesDesc();
        model.addAttribute("recipes", best3WithLikes);
        return "home";
    }

    @GetMapping("/dodaj")
    public String getAddRecipeForm(Model model){
        List<Category> categories = recipeService.getCategoryRepository().findAll();
        model.addAttribute("recipe", new Recipe());
        model.addAttribute("categories", categories);
        return "dodaj";
    }

    @PostMapping("/dodaj")
    public String addRecipeFromForm(@RequestPart(name = "fileupload") MultipartFile file, @ModelAttribute("recipe") Recipe recipe) {
        recipeService.addRecipe(file, recipe);
        return "redirect:/saved";
    }

    @GetMapping("/delete/{id}")
    public String deleteRecipe(@PathVariable("id") Long id, @RequestParam(required = false) String categoryName){
        recipeService.getRecipeRepository().deleteById(id);
        System.out.println(id);
        System.out.println(categoryName);
        return "redirect:/";//recipeService.getView(categoryName);
    }

    @GetMapping("/edit/{id}")
    public String getFormWithRecipeById(@PathVariable("id") Long id, Model model){
        System.out.println(id);
        Recipe recipe = recipeService.getRecipeRepository().getRecipeById(id);
        Category category = recipeService.getCategoryRepository().getCategoryById(recipe.getCategory().getId());
        List<Category> categories = recipeService.getCategoryRepository().findAll();
        System.out.println("desc" + recipe.getDescription());
        model.addAttribute("recipe", recipe);
        model.addAttribute("category", category);
        model.addAttribute("categories", categories);
        return "edit_test";
    }

    @GetMapping("image/{name}")
    public String  showImage(@PathVariable String name, Model model) throws IOException {
        File file = new File("src/main/resources/static/images/recipes/" + name);
        ResponseEntity responseEntity;
        if (!file.exists()) {
            responseEntity = ResponseEntity.notFound().build();
        }
        responseEntity = ResponseEntity.ok()
                .contentType(MediaType.valueOf(URLConnection.guessContentTypeFromName(name)))
                .body(Files.readAllBytes(file.toPath()));
        model.addAttribute("file", responseEntity);
        return "home";
    }

    @PostMapping("/count/{id}")
    public String countLikes(@PathVariable("id") Long id, @RequestParam(required = false) String categoryName){
        recipeService.addLike(id);
        System.out.println(categoryName);
        return recipeService.getView(categoryName);
    }

    @GetMapping("/{category}")
    public String getAllInCategory(@PathVariable("category") String categoryName, Model model){
        model.addAttribute("recipes", recipeService.getAllByCategory(categoryName));
        return categoryName;
    }
}