package kitchen.model;

import java.time.LocalDate;

import java.time.format.DateTimeFormatter;

public class PantryItem extends Ingredient {

    private static final long serialVersionUID = 2L;

    private static final DateTimeFormatter FMT =

        DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private LocalDate expiryDate;

    public PantryItem(String name, double quantity,

                      String unit, LocalDate expiryDate) {

        super(name, quantity, unit);

        this.expiryDate = expiryDate;

    }

    public LocalDate getExpiryDate() { return expiryDate; }

    public boolean isExpired() {

        return LocalDate.now().isAfter(expiryDate);

    }

    public boolean expiresSoon(int withinDays) {

        return !isExpired() &&

               !LocalDate.now().plusDays(withinDays).isBefore(expiryDate);

    }

    @Override

    public String toString() {

        String status = isExpired() ? " [EXPIRED]"

                      : expiresSoon(3) ? " [expires soon]" : "";

        return super.toString() + "  exp: " +

               expiryDate.format(FMT) + status;

    }

}
