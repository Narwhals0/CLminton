package View;

import java.sql.SQLException;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import Controller.HomeController;
import Model.AppAlert;
import Model.AppMenuBar;
import Model.AppPane;
import Model.Product;

import static Controller.HomeController.refreshProductTable;

public class HomeView {
	public static Spinner<Integer> quantitySpinner;
    static Text nameOfProductText = new Text("Product Name: ");
    static Text brandOfProductText = new Text("Product Brand: ");
    static Text priceOfProductText = new Text("Price: ");
	
    static Text totalPriceOfProductText = new Text("Total Price:");
    public static Button addToCartButton = new Button("Add to Cart");
    public static TableView<Product> table;

	    public static void show(Stage primaryStage) {

	        MenuBar mainMenu = AppMenuBar.menuBarUser(primaryStage);

	        GridPane homePane = AppPane.createGridPane();
	        BorderPane root = AppPane.createBorderPane(homePane,mainMenu);
	        table = new TableView<>();

	        HomeController.initProdTableCol(table);
	        refreshProductTable(table);
	        Scene scene = new Scene(root, 1152, 648);
	        primaryStage.setScene(scene);


	        quantitySpinner = new Spinner<>(1, 1, 1);

	        homePane.add(table, 0, 0);

	        primaryStage.setTitle("Home Page");

	        VBox vbox = new VBox(15);
	        vbox.getChildren().addAll(nameOfProductText, brandOfProductText, priceOfProductText, quantitySpinner, totalPriceOfProductText, addToCartButton);
	        vbox.setPadding(new Insets(10));

	        homePane.add(vbox, 1, 0);
	        homePane.setAlignment(Pos.CENTER);


	        table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
	            if (newValue != null) {
	                updateproductDetail(newValue);
	                updateTotalPrice();
	            } else {
	                clearproductDetail();
	            }

	            // kalo di select, max quantity buat spinner ngikut product stock
	            int stock = table.getSelectionModel().getSelectedItem().getStock();

	            ((SpinnerValueFactory.IntegerSpinnerValueFactory) quantitySpinner.getValueFactory()).setMax(stock);
	        });

	        quantitySpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
	            updateTotalPrice();  
	        });

	        addToCartButton.setOnAction(event -> {
	            if(table.getSelectionModel().getSelectedItem() != null) {
	                try {
	                    HomeController.addProductToCart(primaryStage);
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            }
	            else {
	                AppAlert.showInformationAlert("Success", "Product added to cart!");
	            }
	        });

	        configureProductSelection(primaryStage);
	        HomeController.configureAddToCartButton(primaryStage);
	    }

	    private static void updateTotalPrice() {
	        Product selectedProduct = table.getSelectionModel().getSelectedItem();
	        if (selectedProduct != null) {
	            int price = selectedProduct.getPrice();
	            int quantityValue = quantitySpinner.getValue();
	            totalPriceOfProductText.setText("Total Price: " + (price * quantityValue));
	        }
	    }


	    private static void updateproductDetail(Product selectedProduct) {
	        nameOfProductText.setText("Product Name: " + selectedProduct.getName());
	        brandOfProductText.setText("Product Brand: " + selectedProduct.getBrand());
	        priceOfProductText.setText("Price: " + selectedProduct.getPrice());

	        int stock = selectedProduct.getStock();

	        updateTotalPrice();

	    }


	    public static void clearproductDetail() {
	        nameOfProductText.setText("");
	        brandOfProductText.setText("");
	        priceOfProductText.setText("");
	        totalPriceOfProductText.setText("");
	    }

	    public static void configureProductSelection(Stage primaryStage) {
	        table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
	            if (newValue != null) {
	                updateproductDetail(newValue);
	            } else {
	                clearproductDetail();
	            }
	        });
	    }

}
