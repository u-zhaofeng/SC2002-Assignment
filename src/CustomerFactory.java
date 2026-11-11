public class CustomerFactory {
    public Customer createCustomer(int customerNo) {
        switch (customerNo) {
            case 2:
                return new VIPCustomer(customerNo);
            case 3:
                return new CriticCustomer(customerNo);
            default:
                return new Customer(customerNo);
        }
    }
}
