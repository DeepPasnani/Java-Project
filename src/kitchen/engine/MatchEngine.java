package kitchen.engine;

import kitchen.exception.IngredientNotFoundException;

import kitchen.exception.InsufficientQuantityException;

import kitchen.model.Ingredient;

import kitchen.model.Pantry;

import kitchen.model.Recipe;

import java.util.ArrayList;

import java.util.List;

public class MatchEngine {

    private final Pantry pantry;

    public MatchEngine(Pantry pantry) { this.pantry = pantry; }

    public MatchResult checkMatch(Recipe recipe) {

        List<String> missing   = new ArrayList<>();

        List<String> shortfall = new ArrayList<>();

        for (Ingredient req : recipe.getRequiredIngredients()) {

            try {

                double available = pantry.getQuantity(req.getName());

                if (available < req.getQuantity()) {

                    shortfall.add(String.format(

                        "%s: need %.1f %s, have %.1f %s",

                        req.getName(), req.getQuantity(), req.getUnit(),

                        available, req.getUnit()));

                }

            } catch (IngredientNotFoundException e) {

                missing.add(req.getName());

            }

        }

        return new MatchResult(recipe.getName(), missing, shortfall);

    }

    public List<Recipe> getCookableRecipes(List<Recipe> all) {

        List<Recipe> cookable = new ArrayList<>();

        for (Recipe r : all)

            if (checkMatch(r).isFullMatch()) cookable.add(r);

        return cookable;

    }

    public void cookRecipe(Recipe recipe)

            throws IngredientNotFoundException, InsufficientQuantityException {

        for (Ingredient req : recipe.getRequiredIngredients())

            pantry.consume(req.getName(), req.getQuantity());

    }

    public static class MatchResult {

        private final String       recipeName;

        private final List<String> missingIngredients;

        private final List<String> shortfallIngredients;

        public MatchResult(String name, List<String> missing,

                           List<String> shortfall) {

            this.recipeName           = name;

            this.missingIngredients   = missing;

            this.shortfallIngredients = shortfall;

        }

        public boolean isFullMatch() {

            return missingIngredients.isEmpty() &&

                   shortfallIngredients.isEmpty();

        }

        public String getSummary() {

            if (isFullMatch())

                return "  [OK] You have everything to make \"" +

                       recipeName + "\"!";

            StringBuilder sb = new StringBuilder();

            sb.append("  [NO] Cannot make \"").append(recipeName)

              .append("\":\n");

            missingIngredients.forEach(

                m -> sb.append("       Missing   : ").append(m).append("\n"));

            shortfallIngredients.forEach(

                s -> sb.append("       Too little : ").append(s).append("\n"));

            return sb.toString();

        }

    }

}
