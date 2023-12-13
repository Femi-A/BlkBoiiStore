import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.util.Random;



class StoreApp implements Serializable {
    private HashMap<String, User> users = new HashMap<>();
    private Scanner scanner = new Scanner(System.in);
    private List<Product> inventory = new ArrayList<>();
    
    private User currentUser = null;  // Will be assigned when a user logs in


    public StoreApp() {
        
        // Add default admin user
        users.put("admin", new Admin("admin", "12345"));
        inventory.add(new Product("puff puff", 50, 2.50));
        inventory.add(new Product("cheese flavor", 30, 2.00));
        inventory.add(new Product("supa Hot", 200, 3.50));
    }
    
    private void saveState() {
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("storeAppData.ser"))) {
        oos.writeObject(this);
    } catch (IOException e) {
        e.printStackTrace();
    }
}

private static StoreApp loadState() {
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("storeAppData.ser"))) {
        return (StoreApp) ois.readObject();
    } catch (Exception e) {
        return null;
    }
}


private String login() { 
    System.out.print("Username: ");
    String username = scanner.nextLine().trim().toLowerCase();
    System.out.print("Password: ");
    String password = scanner.nextLine();

    if (users.containsKey(username) && users.get(username).password.equals(password)) {
        return username;
    } else {
        System.out.println("Invalid username or password!");
        return null;
    }
}

public void run() {
    while (true) {
        System.out.println("******** Welcome to BlkBoii Store ********");
        System.out.println("1- Login");
        System.out.println("2- Register");
        int choice = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        switch (choice) {
            case 1:
               String loggedInUsername = login();
                    if (loggedInUsername != null) {
                        currentUser = users.get(loggedInUsername);  // Setting the currentUser after successful login
                        if (currentUser instanceof Admin) {
                            while(displayAdminMenu()); // Stay in admin menu
                            currentUser = null;  // Resetting the currentUser after logout
                        } else if (currentUser instanceof Customer) {
                            while(displayCustomerMenu()); // Stay in customer menu
                            currentUser = null;  // Resetting the currentUser after logout
                        } else {
                            System.out.println("Unrecognized user type.");
                        }
                    }
                break;
            case 2:
                register();
                break;
            default:
                System.out.println("Invalid choice!");
            
        }
    }
}


    private void register() {
        System.out.print("Username: ");
        String username = getUsername();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.println("Register as (1- Admin, 2- Customer): ");
        int choice = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        if (users.containsKey(username)) {
            System.out.println("User already exists!");
            return;
        }

        switch (choice) {
            case 1:
                users.put(username, new Admin(username, password));
                break;
            case 2:
                users.put(username, new Customer(username, password));
                break;
            default:
                System.out.println("Invalid choice!");
        }
    }

    private String getUsername() {
        return scanner.nextLine().trim().toLowerCase();
    }

    private boolean displayAdminMenu() {
        System.out.println("******** Admin Menu ********");
        System.out.println("1. Create a customer account");
        System.out.println("2. Remove a customer account");
        System.out.println("3. View inventory");
        System.out.println("4. Add a product");
        System.out.println("5. Remove a product");
        System.out.println("6. Restock a product");
        System.out.println("7. Logout");

        int choice = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        switch (choice) {
            case 1:
    System.out.println("******** Create a customer account ********");
    System.out.print("Enter username for new customer: ");
    String username = scanner.nextLine();
    
    while(users.containsKey(username)) {
        System.out.println("Error: This username already exists");
        System.out.print("Enter username for new customer: ");
        username = scanner.nextLine();
    }
    
    System.out.print("Enter password for new customer: ");
    String password = scanner.nextLine();
    while(password.length() != 5) { // Keep prompting until a valid password is entered
        System.out.println("Error: password should be exactly 5 characters");
        System.out.print("Enter password for new customer: ");
        password = scanner.nextLine();
    }
    ((Admin) users.get("admin")).createCustomerAccount(users, username, password);
    break;

            case 2:
               System.out.println("******** Remove a customer account ********");
            System.out.print("Enter username of customer to remove: ");
            username = scanner.nextLine();  // <-- Notice we removed the 'String' data type declaration here.
            while (!users.containsKey(username) || !(users.get(username) instanceof Customer)) {
                System.out.println("Error: This username doesn't exist");
                System.out.print("Enter username of customer to remove: ");
                username = scanner.nextLine();
            }
            users.remove(username);
            System.out.println("Success: customer account has been removed.");
            break;
            case 3:
                viewInventory();
                break;
            case 4:
                addProduct();
                break;
            case 5:
                removeProduct();
                break;
            case 6:
                restockProduct();
                break;
             case 7:
            return false;  // Logout and exit the loop
    }
    return true; // By default, stay in the menu loop
}

