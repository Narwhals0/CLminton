package View;

import java.sql.SQLException;

import Model.AppMenuBar;
import Model.AppPane;
import Model.HistoryTable;
import Model.Transaction;
import Model.TransactionDetail;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class HistoryView {
	  private static TableView<Transaction> transactionTable;
	    public static TableView<TransactionDetail> transactionDetailTable;
	    public static MenuBar mainMenu;
	    public static  BorderPane root;
	    public static GridPane historyPane;

	    public static void show(Stage primaryStage) {

	        primaryStage.setTitle("My History");
	        mainMenu = AppMenuBar.menuBarUser(primaryStage);
	        Label transaction = new Label("My Transaction");
	        Label transactionDetail = new Label("Transaction Detail");
	        
	        historyPane = AppPane.createGridPane();
	        root = AppPane.createBorderPane(historyPane, mainMenu);

	        try {
	            transactionTable = HistoryTable.createTransactionTable();
	            transactionDetailTable = HistoryTable.createTransactionDetailTable();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        transactionTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
	            if (newValue != null) {
	                String user = "user";
	                HistoryTable.updateTransactionDetail(newValue, user);
	            }
	        });

	        transaction.setStyle("-fx-font-size: 21;");
	        transactionDetail.setStyle("-fx-font-size: 21;");
	        HistoryTable.totalLabel.setStyle("-fx-font-size: 21;");

	        historyPane.setAlignment(Pos.CENTER);

	        historyPane.add(transaction,0,0);
	        historyPane.add(transactionDetail,1,0);
	        historyPane.add(transactionTable, 0, 1);
	        historyPane.add(transactionDetailTable, 1, 1);
	        historyPane.add(HistoryTable.totalLabel, 1, 2);

	        Scene scene = new Scene(root, 1152, 648);
	        primaryStage.setScene(scene);
	    }

}
