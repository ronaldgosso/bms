package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.TextFields;

import com.jfoenix.controls.JFXTextField;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;

public class deleteIndividualController implements Initializable {

	@FXML
	private JFXTextField checkNo;
	@FXML
	private JFXTextField name;

	private static final AudioClip ALERT_AUDIOCLIP = new AudioClip(
			AdminController.class.getResource("/Music/deleted.mp3").toString());

	Connection connection;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		connection = Database.Connector();
		if (connection == null) {
			System.exit(1);
		}

		ObservableList<String> data00 = FXCollections.observableArrayList();
		try {
			String query00 = "select checkNo from workers";
			PreparedStatement state00 = connection.prepareStatement(query00);
			ResultSet set00 = state00.executeQuery();
			while (set00.next()) {
				data00.add(set00.getString("checkNo"));
				TextFields.bindAutoCompletion(checkNo, data00);
			}
		} catch (Exception c) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("ALERT");
			alert.setGraphic(new ImageView(this.getClass().getResource("/img/Delete-icon.png").toString()));
			alert.setContentText("System Error");
			alert.setHeaderText(null);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
			alert.showAndWait();
		}

		checkNo.setOnKeyPressed(e -> {
			String query1 = "select fName from workers where checkNo = '" + checkNo.getText().trim() + "'";

			if (e.getCode() == KeyCode.ENTER) {

				PreparedStatement state1;
				try {
					state1 = connection.prepareStatement(query1);
					ResultSet set1 = state1.executeQuery();
					while (set1.next()) {
						name.setText(set1.getString("fName"));
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		}

		);
	}

	public boolean ifDbIsConnected() {
		try {
			return !connection.isClosed();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public void delete(ActionEvent event) {
		if (empty() & space()) {
			try {
				PreparedStatement statement = null;
				String query = "delete from workers where checkNo = ? and fName = ?";
				statement = connection.prepareStatement(query);
				statement.setString(1, checkNo.getText());
				statement.setString(2, name.getText());
				statement.executeUpdate();

				deleteIndividualController.ALERT_AUDIOCLIP.play();

				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("PROMPT");
				alert.setGraphic(new ImageView(this.getClass().getResource("/img/delete.png").toString()));
				alert.setContentText("Individual Deleted");
				alert.setHeaderText(null);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
				alert.showAndWait();

				statement.close();
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
			checkNo.clear();
			name.clear();
		}

	}

	public boolean space() {
		boolean space = checkNo.getText().contains(" ");
		boolean space2 = name.getText().contains(" ");
		if (!space && !space2) {
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

	public boolean empty() {
		if (!checkNo.getText().isEmpty() && !name.getText().isEmpty()) {
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

	public void refresh(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide();
		Stage primaryStage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("deleteIndividual.fxml"));
		primaryStage.getIcons().add(new Image("/img/baga.png"));
		primaryStage.setTitle("Individual Delete Panel");
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