private boolean displayCustomerMenu() {
    System.out.println("******** Customer Menu ********");
    System.out.println("1. Shop the store");
    System.out.println("2. View and checkout shopping cart");
    System.out.println("3. View balance");
    System.out.println("4. Add balance");
    System.out.println("5. Logout");

    int choice = scanner.nextInt();
    scanner.nextLine();  // Consume newline
    switch (choice) {
        case 1:
            shopStore();
            break;
        case 2:
            viewAndCheckoutShoppingCart();
            break;
        case 3:
            viewBalance();
            break;
        case 4:
            addBalanceMenu();
            break;
        case 5:
            return false;  // Logout and exit the loop
        default:
            System.out.println("Invalid choice!");
    }
    return true; // By default, stay in the menu loop
}

private void shopStore() {
    System.out.println("******** Shop the store ********");
    
    for (Product product : inventory) {
        System.out.println("\nName: " + product.getName());
        System.out.println("Count: " + product.getCount());
        System.out.printf("Price: $%.2f\n", product.getPrice());
    }

    System.out.println("Enter the product you want to add to your cart:");
    String productName = scanner.nextLine();

    Product selectedProduct = null;
    
    for (Product product : inventory) {
        if (product.getName().equalsIgnoreCase(productName)) {
            selectedProduct = product;
            break;
        }
    }

    if (selectedProduct == null) {
        System.out.println("Product not found!");
        return;
    }

    System.out.println("Enter quantity:");
    int quantity = scanner.nextInt();
    scanner.nextLine(); // Consume newline

    if(currentUser == null || !(currentUser instanceof Customer)) {
        System.out.println("Error: No user is currently logged in or the logged-in user is not a customer.");
        return;
    }

    // Cast currentUser to Customer. This is the only definition and casting needed.
    Customer currentCustomer = (Customer) currentUser;

    // Create the CartItem and add it to the cart
    CartItem itemToAdd = new CartItem(selectedProduct, quantity);  
    currentCustomer.addToCart(itemToAdd);
    
    System.out.println("Success: The product has been added to your shopping cart!");
}


private void viewAndCheckoutShoppingCart() {
    System.out.println("******** View and checkout shopping cart ********");

    if (currentUser == null || !(currentUser instanceof Customer)) {
        System.out.println("Error: No user is currently logged in or the logged-in user is not a customer.");
        return;
    }

    Customer currentCustomer = (Customer) currentUser;
    List<CartItem> cart = currentCustomer.getCart();

    if (cart.isEmpty()) {
        System.out.println("Your cart is empty!");
        return;
    }

    double total = 0;
    for (CartItem cartItem : cart) {
        Product product = cartItem.getProduct();
        int quantity = cartItem.getQuantity();
        System.out.println("\nName: " + product.getName());
        System.out.println("Count: " + quantity);
        System.out.printf("Price: $%.2f\n", product.getPrice());
        total += product.getPrice() * quantity;
    }

    System.out.printf("\nYour balance: $%.2f\n", currentCustomer.getBalance());
    System.out.printf("\nTotal: $%.2f\n", total);

    System.out.println("\nCheckout (Y/N)?");
    char choice = scanner.nextLine().trim().charAt(0);
    if (choice == 'Y' || choice == 'y') {
        if (currentCustomer.getBalance() < total) {
            System.out.println("Thank you for shopping at The BlkBoii Store");
            return;
        }
        currentCustomer.deductAmount(total);
        currentCustomer.clearCart(); // Assuming you have a method in Customer to clear the cart after checkout
        System.out.println("\nThank you for shopping at The BlkBoii Store");
        System.out.printf("Your new balance: $%.2f\n", currentCustomer.getBalance());
    }
}


