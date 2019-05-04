package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AdminHomeController  implements Initializable{
	@FXML
	private AnchorPane rootPane;
	@FXML
	private ImageView backView;
	@FXML
	private Label userlabel;





	@FXML
	public void logOut(ActionEvent event) {

		try {
			((Node) event.getSource()).getScene().getWindow().hide();// hide the
																		// last
																		// window
																		// in
																		// calling
																		// the
																		// recent
			Stage primaryStage = new Stage();
			Parent root = FXMLLoader.load(getClass().getResource("Home.fxml"));

			Scene scene = new Scene(root);
			primaryStage.getIcons().add(new Image("/img/baga.png"));
			primaryStage.setTitle("Welcome to Bagamoyo District Council Management System");
			primaryStage.setScene(scene);
			primaryStage.show();
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
	}

	public void addSupervisor(ActionEvent event) {
		try {

			Stage primaryStage = new Stage();
			Parent root = FXMLLoader.load(getClass().getResource("Supervisor.fxml"));

			Scene scene = new Scene(root);
			primaryStage.getIcons().add(new Image("/img/baga.png"));
			primaryStage.setTitle("Add The Department Supervisor");
			primaryStage.setScene(scene);
			primaryStage.show();
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
	}

	public void addDepartment(ActionEvent event) throws Exception {

		Stage primaryStage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("crDepartment.fxml"));

		Scene scene = new Scene(root);
		primaryStage.getIcons().add(new Image("/img/baga.png"));
		primaryStage.setTitle("Add The Department");
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	public void deleteDepartment(ActionEvent event) throws Exception {

		Stage primaryStage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("delDepartment.fxml"));

		Scene scene = new Scene(root);
		primaryStage.getIcons().add(new Image("/img/baga.png"));
		primaryStage.setTitle("Delete The Department");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void deleteSupervisor(ActionEvent event) throws Exception {

		Stage primaryStage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("SupvsorDelete.fxml"));

		Scene scene = new Scene(root);
		primaryStage.getIcons().add(new Image("/img/baga.png"));
		primaryStage.setTitle("Delete The Department");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	public void enrollDepartment(ActionEvent event) throws Exception{
		((Node)event.getSource()).getScene().getWindow().hide();
		Stage primaryStage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("enrollDepartment.fxml"));

		Scene scene = new Scene(root);
		primaryStage.getIcons().add(new Image("/img/baga.png"));
		primaryStage.setTitle("Enroll Deparments");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	public void enrollSupervisor(ActionEvent event) throws Exception{
		((Node)event.getSource()).getScene().getWindow().hide();
		Stage primaryStage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("enrollSupervisor.fxml"));

		Scene scene = new Scene(root);
		primaryStage.getIcons().add(new Image("/img/baga.png"));
		primaryStage.setTitle("Enroll Deparments");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	



	public void changeAdminPassword(ActionEvent event) throws Exception {

		Stage primaryStage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("changePwdAdmin.fxml"));

		Scene scene = new Scene(root);
		primaryStage.getIcons().add(new Image("/img/baga.png"));
		primaryStage.setTitle("Change Administrator Password");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		backView.setImage(new Image(getClass().getResource("/img/face.jpg").toString()));
	}
}
