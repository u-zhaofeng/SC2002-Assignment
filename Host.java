import java.util.ArrayList;
import java.util.List;

public class Host {
    private final SeatingService seating = new SeatingService();

    public List<String> getTasks(WaitingQueue queue, List<Table> tables) {
        ArrayList<String> tasks = new ArrayList<>();
        if (!queue.getCustomerInQueue().isEmpty()) {
            for (Table table : tables) {
                if (table.getCustomer() == null) {
                    tasks.add("Seat a queuing customer");
                    break;
                }
            }
        }
        tasks.add("Wait");
        return tasks;
    }

    public boolean seatCustomer(Customer customer, Table table, WaitingQueue queue) {
        return seating.seatCustomer(customer, table, queue);
    }
}
