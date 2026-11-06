import java.util.List;

public class HappyHourPolicy {
    private static final int DURATION_TURNS = 2;
    private static final int RECOVERY = 15;
    private int activatedTurn = -1;

    public boolean hasBeenActivated() {
        return activatedTurn != -1;
    }

    public boolean activate(int turnNo) {
        if (hasBeenActivated() || turnNo < 1) {
            return false;
        }
        activatedTurn = turnNo;
        return true;
    }

    public boolean isActive(int turnNo) {
        return hasBeenActivated()
                && turnNo >= activatedTurn
                && turnNo < activatedTurn + DURATION_TURNS;
    }

    public int remainingTurns(int turnNo) {
        if (!isActive(turnNo)) {
            return 0;
        }
        return activatedTurn + DURATION_TURNS - turnNo;
    }

    public int billCents(Customer customer, MenuItem item, int turnNo) {
        int customerPrice = customer.calculateBillCents(item); // VIP reduction first
        if (!isActive(turnNo)) {
            return customerPrice;
        }
        return (int) (((long) customerPrice * 80 + 50) / 100);
    }

    public void recoverWaitingCustomers(WaitingQueue queue, List<Table> tables, int turnNo) {
        if (!isActive(turnNo)) {
            return;
        }
        for (Customer customer : queue.getCustomerInQueue()) {
            customer.setSatisfaction(customer.getSatisfaction() + RECOVERY);
        }
        for (Table table : tables) {
            Customer customer = table.getCustomer();
            if (customer != null && customer.getCustomerStatus() == CustomerStatus.SEATED) {
                customer.setSatisfaction(customer.getSatisfaction() + RECOVERY);
            }
        }
    }
}
