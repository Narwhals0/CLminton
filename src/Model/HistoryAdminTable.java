package Model;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import static Util.Connect.*;

public class HistoryAdminTable {
	public static TableView<Transaction> createAdminTable() throws SQLException {

        TableColumn<Transaction, String> transactionIdCol = new TableColumn<>("Transaction ID");
        TableColumn<Transaction, String> userEmailCol = new TableColumn<>("Email");
        TableColumn<Transaction, String> transactionDateCol = new TableColumn<>("Date");

        transactionIdCol.setCellValueFactory(new PropertyValueFactory<>("transactionId"));
        userEmailCol.setCellValueFactory(new PropertyValueFactory<>("userEmail"));
        transactionDateCol.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));

        TableView<Transaction> table = new TableView<>();
        table.getColumns().addAll(transactionIdCol, userEmailCol, transactionDateCol);
        table.setPrefHeight(600);
        table.setPrefWidth(400);

        ObservableList<Transaction> transactions = getAdminTransactions();
        table.setItems(transactions);

        return table;
    }

    public static ObservableList<Transaction> getAdminTransactions() throws SQLException {
        ObservableList<Transaction> transactionsList = FXCollections.observableArrayList();

        String query = "SELECT th.TransactionID, th.TransactionDate, mu.UserEmail, th.DeliveryInsurance " +
                "FROM transactionheader th " +
                "JOIN msuser mu ON th.UserID = mu.UserID ";
        ResultSet resultSet = execQuery(query);

        try {
            while (resultSet.next()) {
                String transactionID = resultSet.getString("TransactionID");
                String transactionDate = resultSet.getString("TransactionDate");
                String userEmail = resultSet.getString("UserEmail");
                int insurance = resultSet.getInt("DeliveryInsurance");
                System.out.println(insurance);


                Transaction transaction = new Transaction(transactionID, userEmail, transactionDate, insurance);
                transactionsList.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return transactionsList;
    }
        public static ResultSet getTransactionDetails(String transactionId) {
            String query = "SELECT p.ProductName, p.ProductPrice, td.Quantity, (p.ProductPrice * td.Quantity) AS TotalPrice " +
                    "FROM transactiondetail td " +
                    "JOIN msproduct p ON td.ProductID = p.ProductID " +
                    "WHERE td.TransactionID = ?";
            return execQuery(query, transactionId);
        }
}
