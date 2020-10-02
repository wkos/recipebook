package pl.wkos.recipebook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.wkos.recipebook.model.Category;
import pl.wkos.recipebook.model.Recipe;
import pl.wkos.recipebook.service.RecipeService;

import java.io.*;
import java.util.List;

@Controller
public class RecipeController {
    RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/")
    public String main(Model model) {
        List<Recipe> best3WithLikes = recipeService.findTop3ByOrderByNumberOfLikesDesc();
        model.addAttribute("recipes", best3WithLikes);
        return "home";
    }

    @GetMapping("/dodaj")
    public String getAddRecipeForm(Model model) {
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

    @PostMapping("/delete/{id}")
    public String deleteRecipe(@PathVariable("id") Long id, @RequestParam(required = false) String categoryName) {
        recipeService.getRecipeRepository().deleteById(id);
        System.out.println(categoryName);
        return recipeService.getView(categoryName, true);
    }

    @GetMapping("/edit/{id}")
    public String getFormWithRecipeById(@PathVariable("id") Long id, Model model) {
        System.out.println(id);
        Recipe recipe = recipeService.getRecipeRepository().getRecipeById(id);
        Category category = recipeService.getCategoryRepository().getCategoryById(recipe.getCategory().getId());
        List<Category> categories = recipeService.getCategoryRepository().findAll();
        model.addAttribute("recipe", recipe);
        model.addAttribute("category", category);
        model.addAttribute("categories", categories);
        return "edit";
    }

    @PostMapping("/edit/{id}")
    public String updateRecipeById(@PathVariable("id") Long id,
                                   @RequestPart(name = "fileupload") MultipartFile file,
                                   @RequestParam(name = "fileOnServer", required = false) String fileOnServer,
                                   @ModelAttribute("recipe") Recipe recipe) throws IOException {
        if (file.equals(null)) {
            MultipartFile multipartFile = new MockMultipartFile("test.xlsx",
                    new FileInputStream(new File("src/main/resources/static/imgaes/recipes/" + fileOnServer)));
            recipeService.saveRecipe(multipartFile, recipe);
        } else {
            File fileToDelete = new File("src/main/resources/static/images/recipes/" +
                    recipeService.getRecipeRepository().getRecipeById(id).getPictureFileName());
            fileToDelete.delete();
            recipeService.saveRecipe(file, recipe);
        }
        System.out.println("desc" + recipe.getDescription());

        return "redirect:/wszystkie";
    }

    @PostMapping("/count/{id}")
    public String countLikes(@PathVariable("id") Long id, @RequestParam(required = false) String categoryName) {
        recipeService.addLike(id);
        return recipeService.getView(categoryName, false);
    }

    @GetMapping("/{category}")
    public String getAllInCategory(@PathVariable("category") String categoryName, Model model) {
        model.addAttribute("recipes", recipeService.getAllByCategory(categoryName));
        model.addAttribute("category", recipeService.getCategoryByName(categoryName));
        return categoryName;
    }

    @GetMapping("/przepis/{id}")
    public String getRecipeById(@PathVariable("id") Long id, Model model) {
        Recipe recipe = recipeService.getRecipeRepository().getRecipeById(id);
        Category category = recipeService.getCategoryRepository().getCategoryById(recipe.getCategory().getId());
        model.addAttribute("recipe", recipe);
        model.addAttribute("categoryName", category.getName());
        return "przepis";//recipeService.getRecipeView(id);
    }
}