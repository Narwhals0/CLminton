package View;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import Controller.HomeController;
import Model.AppAlert;
import Model.AppMenuBar;
import Model.AppPane;
import Model.Product;
import Util.Connect;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HomeAdminView {
	static Scene scene;
    static MenuBar adminMenuBar;
    static TableView<Product> table;

    static BorderPane root;
    static GridPane grid;
    static VBox tableBox;
    static VBox inputForm;

    static Label productListLabel, productNameLabel, productBrandLabel, productPriceLabel, selectedProductNameLabel, addStockLabel, deleteProductLabel;
    static TextField productNameTextField, productPriceTextField;
    static ComboBox<String> productBrandBox;
    static Button addProductBtn, addStockBtn, deleteBtn;
    static Spinner<Integer> quantitySpinner;

    static private String selectedProductId;

    public static void init(Stage primaryStage) {

        adminMenuBar = AppMenuBar.menuBarAdmin(primaryStage);

        // label product list sama tabel
        productListLabel = new Label("Product List");
        productListLabel.setStyle("-fx-font-size:20;"); // besarin tulisan
        // table
        table = new TableView<>();
        HomeController.initProdTableCol(table);
        HomeController.refreshProductTableAdmin(table);

        // form add barang
        productNameLabel = new Label("Product Name");
        productNameTextField = new TextField();
        productBrandLabel = new Label("Product Brand");
        productBrandBox = new ComboBox<>();
        productBrandBox.getItems().addAll("Yonex", "Li-Ning", "Victor");
        productPriceLabel = new Label("Product Price");
        productPriceTextField = new TextField();
        addProductBtn = new Button("Add Product");

        selectedProductNameLabel = new Label("Name : ");

        addStockLabel = new Label("Add Stock");
        quantitySpinner = new Spinner<>(1, Integer.MAX_VALUE, 0);
        addStockBtn = new Button("Add Stock");

        deleteProductLabel = new Label("Delete Product");
        deleteBtn = new Button("Delete");

        grid = AppPane.createGridPane();

        tableBox = new VBox();
        inputForm = new VBox(15);

    }

    public static void set() {

        // set form buat add barang
        inputForm.getChildren().addAll(
                productNameLabel, productNameTextField, productBrandLabel, productBrandBox,
                productPriceLabel, productPriceTextField, addProductBtn
        );

        inputForm.setPadding(new Insets(0, 0, 0, 10));
        tableBox.getChildren().addAll(productListLabel, table);

        // masukin tabel ke grid
        grid.add(tableBox, 0, 0);
        grid.add(inputForm, 1, 0);
        // masukin text buat selected item
        grid.add(selectedProductNameLabel, 0, 1);
        grid.add(addStockLabel, 0, 2);
        grid.add(quantitySpinner, 0, 3);
        grid.add(addStockBtn, 0, 4);
        grid.add(deleteProductLabel, 1, 3);
        grid.add(deleteBtn, 1, 4);

        root = AppPane.createBorderPane(grid, adminMenuBar);
        root.setTop(adminMenuBar);
        root.setCenter(grid);
        grid.setAlignment(Pos.CENTER);

        scene = new Scene(root, 1152, 648);
    }

    public static void setEvent() {
        table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedProductId = table.getSelectionModel().getSelectedItem().getId();
                String admin = "admin";
                updateproductDetail(newValue, admin);
            } else {
                clearproductDetail();
            }
        });


        // button buat add product baru
        addProductBtn.setOnAction(e -> {

            // cari maxIndex buat di assign ke productID dari produk baru
            String idQuery = "SELECT MAX(CAST(SUBSTRING(ProductID, 3) AS SIGNED)) AS maxIndex FROM msproduct";
            int productIndex = 0;
            Connect.rs = Connect.execQuery(idQuery);

            try {
                if (Connect.rs.next()) {
                    int maxIndex = Connect.rs.getInt("maxIndex");
                    productIndex = maxIndex + 1;
                }

                HomeController.refreshProductTable(table);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }

            // id formatting
            String id = String.format("%s%03d", "PD", productIndex);

            // variabel" buat nampung isi dari form
            String name = productNameTextField.getText();
            String brand = productBrandBox.getValue();
            String price = productPriceTextField.getText();

            // masukkin variable ke query, default stock = 0
            String query = "INSERT INTO msproduct (ProductID, ProductName, ProductMerk, ProductPrice, ProductStock) VALUES (?, ?, ?, ?, 0)";
            PreparedStatement ps = Connect.getPreparedStatement(query);

            try {
                ps.setString(1, id);
                ps.setString(2, name);
                ps.setString(3, brand);
                ps.setDouble(4, Double.parseDouble(price));
                ps.execute();

                HomeController.refreshProductTableAdmin(table);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }

            HomeController.refreshProductTableAdmin(table);
        });


        // button buat add product stock
        addStockBtn.setOnAction(e -> {
            if(table.getSelectionModel().getSelectedItem() != null) {
                int addStockValue = quantitySpinner.getValue();

                String query = "UPDATE msproduct SET ProductStock = ProductStock + ? WHERE ProductID = ?";
                PreparedStatement ps = Connect.getPreparedStatement(query);

                try {
                    ps.setInt(1, addStockValue);
                    ps.setString(2, selectedProductId);
                    ps.executeUpdate();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

                HomeController.refreshProductTableAdmin(table);
            }
            else {
                AppAlert.showWarningAlert("Warning", "Please select product first!");
            }

        });

        // button buat hapus product
        deleteBtn.setOnAction(e -> {

            if (table.getSelectionModel().getSelectedItem() != null) {
                String query = "DELETE FROM msproduct WHERE ProductID = ?";
                PreparedStatement ps = Connect.getPreparedStatement(query);

                try {
                    ps.setString(1, selectedProductId);
                    ps.executeUpdate();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

                HomeController.refreshProductTableAdmin(table);
            }
            else {
                AppAlert.showWarningAlert("Warning", "Please select product first!");
            }

        });
    }

    private static void clearproductDetail() {
        selectedProductNameLabel.setText("");
    }

    private static void updateproductDetail(Product selectedProduct,String admin) {
        admin = "admin";
        selectedProductNameLabel.setText("Name : " +selectedProduct.getName());
    }

    public static void show(Stage primaryStage) {
        init(primaryStage);
        set();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Admin Home");
        primaryStage.show();
        setEvent();
    }
}
