package kitchen.model;

import java.io.Serializable;

public class Ingredient implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private double quantity;

    private String unit;

    public Ingredient(String name, double quantity, String unit) {

        if (name == null || name.isBlank())

            throw new IllegalArgumentException("Ingredient name cannot be empty.");

        if (quantity < 0)

            throw new IllegalArgumentException("Quantity cannot be negative.");

        this.name     = name.trim().toLowerCase();

        this.quantity = quantity;

        this.unit     = unit;

    }

    public String getName()               { return name; }

    public double getQuantity()           { return quantity; }

    public void   setQuantity(double qty) { this.quantity = Math.max(0, qty); }

    public String getUnit()               { return unit; }

    public void consume(double amount) {

        this.quantity -= amount;

    }

    @Override

    public boolean equals(Object o) {

        if (this == o) return true;

        if (!(o instanceof Ingredient)) return false;

        return this.name.equalsIgnoreCase(((Ingredient) o).name);

    }

    @Override

    public int hashCode() { return name.toLowerCase().hashCode(); }

    @Override

    public String toString() {

        return String.format("%-20s %6.1f %-4s", name, quantity, unit);

    }

}
