import java.io.Serializable;
import java.util.HashMap;


class Admin extends User {
    public Admin(String username, String password) {
        super(username, password);
    }

    public void createCustomerAccount(HashMap<String, User> users, String username, String password) {
    if (users.containsKey(username)) {
        System.out.println("Error: This username already exists");
        return;
    }
    if (password.length() != 5) {
        System.out.println("Error: password should be exactly 5 characters");
        return;
    }
    users.put(username, new Customer(username, password));
    System.out.println("Success: customer account has been created.");
}

    public void removeCustomerAccount(HashMap<String, User> users, String username) {
        if (users.containsKey(username) && users.get(username) instanceof Customer) {
            users.remove(username);
            System.out.println("Customer account removed.");
        } else {
            System.out.println("Customer not found.");
        }
    }
}