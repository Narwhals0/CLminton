package Model;

public class TransactionDetail {
	private String productName;
    private int productPrice;
    private int quantity;
    private int totalPrice;

    public TransactionDetail(String productName, int productPrice, int quantity, int totalPrice) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public String getProductName() {
        return productName;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getTotalPrice() {
        return totalPrice;
    }
}
