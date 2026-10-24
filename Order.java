
public class Order {
    private MenuItem orderItem;
    private int unitsDone;
    private OrderStatus orderStatus;

    public Order(MenuItem item){
        orderItem = item;
        unitsDone = 0;
        orderStatus = OrderStatus.PREPARING;
    }

    public MenuItem getOrderItem(){
        return orderItem;
    }

    public void advanceOrder(){
        unitsDone++;
    }

    public OrderStatus getOrderStatus(){
        return orderStatus;
    }

    public int getUnitsDone(){
        return unitsDone;
    }

    
    public void setOrderStatus(OrderStatus status){
        orderStatus = status;
    }
}

