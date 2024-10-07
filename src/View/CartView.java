package View;

import Model.AppMenuBar;
import Model.AppPane;
import Model.CartTable;
import Util.Connect;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import static Controller.CartController.*;

public class CartView {

    public static TableView<CartTable> cartTableView;
    public static ObservableList<CartTable> cartItemList;
    static Label productNameLabel;
    static Label productBrandLabel;
    static Label productPriceLabel;
    public static Label productTotalPrice;
    public static Connect connect = Connect.getInstance();
    public static int totalCartPrice;

    static GridPane cartPane;
    static MenuBar menuBar;
    static BorderPane root;
    static GridPane productDetail;


    public static void show(Stage primaryStage) {
        productDetail = AppPane.createGridPane();
        cartPane = AppPane.createGridPane();
        menuBar = AppMenuBar.menuBarUser(primaryStage);
        root = AppPane.createBorderPane(cartPane, menuBar);

        cartTableView = new TableView<>();
        cartItemList = FXCollections.observableArrayList();

        Scene scene = new Scene(root, 1152, 648);
        primaryStage.setTitle("Cart");
        primaryStage.setScene(scene);

        productNameLabel = new Label("Name: ");
        productBrandLabel = new Label("Brand: ");
        productPriceLabel = new Label("Price: ");
        productTotalPrice = new Label("Total Price: ");


        productDetail.add(productNameLabel, 0, 0);
        productDetail.add(productBrandLabel, 0, 1);
        productDetail.add(productPriceLabel, 0, 2);
        productDetail.add(productTotalPrice, 0, 3);

        Label yourCartLabel = new Label("Your Cart List");
        yourCartLabel.setStyle("-fx-font-size: 20;");

        VBox yourCartLabelVBox = new VBox(5, yourCartLabel);
        cartPane.add(yourCartLabelVBox, 0, 0);

        VBox labelsVBox = new VBox(5, productDetail);

        cartPane.add(labelsVBox, 1, 1);
        labelsVBox.setPadding(new Insets(10));

        initializeTable();
        initializeButton(primaryStage);

        getCartData();

        cartTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                displayproductDetail(newSelection);
            }
        });
    }

    private static void initializeTable() {
        TableColumn<CartTable, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));

        TableColumn<CartTable, String> brandColumn = new TableColumn<>("Brand");
        brandColumn.setCellValueFactory(new PropertyValueFactory<>("productBrand"));

        TableColumn<CartTable, Integer> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("productPrice"));

        TableColumn<CartTable, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<CartTable, Integer> totalPriceColumn = new TableColumn<>("Total Price");
        totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalProductPrice"));

        nameColumn.setMaxWidth(150);
        brandColumn.setMaxWidth(100);
        priceColumn.setMaxWidth(100);
        quantityColumn.setMaxWidth(100);
        totalPriceColumn.setMaxWidth(100);

        cartTableView.getColumns().clear();
        cartTableView.getColumns().addAll(nameColumn, brandColumn, priceColumn, quantityColumn, totalPriceColumn);

        cartTableView.setMaxWidth(500); 
        cartPane.add(cartTableView, 0, 1);
        cartPane.setAlignment(Pos.CENTER);

    }

    public static void initializeButton(Stage primaryStage) {

        Button removeCartBtn = new Button("Delete Product");
        removeCartBtn.setOnAction(e -> removeFromCart());
        removeCartBtn.setPrefWidth(400);
        removeCartBtn.setPrefHeight(15);

        Button checkoutBtn = new Button("Checkout");
        checkoutBtn.setOnAction(e -> checkoutAction(primaryStage));
        checkoutBtn.setPrefHeight(15);
        checkoutBtn.setPrefWidth(400);

        VBox buttonsBox = new VBox(15, checkoutBtn, removeCartBtn);
        buttonsBox.setPadding(new Insets(20));
        buttonsBox.setStyle("-fx-alignment: center");
        cartPane.add(buttonsBox, 0, 2);
        BorderPane.setMargin(buttonsBox, new Insets(20));
    }

    public static void clearLabel() {
        productNameLabel.setText("Name: ");
        productBrandLabel.setText("Brand: ");
        productPriceLabel.setText("Price: ");
        productTotalPrice.setText("Total Price: ");
    }

    private static void displayproductDetail(CartTable cartItem) {
        productNameLabel.setText("Name: " + cartItem.getProductName());
        productBrandLabel.setText("Brand: " + cartItem.getProductBrand());
        productPriceLabel.setText("Price: " + cartItem.getProductPrice());

        int totalCartPrice = cartItemList.stream()
                .mapToInt(CartTable::getTotalProductPrice)
                .sum();

        productTotalPrice.setText("Total Price: " + totalCartPrice);
    }
}
