package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.CustomPasswordField;
import org.controlsfx.control.textfield.TextFields;

import com.jfoenix.controls.JFXTextArea;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

public class SysAdmHomeController implements Initializable {
	ObservableList<String> list = FXCollections.observableArrayList();
	Connection connection;
	@FXML
	private TextField sysUsername;
	@FXML
	private TextField delSysUsername;
	@FXML
	private CustomPasswordField sysPassword;
	@FXML
	private CustomPasswordField verify;
	private static final AudioClip ALERT_AUDIOCLIPY = new AudioClip(
			AdminController.class.getResource("/Music/deleted.mp3").toString());
	private static final AudioClip ALERT_AUDIOCLIP = new AudioClip(
			AdminController.class.getResource("/Music/success.wav").toString());
	@FXML
	private JFXTextArea status;

	@FXML
	public void logOut(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide();
		Parent root = FXMLLoader.load(getClass().getResource("Home.fxml"));
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		primaryStage.getIcons().add(new Image("/img/baga.png"));
		primaryStage.setTitle("Welcome to Bagamoyo District Council Management System");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		connection = Database.Connector();
		if (connection == null) {
			System.exit(1);
		}

		PreparedStatement statement2;
		String query2 = "select username from admin";
		try {
			statement2 = connection.prepareStatement(query2);
			ResultSet set2 = statement2.executeQuery();

			while (set2.next()) {
				list.add(set2.getString("username"));
			}
			TextFields.bindAutoCompletion(delSysUsername, list);
		} catch (SQLException e) {

			e.printStackTrace();
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

	public void createSysAdmin(ActionEvent event) {
		if (emptyCreate() & verify() & space()) {
			PreparedStatement statement = null;
			String activate = "INSERT INTO admin (username,password) VALUES (?,?)";
			try {
				statement = connection.prepareStatement(activate);
				statement.setString(1, sysUsername.getText());
				statement.setString(2, sysPassword.getText());
				// set = statement.execute();
				SysAdmHomeController.ALERT_AUDIOCLIP.play();
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("PROMPT");
				alert.setGraphic(new ImageView(this.getClass().getResource("/img/success.jpg").toString()));
				alert.setContentText("System Administrator Added");
				alert.setHeaderText(null);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
				alert.showAndWait();
				statement.execute();
				sysUsername.clear();
				sysPassword.clear();
				verify.clear();
				status.setText("User Added Succesfully");
				

				statement.close();
			} catch (SQLException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("PROMPT");
				alert.setGraphic(new ImageView(this.getClass().getResource("/img/Delete-icon.png").toString()));
				alert.setContentText("User Exists");
				alert.setHeaderText(null);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
				alert.showAndWait();
				sysUsername.clear();
				sysPassword.clear();
				verify.clear();
				delSysUsername.clear();
				status.setText("User Not Added");

			}
		}
	
	}

	public void deleteSysAdmin(ActionEvent e) {
		if (emptyDelete() & spaceDelete()) {
			try {
				PreparedStatement statement = null;
				String query = "delete from admin where username = ?";
				statement = connection.prepareStatement(query);
				statement.setString(1, delSysUsername.getText());

				statement.executeUpdate();

				SysAdmHomeController.ALERT_AUDIOCLIPY.play();

				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("PROMPT");
				alert.setGraphic(new ImageView(this.getClass().getResource("/img/delete.png").toString()));
				alert.setContentText("System Administrator Deleted");
				alert.setHeaderText(null);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
				alert.showAndWait();
				delSysUsername.clear();
				sysUsername.clear();
				sysPassword.clear();
				verify.clear();
				status.setText("User Deleted Succesfully");
				statement.close();
			} catch (SQLException ev) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("PROMPT");
				alert.setGraphic(new ImageView(this.getClass().getResource("/img/Delete-icon.png").toString()));
				alert.setContentText("System Error");
				alert.setHeaderText(null);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
				alert.showAndWait();
				status.setText("User Not Deleted");
			}
		}
	}

	public boolean space() {
		boolean space = sysUsername.getText().contains(" ");
		if (!space) {
			return true;
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("PROMPT");
			alert.setGraphic(new ImageView(this.getClass().getResource("/img/Delete-icon.png").toString()));
			alert.setContentText("No Space Submission");
			alert.setHeaderText(null);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
			alert.showAndWait();
			return false;
		}
	}
	public boolean spaceDelete() {
		boolean space = delSysUsername.getText().contains(" ");
		if (!space) {
			return true;
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("PROMPT");
			alert.setGraphic(new ImageView(this.getClass().getResource("/img/Delete-icon.png").toString()));
			alert.setContentText("No Space Submission");
			alert.setHeaderText(null);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
			alert.showAndWait();
			return false;
		}
	}

	public boolean emptyCreate() {
		if (!sysUsername.getText().isEmpty() & !sysPassword.getText().isEmpty()) {
			return true;
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("PROMPT");
			alert.setGraphic(new ImageView(this.getClass().getResource("/img/Delete-icon.png").toString()));
			alert.setContentText("Fill In The Username or Password Fields");
			alert.setHeaderText(null);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
			alert.showAndWait();
			return false;
		}
	}

	public boolean emptyDelete() {
		if (!delSysUsername.getText().isEmpty()) {
			return true;
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("PROMPT");
			alert.setGraphic(new ImageView(this.getClass().getResource("/img/Delete-icon.png").toString()));
			alert.setContentText("You Cannot Delete A Null Person");
			alert.setHeaderText(null);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
			alert.showAndWait();
			return false;
		}
	}

	public boolean verify() {
		if (verify.getText().equals(sysPassword.getText()) & sysPassword.getText().equals(verify.getText())) {
			return true;
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("PROMPT");
			alert.setGraphic(new ImageView(this.getClass().getResource("/img/Delete-icon.png").toString()));
			alert.setContentText("Password Mismatch");
			alert.setHeaderText(null);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
			alert.showAndWait();
			verify.clear();
			sysPassword.clear();
			return false;
		}
	}
	public void refresh(ActionEvent event) throws Exception{
		((Node) event.getSource()).getScene().getWindow().hide();
		Stage primaryStage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("SysAdmHome.fxml"));
		primaryStage.getIcons().add(new Image("/img/baga.png"));
		primaryStage.setTitle("System Administrator Log In Panel");
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
