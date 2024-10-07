package Controller;

import java.sql.SQLException;

import Model.AppAlert;
import Model.User;
import Util.Connect;
import View.HomeAdminView;
import View.HomeView;

import static View.LoginView.primaryStage;

public class LoginController {
	  static Connect connect = Connect.getInstance();
	    public static void handleLogin(String userEmail, String password) {

	        if (userEmail.isEmpty() || password.isEmpty()) {
	            AppAlert.showInformationAlert("Warning", "Username and password must be filled.");
	            return;
	        }

	        String query = "SELECT UserID, UserRole FROM msuser WHERE UserEmail = ? AND UserPassword = ?";
	        try {
	            connect.ps = connect.getPreparedStatement(query);
	            connect.ps.setString(1, userEmail);
	            connect.ps.setString(2, password);

	            connect.rs = connect.ps.executeQuery();

	            if (connect.rs.next()) {
	                String role = connect.rs.getString("UserRole");
	                String userId = connect.rs.getString("UserID");

	                if ("Admin".equals(role)) {
	                    AppAlert.showInformationAlert("Success", "Admin login successful.");
	                    System.out.println("User login successful.");
	                    User user = new User();
	                    User.setUserId(userId);
	                    User.setCurrentUser(user);

	                    HomeAdminView.show(primaryStage);
	                } else if ("User".equals(role)) {
	                    AppAlert.showInformationAlert("Success", "User login successful.");
	                    System.out.println("User login successful.");
	                    User user = new User();
	                    user.setUserId(userId);
	                    User.setCurrentUser(user);

	                    HomeView.show(primaryStage);
	                }
	            } else {
	                AppAlert.showInformationAlert("Error", "Invalid username or password. Please try again.");
	            }
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	            AppAlert.showInformationAlert("Error", "Database connection error.");
	        } finally {
	            closePreparedStatement();
	        }
	    }

	    private static void closePreparedStatement() {
	        if (connect.ps != null) {
	            try {
	                connect.ps.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	    }
}
