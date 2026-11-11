
import java.util.ArrayList;
import java.util.List;

public class Chef {

    public List<String> getTasks(List<Table> tables) {
        ArrayList<String> tasks = new ArrayList<>();

        for (Table table : tables) {
            if (table.getCustomer() != null &&
                    table.getCustomer().getOrder() != null &&
                    table.getCustomer().getOrder()
                            .getOrderStatus() == OrderStatus.PREPARING) {

                tasks.add("Advance order at Table "
                        + table.getTableNo());
            }
        }

        tasks.add("Wait");
        return tasks;
    }

    public boolean prepareOrder(Table table) {
        if (table == null || table.getCustomer() == null) {
            return false;
        }

        Order order = table.getCustomer().getOrder();

        if (order == null ||
                order.getOrderStatus() != OrderStatus.PREPARING) {
            return false;
        }

        return order.advanceOrder();
    }
}
