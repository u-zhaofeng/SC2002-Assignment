public class Order {
    private MenuItem orderItem;
    private final int billCents;
    private int unitsDone;
    private OrderStatus orderStatus;

    public Order(MenuItem item) {
        this(item, item.getPrice());
    }

    public Order(MenuItem item, int billCents) {
        if (item == null || billCents < 0) {
            throw new IllegalArgumentException("Invalid order item or bill");
        }
        orderItem = item;
        this.billCents = billCents;
        unitsDone = 0;
        orderStatus = OrderStatus.PREPARING;
    }

    public int getBillCents() {
        return billCents;
    }

    public MenuItem getOrderItem() {
        return orderItem;
    }

    public boolean advanceOrder() {
        if (orderStatus != OrderStatus.PREPARING) {
            return false;
        }

        unitsDone++;

        if (unitsDone >= orderItem.getPreparationUnits()) {
            orderStatus = OrderStatus.READY;
        }

        return true;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public int getUnitsDone() {
        return unitsDone;
    }

    public void setOrderStatus(OrderStatus status) {
        orderStatus = status;
    }
}
