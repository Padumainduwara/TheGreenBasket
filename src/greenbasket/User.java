package greenbasket;

import java.io.Serializable;

// Abstraction: Cannot create direct 'User' objects
public abstract class User implements Serializable {
    protected String username;
    protected String password;
    protected String role;

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public boolean login(String uname, String pwd) {
        return this.username.equals(uname) && this.password.equals(pwd);
    }
    
    public String getRole() { return role; }
    public String getUsername() { return username; }
}