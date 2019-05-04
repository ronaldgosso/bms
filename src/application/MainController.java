package application;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import org.controlsfx.control.Notifications;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainController implements Initializable {
	@FXML
	private Label userlabel;
	@FXML
	private ImageView backView;
	@FXML
	private ImageView qrMid;
	@FXML
	private ImageView qr;
	@FXML
	private ImageView setting;
	@FXML
	private PasswordField passManager;
	@FXML
	private TextField userManager;
	@FXML
	protected AnchorPane rootPane;

	private QRCodeWriter qrCodeWriter;

	private ImageView qrView;
	@SuppressWarnings("unused")
	private String fileType;

	private static final AudioClip ALERT_AUDIOCLIP = new AudioClip(
			MainController.class.getResource("/Music/song.wav").toString());

	Connection connection;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		qr.setImage(new Image(getClass().getResource("/img/Qr.png").toString()));
		qrMid.setImage(new Image(getClass().getResource("/img/baga.png").toString()));
		setting.setImage(new Image(getClass().getResource("/img/settings.png").toString()));
		
		backView = new ImageView(getClass().getResource("/img/face.jpg").toString());
		
		new Notification().start();
		animate();
		connection = Database.Connector();
		if (connection == null) {
			System.exit(1);
		}
	}

	public void userGet(String User) {
		userlabel.setText(User);
	}

	public void animate() {
		TranslateTransition tt = new TranslateTransition(Duration.seconds(25), backView);
		tt.setFromX(-(backView.getFitWidth()));
		tt.setToX(rootPane.getPrefWidth());
		tt.setCycleCount(Timeline.INDEFINITE);
		tt.play();
	}

	public boolean ifDbIsConnected() {
		try {
			return !connection.isClosed();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public void sysAdminComs(ActionEvent event) {

		PreparedStatement statement = null;
		ResultSet set = null;
		String activate = "select * from manager where username = ? and password = ? ";
		try {
			statement = connection.prepareStatement(activate);
			statement.setString(1, userManager.getText());
			statement.setString(2, passManager.getText());
			set = statement.executeQuery();

			if (set.next()) {
				((Node) event.getSource()).getScene().getWindow().hide();// hide the last window in calling the recent
				Stage primaryStage = new Stage();
				Parent root = FXMLLoader.load(getClass().getResource("SysAdmHome.fxml"));
				primaryStage.getIcons().add(new Image("/img/baga.png"));
				primaryStage.setTitle("System Administrator Home");
				Scene scene = new Scene(root);
				primaryStage.setScene(scene);
				primaryStage.show();
			} else {
				MainController.ALERT_AUDIOCLIP.play();

				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("ALERT");
				alert.setGraphic(new ImageView(this.getClass().getResource("/img/Delete-icon.png").toString()));
				alert.setContentText("Please be sure of your details");
				alert.setHeaderText(null);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
				alert.showAndWait();

				passManager.clear();
				userManager.clear();
			}
		} catch (SQLException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("ALERT");
			alert.setGraphic(new ImageView(this.getClass().getResource("/img/Delete-icon.png").toString()));
			alert.setContentText("System Error");
			alert.setHeaderText(null);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
			alert.showAndWait();


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void qrLauncher(ActionEvent event) throws Exception {
		qrCodeWriter = new QRCodeWriter();
		String myWeb = "https://drive.google.com/file/d/1Pl0YAtSl77CfpRd3cjnuoQyzIIDyXHhA/view?usp=sharing";
		//https://drive.google.com/file/d/0B3u4VqGT6rQxWlpWQzl6ekxwMlE/view?usp=sharing
		int width = 300;
		int height = 300;
		fileType = "png";

		BufferedImage bufferedImage = null;
		try {
			BitMatrix byteMatrix = qrCodeWriter.encode(myWeb, BarcodeFormat.QR_CODE, width, height);
			bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			bufferedImage.createGraphics();

			Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();
			
			Color color = new Color(255, 255, 255);//transparency color
			color.getTransparency();
			graphics.setColor(color);
			graphics.fillRect(0, 0, width, height);
			graphics.setColor(Color.BLACK);

			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					if (byteMatrix.get(i, j)) {
						graphics.fillRect(i, j, 1, 1);
					}
				}
			}

			// System.out.println("Success...");

		} catch (WriterException ex) {
			ex.printStackTrace();
		}

		qrView = new ImageView();
		qrView.setImage(SwingFXUtils.toFXImage(bufferedImage, null));
		Stage primaryStage = new Stage();
		StackPane root = new StackPane();
		Scene scene = new Scene(root, 350, 350);
		root.getChildren().add(qrView);
		root.setStyle("-fx-background-color:#87CEFA;");
		primaryStage.getIcons().add(new Image("/img/baga.png"));
		primaryStage.setScene(scene);
		primaryStage.setTitle("QR Panel");
		
		primaryStage.show();
	}

}

class Notification extends Thread {
	public void run() {
		try {
			Thread.sleep(300000);// 5 minutes
		} catch (Exception e) {
			e.printStackTrace();
		}
		Image icon = new Image("/img/baga.png");
		Notifications notify = Notifications.create().graphic(new ImageView(icon)).title("Software Time Response")
				.text("You have been in the Panel for 5 minutes,Experienced errors? Contact Us")
				.hideAfter(Duration.seconds(30)).position(Pos.TOP_RIGHT);
		notify.darkStyle();
		Platform.runLater(new Runnable() {
			public void run() {
				notify.show();
			}
		});
	}
}
