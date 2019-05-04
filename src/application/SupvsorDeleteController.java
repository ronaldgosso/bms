package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.TextFields;

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
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;

public class SupvsorDeleteController implements Initializable {
	
	@FXML
	private TextField supervisor;

	private static final AudioClip ALERT_AUDIOCLIP = new AudioClip(
			AdminController.class.getResource("/Music/deleted.mp3").toString());
     
	

	
	Connection connection;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		 ObservableList<String>  data= FXCollections.observableArrayList();
		connection = Database.Connector();
		if (connection == null) {
			System.exit(1);
		}
		try {
			PreparedStatement state;
			String query2 = "select username from supervisor";
			state  = connection.prepareStatement(query2);
			ResultSet set = state.executeQuery();
	
			while(set.next()) {
				data.add(set.getString("username"));
			}
			TextFields.bindAutoCompletion(supervisor, data);
		}catch(Exception e) {
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

	public boolean ifDbIsConnected() {
		try {
			return !connection.isClosed();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}

	public void delete(ActionEvent event) {
		if(empty() & space()) {
		try {
			PreparedStatement statement = null;
			String query = "delete from supervisor where username = ?";
			statement = connection.prepareStatement(query);
			statement.setString(1, supervisor.getText());
			statement.executeUpdate();
            

			 SupvsorDeleteController.ALERT_AUDIOCLIP.play();
			   
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("PROMPT");
				alert.setGraphic(new ImageView(this.getClass().getResource("/img/delete.png").toString()));
				alert.setContentText("Supervisor Deleted");
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
	
		supervisor.clear();
	}
	}
	
	public boolean space() {
		boolean space = supervisor.getText().contains(" ");
		if (!space) {
			return true;
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("PROMPT");
			alert.setGraphic(new ImageView(this.getClass().getResource("/img/Delete-icon.png").toString()));
			alert.setContentText("Do Not Submit A Spaced Word");
			alert.setHeaderText(null);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
			alert.showAndWait();
			return false;
		}
	}
	public boolean empty() {
		if (!supervisor.getText().isEmpty() ) {
			return true;
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("PROMPT");
			alert.setGraphic(new ImageView(this.getClass().getResource("/img/Delete-icon.png").toString()));
			alert.setContentText("Fill In The Username Field");
			alert.setHeaderText(null);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
			alert.showAndWait();
			return false;
		}
	}
	public void refresh(ActionEvent event) throws Exception{
		((Node) event.getSource()).getScene().getWindow().hide();
		Stage primaryStage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("SupvsorDelete.fxml"));
		primaryStage.getIcons().add(new Image("/img/baga.png"));
		primaryStage.setTitle("Supervisor Delete Panel");
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	
}
