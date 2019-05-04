package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;

public class LogSqlController implements Initializable {
     @FXML
     private Label isConnected;
     @FXML
     private PasswordField pass;
     @FXML
     private ImageView logView;
     @FXML
     private TextField user;
     
     private static final AudioClip ALERT_AUDIOCLIP = new AudioClip(AdminController.class.getResource("/Music/song.wav").toString());
     
     Connection connection;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		logView.setImage(new Image(getClass().getResource("/img/log.png").toString()));
		
		connection = Database.Connector();
		if(connection == null){
			System.exit(1);
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
	
	public void Login(ActionEvent event) throws Exception{
		try{
			PreparedStatement statement = null;
			ResultSet set = null;
			String activate = " select * from supervisor where username = ? and password = ? ";
				statement = connection.prepareStatement(activate);
				statement.setString(1,user.getText());
				statement.setString(2,pass.getText());
				set = statement.executeQuery();
				
				if(set.next()){
				((Node)event.getSource()).getScene().getWindow().hide();//hide the last window in calling the recent
				Stage primaryStage = new Stage();
				Parent root = FXMLLoader.load(getClass().getResource("supervisorHome.fxml"));
				Scene scene = new Scene(root);
				primaryStage.getIcons().add(new Image("/img/baga.png"));
				primaryStage.setTitle("Supervisor`s Panel");
				primaryStage.setScene(scene);
				//Image icon = new Image("/img/baga.png");
			     
			
				
				primaryStage.show();
				
				user.clear();
				pass.clear();
			}else{
				isConnected.setText("Failed");
				pass.clear();
				user.clear();
			
				
				
                LogSqlController.ALERT_AUDIOCLIP.play();
				
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("ALERT");
				alert.setGraphic(new ImageView(this.getClass().getResource("/img/Delete-icon.png").toString()));
				alert.setContentText("Please be sure of your details");
				alert.setHeaderText(null);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
				alert.showAndWait();
				
			}
		}catch(SQLException e){
			isConnected.setText("Failed");
			
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

	
	public void adminLog(ActionEvent event)throws Exception{
		((Node)event.getSource()).getScene().getWindow().hide();//hide the last window in calling the recent
		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/Main.fxml").openStream());
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
