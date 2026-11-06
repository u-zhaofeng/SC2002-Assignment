import java.util.List;

public class CriticCustomer extends Customer {
    public CriticCustomer(int customerNo) {
        super(customerNo);
    }

    @Override
    public String getTypeLabel() {
        return "Critic";
    }

    @Override
    public int getWaitingSatisfactionLoss() {
        return 12;
    }

    @Override
    public MenuItem chooseFood(List<MenuItem> menu) {
        for (MenuItem item : menu) {
            if ("Burger".equalsIgnoreCase(item.getName())) {
                return item;
            }
        }
        return super.chooseFood(menu);
    }
}
