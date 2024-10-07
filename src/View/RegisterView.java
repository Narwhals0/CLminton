package View;

import Controller.RegisterController;
import Model.AppPane;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import static Model.AppMenuBar.menuBarReg;

public class RegisterView {
	public static RadioButton rbMale;
    public static RadioButton rbFemale;
    public static void show(Stage primaryStage) {
        primaryStage.setTitle("Register Form");

        Label lblEmail = new Label("Email:");
        Label lblPassword = new Label("Password:");
        Label lblConfirmPassword = new Label("Confirm Password:");
        Label lblAge = new Label("Age:");
        Label lblGender = new Label("Gender:");
        Label lblNationality = new Label("Nationality:");

        TextField txtEmail = new TextField();
        PasswordField txtPassword = new PasswordField();
        PasswordField txtConfirmPassword = new PasswordField();

        Spinner<Integer> ageSpinner = new Spinner<>(0, Integer.MAX_VALUE, 0);
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0);
        ageSpinner.setValueFactory(valueFactory);

        ToggleGroup genderGroup = new ToggleGroup();
        rbMale = new RadioButton("Male");
        rbMale.setToggleGroup(genderGroup);
        rbFemale = new RadioButton("Female");
        rbFemale.setToggleGroup(genderGroup);

        ComboBox<String> nationalityComboBox = new ComboBox<>();
        nationalityComboBox.getItems().addAll("Indonesia", "Malaysia", "Singapore");

        Button btnRegister = new Button("Register");

        GridPane registerGrid = AppPane.createGridPane();
        registerGrid.add(lblEmail, 0, 1);
        registerGrid.add(txtEmail, 1, 1);
        registerGrid.add(lblPassword, 0, 2);
        registerGrid.add(txtPassword, 1, 2);
        registerGrid.add(lblConfirmPassword, 0, 3);
        registerGrid.add(txtConfirmPassword, 1, 3);
        registerGrid.add(lblAge, 0, 4);
        registerGrid.add(ageSpinner, 1, 4);

        VBox rightColumn = new VBox(10, lblGender, rbMale, rbFemale, lblNationality, nationalityComboBox, btnRegister);
        rightColumn.setPadding(new Insets(0, 10, 0, 10));
        registerGrid.add(rightColumn, 2, 1, 1, 7);

        GridPane.setHalignment(rightColumn, HPos.RIGHT);

        registerGrid.setAlignment(Pos.CENTER);


        btnRegister.setOnAction(e -> RegisterController.handleRegister(primaryStage, txtEmail.getText(), txtPassword.getText(),
                txtConfirmPassword.getText(), ageSpinner.getValue().toString(), genderGroup, nationalityComboBox.getValue()));


        MenuBar registerMenuBar = menuBarReg(primaryStage);


        BorderPane root = AppPane.createBorderPane(registerGrid, registerMenuBar);
        Scene scene = new Scene(root, 1152, 648);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
