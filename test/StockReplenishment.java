package test;

import javax.swing.JOptionPane;
import java.util.Collection;
import java.util.ArrayList;

public class StockReplenishment {
    private int threshold; 
    private InventorySystem inventorySystem;

    // Constructor
    public StockReplenishment(int threshold, InventorySystem inventorySystem) {
        this.threshold = threshold;
        this.inventorySystem = inventorySystem;
    }

    // Check and replenish stock if below the threshold
    public void checkAndReplenishStock() {
        // Ensure products are loaded first
        if (inventorySystem.getProducts().isEmpty()) {
            System.out.println("No products in inventory.");
            return;
        }

        // Create an array of product names or IDs to allow the user to choose which product to replenish
        Collection<InventorySystem.ProductTracker> products = inventorySystem.getProducts();
        String[] productNames = new String[products.size()];
        int i = 0;

        for (InventorySystem.ProductTracker product : products) {
            productNames[i] = "Product ID: " + product.getProductID() + " - Stock: " + product.getStockLevel();
            i++;
        }

        // Step 2: Prompt user to select a product
        String selectedProduct = (String) JOptionPane.showInputDialog(
            null,
            "Select the product to replenish:",
            "Product Selection",
            JOptionPane.QUESTION_MESSAGE,
            null,
            productNames,
            productNames[0]
        );

        if (selectedProduct == null) {
            // If the user cancels the dialog
            return;
        }

        // Find the selected product by ID
        String selectedProductID = selectedProduct.split(" ")[2]; // Extract productID from the string
        InventorySystem.ProductTracker productToReplenish = inventorySystem.getProductByID(selectedProductID);

        if (productToReplenish == null) {
            System.out.println("Product not found.");
            return;
        }

        // Now prompt the user for the replenishment quantity
        replenishStock(productToReplenish);
    }

    // Replenish the stock by placing an order from the warehouse
    private void replenishStock(InventorySystem.ProductTracker product) {
        // Prompt user for the replenishment quantity
        String input = JOptionPane.showInputDialog(null, 
            "Enter the quantity to replenish for Product ID: " + product.getProductID(),
            "Replenish Stock", JOptionPane.QUESTION_MESSAGE);

        if (input != null) { // Check if the user didn't cancel the dialog
            try {
                int replenishmentQuantity = Integer.parseInt(input);

                // Validate the input
                if (replenishmentQuantity <= 0) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid quantity greater than zero.", 
                        "Invalid Quantity", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Update the stock level
                product.setStockLevel(product.getStockLevel() + replenishmentQuantity);
                System.out.println("Stock replenished for Product ID: " + product.getProductID() + 
                                   ". New Stock Level: " + product.getStockLevel());

                // Optionally, update in the database as well (if you have database integration)
                // inventorySystem.updateStockLevel(product.getProductID(), product.getStockLevel());

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter a numeric value.", 
                    "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
