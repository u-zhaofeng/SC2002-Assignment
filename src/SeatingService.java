public class SeatingService {
    public boolean seatCustomer(Customer customer, Table table, WaitingQueue queue) {
        if (customer == null || table == null || queue == null) {
            return false;
        }
        if (table.getCustomer() != null || !queue.getCustomerInQueue().contains(customer)) {
            return false;
        }
        table.fillTable(customer);
        queue.removeCustomer(customer);
        customer.setCustomerStatus(CustomerStatus.SEATED);
        return true;
    }
}
