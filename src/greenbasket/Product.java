package greenbasket;

import java.io.Serializable;

public class Product implements Serializable {
    // Encapsulation: Variables are private
    private String productID;
    private String name;
    private String category;
    private String supplier; 
    private double price;
    private int quantity;

    // Constructor to initialize product
    public Product(String productID, String name, String category, String supplier, double price, int quantity) {
        this.productID = productID;
        this.name = name;
        this.category = category;
        this.supplier = supplier; 
        this.price = price;
        this.quantity = quantity;
    }

    // Getters
    public String getProductID() { return productID; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getSupplier() { return supplier; } 
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }

    // Setter for Quantity (for updates)
    public void setQuantity(int quantity) { this.quantity = quantity; }

    // Helper to show data in Table
    public Object[] toRowData() {
        return new Object[]{productID, name, category, supplier, price, quantity};
    }
    
    @Override
    public String toString() {
        return "ID: " + productID + " | " + name;
    }
}