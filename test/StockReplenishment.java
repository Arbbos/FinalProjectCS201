package test;
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
        for (InventorySystem.ProductTracker product : inventorySystem.getProducts()) {
            if (product.getStockLevel() < threshold) {
                replenishStock(product);
            }
        }
    }

    // Replenish the stock by placing an order from the warehouse
    private void replenishStock(InventorySystem.ProductTracker product) {
        int replenishmentQuantity = 50;
        product.setStockLevel(product.getStockLevel() + replenishmentQuantity);
        System.out.println("Stock replenished for Product ID: " + product.getProductID() + ". New Stock Level: " + product.getStockLevel());
    }
}