package pigGame;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class pigMainMenuController implements Initializable {
	
	// UI items
	private Stage stage;
	private Scene scene;
	public Pane statsAnchorPane;
		
	public Button btn_playGame;
	public Button btn_viewStats;
	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	
	
	public void SwitchToPlayArea(ActionEvent event) throws IOException {
		Parent playAreaGroup = FXMLLoader.load(getClass().getResource("pig_playArea.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(playAreaGroup);
		stage.setScene(scene);
		stage.show();

	}
	
	public void switchToViewStats(ActionEvent event) throws IOException {
				
		Parent statsViewGroup = FXMLLoader.load(getClass().getResource("pig_statsArea.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(statsViewGroup);
		stage.setScene(scene);
		stage.show();

	}


}
