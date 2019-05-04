package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;

public class AdminController implements Initializable {
	@FXML
	private TextField user;
	@FXML
	private ImageView adminView;
	@FXML
	private PasswordField pass;

	private static final AudioClip ALERT_AUDIOCLIP = new AudioClip(
			AdminController.class.getResource("/Music/song.wav").toString());

	Connection connection;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		adminView.setImage(new Image(getClass().getResource("/img/settings.png").toString()));

		connection = Database.Connector();
		if (connection == null) {
			System.exit(1);
		}

	}

	public boolean ifDbIsConnected() {
		try {
			return !connection.isClosed();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public void Login(ActionEvent event) {
		PreparedStatement statement = null;
		ResultSet set = null;
		String activate = "select * from admin where username = ? and password = ?";
		try {
			statement = connection.prepareStatement(activate);
			statement.setString(1, user.getText());
			statement.setString(2, pass.getText());
			set = statement.executeQuery();

			if (set.next()) {
				((Node) event.getSource()).getScene().getWindow().hide();// hide the last window in calling the recent
				Stage primaryStage = new Stage();
				Parent root = FXMLLoader.load(getClass().getResource("AdminHome.fxml"));
				primaryStage.getIcons().add(new Image("/img/baga.png"));
				primaryStage.setTitle("System Administrator Home");
				Scene scene = new Scene(root);
				primaryStage.setScene(scene);
				primaryStage.show();
			} else {
				AdminController.ALERT_AUDIOCLIP.play();

				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("ALERT");
				alert.setGraphic(new ImageView(this.getClass().getResource("/img/Delete-icon.png").toString()));
				alert.setContentText("Please be sure of your details");
				alert.setHeaderText(null);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
				alert.showAndWait();

				pass.clear();
				user.clear();
			}
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("ALERT");
			alert.setGraphic(new ImageView(this.getClass().getResource("/img/Delete-icon.png").toString()));
			alert.setContentText("System Error");
			alert.setHeaderText(null);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
			alert.showAndWait();

		}
	}
}
