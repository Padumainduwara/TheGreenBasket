package greenbasket;

// Inheritance: Inherits from SalesAssistant/User
public class StoreManager extends SalesAssistant { 
    public StoreManager(String username, String password) {
        super(username, password);
        this.role = "Store Manager"; // Specific Role
    }
}