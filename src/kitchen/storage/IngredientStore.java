package kitchen.storage;

import kitchen.model.Ingredient;

import java.util.ArrayList;

import java.util.List;

import java.util.Optional;

public class IngredientStore<T extends Ingredient> {

    private final List<T> items = new ArrayList<>();

    public void add(T item) {

        Optional<T> existing = find(item.getName());

        if (existing.isPresent()) {

            existing.get().setQuantity(

                existing.get().getQuantity() + item.getQuantity());

        } else {

            items.add(item);

        }

    }

    public void remove(String name) {

        items.removeIf(i -> i.getName().equalsIgnoreCase(name));

    }

    public Optional<T> find(String name) {

        return items.stream()

                    .filter(i -> i.getName().equalsIgnoreCase(name))

                    .findFirst();

    }

    public List<T> getAll()  { return new ArrayList<>(items); }

    public boolean isEmpty() { return items.isEmpty(); }

    public int     size()    { return items.size(); }

    public void    clear()   { items.clear(); }

}
