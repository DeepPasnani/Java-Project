package kitchen.util;

import kitchen.model.Pantry;

import kitchen.model.PantryItem;

import java.util.List;

public class ExpiryScanner extends Thread {

    private final Pantry   pantry;

    private volatile boolean running = true;

    private static final long SCAN_INTERVAL_MS = 5_000;

    public ExpiryScanner(Pantry pantry) {

        super("ExpiryScanner");

        this.pantry = pantry;

        setDaemon(true);

    }

    public void stopScanning() { running = false; interrupt(); }

    @Override

    public void run() {

        while (running) {

            try {

                Thread.sleep(SCAN_INTERVAL_MS);

                scan();

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();

                break;

            }

        }

    }

    private void scan() {

        List<PantryItem> items = pantry.getAllItems();

        boolean anyAlert = false;

        for (PantryItem item : items) {

            if (item.isExpired()) {

                alert("EXPIRED  : " + item.getName() +

                      " (expired " + item.getExpiryDate() + ")");

                anyAlert = true;

            } else if (item.expiresSoon(3)) {

                alert("EXP SOON : " + item.getName() +

                      " expires on " + item.getExpiryDate());

                anyAlert = true;

            }

        }

        if (anyAlert) System.out.print("\n> ");

    }

    private void alert(String msg) {

        System.out.println("\r[Expiry Scanner] " + msg);

    }

}
