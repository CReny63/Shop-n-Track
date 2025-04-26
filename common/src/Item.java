package common.src;

/**
 * common.src.Item: Represents a product.
 * CSV format: name, store, current price, image file, [previous price 1, previous price 2, ...]
 */
public class Item {
    public String itemName;
    public String storeName;
    public double currentPrice;
    public String imageFile;
    public double[] previousPrices;

    public Item(String itemName, String storeName, double currentPrice, String imageFile, double[] previousPrices) {
        this.itemName = itemName;
        this.storeName = storeName;
        this.currentPrice = currentPrice;
        this.imageFile = imageFile;
        this.previousPrices = previousPrices;
    }
}
