
import java.util.ArrayList;
import java.util.List;

public class Waiter {

    public List<String> getTasks(
            WaitingQueue waitingQueue,
            List<Table> tables) {

        ArrayList<String> tasks = new ArrayList<>();

        boolean tableAvailable = false;
        boolean waitingSeatedCustomer = false;
        boolean orderReady = false;

        for (Table table : tables) {
            if (table.getCustomer() == null) {
                tableAvailable = true;
            }
            else if (table.getCustomer().getOrder() == null) {
                waitingSeatedCustomer = true;
            }
            else if (table.getCustomer().getOrder()
                    .getOrderStatus() == OrderStatus.READY) {
                orderReady = true;
            }
        }

        if (!waitingQueue.getCustomerInQueue().isEmpty()
                && tableAvailable) {
            tasks.add("Seat a queuing customer");
        }

        if (waitingSeatedCustomer) {
            tasks.add("Take seated customer order");
        }

        if (orderReady) {
            tasks.add("Serve a READY order");
        }

        tasks.add("Wait");

        return tasks;
    }

    public boolean seatCustomer(
            Customer cust,
            Table table,
            WaitingQueue waitingQueue) {

        if (cust == null || table == null) {
            return false;
        }

        if (table.getCustomer() != null) {
            return false;
        }

        if (!waitingQueue.getCustomerInQueue().contains(cust)) {
            return false;
        }

        table.fillTable(cust);
        waitingQueue.removeCustomer(cust);
        cust.setCustomerStatus(CustomerStatus.SEATED);

        return true;
    }

    public boolean takeOrder(Table table, MenuItem orderItem) {
        if (table == null || table.getCustomer() == null) {
            return false;
        }

        Customer cust = table.getCustomer();

        if (cust.getOrder() != null ||
                cust.getCustomerStatus() != CustomerStatus.SEATED) {
            return false;
        }

        cust.setOrder(orderItem);
        return true;
    }

    public boolean serveOrder(Table table, int turnNo) {
        if (table == null || table.getCustomer() == null) {
            return false;
        }

        Customer cust = table.getCustomer();
        Order order = cust.getOrder();

        if (order == null ||
                order.getOrderStatus() != OrderStatus.READY) {
            return false;
        }

        order.setOrderStatus(OrderStatus.SERVED);
        cust.markServed(turnNo);

        return true;
    }
}
