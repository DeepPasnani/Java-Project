package kitchen.model;

import java.time.LocalDate;

public class PerishableItem extends PantryItem {

    private static final long serialVersionUID = 3L;

    private double maxStorageTempCelsius;

    public PerishableItem(String name, double quantity, String unit,

                          LocalDate expiryDate, double maxStorageTempCelsius) {

        super(name, quantity, unit, expiryDate);

        this.maxStorageTempCelsius = maxStorageTempCelsius;

    }

    public double getMaxStorageTempCelsius() {

        return maxStorageTempCelsius;

    }

    public String getStorageLabel() {

        if (maxStorageTempCelsius <= 4)   return "Refrigerate (<= 4 deg C)";

        if (maxStorageTempCelsius <= -18) return "Freeze (<= -18 deg C)";

        return "Cool storage (<= " + (int) maxStorageTempCelsius + " deg C)";

    }

    @Override

    public String toString() {

        return super.toString() + "  [" + getStorageLabel() + "]";

    }

}
