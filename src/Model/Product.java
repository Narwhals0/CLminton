package Model;

public class Product {
    private String id;
    private String name;
    private String brand;
    private int stock;
    private int price;

    public Product(String id, String name, String brand, int stock, int price) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.stock = stock;
        this.price = price;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return this.brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getStock() {
        return this.stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getPrice() {
        return this.price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

}
