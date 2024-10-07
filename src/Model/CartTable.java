package Model;

public class CartTable {
	 private String productID;
	    private String productName;
	    private String productBrand;
	    private Integer productPrice;
	    private Integer quantity;

	    public CartTable(String productID, String productName, String productBrand, Integer productPrice, Integer quantity) {
	        this.productID = productID;
	        this.productName = productName;
	        this.productBrand = productBrand;
	        this.productPrice = productPrice;
	        this.quantity = quantity;
	    }

	    public CartTable(String productName,int productPrice){
	        this.productName = productName;
	        this.productPrice = productPrice;
	    }

	    public Integer getTotalProductPrice() {
	        if (productPrice != null && quantity != null) {
	            return productPrice * quantity;
	        } else {
	            return 0;
	        }
	    }

	    public String getProductID() {
	        return productID;
	    }
	    public void setProductID(String productID) {
	        this.productID = productID;
	    }
	    public String getProductName() {
	        return productName;
	    }
	    public void setProductName(String productName) {
	        this.productName = productName;
	    }
	    public String getProductBrand() {
	        return productBrand;
	    }
	    public void setProductBrand(String productBrand) {
	        this.productBrand = productBrand;
	    }
	    public Integer getProductPrice() {
	        return productPrice;
	    }
	    public void setProductPrice(Integer productPrice) {
	        this.productPrice = productPrice;
	    }
	    public Integer getQuantity() {
	        return quantity;
	    }
	    public void setQuantity(Integer quantity) {
	        this.quantity = quantity;
	    }

}
