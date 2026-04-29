package kitchen.model;

import kitchen.storage.Scalable;

import java.io.Serializable;

import java.util.ArrayList;

import java.util.List;

public class Recipe implements Scalable, Serializable {

    private static final long serialVersionUID = 4L;

    private String name;

    private String category;

    private int    baseServings;

    private int    currentServings;

    private List<Ingredient> requiredIngredients;

    private List<Double>     baseQuantities;

    public Recipe(String name, String category, int servings) {

        this.name                = name.trim();

        this.category            = category;

        this.baseServings        = servings;

        this.currentServings     = servings;

        this.requiredIngredients = new ArrayList<>();

        this.baseQuantities      = new ArrayList<>();

    }

    public void addIngredient(Ingredient ing) {

        requiredIngredients.add(ing);

        baseQuantities.add(ing.getQuantity());

    }

    @Override

    public void scaleTo(int targetServings) {

        if (targetServings <= 0)

            throw new IllegalArgumentException("Servings must be positive.");

        double factor = (double) targetServings / baseServings;

        for (int i = 0; i < requiredIngredients.size(); i++) {

            requiredIngredients.get(i).setQuantity(

                baseQuantities.get(i) * factor);

        }

        this.currentServings = targetServings;

    }

    @Override public int    getServings()    { return currentServings; }

    public        String getName()           { return name; }

    public        String getCategory()       { return category; }

    public        int    getBaseServings()   { return baseServings; }

    public List<Ingredient> getRequiredIngredients() {

        return requiredIngredients;

    }

    @Override

    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append(String.format("  Recipe : %s  [%s]  (serves %d)\n",

                                name, category, currentServings));

        sb.append("  Ingredients:\n");

        for (Ingredient ing : requiredIngredients)

            sb.append("    - ").append(ing).append("\n");

        return sb.toString();

    }

}
