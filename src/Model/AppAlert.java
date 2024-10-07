package Model;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class AppAlert {
	 public static void showInformationAlert(String title, String message) {
	        Alert alert = new Alert(Alert.AlertType.INFORMATION);
	        alert.setTitle(title);
	        alert.setHeaderText(null);
	        alert.setContentText(message);
	        alert.showAndWait();

	    }

	    public static void showWarningAlert(String title, String message) {
	        Alert alert = new Alert(Alert.AlertType.WARNING);
	        alert.setTitle(title);
	        alert.setHeaderText(null);
	        alert.setContentText(message);
	        alert.showAndWait();

	    }

	    public static boolean showConfirmationAlert(String title, String message, int totalPrice) {
	        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
	        alert.setTitle("Confirmation");
	        alert.setHeaderText("Total Price: " + totalPrice);
	        alert.setContentText("Do you want to proceed with the checkout?");

	        ButtonType yesButton = new ButtonType("Yes");
	        ButtonType noButton = new ButtonType("No");
	        alert.getButtonTypes().setAll(yesButton, noButton);

	        Optional<ButtonType> result = alert.showAndWait();
	        /*result.isPresent() = liat apakah confirmation dialog muncul apa tidak
	        result.get() = cek apakah button yang dipencet itu "Yes"
	        Setelah showConfirmationDialog return true maka :
	        cartTableView akan diclear
	        Update label ke yang awal
	        pindah ke scene transactionCardPopUp */

	        return result.isPresent() && result.get() == yesButton;

	    }
}
