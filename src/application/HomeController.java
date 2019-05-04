package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.controlsfx.control.Notifications;

import com.jfoenix.controls.JFXToggleButton;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

public class HomeController implements Initializable {

	@FXML
	private MediaView media;
	private MediaPlayer mp;
	private Media me;
	@FXML
	private ImageView imageTop;
	@FXML
	private ImageView leftView;
	@FXML
	private ImageView sysAdminView;
	@FXML
	private ImageView adminView;
	@FXML
	private ImageView supervisorView;
	@FXML
	private ImageView rightView;
	@FXML
	private ImageView callView;
	@FXML
	JFXToggleButton turnOffSlider;
	@FXML
	Label date;
	@FXML
	private PieChart pie;

	@FXML
	private AnchorPane root;

	private static final AudioClip ALERT_AUDIOCLIP = new AudioClip(
			AdminController.class.getResource("/Music/back.wav").toString());

	Stage primaryStage = new Stage();

	@SuppressWarnings("static-access")
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// splash
		if (!NaivgationDrawer.isSplashLoaded) {
			loadSplashScreen();
		}

		Image image = new Image(
				getClass().getResource("/img/back.gif").toString());
		image.getHeight();
		image.getWidth();
		leftView.setImage(
				new Image(getClass().getResource("/img/tz.png").toString()));
		rightView.setImage(
				new Image(getClass().getResource("/img/baga.png").toString()));
		sysAdminView.setImage(new Image(
				getClass().getResource("/img/system-admin.png").toString()));
		adminView.setImage(
				new Image(getClass().getResource("/img/Admin.png").toString()));
		supervisorView.setImage(new Image(
				getClass().getResource("/img/Supervisor-icon.png").toString()));
		callView.setImage(
				new Image(getClass().getResource("/img/call.png").toString()));
		imageTop.setImage(image);

		// Thread
		new Notify().start();

		//

		// Video slider::
		String path = new File("resources/video/baga.mp4").getAbsolutePath();
		me = new Media(new File(path).toURI().toString());
		mp = new MediaPlayer(me);
		media.setMediaPlayer(mp);
		mp.setAutoPlay(true);
		mp.setCycleCount(mp.INDEFINITE);// repeat forever

		// toggleButton
		turnOffSlider.selectedProperty()
				.addListener(new ChangeListener<Boolean>() {

					@Override
					public void changed(ObservableValue<? extends Boolean> arg0,
							Boolean arg1, Boolean arg2) {
						if (turnOffSlider.isSelected() == true) {
							mp.stop();
						} else {
							mp.stop();

							mp.seek(mp.getStartTime());

							mp.play();
						}

					}

				});

		// Date::
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

		// Get the date today using Calendar object.
		Date today = Calendar.getInstance().getTime();
		// Using DateFormat format method we can create a string
		// representation of a date with the defined format.
		String issuedDate = df.format(today) + "  "
				+ new SimpleDateFormat("EEEE").format(new Date());

		date.setText(issuedDate);

