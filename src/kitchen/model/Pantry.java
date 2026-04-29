package kitchen.model;

import kitchen.exception.IngredientNotFoundException;

import kitchen.exception.InsufficientQuantityException;

import kitchen.storage.IngredientStore;

import kitchen.storage.Saveable;

import java.io.*;

import java.util.List;

public class Pantry implements Saveable {

    private IngredientStore<PantryItem> store = new IngredientStore<>();

    public void addItem(PantryItem item)  { store.add(item); }

    public void removeItem(String name)   { store.remove(name); }

    public List<PantryItem> getAllItems() { return store.getAll(); }

    public int  itemCount()              { return store.size(); }

    public boolean has(String name)      { return store.find(name).isPresent(); }

    public double getQuantity(String ingredientName)

            throws IngredientNotFoundException {

        return store.find(ingredientName)

                    .orElseThrow(

                        () -> new IngredientNotFoundException(ingredientName))

                    .getQuantity();

    }

    public void consume(String ingredientName, double amount)

            throws IngredientNotFoundException, InsufficientQuantityException {

        PantryItem item = store.find(ingredientName)

                               .orElseThrow(

                                   () -> new IngredientNotFoundException(

                                       ingredientName));

        if (item.getQuantity() < amount)

            throw new InsufficientQuantityException(

                ingredientName, amount, item.getQuantity());

        item.consume(amount);

        if (item.getQuantity() == 0)

            store.remove(ingredientName);

    }

    @Override

    public void save(String filePath) throws IOException {

        try (ObjectOutputStream oos = new ObjectOutputStream(

                new BufferedOutputStream(

                    new FileOutputStream(filePath)))) {

            oos.writeObject(store.getAll());

        }

    }

    @SuppressWarnings("unchecked")

    @Override

    public void load(String filePath) throws IOException {

        File f = new File(filePath);

        if (!f.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(

                new BufferedInputStream(

                    new FileInputStream(filePath)))) {

            List<PantryItem> saved = (List<PantryItem>) ois.readObject();

            store.clear();

            saved.forEach(store::add);

        } catch (ClassNotFoundException e) {

            throw new IOException("Save file is corrupt or outdated.", e);

        }

    }

}
