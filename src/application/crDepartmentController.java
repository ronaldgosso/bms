package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.TextFields;

import com.jfoenix.controls.JFXDatePicker;

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
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;

public class crDepartmentController implements Initializable{
	@FXML private TextField id;
	@FXML private TextArea status;
	@FXML private TextField serial;
	@FXML private TextField sname;
	@FXML private JFXDatePicker date;
	private   PreparedStatement statement = null;
	private ObservableList<String> reserveWord  = FXCollections.observableArrayList();
	
	private static final AudioClip ALERT_AUDIOCLIP = new AudioClip(AdminController.class.getResource("/Music/success.wav").toString());
	Connection connection;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		connection = Database.Connector();
		if(connection == null){
			System.exit(1);
			
			
			try{
				PreparedStatement statement2;
				ResultSet res2;
				String query = "select username from supervisor";
				statement2 = connection.prepareStatement(query);
				res2 = statement2.executeQuery();
				while(res2.next()){
					reserveWord.add(res2.getString("username"));
				}
				TextFields.bindAutoCompletion(sname, reserveWord);
			}catch(SQLException e){
				Alert alert = new Alert(AlertType.ERROR);
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
	public boolean ifDbIsConnected(){
		try{
			return !connection.isClosed();
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	public void createDepartment(ActionEvent event){
      
		if(empty()  & space()) {
		String activate = "INSERT INTO department (name,serial,supervisor,date) VALUES (?,?,?,?)";
		try{
			statement = connection.prepareStatement(activate);
			statement.setString(1,id.getText());
			statement.setString(2,serial.getText());
		    statement.setString(3,sname.getText());
			statement.setString(4,((TextField)date.getEditor()).getText());
		    
			    crDepartmentController.ALERT_AUDIOCLIP.play();
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("PROMPT");
				alert.setGraphic(new ImageView(this.getClass().getResource("/img/success.jpg").toString()));
				alert.setContentText("Department Added");
				alert.setHeaderText(null);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
				alert.showAndWait();
			    statement.execute();
				id.clear();
				serial.clear();
				sname.clear();
				date.setValue(null);
				status.setText("Department Created Succesfully");
			statement.close();
		}catch(SQLException e){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("PROMPT");
			alert.setGraphic(new ImageView(this.getClass().getResource("/img/Delete-icon.png").toString()));
			alert.setContentText("Check Serial OR ID Duplication");
			alert.setHeaderText(null);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
			alert.showAndWait();
			status.setText("Department NOT Created Succesfully");
		  
		}
		}
		
	}
	public boolean empty() {
		if (!id.getText().isEmpty() & !serial.getText().isEmpty()  &  !sname.getText().isEmpty()  &  !date.getEditor().getText().isEmpty()) {
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
		boolean space2 = serial.getText().contains(" ");
		boolean space3 = sname.getText().contains(" ");
		
		
		if(!space && !space2 && !space3) {
			return true;
		}else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("PROMPT");
			alert.setGraphic(new ImageView(this.getClass().getResource("/img/Delete-icon.png").toString()));
			alert.setContentText("Submision of Empty values is PROHIBITED");
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
		Parent root = FXMLLoader.load(getClass().getResource("crDepartment.fxml"));
		primaryStage.getIcons().add(new Image("/img/baga.png"));
		primaryStage.setTitle("Department Create Panel");
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	
	

}
