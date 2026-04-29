package kitchen.exception;

public class InsufficientQuantityException extends Exception {

    private final String ingredientName;

    private final double required;

    private final double available;

    public InsufficientQuantityException(String ingredientName,

                                          double required, double available) {

        super(String.format(

            "Not enough \"%s\": need %.1f, only have %.1f",

            ingredientName, required, available

        ));

        this.ingredientName = ingredientName;

        this.required       = required;

        this.available      = available;

    }

    public String getIngredientName() { return ingredientName; }

    public double getRequired()       { return required; }

    public double getAvailable()      { return available; }

}
