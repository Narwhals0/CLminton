package View;

import Controller.LoginController;
import Model.AppMenuBar;
import Model.AppPane;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class LoginView {
	 public static Stage primaryStage;
	 
	    public LoginView(Stage primaryStage) {
	        this.primaryStage = primaryStage;
	    }

	    public static void show() {
	        primaryStage.setTitle("Login Form");

	        Label lblLogin = new Label("Login");
	        Label lblEmail = new Label("Email:");
	        Label lblPassword = new Label("Password:");

	        TextField txtEmail = new TextField();
	        PasswordField txtPassword = new PasswordField();

	        Button btnLogin = new Button("Login");

	        GridPane loginGrid = AppPane.createGridPane();
	        loginGrid.add(lblLogin, 0, 0, 2, 1);
	        loginGrid.add(lblEmail, 0, 1);
	        loginGrid.add(txtEmail, 1, 1);
	        loginGrid.add(lblPassword, 0, 2);
	        loginGrid.add(txtPassword, 1, 2);
	        loginGrid.add(btnLogin, 1, 3);
	        loginGrid.setAlignment(Pos.CENTER);


	        btnLogin.setOnAction(e -> LoginController.handleLogin(txtEmail.getText(), txtPassword.getText()));

	        MenuBar menuBar = AppMenuBar.menuBarReg(primaryStage);

	        BorderPane borderPane = AppPane.createBorderPane(loginGrid, menuBar);
	        Scene scene = new Scene(borderPane, 1152, 648);
	        primaryStage.setScene(scene);
	        primaryStage.show();
	    }
}
