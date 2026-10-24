import java.util.ArrayList;
import java.util.List;

public class Chef{
    public List<String> getTasks(List <Table> tables){
        ArrayList<String> tasks = new ArrayList<>();
        for (Table table : tables){
            if (table.getCustomer() != null && table.getCustomer().getOrder()!= null &&table.getCustomer().getOrder().getOrderStatus() == OrderStatus.PREPARING){
                tasks.add("Advance a customer order");
                break;
            }
        }
        tasks.add("Wait");
        return tasks;
    }
}