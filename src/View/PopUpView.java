package View;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

import Model.AppPane;
import Model.CartTable;
import Model.User;
import Util.Connect;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class PopUpView {
	static ArrayList<CartTable> cartList = new ArrayList<>();
    static int totalCartPrice;
    static int transactionIndex = 0;

    static Label listLabel = new Label("List");
    static Label productNameLabel = new Label();
    static Label courierLabel = new Label("Courier");
    static Label totalPrice = new Label();

    static CheckBox checkBox = new CheckBox("Use Insurance");
    static Button checkoutButton = new Button("Checkout");

    static ComboBox<String> comboBox = new ComboBox<>();

    public static void show() {
        Stage secondaryStage = new Stage();
        GridPane root = AppPane.createGridPane();

        BackgroundFill backgroundFill = new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY);
        Background background = new Background(backgroundFill);
        root.setBackground(background);

        comboBox.getItems().addAll("JNE Express", "J&T Express", "POS Indonesia");
        GridPane.setHalignment(comboBox, HPos.CENTER);

        root.add(listLabel, 0, 0);
        root.add(productNameLabel, 0, 1);
        root.add(courierLabel, 0, 2);
        root.add(comboBox, 0, 3);
        root.add(checkBox, 0, 4);
        root.add(totalPrice, 0, 5);
        root.add(checkoutButton, 0, 6);

        getData();

        checkBox.setOnAction(event -> handleCheckBoxAction());
        checkoutButton.setOnAction(event -> checkoutAction(secondaryStage));

        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 854, 480);

        secondaryStage.setTitle("Transaction Card");
        secondaryStage.setScene(scene);
        secondaryStage.show();
    }

    public static void getData() {
        String userId = User.userId;

        try (PreparedStatement preparedStatement = Connect.getPreparedStatement(
                "SELECT msproduct.ProductID, msproduct.ProductName, msproduct.ProductMerk, msproduct.ProductPrice, carttable.Quantity "
                        + "FROM msproduct "
                        + "JOIN carttable ON msproduct.ProductID = carttable.ProductID "
                        + "WHERE carttable.UserId = ?")) {

            preparedStatement.setString(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                cartList.clear();

                while (resultSet.next()) {
                    String name = resultSet.getString("ProductName");
                    int price = resultSet.getInt("ProductPrice");
                    int quantity = resultSet.getInt("Quantity");
                    int totalPrice = (price * quantity);
                    totalCartPrice += totalPrice;

                    CartTable cartItem = new CartTable(name, totalPrice);
                    cartList.add(cartItem);

                    System.out.println("Name: " + name);
                    System.out.println("Price: " + totalPrice);
                }

                ArrayList<String> productInfoList = new ArrayList<>();

                for (CartTable cartItem : cartList) {
                    productInfoList.add(cartItem.getProductName() + " : " + cartItem.getProductPrice());
                }

                productNameLabel.setText(String.join("\n", productInfoList));
                totalPrice.setText("Total price : " + totalCartPrice);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void handleCheckBoxAction() {
        if (checkBox.isSelected()) {
            totalCartPrice += 90000;
            totalPrice.setText("Total price : " + totalCartPrice);
        } else {
            totalCartPrice -= 90000;
            totalPrice.setText("Total price : " + totalCartPrice);
        }
    }

    private static boolean checkoutAction(Stage secondaryStage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Are you sure you want to Checkout all the item");
        alert.setContentText("Need Confirmation");

        ButtonType yesButton = new ButtonType("OK");
        ButtonType noButton = new ButtonType("Cancel");
        alert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == yesButton) {
            if (insertTransactionToDatabase()) {
                System.out.println("Checkout successful!");
                secondaryStage.close();
            } else {
                System.out.println("Checkout failed!");
            }
        }

        return result.isPresent() && result.get() == yesButton;
    }

    private static String generateTransactionID() {
        String query = "SELECT MAX(CAST(SUBSTRING(TransactionID, 3) AS SIGNED)) AS maxIndex FROM transactionheader";
        Connect.rs = Connect.execQuery(query);

        try {
            if (Connect.rs.next()) {
                int maxIndex = Connect.rs.getInt("maxIndex");
                transactionIndex = maxIndex + 1;
            } else {
                System.out.println("Error");
                return null;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        String indexStr = String.format("%03d", transactionIndex);

        return "TH" + indexStr;

    }

    private static boolean insertTransactionToDatabase() {
        try {
            String courier = comboBox.getValue();
            if (courier == null || courier.isEmpty()) {
                System.out.println("Please select a courier.");
                return false;
            }

            String userID = User.userId;

            LocalDate currentDate = LocalDate.now();
            String formattedDate = currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            String transactionID = generateTransactionID();
            if (transactionID == null) {
                System.out.println("Failed to generate Transaction ID.");
                return false;
            }

            String insertTransactionHeaderQuery = "INSERT INTO transactionheader (TransactionID, UserID, TransactionDate, DeliveryInsurance, CourierType) VALUES (?, ?, ?, ?, ?)";
            Connect.getInstance().execUpdate(insertTransactionHeaderQuery, transactionID, userID, formattedDate, checkBox.isSelected() ? 1 : 0, courier);


            for (CartTable cartItem : cartList) {
                int quantity = 0;
                String productID = getProductIDFromDatabase(cartItem.getProductName());
                
                String selectQuantityQuery = "SELECT SUM(carttable.Quantity) AS TotalQuantity "
                        + "FROM msproduct "
                        + "JOIN carttable ON msproduct.ProductID = carttable.ProductID "
                        + "WHERE carttable.UserId = ? AND msproduct.ProductID = ?";
                
                try (PreparedStatement quantityStatement = Connect.getPreparedStatement(selectQuantityQuery)) {
                    quantityStatement.setString(1, userID);
                    quantityStatement.setString(2, productID);
                    
                    try (ResultSet quantityResult = quantityStatement.executeQuery()) {
                        if (quantityResult.next()) {
                            quantity = quantityResult.getInt("TotalQuantity");
                        }
                    }
                }

                if (productID != null && quantity != 0) {
                    String insertTransactionDetailQuery = "INSERT INTO transactiondetail (ProductID, TransactionID, Quantity) VALUES (?, ?, ?)";
                    Connect.getInstance().execUpdate(insertTransactionDetailQuery, productID, transactionID, quantity);
                } else {
                    System.out.println("Product ID or Quantity not found for: " + cartItem.getProductName());
                }
            }
            

            clearUserCart(userID);

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    private static String getProductIDFromDatabase(String productName) throws SQLException {
        String query = "SELECT ProductID FROM msproduct WHERE ProductName = ?";
        PreparedStatement preparedStatement = Connect.getPreparedStatement(query);
        preparedStatement.setString(1, productName);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getString("ProductID");
        } else {
            return null;
        }
    }

    private static void clearUserCart(String userID) throws SQLException {
        String clearCartQuery = "DELETE FROM carttable WHERE UserID = ?";
        PreparedStatement clearCartStatement = Connect.getPreparedStatement(clearCartQuery);
        clearCartStatement.setString(1, userID);
        clearCartStatement.executeUpdate();
    }
}
