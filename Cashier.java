import java.util.List;

public class Cashier {
    public void handlePayments(List<Table> tables){
        for (Table table : tables){
            if (table.getCustomer().getCustomerStatus() == CustomerStatus.READY_TO_PAY){
                // handle payments
                break;
            }
        }
    }
}
