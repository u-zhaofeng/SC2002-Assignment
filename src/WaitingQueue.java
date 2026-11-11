
import java.util.ArrayList;
import java.util.List;

public class WaitingQueue {
    private final List<Customer> customerInQueue =
            new ArrayList<>();

    public List<Customer> getCustomerInQueue() {
        return customerInQueue;
    }

    public void addCustomer(Customer cust) {
        customerInQueue.add(cust);
    }

    public void removeCustomer(Customer cust) {
        customerInQueue.remove(cust);
    }

    public Customer findCustomer(int customerNo) {
        for (Customer cust : customerInQueue) {
            if (cust.getCustomerNo() == customerNo) {
                return cust;
            }
        }

        return null;
    }


}
