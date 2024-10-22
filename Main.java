import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        InventorySystem inventorySystem = new InventorySystem();
        int choice;

        // Sample products to prepopulate inventory
        inventorySystem.addProduct("PT01", "T-Shirt", "Tops", 50, "M", "Red");
        inventorySystem.addProduct("PT01", "T-Shirt", "Tops", 30, "L", "Red");
        inventorySystem.addProduct("PT02", "Polo","Tops",  30, "L", "Blue");
        inventorySystem.addProduct("PT03", "Blouse","Tops", 20, "S", "White");
        inventorySystem.addProduct("PB01", "Shorts","Pants", 25, "M", "Black");

        do {
            System.out.println("\n--- Inventory Management System ---");
            System.out.println("1. Add or Update Product");
            System.out.println("2. Get Product Info by ID or Name");
            System.out.println("3. List All Products");
            System.out.println("4. Get Transaction Records");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                // In case 1 of your switch statement
                case 1:
                    // Add or update a product
                    System.out.print("Enter Product ID: ");
                    String productID = scanner.nextLine();

                    // Validate product ID
                    if (!isValidProductID(productID)) {
                    System.out.println("Invalid Product ID. Please enter a valid ID.");
                    break;
                    }

                    System.out.print("Enter Name: ");
                    String subdivision = scanner.nextLine();

                    System.out.print("Enter Stock Level: ");
                    int stockLevel = scanner.nextInt();
                    scanner.nextLine(); 

                    System.out.print("Enter Category (Tops/Pants): ");
                    String category = scanner.nextLine(); // Get category

                    // Size validation
                    String size;
                    while (true) {
                    System.out.print("Enter Size (XS, S, M, L, XL, XXL): ");
                    size = scanner.nextLine().toLowerCase(); // Convert to lowercase
                    if (isValidSize(size)) {
                        break; // Valid size, exit loop
                    } else {
                        System.out.println("Invalid size. Please enter XS, S, M, L, XL, or XXL.");
                    }
                    }

                    System.out.print("Enter Color: ");
                    String color = scanner.nextLine();

                    inventorySystem.addProduct(productID, subdivision, category, stockLevel, size.toUpperCase(), color); // Store size in uppercase
                    break;


                case 2:
                    // Get product info by ID or name
                    System.out.print("Enter Product ID or Name: ");
                    String input = scanner.nextLine();
                    
                    // First check if input is a valid ID
                    List<InventorySystem.ProductTracker> productInfoList = inventorySystem.getProductByID(input);
                    
                    if (productInfoList != null && !productInfoList.isEmpty()) {
                        // Display all products with the same ID
                        System.out.println("Product Info:");
                        for (InventorySystem.ProductTracker productInfo : productInfoList) {
                            System.out.println(productInfo);
                        }
                    } else {
                        // If not found by ID, check by name
                        InventorySystem.ProductTracker productInfoByName = inventorySystem.getProductByName(input);
                        
                        if (productInfoByName != null) {
                            System.out.println("Product Info: " + productInfoByName);
                        } else {
                            System.out.println("Product does not exist.");
                        }
                    }
                    break;
                

                case 3:
                    // List all products
                    System.out.println("Listing all products:");
                    inventorySystem.listAllProducts();
                    break;

                case 4:
                    // Transaction Records
                    break;

                case 5:
                    System.out.println("Exiting the Inventory Management System.");
                    break;

                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        } while (choice != 5);

        scanner.close();
    }

    // Method to validate the size input
    private static boolean isValidSize(String size) {
        return size.equals("xs") || size.equals("s") || size.equals("m") ||
               size.equals("l") || size.equals("xl") || size.equals("xxl");
    }

    // Method to validate the product ID
    private static boolean isValidProductID(String productID) {
        return productID.equals("PT01") || productID.equals("PT02") || productID.equals("PT03") ||
               productID.equals("PB01") || productID.equals("PB02") || productID.equals("PB03");
    }
}
