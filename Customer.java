import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

class Customer extends User {
    private Map<Product, Integer> shoppingCart = new HashMap<>();
    private double balance = 0.0;
    private List<CartItem> cart = new ArrayList<>();

    public Customer(String username, String password) {
        super(username, password);
    }

    public List<CartItem> getCart() {
        return cart;
    }

    public void addToCart(CartItem item) {
        cart.add(item);
    }

    public void clearCart() {
        cart.clear();
    }


    public double getBalance() {
        return balance;
    }

    public void addBalance(double amount) {
        this.balance += amount;
    }

 public void deductAmount(double amount) {
        if (amount <= balance) {
            balance -= amount;
        } else {
            System.out.println("Error: Insufficient balance.");
        }
    }
}
