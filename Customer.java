import java.util.List;

public class Customer {
    private int satisfaction;
    private final int customerNo;
    private Order order;
    private CustomerStatus status;

    private int servedTurn;
    private int readyToPayTurn;

    public Customer(int num) {
        customerNo = num;
        satisfaction = 100;
        order = null;
        status = CustomerStatus.WAITING;
        servedTurn = -1;
        readyToPayTurn = -1;
    }

    public int getSatisfaction() {
        return satisfaction;
    }

    public int getCustomerNo() {
        return customerNo;
    }

    public void setSatisfaction(int sat) {
        satisfaction = Math.max(0, Math.min(100, sat));
    }

    public void decreaseSatisfaction(int amount) {
        setSatisfaction(satisfaction - amount);
    }

    public String getTypeLabel() {
        return "Regular";
    }

    public int getWaitingSatisfactionLoss() {
        return 8;
    }

    public void applyWaitingPenalty() {
        decreaseSatisfaction(getWaitingSatisfactionLoss());
    }

    public MenuItem chooseFood(List<MenuItem> menu) {
        if (menu == null || menu.isEmpty()) {
            throw new IllegalArgumentException("Menu cannot be empty");
        }
        return menu.get((customerNo - 1) % menu.size());
    }

    public int calculateBillCents(MenuItem item) {
        return item.getPrice();
    }

    public void setOrder(MenuItem orderItem) {
        setOrder(orderItem, calculateBillCents(orderItem));
    }

    public void setOrder(MenuItem orderItem, int billCents) {
        order = new Order(orderItem, billCents);
    }

    public Order getOrder() {
        return order;
    }

    public CustomerStatus getCustomerStatus() {
        return status;
    }

    public void setCustomerStatus(CustomerStatus s) {
        status = s;
    }

    public int getServedTurn() {
        return servedTurn;
    }

    public void markServed(int turnNo) {
        servedTurn = turnNo;
        status = CustomerStatus.SERVED;
    }

    public int getReadyToPayTurn() {
        return readyToPayTurn;
    }

    public void markReadyToPay(int turnNo) {
        readyToPayTurn = turnNo;
        status = CustomerStatus.READY_TO_PAY;
    }
}
