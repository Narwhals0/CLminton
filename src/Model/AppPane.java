package Model;

import javafx.geometry.Insets;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class AppPane {
	  static GridPane gridPane;
	    static BorderPane borderPane;

	    public static GridPane createGridPane() {
	        gridPane = new GridPane();
	        gridPane.setHgap(10);
	        gridPane.setVgap(10);
	        gridPane.setPadding(new Insets(20, 20, 20, 20));

	        return gridPane;
	    }

	    public static BorderPane createBorderPane(GridPane content, MenuBar menuBar) {
	        borderPane = new BorderPane();
	        borderPane.setTop(menuBar);
	        if (content != null) {
	            borderPane.setCenter(content);
	        }

	        return borderPane;
	    }
}
