package application;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;
import org.controlsfx.control.textfield.TextFields;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;

public class WebviewController implements Initializable {
	@FXML
	private WebView view;// make the load of the contents of the Web
	@FXML
	private WebEngine engine;// load the url inputs
	@FXML
	private Label webLogo;
	@FXML
	private ImageView webpic;
	@FXML
	private ImageView exitView;
	@FXML
	private TextField searchEngine;
	@FXML
	private Button searchActionEngine;
	@FXML
	private ImageView browserbaga;
	@FXML
	private ImageView searchImage;

	private static final AudioClip ALERT_AUDIOCLIP = new AudioClip(
			AdminController.class.getResource("/Music/song.wav").toString());

	private String[] words = { "a", "about", "all", "also", "and", "as", "at", "be", "because", "but", "by", "can",
			"come", "could", "day", "d", "even", "find", "first", "fo", "from", "get", "give", "go", "have", "he",
			"her", "here", "him", "his", "how", "I", "if", "in", "into", "it", "its", "just", "know", "like", "look",
			"make", "man", "many", "me", "more", "my", "new", "no", "not", "now", "of", "on", "one", "only", "or",
			"other", "our", "out", "people", "say", "see", "she", "so", "some", "take", "tell", "than", "that", "the",
			"their", "them", "then", "there", "these", "they", "thing", "think", "this", "those", "time", "to", "two",
			"up", "use", "very", "want", "way", "we", "well", "what", "when", "which", "who", "will", "with", "would",
			"year", "you", "your", "Tanzania", "God", "Tange", "Mberwa", "Heaven", "Bagamoyo", "Arusha", "magufuli",
			"a", "as", "are", "ability", "access", "add", "aim", "air", "after", "again", "ahead", "along", "apples",
			"apple", "as", "assume", "allow", "aid", "amaze", "ago", "aspects", "award", "about", "above", "abroad",
			"absence", "absent", "absolute", "abstract", "abuse", "abusive", "academic", "accept", "acceptable",
			"account", "accountant", "accurate", "acquire", "act", "action", "active", "actor", "actress", "across",
			"active", "addition", "Address", "admission", "admire", "actual", "actually", "adult", "actually",
			"advantage", "afford", "affect", "aftre", "age", "agency", "agent", "advise", "advice", "alert", "alilgn",
			"adjourn", "alpha", "enhance", "improve", "leverage", "impossible", "possible", "use", "usage", "obtain",
			"get", "let", "it", "rain", "open", "gates", "heaven", "pray", "don't", "stop", "time", "is", "near",
			"grapes", "advert", "find", "new", "things", "security", "invest", "investment", "contacts", "personal",
			"buy", "go", "run", "margin", "find", "funds", "supply", "borrow", "profit", "loss", "make", "sense",
			"investors", "speculative", "power", "true", "false", "vintage", "furnish", "provide", "render",
			"necessary", "room", "somethimg", "useful", "provide", "electric", "decision", "decisions", "fun", "for",
			"free", "friend", "public", "private", "ceertificate", "bond", "choice", "cloud", "before", "sit",
			"stand up", "matter", "very", "interest", "view", "context", "between", "besides", "under", "in", "on",
			"I'm", "were", "would", "have", "been", "has", "we", "shall", "will", "he", "she", "It", "I", "arrange",
			"floor", "pavement", "base", "basement", "blocks", "block", "building", "whose", "who", "which", "where",
			"you", "dancing", "dance", "date", "lunch", "eat", "movies", "out", "tickets", "tummy", "hurts", "head",
			"arm", "arms", "soul", "reference", "Joan", "Ronald", "husband", "wife", "children", "wealthy", "family",
			"school", "schools", "sucks", "LOL", "ROFL", "irritate", "feeling", "lie", "lies", "hate", "love", "kiss",
			"kissing", "making", "together", "ship", "great", "warrior", "broken", "mirror", "first", "second",
			"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "January", "February", "March", "April", "May",
			"June", "July", "August", "September", "October", "November", "December", "work", "walk", "target", "cruel",
			"cold", "Idol", "GOD", "calculate", "tons", "what", "had", "it", "these", "victims", "annoy", "annoying",
			"was", "though", "association", "violet", "one", "two", "three", "four", "five", "six", "seven", "eight",
			"nine", "ten", "open", "close", "appprove", "proud", "of", "for", "mine", "us", "wet", "dry", "shelter",
			"shame", "me", "high", "low", "middle", "medium", "mistake", "most", "more", "intelligent", "software",
			"tools", "catch", "throw", "fall", "up", "down", "Earth", "Imaginary friends", "pink", "color", "cup",
			"dice", "roll", "blow", "Grace", "weird", "mind", "sight", "acknowledge", "desire", "do", "never", "call",
			"ever", "forever", "dive in", "David", "Janelle", "Victor", "Victoria", "wow"

	};

