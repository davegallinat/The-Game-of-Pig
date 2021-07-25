package pigGame;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) {		
		
		try {
			Parent mainMenuGroup = FXMLLoader.load(getClass().getResource("pig_mainMenu.fxml"));
			//Parent playAreaGroup = FXMLLoader.load(getClass().getResource("pig_mainMenu.fxml"));
			//Parent statsViewGroup = FXMLLoader.load(getClass().getResource("pig_mainMenu.fxml"));
		
			final Scene mainMenuScene = new Scene(mainMenuGroup);
			//final Scene playAreaScene = new Scene(playAreaGroup);
			//final Scene statsViewScene = new Scene(statsViewGroup);
			
			primaryStage.setScene(mainMenuScene);
			primaryStage.show();
		
		
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
