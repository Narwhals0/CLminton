package Model;

import java.sql.ResultSet;
import java.sql.SQLException;

import View.HistoryAdminView;
import View.HistoryView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import static Util.Connect.*;

public class HistoryTable {
	  public static Label totalLabel = new Label("Total Price: ");



	    public static TableView<Transaction> createTransactionTable() throws SQLException {
	        String userId = User.userId;

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

	        ObservableList<Transaction> transactions = getTransactions(userId);
	        table.setItems(transactions);


	        return table;
	    }


	    public static TableView<TransactionDetail> createTransactionDetailTable() {
	        TableView<TransactionDetail> table = new TableView<>();
	        table.setPrefHeight(600);
	        table.setPrefWidth(400);

	        TableColumn<TransactionDetail, String> productNameCol = new TableColumn<>("Product Name");
	        TableColumn<TransactionDetail, Integer> productPriceCol = new TableColumn<>("Product Price");
	        TableColumn<TransactionDetail, Integer> quantityCol = new TableColumn<>("Quantity");
	        TableColumn<TransactionDetail, Integer> totalPriceCol = new TableColumn<>("Total Price");

	        productNameCol.setCellValueFactory(new PropertyValueFactory<>("productName"));
	        productPriceCol.setCellValueFactory(new PropertyValueFactory<>("productPrice"));
	        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
	        totalPriceCol.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

	        table.getColumns().addAll(productNameCol, productPriceCol, quantityCol, totalPriceCol);


	        return table;
	    }

	    public static ObservableList<Transaction> getTransactions(String userId) throws SQLException {
	        ObservableList<Transaction> transactionsList = FXCollections.observableArrayList();

	        String query = "SELECT th.TransactionID, th.TransactionDate, mu.UserEmail, th.DeliveryInsurance " +
	                "FROM transactionheader th " +
	                "JOIN msuser mu ON th.UserID = mu.UserID " +
	                "WHERE th.UserID = ?";
	        ResultSet resultSet = execQuery(query, userId);

	        try {
	            while (resultSet.next()) {
	                String transactionID = resultSet.getString("TransactionID");
	                String transactionDate = resultSet.getString("TransactionDate");
	                String userEmail = resultSet.getString("UserEmail");
	                int insurance = resultSet.getInt("DeliveryInsurance");

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


	    public static void updateTransactionDetail(Transaction selectedTransaction, String admin) {
	        try {
	           
	            ResultSet resultSet = getTransactionDetails(String.valueOf(selectedTransaction.getTransactionId()));

	            
	            ObservableList<TransactionDetail> details = FXCollections.observableArrayList();
	            while (resultSet.next()) {
	                String productName = resultSet.getString("ProductName");
	                int productPrice = resultSet.getInt("ProductPrice");
	                int quantity = resultSet.getInt("Quantity");
	                int totalPrice = resultSet.getInt("TotalPrice");


	               
	                TransactionDetail transactionDetail = new TransactionDetail(productName, productPrice, quantity, totalPrice);
	                details.add(transactionDetail);
	            }

	           
	            if(admin.equals("admin")){
	                HistoryAdminView.transactionDetailTable.setItems(details);
	            }
	            else{
	                HistoryView.transactionDetailTable.setItems(details);
	            }

	           int totalPrice = calculateTotalPrice(details, selectedTransaction.getInsurance());
	            totalLabel.setText("Total Price: " + totalPrice); 
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    private static int calculateTotalPrice(ObservableList<TransactionDetail> details, int insurance) {
	        int totalPriceNoInsurance = details.stream().mapToInt(TransactionDetail::getTotalPrice).sum();

	        if (insurance == 1) {
	            return totalPriceNoInsurance + 90000;
	        } else {
	            return totalPriceNoInsurance;
	        }
	    }
}
