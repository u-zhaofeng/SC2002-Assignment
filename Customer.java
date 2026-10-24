public class Customer {
    private int satisfaction;
    private final int customerNo;
    private Order order;
    private CustomerStatus status;

    public Customer(int num) {
        customerNo = num;
        satisfaction = 100;
        order = null;
    }
    public int getSatisfaction() {
        return satisfaction;
    }
    public int getCustomerNo() {
        return customerNo;
    }
    public void setSatisfaction(int sat) {
        satisfaction = sat;
    }
    public void decreaseSatisfaction(int amount){
        satisfaction -= amount;
    }
    public void setOrder(MenuItem orderItem) {
        order = new Order(orderItem);
    }
    public Order getOrder() {
        return order;
    }
    public CustomerStatus getCustomerStatus(){
        return status;
    }
    public void setCustomerStatus(CustomerStatus s){
        status = s;
    }
}