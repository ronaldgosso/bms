package application;

import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class currentEntryController implements Initializable{
	@FXML
	private TextArea statusText;
    
	Connection connection;
	
	public void exit(ActionEvent ev)throws Exception {
		((Node)ev.getSource()).getScene().getWindow().hide();//hide the last window in calling the recent

	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		connection = Database.Connector();
		  try{
			  if( !connection.isClosed()) {
				  statusText.setText("Maria Database Connected \n"
					  		+ "The Data Submitted Will Be Visible By Enrolling \n"
					  		+ "Dedicated Server Is Locally (127.0.0.1) \n"
					  		);
			  }else {
				  statusText.setText("Connection Faulty");
			  }
		  }catch(Exception e) {
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
