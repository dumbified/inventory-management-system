package models;

// model class for sales record and provides access to its data through getter methods.

import java.util.Date;

public class Sales {
    private int id;
    private String customer;
    private int productId;
    private int quantity;
    private double totalPrice;
    private String paymentMethod;
    private Date date;

    // Constructor
    public Sales(int id, String customer, int productId, int quantity, double totalPrice, String paymentMethod, Date date) {
        this.id = id;
        this.customer = customer;
        this.productId = productId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.paymentMethod = paymentMethod;
        this.date = date;
    }

    // Constructor without ID
    public Sales(String customer, int productId, int quantity, double totalPrice, String paymentMethod, Date date) {
        this.customer = customer;
        this.productId = productId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.paymentMethod = paymentMethod;
        this.date = date;
    }

    // Getters
    public int getId() { return id; }
    public String getCustomer() { return customer; }
    public int getProductId() { return productId; }
    public int getQuantity() { return quantity; }
    public double getTotalPrice() { return totalPrice; }
    public String getPaymentMethod() { return paymentMethod; }
    public Date getDate() { return date; }
}
