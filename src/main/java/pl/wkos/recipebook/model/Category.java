package pl.wkos.recipebook.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String pictureFileName;

    @OneToMany(mappedBy = "category")
    private List<Recipe> recipeList = new ArrayList<>();

//    public void addRecipe(Recipe recipe){
//        recipeList.add(recipe);
//        recipe.setCategory(this);
//    }
}
