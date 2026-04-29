import kitchen.engine.MatchEngine;

import kitchen.engine.RecipeBook;

import kitchen.exception.IngredientNotFoundException;

import kitchen.exception.InsufficientQuantityException;

import kitchen.model.*;

import kitchen.util.ExpiryScanner;

import java.io.IOException;

import java.time.LocalDate;

import java.time.format.DateTimeFormatter;

import java.time.format.DateTimeParseException;

import java.util.List;

import java.util.Scanner;

public class KitchenApp {

    private static final String SAVE_FILE = "data/pantry.dat";

    private static final DateTimeFormatter DATE_FMT =

        DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private final Pantry      pantry     = new Pantry();

    private final RecipeBook  recipeBook = new RecipeBook();

    private final MatchEngine engine     = new MatchEngine(pantry);

    private final Scanner     sc         = new Scanner(System.in);

    public static void main(String[] args) {

        new KitchenApp().run();

    }

    private void run() {

        loadPantry();

        ExpiryScanner scanner = new ExpiryScanner(pantry);

        scanner.start();

        System.out.println("\n=== Kitchen & Recipe Inventory App ===");

        System.out.println("    OOP Project  |  BE04000231\n");

        boolean exit = false;

        while (!exit) {

            printMenu();

            switch (prompt("> ").trim()) {

                case "1" -> viewPantry();

                case "2" -> addItem();

                case "3" -> removeItem();

                case "4" -> viewRecipes();

                case "5" -> checkWhatICanCook();

                case "6" -> cookARecipe();

                case "7" -> scaleRecipe();

                case "0" -> exit = true;

                default  -> System.out.println("  Invalid option.");

            }

        }

        savePantry();

        scanner.stopScanning();

        System.out.println("  Pantry saved. Goodbye!\n");

    }

    private void viewPantry() {

        List<PantryItem> items = pantry.getAllItems();

        System.out.println("\n--- Pantry (" + items.size() + " items) ---");

        if (items.isEmpty()) {

            System.out.println("  (empty)");

        } else {

            System.out.printf("  %-20s %6s %-4s  %s%n",

                              "Name", "Qty", "Unit", "Expiry");

            items.forEach(i -> System.out.println("  " + i));

        }

        System.out.println();

    }

    private void addItem() {

        System.out.println("\n--- Add Item ---");

        String name = prompt("  Name       : ").trim().toLowerCase();

        if (name.isEmpty()) { System.out.println("  Cancelled."); return; }

        double    qty  = readDouble("  Quantity   : ");

        String    unit = prompt("  Unit (g/ml/pcs): ").trim();

        LocalDate exp  = readDate("  Expiry (dd-MM-yyyy): ");

        PantryItem item;

        if (prompt("  Perishable? [y/n]: ").trim().equalsIgnoreCase("y")) {

            double temp = readDouble("  Max storage temp (degC): ");

            item = new PerishableItem(name, qty, unit, exp, temp);

        } else {

            item = new PantryItem(name, qty, unit, exp);

        }

        pantry.addItem(item);

        System.out.println("  Added: " + item.getName());

        savePantry();

    }

    private void removeItem() {

        System.out.println("\n--- Remove Item ---");

        String name = prompt("  Item name: ").trim().toLowerCase();

        if (!pantry.has(name)) {

            System.out.println("  Not in pantry."); return;

        }

        pantry.removeItem(name);

        System.out.println("  Removed \"" + name + "\".");

        savePantry();

    }

    private void viewRecipes() {

        System.out.println("\n--- All Recipes ---");

        recipeBook.getAllRecipes().forEach(r -> System.out.println(r));

    }

    private void checkWhatICanCook() {

        System.out.println("\n--- What Can I Cook? ---");

        List<Recipe> list = engine.getCookableRecipes(

            recipeBook.getAllRecipes());

        if (list.isEmpty()) {

            System.out.println("  Nothing matches your current pantry.\n");

        } else {

            list.forEach(r -> System.out.println(

                "  [OK] " + r.getName() + " [" + r.getCategory() + "]"));

            System.out.println();

        }

    }

    private void cookARecipe() {

        System.out.println("\n--- Cook a Recipe ---");

        String name = prompt("  Recipe name: ").trim();

        recipeBook.findByName(name).ifPresentOrElse(recipe -> {

            MatchEngine.MatchResult result = engine.checkMatch(recipe);

            System.out.println(result.getSummary());

            if (result.isFullMatch()) {

                if (prompt("  Deduct ingredients? [y/n]: ")

                        .trim().equalsIgnoreCase("y")) {

                    try {

                        engine.cookRecipe(recipe);

                        System.out.println("  Done! Pantry updated.");

                        savePantry();

                    } catch (IngredientNotFoundException |

                             InsufficientQuantityException e) {

                        System.out.println("  Error: " + e.getMessage());

                    }

                }

            }

        }, () -> System.out.println("  Recipe not found."));

    }

    private void scaleRecipe() {

        System.out.println("\n--- Scale a Recipe ---");

        String name = prompt("  Recipe name: ").trim();

        recipeBook.findByName(name).ifPresentOrElse(recipe -> {

            int servings = (int) readDouble("  How many servings? ");

            try {

                recipe.scaleTo(servings);

                System.out.println(recipe);

                recipe.scaleTo(recipe.getBaseServings());

            } catch (IllegalArgumentException e) {

                System.out.println("  " + e.getMessage());

            }

        }, () -> System.out.println("  Recipe not found."));

    }

    private void loadPantry() {

        try {

            pantry.load(SAVE_FILE);

            if (pantry.itemCount() > 0)

                System.out.println("  Loaded " + pantry.itemCount() +

                                   " item(s) from saved pantry.");

        } catch (IOException e) {

            System.out.println("  Could not load saved pantry: " +

                               e.getMessage());

        }

    }

    private void savePantry() {

        try {

            new java.io.File("data").mkdirs();

            pantry.save(SAVE_FILE);

        } catch (IOException e) {

            System.out.println("  Warning: could not save — " +

                               e.getMessage());

        }

    }

    private String prompt(String msg) {

        System.out.print(msg);

        return sc.nextLine();

    }

    private double readDouble(String msg) {

        while (true) {

            try { return Double.parseDouble(prompt(msg).trim()); }

            catch (NumberFormatException e) {

                System.out.println("  Enter a valid number."); }

        }

    }

    private LocalDate readDate(String msg) {

        while (true) {

            try { return LocalDate.parse(prompt(msg).trim(), DATE_FMT); }

            catch (DateTimeParseException e) {

                System.out.println("  Use format dd-MM-yyyy."); }

        }

    }

    private void printMenu() {

        System.out.println("  1. View pantry");

        System.out.println("  2. Add ingredient");

        System.out.println("  3. Remove ingredient");

        System.out.println("  4. View all recipes");

        System.out.println("  5. What can I cook right now?");

        System.out.println("  6. Cook a recipe (deduct ingredients)");

        System.out.println("  7. Scale a recipe to X servings");

        System.out.println("  0. Exit & save");

        System.out.println();

    }

}
