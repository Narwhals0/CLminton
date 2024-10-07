package BADFinalProjectLAB;

import View.LoginView;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
		LoginView LoginView = new LoginView(primaryStage);
		LoginView.show();
	}
	
	public static void main(String[] args) {
		launch();
	}

}
