package idv.david.foodgodapp;

public class Customer {
    private int customerNo;
    private String customerId;
    private int customerPassword;

    public Customer() {
    }

    public Customer(int customerNo, String customerId, int customerPassword) {
        this.customerNo = customerNo;
        this.customerId = customerId;
        this.customerPassword = customerPassword;
    }

    public int getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(int customerNo) {
        this.customerNo = customerNo;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public int getCustomerPassword() {
        return customerPassword;
    }

    public void setCustomerPassword(int customerPassword) {
        this.customerPassword = customerPassword;
    }


}
