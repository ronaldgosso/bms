package application;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

public class downloadFileController implements Initializable {

	@FXML
	private JFXTextField fileName;

	// permission denied -> private String userDir =
	// System.getProperty("user.home");//get any user Home....
	Connection connection;
	PreparedStatement statement2;
	ResultSet set2;
	String query2 = "select name from file";
	ObservableList<Object> list = FXCollections.observableArrayList();

	private static final AudioClip ALERT_AUDIOCLIPY = new AudioClip(
			MainController.class.getResource("/Music/success.wav").toString());

	public boolean ifDbIsConnected() {
		try {
			return !connection.isClosed();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

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

	public void downloadFile(ActionEvent ev) {

		PreparedStatement myStmt = null;
		ResultSet myRs = null;

		InputStream input = null;
		FileOutputStream output = null;
		if(empty()) {

		try {

			String sql = "select fileContent from file where name = ?";
			myStmt = connection.prepareStatement(sql);
			myStmt.setString(1, fileName.getText());
			myRs = myStmt.executeQuery();

			// 3. Set up a handle to the file
			String home = System.getProperty("user.home");
			File theFile = new File(home + "/Downloads/" + fileName.getText());
			output = new FileOutputStream(theFile);

			if (myRs.next()) {

				input = myRs.getBinaryStream("fileContent");

				byte[] buffer = new byte[5242880];//5MBs
				while (input.read(buffer) > 0) {
					output.write(buffer);
				}

			}

			downloadFileController.ALERT_AUDIOCLIPY.play();
			String downloaded = fileName.getText();
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("ALERT");
			alert.setGraphic(new ImageView(this.getClass().getResource("/img/folder.png").toString()));
			alert.setContentText("File" + " " + downloaded + " " + "Stored Into DOWNLOADS");
			alert.setHeaderText(null);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
			alert.showAndWait();

		} catch (Exception exc) {
			exc.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {

					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("ALERT");
					alert.setGraphic(new ImageView(this.getClass().getResource("/img/folder.png").toString()));
					alert.setContentText("File Does Not EXIST");
					alert.setHeaderText(null);
					Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
					stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
					alert.showAndWait();
				}
			}

			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {

					e.printStackTrace();
				}
			}

		}
		}
	}

	/*
	 * public void downloadFile(ActionEvent ev) { String cmd = fileName.getText();
	 * // String cmd2 = location.getText(); byte[] fileBytes; String query; //if
	 * (empty() & space()) { try { query = "select * from file where name = ?";
	 * PreparedStatement state = connection.prepareStatement(query);
	 * state.setString(1, cmd); ResultSet rs = state.executeQuery(); if (rs.next())
	 * { fileBytes = rs.getBytes(1); //File file = new
	 * File(System.getProperty("user.home")+cmd); // file.setExecutable(true);
	 * //file.setReadable(true); //file.setWritable(true); /*
	 * AccessController.checkPermission(new FilePermission(cmd2, "read,write"));
	 * Set<PosixFilePermission> perms = new HashSet<PosixFilePermission>(); //add
	 * owners permission perms.add(PosixFilePermission.OWNER_READ);
	 * perms.add(PosixFilePermission.OWNER_WRITE);
	 * perms.add(PosixFilePermission.OWNER_EXECUTE); //add group permissions
	 * perms.add(PosixFilePermission.GROUP_READ);
	 * perms.add(PosixFilePermission.GROUP_WRITE);
	 * perms.add(PosixFilePermission.GROUP_EXECUTE); //add others permissions
	 * perms.add(PosixFilePermission.OTHERS_READ);
	 * perms.add(PosixFilePermission.OTHERS_WRITE);
	 * perms.add(PosixFilePermission.OTHERS_EXECUTE);
	 * 
	 * Files.setPosixFilePermissions(Paths.get(file.getAbsolutePath()), perms);
	 *
	 * String home = System.getProperty("user.home"); File file = new
	 * File(home+"/Downloads/"+cmd); OutputStream targetFile = new
	 * FileOutputStream(file); System.out.println("\nSaved to file: " +
	 * file.getAbsolutePath()); targetFile.write(fileBytes); targetFile.close();
	 * 
	 * } fileName.clear(); // location.clear(); } catch (Exception e) {
	 * //StringWriter sw = new StringWriter(); //e.printStackTrace(new
	 * PrintWriter(sw)); //String exceptionAsString = sw.toString();
	 * 
	 * //feedBack.setText(exceptionAsString); } }
	 * 
	 * }
	 */

	public boolean empty() {
		if (!fileName.getText().isEmpty()) {
			return true;
		} else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("PROMPT");
			alert.setGraphic(new ImageView(this.getClass().getResource("/img/alert.jpg").toString()));
			alert.setContentText("Error Occured\n" + "Provide File Name and Location");
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
		Parent root = FXMLLoader.load(getClass().getResource("downloadFile.fxml"));
		primaryStage.getIcons().add(new Image("/img/baga.png"));
		primaryStage.setTitle("FILE Download Panel");
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void exit(ActionEvent en) {
		((Node) en.getSource()).getScene().getWindow().hide();
	}
}