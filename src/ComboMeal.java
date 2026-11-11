import java.util.Objects;

public class ComboMeal extends MenuItem {
    private final MenuItem first;
    private final MenuItem second;

    public ComboMeal(MenuItem first, MenuItem second) {
        super("ComboMeal (" + Objects.requireNonNull(first, "first item").getName()
                        + " + " + Objects.requireNonNull(second, "second item").getName() + ")",
                first.getPreparationUnits() + second.getPreparationUnits(),
                (int) (((long) first.getPrice() + second.getPrice()) * 90 + 50) / 100);
        this.first = first;
        this.second = second;
    }

    public MenuItem getFirstItem() {
        return first;
    }

    public MenuItem getSecondItem() {
        return second;
    }
}
