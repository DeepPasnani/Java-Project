package kitchen.engine;

import kitchen.model.Ingredient;

import kitchen.model.Recipe;

import java.util.ArrayList;

import java.util.List;

import java.util.Optional;

import java.util.stream.Collectors;

public class RecipeBook {

    private final List<Recipe> recipes = new ArrayList<>();

    public RecipeBook() { loadBuiltInRecipes(); }

    private void loadBuiltInRecipes() {

        Recipe omelette = new Recipe("Masala Omelette", "Breakfast", 1);

        omelette.addIngredient(new Ingredient("egg",          2,  "pcs"));

        omelette.addIngredient(new Ingredient("onion",       30,  "g"));

        omelette.addIngredient(new Ingredient("tomato",      40,  "g"));

        omelette.addIngredient(new Ingredient("green chilli", 5,  "g"));

        omelette.addIngredient(new Ingredient("oil",         10,  "ml"));

        omelette.addIngredient(new Ingredient("salt",         2,  "g"));

        recipes.add(omelette);

        Recipe dal = new Recipe("Dal Tadka", "Lunch", 2);

        dal.addIngredient(new Ingredient("toor dal", 150, "g"));

        dal.addIngredient(new Ingredient("onion",     60, "g"));

        dal.addIngredient(new Ingredient("tomato",    80, "g"));

        dal.addIngredient(new Ingredient("ghee",      20, "ml"));

        dal.addIngredient(new Ingredient("cumin",      5, "g"));

        dal.addIngredient(new Ingredient("turmeric",   3, "g"));

        dal.addIngredient(new Ingredient("salt",       5, "g"));

        recipes.add(dal);

        Recipe pasta = new Recipe("Pasta Arrabbiata", "Dinner", 2);

        pasta.addIngredient(new Ingredient("pasta",      200, "g"));

        pasta.addIngredient(new Ingredient("tomato",     150, "g"));

        pasta.addIngredient(new Ingredient("garlic",      10, "g"));

        pasta.addIngredient(new Ingredient("olive oil",   30, "ml"));

        pasta.addIngredient(new Ingredient("red chilli",   5, "g"));

        pasta.addIngredient(new Ingredient("salt",         5, "g"));

        recipes.add(pasta);

        Recipe rice = new Recipe("Vegetable Fried Rice", "Dinner", 2);

        rice.addIngredient(new Ingredient("rice",      200, "g"));

        rice.addIngredient(new Ingredient("egg",         2, "pcs"));

        rice.addIngredient(new Ingredient("carrot",     50, "g"));

        rice.addIngredient(new Ingredient("peas",       50, "g"));

        rice.addIngredient(new Ingredient("soy sauce",  20, "ml"));

        rice.addIngredient(new Ingredient("oil",        20, "ml"));

        rice.addIngredient(new Ingredient("garlic",      5, "g"));

        rice.addIngredient(new Ingredient("salt",        3, "g"));

        recipes.add(rice);

    }

    public List<Recipe>       getAllRecipes()         { return new ArrayList<>(recipes); }

    public void               addCustomRecipe(Recipe r) { recipes.add(r); }

    public List<Recipe> getByCategory(String category) {

        return recipes.stream()

                      .filter(r -> r.getCategory().equalsIgnoreCase(category))

                      .collect(Collectors.toList());

    }

    public Optional<Recipe> findByName(String name) {

        return recipes.stream()

                      .filter(r -> r.getName().equalsIgnoreCase(name))

                      .findFirst();

    }

}
