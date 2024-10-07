package Controller;

import Util.Connect;
import View.LoginView;
import View.RegisterView;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import static Model.AppAlert.*;

import java.sql.SQLException;

public class RegisterController {
	static Connect connect = Connect.getInstance();
    static int userIndex = 0;
    public static void handleRegister(Stage primaryStage, String email, String password, String confirmPassword, String age, ToggleGroup genderGroup, String nationality) {
        if (!isValidRegistrationInput(email, password, confirmPassword, age, genderGroup, nationality)) {
            showInformationAlert("Error", "Invalid registration input. Please check your input.");
            return;
        }

        if (isEmailRegistered(email)) {
            showInformationAlert("Error", "This email is already registered.");
            return;
        }

        String gender = (genderGroup.getSelectedToggle() == RegisterView.rbMale) ? "Male" : "Female";

        String userID = generateUserID();

        if (insertUserToDatabase(userID, email, password, "User", age, gender, nationality)) {
            showInformationAlert("Success", "Registration successful. Your User ID is: " + userID);
            LoginView.show();
        } else {
            showInformationAlert("Error", "Failed to register. Please try again.");
        }
    }



    private static boolean isValidRegistrationInput(String email, String password, String confirmPassword, String age, ToggleGroup genderGroup, String nationality) {
        if (!email.endsWith("@gmail.com")) {
            showInformationAlert("Error", "Email must end with '@gmail.com'.");
            return false;
        }

        if (password.length() < 6) {
            showInformationAlert("Error", "Password must contain at least 6 characters.");
            return false;
        }

        if (!password.equals(confirmPassword)) {
            showInformationAlert("Error", "Confirm Password must be the same as Password.");
            return false;
        }

        try {
            int ageValue = Integer.parseInt(age);
            if (ageValue <= 0) {
                showInformationAlert("Error", "Age must be greater than 0.");
                return false;
            }
        } catch (NumberFormatException e) {
            showInformationAlert("Error", "Invalid age format.");
            return false;
        }

        if (genderGroup.getSelectedToggle() == null) {
            showInformationAlert("Error", "Gender must be selected.");
            return false;
        }

        if (nationality == null || nationality.isEmpty()) {
            showInformationAlert("Error", "Nationality must be selected.");
            return false;
        }

        return true;
    }

    private static String generateUserID() {
        String query = "SELECT MAX(CAST(SUBSTRING(UserID, 3) AS SIGNED)) AS maxIndex FROM msuser";
        connect.rs = connect.execQuery(query);

        try {
            if (connect.rs.next()) {
                int maxIndex = connect.rs.getInt("maxIndex");
                userIndex = maxIndex + 1;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        String indexStr = String.format("%03d", userIndex);
        return "UA" + indexStr;
    }

    private static boolean isEmailRegistered(String email) {
        String query = "SELECT UserEmail FROM msuser WHERE UserEmail = ?";
        connect.rs = connect.execQuery(query, email);

        try {
            return connect.rs.next();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    private static boolean insertUserToDatabase(String userID, String email, String password, String role, String age, String gender, String nationality) {
        String query = "INSERT INTO msuser (UserID, UserEmail, UserPassword, UserRole, UserAge, UserGender, UserNationality) VALUES (?, ?, ?, ?, ?, ?, ?)";
        connect.execUpdate(query, userID, email, password, role, age, gender, nationality);
        return true;
    }
}
