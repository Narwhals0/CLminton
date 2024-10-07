package Controller;

import java.sql.ResultSet;
import java.sql.SQLException;

import Model.AppAlert;
import Model.CartTable;
import Model.User;
import Util.Connect;
import View.CartView;
import View.PopUpView;
import javafx.stage.Stage;

import static View.CartView.*;

public class CartController {
	static String userId = User.userId;

    public static void getCartData() {
        try {
            String selectquery = "SELECT msproduct.ProductID, msproduct.ProductName, msproduct.ProductMerk, msproduct.ProductPrice, carttable.Quantity "
                    + "FROM msproduct "
                    + "JOIN carttable ON msproduct.ProductID = carttable.ProductID "
                    + "WHERE carttable.UserId = '" + userId + "'";
            ResultSet resultSet = Connect.execQuery(selectquery);

            cartItemList.clear(); 
            totalCartPrice = 0; 

            while (resultSet.next()) {
                String productId = resultSet.getString("ProductID");
                String name = resultSet.getString("ProductName");
                String brand = resultSet.getString("ProductMerk");
                int price = resultSet.getInt("ProductPrice");
                int quantity = resultSet.getInt("Quantity");
                int totalPrice = (price * quantity);
                totalCartPrice += totalPrice;

                CartTable cartItem = new CartTable(productId, name, brand, price, quantity);
                cartItemList.add(cartItem);
            }
            cartTableView.getItems().clear();
            cartTableView.getItems().addAll(cartItemList);
            
            updateTotalPriceLabel();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void checkoutAction(Stage primaryStage) {
        if (cartTableView.getItems().isEmpty()) {
            AppAlert.showWarningAlert("Warning", "Please insert an item into your cart.");
        } else {
                cartTableView.getItems().clear();
                clearLabel();
                PopUpView.show();
        }
    }

    public static void updateTotalPriceLabel() {
        CartView.productTotalPrice.setText("Total Price: " + CartView.totalCartPrice);
    }

    public static void removeFromCart() {
        CartTable selectedCartItem = cartTableView.getSelectionModel().getSelectedItem();
        
        if (selectedCartItem == null) {
            AppAlert.showInformationAlert("Warning","Please select Product to Delete.");
        } else {
            int removedItemTotalPrice = selectedCartItem.getTotalProductPrice();
            removeItem(selectedCartItem);
            totalCartPrice -= removedItemTotalPrice;
            productTotalPrice.setText("Total Price: " + totalCartPrice);
        }
    }

    public static void removeItem(CartTable selectedCartItem) {
        cartTableView.getItems().remove(selectedCartItem);
        String productId = selectedCartItem.getProductID();
        String query = "DELETE FROM carttable WHERE UserID = '" + userId + "' AND ProductID = '" + productId + "'";
        connect.execUpdate(query);
        updateTotalPriceLabel();
    }
}
