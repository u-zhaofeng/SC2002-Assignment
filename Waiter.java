import java.util.ArrayList;
import java.util.List;


public class Waiter{
    public List<String> getTasks(WaitingQueue waitingQueue, List <Table> tables){
        // find all tasks for waiter
        ArrayList<String> tasks = new ArrayList<>();
        boolean tableAvailable = false, waitingSeatedCustomer = false, orderReady = false;
        for (Table table : tables){
            if (table.getCustomer() == null){
                tableAvailable = true;
            }
            else if(table.getCustomer().getOrder() == null){
                waitingSeatedCustomer = true;
            }
            else if(table.getCustomer().getOrder().getOrderStatus() == OrderStatus.READY){
                orderReady = true;
            }
        }
        if (!waitingQueue.getCustomerInQueue().isEmpty() && tableAvailable){
            tasks.add("Seat a queuing customer");
        }
        if (waitingSeatedCustomer){
            tasks.add("Take seated customer order");
        }
        if (orderReady){
            tasks.add("Serve a READY order");
        }  
        tasks.add("Wait");
        return tasks;
    }
}