		// pieChart::
		ObservableList<Data> list = FXCollections.observableArrayList(
				new PieChart.Data("Secondary Schools", 9),
				new PieChart.Data("Primary Schools", 32), // having
															// the
															// contents
															// of
															// the
															// chart
				new PieChart.Data("Wards", 11), // with
				// their
				// respective
				// values
				new PieChart.Data("Hospital", 1),
				new PieChart.Data("Indigenous", 60),
				new PieChart.Data("Dispensaries", 16));
		pie.setData(list);
		pie.setTitle("Statistics Summary");

	}

	public void loadSplashScreen() {
		try {
			NaivgationDrawer.isSplashLoaded = true;

			StackPane pane = FXMLLoader
					.load(getClass().getResource(("splashSample.fxml")));
			root.getChildren().setAll(pane);

			HomeController.ALERT_AUDIOCLIP.play();

			FadeTransition fadeIn = new FadeTransition(Duration.seconds(13),
					pane);
			fadeIn.setFromValue(0);
			fadeIn.setToValue(1);
			fadeIn.setCycleCount(1);

			FadeTransition fadeOut = new FadeTransition(Duration.seconds(5),
					pane);
			fadeOut.setFromValue(1);
			fadeOut.setToValue(0);
			fadeOut.setCycleCount(1);

			fadeIn.play();

			fadeIn.setOnFinished((e) -> {
				fadeOut.play();
			});

			fadeOut.setOnFinished((e) -> {
				try {
					AnchorPane parentContent = FXMLLoader
							.load(getClass().getResource(("Home.fxml")));
					root.getChildren().setAll(parentContent);

					HomeController.ALERT_AUDIOCLIP.stop();

				} catch (IOException ex) {
					Logger.getLogger(HomeController.class.getName())
							.log(Level.SEVERE, null, ex);
				}
			});

		} catch (IOException ex) {
			Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}

	public void sysAdmin(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide();
		Parent root = FXMLLoader.load(getClass().getResource("SysAdm.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("System Administrator Log In Panel");
		primaryStage.getIcons().add(new Image("/img/baga.png"));
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void admin(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide();
		Parent root = FXMLLoader.load(getClass().getResource("Admin.fxml"));
		Scene scene = new Scene(root);
		primaryStage.getIcons().add(new Image("/img/baga.png"));
		primaryStage.setTitle("Administration Log In Panel");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void supervisor(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide();
		Parent root = FXMLLoader.load(getClass().getResource("LogSql.fxml"));
		primaryStage.getIcons().add(new Image("/img/baga.png"));
		primaryStage.setTitle("Supervisor Log In Panel");
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void contactsPage(ActionEvent event) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("Contacts.fxml"));
		primaryStage.getIcons().add(new Image("/img/baga.png"));
		primaryStage.setTitle("Feel Free,Make Contacts With Us");
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void webLauncher(ActionEvent event) throws Exception {

		Parent root = FXMLLoader.load(getClass().getResource("Webview.fxml"));
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		primaryStage.getIcons().add(new Image("/img/baga.png"));
		primaryStage.setTitle("Bagamoyo System Web Browser (Experimental)");
		primaryStage.setScene(scene);
		Image icon = new Image("/img/baga.png");
		Notifications notify = Notifications.create()
				.graphic(new ImageView(icon)).title("Software Panels Response")
				.text("Welcome To The System Web Browser")
				.hideAfter(Duration.seconds(10)).position(Pos.TOP_RIGHT);
		notify.darkStyle();
		notify.show();
		primaryStage.show();
	}

	public void mediaPlayer(ActionEvent event) throws Exception {

		Parent root = FXMLLoader.load(getClass().getResource("Player.fxml"));
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		primaryStage.getIcons().add(new Image("/img/baga.png"));
		primaryStage.setTitle("System Media Player (Experimental)");
		primaryStage.setScene(scene);

		Image icon = new Image("/img/baga.png");
		Notifications notify = Notifications.create()
				.graphic(new ImageView(icon)).title("Software Panels Response")
				.text("Welcome To The System Media Player")
				.hideAfter(Duration.seconds(10)).position(Pos.TOP_RIGHT);
		notify.darkStyle();
		notify.show();

		primaryStage.show();

	}

	public void exitApp(ActionEvent event) {
		Platform.exit();
		System.exit(0);
	}

	public void logOutToHome(ActionEvent event) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("Home.fxml"));
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		primaryStage.getIcons().add(new Image("/img/baga.png"));
		primaryStage.setTitle(
				"Welcome to Bagamoyo District Council Management System");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	/*
	 * public void Notification(ActionEvent e){ Image icon = new
	 * Image("img/baga.png"); Notifications notify = Notifications.create()
	 * .graphic(new ImageView(icon)) .title("System Time Response")
	 * .text("You have used the software by 1 hour")
	 * .hideAfter(Duration.seconds(5)) .position(Pos.TOP_RIGHT); notify.show();
	 * }
	 */

}

class Notify extends Thread {
	public void run() {
		try {
			Thread.sleep(3600000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Image icon = new Image("/img/baga.png");
		Notifications notify = Notifications.create()
				.graphic(new ImageView(icon)).title("Software Time Response")
				.text("You have used the software by 1 hour")
				.hideAfter(Duration.seconds(30)).position(Pos.TOP_RIGHT);
		notify.darkStyle();
		Platform.runLater(new Runnable() {
			public void run() {
				notify.show();
			}
		});
	}

}
