package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

public class delDepartmentController implements Initializable {
	@FXML
	private TextField id;
	@FXML
	private TextField supervisor;

	private static final AudioClip ALERT_AUDIOCLIP = new AudioClip(
			AdminController.class.getResource("/Music/deleted.mp3").toString());
	
	Connection connection;
	PreparedStatement statement2;
	ResultSet set2;
	String query2 = "select name from department";
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
			while(set2.next()) {
				list.add(set2.getString("name"));	
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		TextFields.bindAutoCompletion(id, list);
	
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
			String query = "delete from department where name = ? and supervisor = ?";
			statement = connection.prepareStatement(query);
			statement.setString(1, id.getText());
			statement.setString(2, supervisor.getText());
			statement.executeUpdate();
            
			
			 delDepartmentController.ALERT_AUDIOCLIP.play();
			   
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("PROMPT");
				alert.setGraphic(new ImageView(this.getClass().getResource("/img/delete.png").toString()));
				alert.setContentText("Department Deleted");
				alert.setHeaderText(null);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
				alert.showAndWait();
				
			statement.close();
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("PROMPT");
			alert.setGraphic(new ImageView(this.getClass().getResource("/img/Delete-icon.png").toString()));
			alert.setContentText("System Error");
			alert.setHeaderText(null);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
			alert.showAndWait();
		
		}
		id.clear();
		supervisor.clear();
	}
	}
	public boolean empty() {
		if (!id.getText().isEmpty() && !supervisor.getText().isEmpty()) {
			return true;
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("PROMPT");
			alert.setGraphic(new ImageView(this.getClass().getResource("/img/Delete-icon.png").toString()));
			alert.setContentText("Fill The Spaces Provided");
			alert.setHeaderText(null);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
			alert.showAndWait();
			return false;
		}
	}
	
	public boolean space() {
		boolean space = id.getText().contains(" ");
		boolean space2 = supervisor.getText().contains(" ");
		if(!space && !space2) {
			return true;
		}else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("PROMPT");
			alert.setGraphic(new ImageView(this.getClass().getResource("/img/Delete-icon.png").toString()));
			alert.setContentText("Submission of Empty values is PROHIBITED");
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
		Parent root = FXMLLoader.load(getClass().getResource("delDepartment.fxml"));
		primaryStage.getIcons().add(new Image("/img/baga.png"));
		primaryStage.setTitle("Department Delete Panel");
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
