
import java.util.List;

public class Cashier {

    public Customer handlePayments(
            List<Table> tables, int turnNo) {

        Table selectedTable = null;

        for (Table table : tables) {
            Customer cust = table.getCustomer();

            if (cust == null) {
                continue;
            }

            if (cust.getCustomerStatus()
                    != CustomerStatus.READY_TO_PAY) {
                continue;
            }

            if (cust.getReadyToPayTurn() >= turnNo) {
                continue;
            }

            if (selectedTable == null) {
                selectedTable = table;
            }
            else {
                Customer selectedCustomer =
                        selectedTable.getCustomer();

                if (cust.getReadyToPayTurn()
                        < selectedCustomer.getReadyToPayTurn()) {
                    selectedTable = table;
                }
                else if (cust.getReadyToPayTurn()
                        == selectedCustomer.getReadyToPayTurn()
                        && cust.getCustomerNo()
                        < selectedCustomer.getCustomerNo()) {
                    selectedTable = table;
                }
            }
        }

        if (selectedTable == null) {
            return null;
        }

        Customer cust = selectedTable.getCustomer();

        cust.getOrder().setOrderStatus(OrderStatus.PAID);
        cust.setCustomerStatus(CustomerStatus.PAID);

        selectedTable.clearTable();

        return cust;
    }
}
