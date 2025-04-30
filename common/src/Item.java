package common.src;

/**
 * common.src.Item: Represents a product.
 * CSV format: name, store, current price, image file, [previous price 1, previous price 2, ...]
 */
public class Item {
    public String itemName;
    public String storeName;
    public String storeReputation;
    public int points;
    public double currentPrice;
    public String imageFile;
    public double[] previousPrices;

    public Item(String itemName, String storeName, String storeReputation, int points, double currentPrice, String imageFile, double[] previousPrices) {
        this.itemName = itemName;
        this.storeName = storeName;
        this.storeReputation = storeReputation;
        this.points = points;
        this.currentPrice = currentPrice;
        this.imageFile = imageFile;
        this.previousPrices = previousPrices;
    }
}