	@Override
	public void initialize(URL location, ResourceBundle Resources) {
		
		webpic.setImage(new Image (getClass().getResource("/img/4k.jpg").toString()));
		browserbaga.setImage(new Image (getClass().getResource("/img/baga.png").toString()));
		searchImage.setImage(new Image (getClass().getResource("/img/search.png").toString()));
		exitView.setImage(new Image (getClass().getResource("/img/off.png").toString()));
		
		new Notify().start();
		engine = view.getEngine();// take the engine inputs url to the WEBVIEW
		TextFields.bindAutoCompletion(searchEngine, words);
	}

	public void intNews(ActionEvent event) {
		webpic.setVisible(false);
		webLogo.setText(" ");
		browserbaga.setVisible(false);
		searchImage.setVisible(false);
		searchEngine.setVisible(false);
		searchActionEngine.setVisible(false);

		engine.load("http://www.bbc.com/swahili");
		engine.getOnError();
	}

	public void insideNews(ActionEvent event) {
		webpic.setVisible(false);
		browserbaga.setVisible(false);
		searchImage.setVisible(false);
		webLogo.setText(" ");
		searchEngine.setVisible(false);
		searchActionEngine.setVisible(false);
		engine.load("https://globalpublishers.co.tz/");
		engine.getOnError();
	}

	public void regWeb(ActionEvent event) {
		webpic.setVisible(false);
		browserbaga.setVisible(false);
		searchImage.setVisible(false);
		webLogo.setVisible(false);
		searchEngine.setVisible(false);
		searchActionEngine.setVisible(false);
		engine.load("http://pwani.go.tz/");
		engine.getOnError();
	}

	public void pdfConverter(ActionEvent event) {
		webpic.setVisible(false);
		browserbaga.setVisible(false);
		searchImage.setVisible(false);
		webLogo.setVisible(false);
		searchEngine.setVisible(false);
		searchActionEngine.setVisible(false);
		engine.load("http://www.ilovepdf.com/");
		engine.getOnError();
	}

	/*
	 * public void pdfConverter(ActionEvent event){ webpic.setVisible(false);
	 * browserbaga.setVisible(false); searchImage.setVisible(false);
	 * webLogo.setVisible(false); searchEngine.setVisible(false);
	 * searchActionEngine.setVisible(false);
	 * engine.load("http://www.ilovepdf.com/"); }
	 */
	public void search(ActionEvent event) {
		String input = searchEngine.getText();

		if (input.isEmpty()) {
			browserbaga.setVisible(false);
			searchImage.setVisible(false);
			webpic.setVisible(false);
			webLogo.setVisible(false);
			searchEngine.setVisible(false);
			searchActionEngine.setVisible(false);
			engine.load("https://www.google.com/");
			engine.getOnError();
		} else {
			WebviewController.ALERT_AUDIOCLIP.play();

			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("CONFIRM");
			alert.setGraphic(new ImageView(this.getClass().getResource("/img/alert.jpg").toString()));
			alert.setContentText("Better use GOOGLE button the server is out of budget,Wanna prove?");
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				browserbaga.setVisible(false);
				searchImage.setVisible(false);

				String fully = "https://www.google.com/" + input;
				webpic.setVisible(false);
				webLogo.setVisible(false);
				searchEngine.setVisible(false);
				searchActionEngine.setVisible(false);
				engine.load(fully);
			} else {
				alert.close();
			}
		}

	}

	public void google(ActionEvent event) {
		webpic.setVisible(false);
		webLogo.setVisible(false);
		searchEngine.setVisible(false);
		browserbaga.setVisible(false);
		searchImage.setVisible(false);
		searchActionEngine.setVisible(false);
		engine.load("https://www.google.com");
		engine.getOnError();
	}

	public void homePage(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide();
		Parent root = FXMLLoader.load(getClass().getResource("Webview.fxml"));
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		primaryStage.getIcons().add(new Image("/img/baga.png"));
		primaryStage.setTitle("Bagamoyo System Web Browser (Experimental)");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void exit(ActionEvent event) throws Exception {
		WebviewController.ALERT_AUDIOCLIP.play();
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("CONFIRM");
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));

		alert.setContentText("Do you want to exit the Browser?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			((Node) event.getSource()).getScene().getWindow().hide();
			

		} else {
			alert.close();
		}

	}

	class Notify extends Thread {
		public void run() {
			try {
				Thread.sleep(6000);
			} catch (Exception e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("PROMPT");
				alert.setGraphic(new
				ImageView(this.getClass().getResource("/img/Delete-icon.png").toString()));
				alert.setContentText("System Error");
				alert.setHeaderText(null);
				 Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new
				Image(this.getClass().getResource("/img/baga.png").toString()));
				alert.showAndWait();
			}
			Image icon = new Image("/img/baga.png");
			Notifications notify = Notifications.create().graphic(new ImageView(icon)).title("Sofware Server Warnings")
					.text("Check your connection and make sure you are connected").hideAfter(Duration.seconds(30))
					.position(Pos.TOP_RIGHT);
			notify.darkStyle();
			Platform.runLater(new Runnable() {
				public void run() {
					notify.show();
				}
			});
		}

	}
}
