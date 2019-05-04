package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXPasswordField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;

public class SupervisorController implements Initializable {
	@FXML
	private TextField fname;
	@FXML
	private TextArea status;
	@FXML
	private TextField lname;
	@FXML
	private TextField eadd;
	@FXML
	private TextField username;
	@FXML
	private JFXPasswordField password;
	@FXML
	private JFXDatePicker date;
	@FXML
	private PasswordField verify;

	Connection connection;
	private static final AudioClip ALERT_AUDIOCLIP = new AudioClip(
			AdminController.class.getResource("/Music/song.wav").toString());

	public boolean ifDbIsConnected() {
		try {
			return !connection.isClosed();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public void createSupervisor(ActionEvent event) {

		if (empty() & verify() & emailValidator() & space()) {
			PreparedStatement statement = null;
			String activate = "INSERT INTO supervisor (username,password,firstname,lastname,email,date) VALUES (?,?,?,?,?,?)";
			try {
				statement = connection.prepareStatement(activate);
				statement.setString(1, username.getText());
				statement.setString(2, password.getText());
				statement.setString(3, fname.getText());
				statement.setString(4, lname.getText());
				statement.setString(5, eadd.getText());
				statement.setString(6, ((TextField) date.getEditor()).getText());

				SupervisorController.ALERT_AUDIOCLIP.play();

				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("PROMPT");
				alert.setGraphic(new ImageView(this.getClass().getResource("/img/success.jpg").toString()));
				alert.setContentText("Supervisor Added");
				alert.setHeaderText(null);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
				alert.showAndWait();

				statement.execute();

				username.clear();
				eadd.clear();
				verify.clear();
				password.clear();
				fname.clear();
				lname.clear();
				date.setValue(null);
				status.setText("Supervisor Created Succesfully");

				statement.close();

			}

			catch (SQLException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("ALERT");
				alert.setGraphic(new ImageView(this.getClass().getResource("/img/Delete-icon.png").toString()));
				alert.setContentText("User Exists");
				alert.setHeaderText(null);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
				alert.showAndWait();
                e.printStackTrace();
				username.clear();
				eadd.clear();
				verify.clear();
				password.clear();
				fname.clear();
				lname.clear();
				date.setValue(null);
				status.setText("Supervisor NOT  Created Succesfully");

			}
		}
	}

	public boolean space() {
		boolean space = username.getText().contains(" ");
		boolean space2 = password.getText().contains(" ");
		boolean space3 = fname.getText().contains(" ");
		if (!space && !space2 && !space3) {
			return true;
		} else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("PROMPT");
			alert.setGraphic(new ImageView(this.getClass().getResource("/img/alert.jpg").toString()));
			alert.setContentText("Error Occured\n" + "Space Inputs are PROHIBITED");
			alert.setHeaderText(null);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
			alert.showAndWait();
			return false;
		}
	}

	public boolean empty() {
		if (!username.getText().isEmpty() || !password.getText().isEmpty() || !fname.getText().isEmpty()
				|| !eadd.getText().isEmpty()) {
			return true;

		} else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("PROMPT");
			alert.setGraphic(new ImageView(this.getClass().getResource("/img/alert.jpg").toString()));
			alert.setContentText("Error Occured\n" + "Please Fill In the Blanks");
			alert.setHeaderText(null);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
			alert.showAndWait();
			return false;
		}

	}

	public boolean verify() {
		if (password.getText().equals(verify.getText())) {
			return true;
		} else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("PROMPT");
			alert.setGraphic(new ImageView(this.getClass().getResource("/img/alert.jpg").toString()));
			alert.setContentText("Error Occured\n" + "Please  Match Your Passwords");
			alert.setHeaderText(null);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
			alert.showAndWait();

			return false;
		}
	}

	public boolean emailValidator() {
		String validator = "[a-zA-Z0-9][a-zA-Z0-9._]*@[a-zA-z0-9]+([.][a-zA-Z]+)+";//REGX
		Pattern p = Pattern.compile(validator);
		Matcher m = p.matcher(eadd.getText());
		if (m.find() && m.group().equals(eadd.getText())) {
			return true;
		}

		else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("PROMPT");
			alert.setGraphic(new ImageView(this.getClass().getResource("/img/alert.jpg").toString()));
			alert.setContentText("Error Occured\n" + "Please Enter A Valid Email");
			alert.setHeaderText(null);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
			alert.showAndWait();
			return false;
		}

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		connection = Database.Connector();
		if (connection == null) {
			System.exit(1);

		}
	}
}
