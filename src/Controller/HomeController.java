package Controller;

import java.sql.SQLException;
import java.util.Vector;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import Model.AppAlert;
import Model.Product;
import Model.User;
import Util.Connect;
import View.HomeView;

import static View.HomeView.*;
import static Model.User.userId;

public class HomeController {
	 static Vector<Product> productList = new Vector<>();

	    public static void initProdTableCol(TableView table) {
	        TableColumn<Product, String> nameColumn = new TableColumn<>("Name");
	        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

	        TableColumn<Product, String> brandColumn = new TableColumn<>("Brand");
	        brandColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));

	        TableColumn<Product, Integer> stockColumn = new TableColumn<>("Stock");
	        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));

	        TableColumn<Product, Integer> priceColumn = new TableColumn<>("Price");
	        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

	        table.getColumns().clear();
	        table.getColumns().addAll(nameColumn, brandColumn, stockColumn, priceColumn);
	    }


	    public static void fetchProductDataUser() {
	        productList.clear();

	        String query = "SELECT * FROM msproduct WHERE ProductStock > 0";
	        Connect.rs = Connect.execQuery(query);

	        try {
	            while (Connect.rs.next()) {
	                String id = Connect.rs.getString("ProductID");
	                String name = Connect.rs.getString("ProductName");
	                String brand = Connect.rs.getString("ProductMerk");
	                int stock = Connect.rs.getInt("ProductStock");
	                int price = Connect.rs.getInt("ProductPrice");

	                Product product = new Product(id, name, brand, stock, price);
	                productList.add(product);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    public static void fetchProductDataAdmin() {
	        productList.clear();

	        String query = "SELECT * FROM msproduct";
	        Connect.rs = Connect.execQuery(query);

	        try {
	            while (Connect.rs.next()) {
	                String id = Connect.rs.getString("ProductID");
	                String name = Connect.rs.getString("ProductName");
	                String brand = Connect.rs.getString("ProductMerk");
	                int stock = Connect.rs.getInt("ProductStock");
	                int price = Connect.rs.getInt("ProductPrice");

	                Product product = new Product(id, name, brand, stock, price);
	                productList.add(product);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    public static void configureAddToCartButton(Stage primaryStage) {
	        HomeView.addToCartButton.setOnAction(event -> {
	            try {
	                addProductToCart(primaryStage);
	            } catch (SQLException e) {
	                throw new RuntimeException(e);
	            }
	            AppAlert.showInformationAlert("Success", "Product added to cart!");
	            HomeView.show(primaryStage);
	        });
	    }

	    public static void addProductToCart(Stage primaryStage) throws SQLException {
	        if (table.getSelectionModel().getSelectedItem() != null) {
	            Product selectedProduct = table.getSelectionModel().getSelectedItem();
	            String userId = User.userId;  

	            int quantityValue = quantitySpinner.getValue();
	            insertProductIntoCart(userId, selectedProduct.getId(), quantityValue);
	            System.out.println("Product added to cart!");
	        } else {
	            System.out.println("Please select a product first!");
	        }
	    }

	    private static void insertProductIntoCart(String userId, String productId, int quantity) throws SQLException {
	        String selectQuery = "SELECT * FROM carttable WHERE UserID = ? AND ProductID = ?";
	        String insertQuery = "INSERT INTO carttable (UserID, ProductID, Quantity) VALUES (?, ?, ?)";
	        String updateQuery = "UPDATE carttable SET Quantity = Quantity + ? WHERE UserID = ? AND ProductID = ?";
	        String updateStockData = "UPDATE msproduct SET ProductStock = ProductStock - ? WHERE ProductID = ?";

	        try {

	            Connect.ps = Connect.getPreparedStatement(selectQuery);
	            Connect.ps.setString(1, userId);
	            Connect.ps.setString(2, productId);
	            Connect.rs = Connect.ps.executeQuery();

	            if (Connect.rs.next()) {
	                // Tambahin quantity di cart pas dia add to cart product yg sama di waktu yg beda
	                Connect.ps = Connect.getPreparedStatement(updateQuery);
	                Connect.ps.setInt(1, quantity);
	                Connect.ps.setString(2, userId);
	                Connect.ps.setString(3, productId);
	                Connect.ps.executeUpdate();

	                System.out.println("Quantity updated in the cart!");
	            } else {
	                // Bikin record product baru buat di cart kalo baru pertama kali add to cart product
	                Connect.ps = Connect.getPreparedStatement(insertQuery);
	                Connect.ps.setString(1, userId);
	                Connect.ps.setString(2, productId);
	                Connect.ps.setInt(3, quantity);
	                Connect.ps.executeUpdate();
	                System.out.println("Product added to cart!");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        } finally {
	            Connect.ps = Connect.getPreparedStatement(updateStockData);
	            Connect.ps.setInt(1, quantity);
	            Connect.ps.setString(2, productId);
	            Connect.ps.executeUpdate();
	            closePreparedStatement();
	        }
	    }

	    private static void closePreparedStatement() {
	        if (Connect.ps != null) {
	            try {
	                Connect.ps.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	    }

	    public static void refreshProductTable(TableView table) {
	        HomeController.fetchProductDataUser();
	        ObservableList<Product> productObs = FXCollections.observableArrayList(HomeController.productList);
	        table.setItems(productObs);

	        if (table.getItems().isEmpty()) {
	            clearproductDetail();
	            table.setPlaceholder(new Label("No products available."));
	        } else {
	            table.setPlaceholder(null);
	        }
	    }

	    public static void refreshProductTableAdmin(TableView table) {
	        HomeController.fetchProductDataAdmin();
	        ObservableList<Product> productObs = FXCollections.observableArrayList(HomeController.productList);
	        table.setItems(productObs);
	    }
}