private void viewBalance() {
    // Logic to display the current balance of the logged-in customer
}

private void addBalanceMenu() {
    System.out.println("******** Add balance menu ********");
    
    if(currentUser == null || !(currentUser instanceof Customer)) {
        System.out.println("Error: No user is currently logged in or the logged-in user is not a customer.");
        return;
    }
    
    System.out.println("Welcome to the number guessing game!");
    System.out.println("Guess the number between 1 and 10 to win $100!");
    System.out.println("You have 3 attempts.");

    int randomNumber = new Random().nextInt(10) + 1; // Generates a number between 1 and 10
    boolean hasWon = false;
    
    for (int i = 0; i < 3; i++) {
        System.out.print("Enter your guess: ");
        int guess = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        
        if (guess < 1 || guess > 10) {
            System.out.println("Please guess a number between 1 and 10.");
            continue;
        }

        if (guess == randomNumber) {
            hasWon = true;
            break;
        }

        if (guess < randomNumber) {
            System.out.println("Guess higher!");
        } else {
            System.out.println("Guess lower!");
        }
    }

    if (hasWon) {
        System.out.println("Congratulations! You've guessed the right number!");
        Customer currentCustomer = (Customer) currentUser;
        currentCustomer.addBalance(100);
        System.out.println("You've won $100! Your new balance is: $" + currentCustomer.getBalance());
    } else {
        System.out.println("Sorry, you didn't guess the correct number. Better luck next time! The number was: " + randomNumber);
    }
}


private void viewInventory() {
    System.out.println("******** View inventory ********");
    for (Product product : inventory) {
        System.out.println("\nName: " + product.getName());
        System.out.println("Count: " + product.getCount());
        System.out.printf("Price: $%.2f\n", product.getPrice());
    }
}



private void addProduct() {
    System.out.println("Enter product name:");
    String name = scanner.nextLine();
    System.out.println("Enter product count:");
    int count = scanner.nextInt();
    scanner.nextLine();  // Consume newline
    System.out.println("Enter product price:");
    double price = scanner.nextDouble();
    scanner.nextLine();  // Consume newline

    inventory.add(new Product(name, count, price));
    System.out.println("Product added successfully!");
}
    
private void removeProduct() {
    System.out.println("******** Remove a product ********");
    System.out.print("Enter product name: ");
    String productName = scanner.nextLine();

    // Check if the product exists in the inventory
    Product productToRemove = null;
    for (Product product : inventory) {
        if (product.getName().equalsIgnoreCase(productName)) {
            productToRemove = product;
            break;
        }
    }

    // If product does not exist, display an error
    if (productToRemove == null) {
        System.out.println("Error: Product not found in the inventory.");
        return;
    }

    System.out.print("Enter new count: ");
    int count = scanner.nextInt();
    scanner.nextLine();  // Consume newline
    System.out.print("Enter price: $");
    double price = scanner.nextDouble();
    scanner.nextLine();  // Consume newline

    // Remove the product from the inventory list
    inventory.remove(productToRemove);
    
    System.out.println("Success: Product has been removed from inventory.");
}


private void restockProduct() {
    System.out.println("******** Restock inventory ********");
    
    System.out.print("Enter product name: ");
    String productName = scanner.nextLine();
    
    Product productToRestock = null;
    for (Product product : inventory) {
        if (product.getName().equalsIgnoreCase(productName)) {
            productToRestock = product;
            break;
        }
    }
    
    if (productToRestock == null) {
        System.out.println("Error: This product doesn't exist in the inventory.");
        return;
    }
    
    System.out.print("update count: ");
    int updatedCount = scanner.nextInt();
    scanner.nextLine();  // Consume newline left over by nextInt
    
    System.out.print("update price: $");
    double updatedPrice = scanner.nextDouble();
    scanner.nextLine();  // Consume newline left over by nextDouble
    
    productToRestock.setCount(updatedCount);
    productToRestock.setPrice(updatedPrice);

    System.out.println("Success: inventory has been stocked.");
}


}
