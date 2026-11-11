public class VIPCustomer extends Customer {
    public VIPCustomer(int customerNo) {
        super(customerNo);
    }

    @Override
    public String getTypeLabel() {
        return "VIP";
    }

    @Override
    public int getWaitingSatisfactionLoss() {
        return 5;
    }

    @Override
    public int calculateBillCents(MenuItem item) {
        return (item.getPrice() * 90 + 50) / 100;
    }
}
