package pigGame;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameOfPigController implements Initializable {
	
	// UI items
	private Stage stage;
	private Scene scene;
	public Pane statsAnchorPane;
	
	//public Button btn_playGame;
	//public Button btn_viewStats;
	public Button btn_roll;
	public Button btn_hold;
	public Button btn_quitGame;
	public Button btn_resetGame;
	//public Button btn_returnToMainFromStats;
	
	public Label lbl_playerOneScore;
	public Label lbl_p1Name;
	public Label lbl_roundScore;
	public Label lbl_playerTwoScore;
	public Label lbl_p2Name;
	public Label lbl_turnTracker;
	public Label lbl_leaderboard;
	
	public TextField txt_p1Name;
	public TextField txt_p2Name;
	
	// things that require files
	public static String resourcePath = "src/resources/";
	public Image[] dieImage = new Image[7];
	public ImageView img_dieImageDisplay;
	public static File file_pigStats = new File(resourcePath + "pigStats.txt");
	public static Scanner gameData = null;
	
	// game play
	public static int roundScore;
	public static int dieValue;
	public static int dieRoll;
	
	// create players
	Player playerOne = new Player();
	Player playerTwo = new Player();
	Player playerComp = new Player();
	Player activePlayer = playerOne; // Ready Player One
	
	
	// Player
	public class Player {
		
		private String name;
		private int score;
		private boolean isPlayerOne;
		//private boolean cPlayer;
		
		public Player() {
			
		}

		public String getName() {
			return this.name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getScore() {
			return score;
		}

		public void setScore(int score) {
			this.score = score;
		}
		
		public void setPlayerOne(boolean isPlayerOne) {
			this.isPlayerOne = isPlayerOne;
		}
		
		public boolean isPlayerOne() {
			return isPlayerOne;
			
		}
		
		public boolean computerFlag(boolean cPlayer) {
			return cPlayer;
			
		}
		
	}
	// end Player
	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		// check stats file
		checkForFile();
				
		// pre-load dice images
		for (int i = 1; i < dieImage.length; i++) {
			dieImage[i] = new Image("file:" + resourcePath + i + ".png");
		}
		
		
		// initialize a game state...
		// and know your roots
		playerOne.setName("Player 1");
		playerOne.setScore(0);
		playerOne.setPlayerOne(true);
		playerTwo.setName("Player 2");
		playerTwo.setScore(0);
		playerComp.setName("Computer");
		playerComp.setScore(0);
		roundScore = 0;
		
	}
	
	
	// Switching technique based on tutorial from "BroCode"
	// https://www.youtube.com/watch?v=hcM-R-YOKkQ
	public void switchToMainMenu(ActionEvent event) throws IOException {
		Parent mainMenuGroup = FXMLLoader.load(getClass().getResource("pig_mainMenu.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(mainMenuGroup);
		stage.setScene(scene);
		stage.show();

	}

	
	// do the file things
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
	
	
	// rage quit!
	public void resetGame() {
		playerOne.setScore(0);
		playerTwo.setScore(0);
		roundScore = 0;
		activePlayer = playerOne;
		
		lbl_turnTracker.setText("Current Round - " + activePlayer.getName());
		lbl_playerOneScore.setText(playerOne.getScore()+"");
		lbl_playerTwoScore.setText(playerTwo.getScore()+"");
		lbl_roundScore.setText(roundScore+"");
		
		
		btn_roll.setDisable(false);
		btn_hold.setDisable(false);
		
	}
	
	
	// this is the animated die code
	int frames;
	private AnimationTimer timer = new AnimationTimer() {

		@Override
		public void handle(long arg0) {
			dieRoll = (int)((Math.random() * (6)) + 1);
			img_dieImageDisplay.setImage(dieImage[dieRoll]);
			frames++;
			
			if (frames >= 40) {
				timer.stop();
				btn_roll.setDisable(false);
				btn_hold.setDisable(false);
				handleRound(dieRoll);
			}
		}
		
	};
	
	
	// snake eyes
	public void rollDie() {
		frames = 0;
		btn_roll.setDisable(true);
		btn_hold.setDisable(true);
		
		playerOne.setName(txt_p1Name.getText());
		playerTwo.setName(txt_p2Name.getText());
		
		timer.start();
	}
	
	
	// should we keep playing this round?
	public void handleRound(int dieValue) {
				
		if (dieValue == 1) {
			roundScore = 0;
			gameMessage("BUST!", 1500, true, true);
			switchPlayer();
			
		}
		
		else {
			roundScore = roundScore + dieValue;
			lbl_roundScore.setText(roundScore+"");
			checkWin();
			
		}
	}
	
	
	public void holdScore() {
		
		activePlayer.setScore(activePlayer.getScore() + roundScore);
		gameMessage("Switch", 500, true, true);
		switchPlayer();
		
	}
	
	
	public void checkWin() {
		
		Date date = Calendar.getInstance().getTime();
		DateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy HH:mm");
		String strDate = dateFormat.format(date);
		
		if ((activePlayer.getScore() + roundScore) >= 10) {
			activePlayer.setScore(activePlayer.getScore() + roundScore);
			
			//Game p1Game;
			//Game p2Game;
			
			String p1Game;
			String p2Game;
			
			if (activePlayer.isPlayerOne()) {
				lbl_playerOneScore.setText(playerOne.getScore()+"");
				
				//p1Game = new Game(playerOne.getName(), playerOne.getScore(), "W", strDate);
				//p2Game = new Game(playerTwo.getName(), playerTwo.getScore(), "L", strDate);
				
				p1Game = playerOne.getName() + "," + playerOne.getScore() + "," + "W" + "," + strDate;
				p2Game = playerTwo.getName() + "," + playerTwo.getScore() + "," + "L" + "," + strDate;


			}
			else {
				lbl_playerTwoScore.setText(playerTwo.getScore()+"");
				
				//p2Game = new Game(playerTwo.getName(), playerTwo.getScore(), "W", strDate);
				//p1Game = new Game(playerOne.getName(), playerOne.getScore(), "L", strDate);
				
				p2Game = playerTwo.getName() + "," + playerTwo.getScore() + "," + "W" + "," + strDate;
				p1Game = playerOne.getName() + "," + playerOne.getScore() + "," + "L" + "," + strDate;


			}
						
			try {
				PrintWriter toFile = new PrintWriter(new FileWriter(file_pigStats, true));
				toFile.append(p1Game + "\n");
				toFile.append(p2Game + "\n");
				toFile.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			gameMessage("YOU WIN!", 3000, true, false);
		}
		
	}
	
	
	// generate message based on game play
	// method accepts:
	// msg = String of what the message is
	// msgTime = how long to display before continuing (milliseconds)
	// disableButtons = can the user press any game buttons during message
	// goAfter = should we actually keep playing after the message
	public void gameMessage(String msg, int msgTime, boolean disableButtons, boolean goAfter) {
		
		PauseTransition pause = new PauseTransition(Duration.millis(1));
		
		pause.setOnFinished(e-> {
			btn_roll.setDisable(disableButtons);
			btn_hold.setDisable(disableButtons);
			lbl_roundScore.setText(msg);
		});
		pause.play();
		
		if (goAfter) {
			PauseTransition nowGo = new PauseTransition(Duration.millis(msgTime));
			nowGo.setOnFinished(e-> {
				btn_roll.setDisable(false);
				btn_hold.setDisable(false);
				lbl_turnTracker.setText("Current Round - " + activePlayer.getName());
				roundScore = 0;
				lbl_roundScore.setText(roundScore+"");
			});
			nowGo.play();
		}
	}
	
	public Player switchPlayer() {
		
		if (activePlayer.equals(playerOne)) {
			playerOne.setScore(activePlayer.getScore());
			lbl_playerOneScore.setText(playerOne.getScore()+"");
			activePlayer = playerTwo;
			
		}
		else if (activePlayer.equals(playerTwo)) {
			playerTwo.setScore(activePlayer.getScore());
			lbl_playerTwoScore.setText(playerTwo.getScore()+"");
			activePlayer = playerOne;
			
		}
		return activePlayer;
		
	}
	
}
