package test;
public class OrderFulfillment {
    private String orderID;
    private String productID;
    private int quantity;
    private String status;
    private InventorySystem inventorySystem;

    // Constructor
    public OrderFulfillment(String orderID, String productID, int quantity, InventorySystem inventorySystem) {
        this.orderID = orderID;
        this.productID = productID;
        this.quantity = quantity;
        this.status = "Processing";
        this.inventorySystem = inventorySystem;
    }

    // Update the order status
    public void updateStatus(String newStatus) {
        this.status = newStatus;
    }

    // Process the order and update the inventory
    public void processOrder() {
        InventorySystem.ProductTracker product = inventorySystem.getProductByID(productID);
        
        if (product != null) {
            if (product.getStockLevel() >= quantity) {
                product.setStockLevel(product.getStockLevel() - quantity); // Deduct stock
                System.out.println("Order fulfilled. Product ID: " + productID + ", Quantity: " + quantity);
                updateStatus("Shipped");
            } else {
                System.out.println("Not enough stock to fulfill the order. Available stock: " + product.getStockLevel());
                updateStatus("Pending - Insufficient Stock");
            }
        } else {
            System.out.println("Product not found for the given ID: " + productID);
            updateStatus("Failed - Product Not Found");
        }
    }

    @Override
    public String toString() {
        return "Order ID: " + orderID + ", Product ID: " + productID + ", Quantity: " + quantity + ", Status: " + status;
    }
}