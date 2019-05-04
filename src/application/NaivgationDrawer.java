package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class NaivgationDrawer extends Application {
	public static Boolean isSplashLoaded = false;

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("Home.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(
				getClass().getResource("application.css").toExternalForm());
		scene.getStylesheets().add(getClass()
				.getResource("BasicApplication.css").toExternalForm());
		primaryStage.getIcons().add(new Image("/img/baga.png"));
		primaryStage.setTitle(
				"Welcome to Bagamoyo District Council Management System");
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}
}
