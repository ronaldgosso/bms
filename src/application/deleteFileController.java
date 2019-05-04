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
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;

public class deleteFileController implements Initializable {

	@FXML
	private JFXTextField fileName;
	Connection connection;
	private static final AudioClip ALERT_AUDIOCLIP = new AudioClip(
			AdminController.class.getResource("/Music/deleted.mp3").toString());
	PreparedStatement statement2;
	ResultSet set2;
	String query2 = "select name from file";
	ObservableList<Object> list = FXCollections.observableArrayList();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		connection = Database.Connector();
		if (connection == null) {
			System.exit(1);
		}
		try {
			statement2 = connection.prepareStatement(query2);
			set2 = statement2.executeQuery();
			while (set2.next()) {
				list.add(set2.getString("name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		TextFields.bindAutoCompletion(fileName, list);

	}

	public void deleteFiles(ActionEvent event) {
		if (empty()) {
			try {
				PreparedStatement statement = null;
				String query = "delete from file where name = ?";
				statement = connection.prepareStatement(query);
				statement.setString(1, fileName.getText());
				statement.executeUpdate();
				deleteFileController.ALERT_AUDIOCLIP.play();
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("PROMPT");
				alert.setGraphic(new ImageView(this.getClass().getResource("/img/delete.png").toString()));
				alert.setContentText("File Deleted");
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

			fileName.clear();
		}

	}

	public boolean empty() {
		if (!fileName.getText().isEmpty()) {
			return true;
		} else {

			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("PROMPT");
			alert.setGraphic(new ImageView(this.getClass().getResource("/img/Delete-icon.png").toString()));
			alert.setContentText("You Cannot Delete An Empty File");
			alert.setHeaderText(null);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
			alert.showAndWait();
			return false;
		}
	}

	

	public void exit(ActionEvent en) {
		((Node) en.getSource()).getScene().getWindow().hide();
	}
	public void refresh(ActionEvent event) throws Exception{
		((Node) event.getSource()).getScene().getWindow().hide();
		Stage primaryStage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("deleteFile.fxml"));
		primaryStage.getIcons().add(new Image("/img/baga.png"));
		primaryStage.setTitle("FILE Delete Panel");
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
