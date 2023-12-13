import java.io.Serializable;


public class Product implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String name;
    private int count;
    private double price;

    public Product(String name, int count, double price) {
        this.name = name;
        this.count = count;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Name: " + name + "\nCount: " + count + "\nPrice: $" + price;
    }

    // ... Any other methods or properties your original class had ...
}