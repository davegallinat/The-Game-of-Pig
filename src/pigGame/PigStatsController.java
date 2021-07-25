package pigGame;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.stream.Collectors;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class PigStatsController implements Initializable {
	
	// UI items
	private Stage stage;
	private Scene scene;
	public Pane statsAnchorPane;

	// stats view stuff
	public List<String> leaderList = new ArrayList<>();
		
	public Label lbl_leaderboard;
	
	// file things
	public static String resourcePath = "src/resources/";
	public Image[] dieImage = new Image[7];
	public ImageView img_dieImageDisplay;
	public static File file_pigStats = new File(resourcePath + "pigStats.txt");
	public static Scanner gameData = null;


	@FXML
	private TableView<Game> statsTable = new TableView<>();
	private TableColumn<Game, String> col_gamePlayer = new TableColumn<>("Player");
	private TableColumn<Game, Integer> col_gameScore = new TableColumn<>("Score");
	private TableColumn<Game, String> col_gameOutcome = new TableColumn<>("Win/Lose");
	private TableColumn<Game, String> col_gameDate = new TableColumn<>("Date");
	
		
	// Game
	public class Game {
			
	private StringProperty playerName;
	private IntegerProperty playerScore;
	private StringProperty winOrLose;
	private StringProperty gameDate;
			
	// constructor
	public Game(String playerName, int playerScore, String winOrLose, String gameDate) {
		this.playerName = new SimpleStringProperty(playerName);
		this.playerScore = new SimpleIntegerProperty(playerScore);
		this.winOrLose = new SimpleStringProperty(winOrLose);
		this.gameDate = new SimpleStringProperty(gameDate);
	}
			
	// playerName
	public String getPlayerName() {
		return playerName.get();
	}
	public void setPlayerName(String playerName) {
		this.playerName.set(playerName);
	}
	public StringProperty playerNameProperty() {
		if (playerName == null) playerName = new SimpleStringProperty(this, "playerName");
			return playerName;
		}
	
	// playerScore
	public int getPlayerScore() {
		return playerScore.get();
	}
	public void setPlayerScore(int playerScore) {
		this.playerScore.set(playerScore);
	}
	public IntegerProperty playerScoreProperty() {
		if (playerScore == null) playerScore = new SimpleIntegerProperty(this, "playerScore");
			return playerScore;
		}
	
	// win or lose
	public String getWinOrLose() {
		return winOrLose.get();
	}
	public void setWinOrLose(String winOrLose) {
		this.winOrLose.set(winOrLose);
	}
	public StringProperty winOrLoseProperty() {
		if (winOrLose == null) winOrLose = new SimpleStringProperty(this, "winOrLose");
		return winOrLose;
	}
	
	// game date
	public String getGameDate() {
		return gameDate.get();
	}
	public void setGameDate(String gameDate) {
		this.gameDate.set(gameDate);
	}
	public StringProperty gameDateProperty() {
		if (gameDate == null) gameDate = new SimpleStringProperty(this, "gameDate");
			return gameDate;
		}
			
	// toString
	public String toString() {
		return playerName.get()+","+playerScore.get()+","+winOrLose.get()+","+gameDate.get()+"\n";
	}
	
	}
	// end Game
		
		
	public void switchToMainMenu(ActionEvent event) throws IOException {
		Parent mainMenuGroup = FXMLLoader.load(getClass().getResource("pig_mainMenu.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(mainMenuGroup);
		stage.setScene(scene);
		stage.show();

	}
		
		
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		checkForFile();
		getStats();
	}
	
	
	public void checkForFile() {
		
		try {		
			if (file_pigStats.createNewFile()) {
			}
			else {
			}	
		} catch (IOException e) {
			   System.out.println("File Error");	   
		}
				
		try {
			gameData = new Scanner(file_pigStats);
			
		} catch (FileNotFoundException e) {
			   System.out.println("File Not Found");	
		}			
	}
	
	
	public void getStats() {
		
		Game sg = null;
		String[] singleGame = null;
		List<Game> gamesList = new ArrayList<>();
		List<String> leaderList = new ArrayList<>();
		
		
		//String gameLine;
		while(gameData.hasNextLine()){
			String gameLine = gameData.nextLine();
		
			if (!gameLine.isEmpty()) {
				singleGame = gameLine.split(",");
				sg = new Game(
					singleGame[0],
					Integer.parseInt(singleGame[1]),
					singleGame[2],
					singleGame[3]		
				);
				
				if (singleGame[2].equals("W")) {
				leaderList.add(singleGame[0]);
				}
			}
			else {
				gameData.nextLine();
			}
			
			gamesList.add(sg);	
		}
				
		
	// generate leaderBoard top three
	Map<String, Long> leaderBoard = leaderList.stream()
		//.sorted()
		.collect(Collectors.groupingBy(e->e, TreeMap::new, Collectors.counting()));

	String strLeaderBoard = leaderBoard.entrySet()
		.stream()
		.map(e->e.getKey() + " (" + e.getValue() + " wins)")
		.limit(3)
		.collect(Collectors.joining(", "));
	
	lbl_leaderboard.setText("Leaderboard: " + strLeaderBoard);
	
	
	// tooltip to display the rest of the player records	
	String fullLeaderBoard = leaderBoard.entrySet()
		.stream()
		.map(e->e.getKey() + " (" + e.getValue() + " wins)")
		.collect(Collectors.joining("\n"));
	
	lbl_leaderboard.setTooltip(new Tooltip(fullLeaderBoard));	
	
	
	// generate statistics table
	ObservableList<Game> games = FXCollections.observableArrayList(gamesList);
				
	statsTable.setItems(games);
			
	col_gamePlayer.setCellValueFactory(p-> p.getValue().playerNameProperty());
	col_gameScore.setCellValueFactory(q-> q.getValue().playerScoreProperty().asObject());
	col_gameOutcome.setCellValueFactory(p-> p.getValue().winOrLoseProperty());
	col_gameDate.setCellValueFactory(p-> p.getValue().gameDateProperty());

	statsTable.getColumns().setAll(col_gamePlayer, col_gameScore, col_gameOutcome, col_gameDate);
		
	}

}
