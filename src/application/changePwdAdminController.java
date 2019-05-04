package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXPasswordField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;

public class changePwdAdminController implements Initializable {
	@FXML
	private TextField oldPassword;
	@FXML
	private ImageView topPass;
	@FXML
	private ImageView midPass;
	@FXML
	private JFXPasswordField newPassword;
	@FXML
	private PasswordField verify;

	private static final AudioClip ALERT_AUDIOCLIP = new AudioClip(
			changePwdAdminController.class.getResource("/Music/success.wav").toString());

	private static final AudioClip ALERT_AUDIOCLIPY = new AudioClip(
			AdminController.class.getResource("/Music/song.wav").toString());

	Connection connection;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		topPass.setImage((new Image(getClass().getResource("/img/Cpass.png").toString())));
		midPass.setImage((new Image(getClass().getResource("/img/pwd.png").toString())));

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

	public void change(ActionEvent ev) {
		PreparedStatement state = null;
		if ( empty() & space() & verify()) {
				try {
					String query = "UPDATE manager SET password = ?";
					state = connection.prepareStatement(query);
					state.setString(1, newPassword.getText());
					state.executeUpdate();

					state.close();

					changePwdAdminController.ALERT_AUDIOCLIP.play();

					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("PROMPT");
					alert.setGraphic(new ImageView(this.getClass().getResource("/img/success.jpg").toString()));
					alert.setContentText("Admin Password Changed");
					alert.setHeaderText(null);
					Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
					stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
					alert.showAndWait();

					newPassword.clear();
					oldPassword.clear();
					verify.clear();

				} catch (Exception c) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("PROMPT");
					alert.setGraphic(new ImageView(this.getClass().getResource("/img/Delete-icon.png").toString()));
					alert.setContentText("System Error");
					alert.setHeaderText(null);
					Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
					stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
					alert.showAndWait();
				}	
		}

	}
	
	public boolean verify() {
		if(newPassword.getText().equals(verify.getText())) {
			return true;
		}else {
			changePwdAdminController.ALERT_AUDIOCLIPY.play();
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("PROMPT");
			alert.setGraphic(new ImageView(this.getClass().getResource("/img/think.jpeg").toString()));
			alert.setContentText("Passwords Mismatch");
			alert.setHeaderText(null);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
			alert.showAndWait();
			return false;
		}
	}
	
	public boolean empty() {
		if(!newPassword.getText().isEmpty() && !verify.getText().isEmpty()) {
			return true;
		}else {
			changePwdAdminController.ALERT_AUDIOCLIPY.play();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("PROMPT");
			alert.setGraphic(new ImageView(this.getClass().getResource("/img/Delete-icon.png").toString()));
			alert.setContentText("No Empty Passwords Are Required");
			alert.setHeaderText(null);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
			alert.showAndWait();
			return false;
		}
	}
	public boolean space() {
		boolean space = newPassword.getText().contains(" ");
		boolean space2 =  verify.getText().contains(" ");
		
		if (!space & !space2) {
			return true;
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("PROMPT");
			alert.setGraphic(new ImageView(this.getClass().getResource("/img/Delete-icon.png").toString()));
			alert.setContentText("No Empty Password Space Submission");
			alert.setHeaderText(null);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
			alert.showAndWait();
			return false;
		}
	}
}
