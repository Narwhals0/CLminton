package View;

import java.sql.SQLException;

import Model.AppMenuBar;
import Model.AppPane;
import Model.HistoryAdminTable;
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

public class HistoryAdminView {
	 	private static TableView<Transaction> transactionTable;
	    public static TableView<TransactionDetail> transactionDetailTable;
	    static MenuBar mainMenu;
	    static BorderPane root;
	    static GridPane historyPane;
	    
	    public static void show(Stage primaryStage) {

	        primaryStage.setTitle("My History");        //title di soal "My History"
	        mainMenu = AppMenuBar.menuBarAdmin(primaryStage);
	        Label trans = new Label("All Transaction");
	        Label transD = new Label("Transaction Detail");
	        historyPane = AppPane.createGridPane();
	        root = AppPane.createBorderPane(historyPane, mainMenu);

	        try {
	            transactionTable = HistoryAdminTable.createAdminTable();
	            transactionDetailTable = HistoryTable.createTransactionDetailTable();
	        } catch (SQLException e) {
	            throw new RuntimeException(e);
	        }

	        transactionTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
	            if (newValue != null) {
	                String admin  = "admin";
	                HistoryTable.updateTransactionDetail(newValue, admin);
	            }
	        });

	        trans.setStyle("-fx-font-size: 21;");
	        transD.setStyle("-fx-font-size: 21;");
	        HistoryTable.totalLabel.setStyle("-fx-font-size: 21;");
	        historyPane.setAlignment(Pos.CENTER);
	        historyPane.add(trans,0,0);
	        historyPane.add(transD,1,0);
	        historyPane.add(transactionTable, 0, 1);
	        historyPane.add(transactionDetailTable, 1, 1);
	        historyPane.add(HistoryTable.totalLabel, 1, 2);

	        Scene scene = new Scene(root, 1152, 648);
	        primaryStage.setScene(scene);
	    }
}
