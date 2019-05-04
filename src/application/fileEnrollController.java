package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXListView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class fileEnrollController implements Initializable{
	@FXML
	private JFXListView<String> fileList;
	private ObservableList<String> data;
	
	Connection connection;
	PreparedStatement statement;
	ResultSet set ;
	private String intake = "select name from file";
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	
		connection = Database.Connector();
		if(connection == null){
			System.exit(1);
		}
		
		try {
			data = FXCollections.observableArrayList();
			statement = connection.prepareStatement(intake);
            set = statement.executeQuery();
            
            while(set.next()) {
            	data.add(
            			set.getString("name"));
            }
          //  fileList.getItems().addAll(data);
            fileList.setItems(data);
        
			
			
			
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
	
	public boolean ifDbIsConnected(){
		try{
			return !connection.isClosed();
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	public void exit(ActionEvent en) {
		((Node)en.getSource()).getScene().getWindow().hide();
	}

}
