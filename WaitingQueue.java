import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class WaitingQueue{
    private final List<Customer> customerInQueue = new ArrayList<>();

    public List<Customer> getCustomerInQueue(){
        return customerInQueue;
    }

    public void addCustomer(Customer cust){
        customerInQueue.add(cust);
    }

    public void removeCustomer(Customer cust){
        customerInQueue.remove(cust);
    }
}