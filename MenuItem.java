public class MenuItem {
    private final String name;
    private final int preparationUnits;
    private final int priceCents;

    public MenuItem(String name, int preparationUnits, int priceCents) {
        this.name = name;
        this.preparationUnits = preparationUnits;
        this.priceCents = priceCents;
    }
    public String getName() {
        return name;
    }
    public int getPreparationUnits() {
        return preparationUnits;
    }

    public int getPrice(){
        return priceCents;
    }
}