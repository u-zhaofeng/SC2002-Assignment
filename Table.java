public class Table{
    private Customer customer;
    private int tableNo;
    public Table(int num) {
        tableNo = num;
        customer = null;
    }
    public Customer getCustomer() {
        return customer;
    }
    public int getTableNo() {
        return tableNo;
    }

    public void clearTable(){
        customer = null;
    }
    
    public void fillTable(Customer cust){
        customer = cust;
    }
}