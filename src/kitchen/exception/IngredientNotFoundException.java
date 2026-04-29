package kitchen.exception;

public class IngredientNotFoundException extends Exception {

    private final String ingredientName;

    public IngredientNotFoundException(String ingredientName) {

        super("Ingredient not found in pantry: \"" + ingredientName + "\"");

        this.ingredientName = ingredientName;

    }

    public String getIngredientName() {

        return ingredientName;

    }

}
