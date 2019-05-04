package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Duration;

import java.awt.Desktop;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.controlsfx.control.Notifications;

import com.jfoenix.controls.JFXButton;

public class supervisorHomeController implements Initializable {
	@FXML
	private JFXButton addWorker;
	@FXML
	private JFXButton deleteWorker;
	@FXML
	private JFXButton enrollWorker;
	@FXML
	private JFXButton editWorker;
	@FXML
	private ImageView backView;
	@FXML
	private ImageView leftView;
	@FXML
	private ImageView rightView;

	Stage primaryStage = new Stage();

	Connection connection;

	private static final AudioClip ALERT_AUDIOCLIPY = new AudioClip(
			MainController.class.getResource("/Music/success.wav").toString());

	private Desktop desktop = Desktop.getDesktop();

	private File file;
	private File fily;
	private String uploaded;
	private FileChooser chooser;
	private static final AudioClip ALERT_AUDIOCLIP = new AudioClip(
			supervisorHomeController.class.getResource("/Music/song.wav").toString());

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		leftView.setImage(new Image(getClass().getResource("/img/tz.png").toString()));
		rightView.setImage(new Image(getClass().getResource("/img/baga.png").toString()));
		backView.setImage(new Image(getClass().getResource("/img/nature.jpg").toString()));

		connection = Database.Connector();
		if (connection == null) {
			System.exit(1);
		}

	}

	public void exit(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide();
		Parent root = FXMLLoader.load(getClass().getResource("Home.fxml"));
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		primaryStage.getIcons().add(new Image("/img/baga.png"));
		primaryStage.setTitle("Welcome to Bagamoyo District Council Management System");
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	public void chandePwd(ActionEvent event) {

		supervisorHomeController.ALERT_AUDIOCLIP.play();
		Image icon = new Image("/img/stop big.gif");
		Notifications notify = Notifications.create().graphic(new ImageView(icon)).title("Software Panels Response")
				.text("Contact Your Administrator").hideAfter(Duration.seconds(20)).position(Pos.TOP_CENTER);
		notify.darkStyle();
		notify.show();
	}

	public boolean ifDbIsConnected() {
		try {
			return !connection.isClosed();
		} catch (Exception e) {

			return false;
		}
	}

	public void nChatServer(ActionEvent event) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("ServerPanel.fxml"));
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		primaryStage.getIcons().add(new Image("/img/baga.png"));
		primaryStage.setTitle("nChat Server Panel");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void nChatClient(ActionEvent event) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("ClientPanel.fxml"));
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		primaryStage.getIcons().add(new Image("/img/baga.png"));
		primaryStage.setTitle("nChat Client Panel");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void addIndividual(ActionEvent eb) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("addIndividual.fxml"));
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		primaryStage.getIcons().add(new Image("/img/baga.png"));
		primaryStage.setTitle("Add Individual");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void deleteIndividual(ActionEvent event) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("deleteIndividual.fxml"));
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		primaryStage.getIcons().add(new Image("/img/baga.png"));
		primaryStage.setTitle("Individual Delete");
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	public void editIndividual(ActionEvent event) throws Exception {
		Stage primaryStage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("editIndividual.fxml"));
		Scene scene = new Scene(root);
		primaryStage.getIcons().add(new Image("/img/baga.png"));
		primaryStage.setTitle("Edit Individual");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void importExcell(ActionEvent event) throws Exception {
		Stage primaryStage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("excellFile.fxml"));
		Scene scene = new Scene(root);
		primaryStage.getIcons().add(new Image("/img/baga.png"));
		primaryStage.setTitle("Excell File Upload");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void viewFile(ActionEvent event) {
		try {
			desktop.open(file);
		} catch (Exception e) {

			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("ALERT");
			alert.setGraphic(new ImageView(this.getClass().getResource("/img/Delete-icon.png").toString()));
			alert.setContentText("NO FILE EXISTS");
			alert.setHeaderText(null);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
			alert.showAndWait();
		}

	}

	public void addFile(ActionEvent i) {
		chooser = new FileChooser();
		chooser.getExtensionFilters().addAll(new ExtensionFilter("PDF Files", "*.pdf"),
				new ExtensionFilter("Doc Files", "*.doc", "*.docx"), new ExtensionFilter("Text Files", "*.txt"),
				new ExtensionFilter("All Files", "*.*"));

		file = chooser.showOpenDialog(primaryStage);
		if (file != null) {
			supervisorHomeController.ALERT_AUDIOCLIPY.play();
			uploaded = file.getName();
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("ALERT");
			alert.setGraphic(new ImageView(this.getClass().getResource("/img/success.jpg").toString()));
			alert.setContentText(uploaded + " " + "Uploaded To The System");
			alert.setHeaderText(null);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
			alert.showAndWait();
		}else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("ALERT");
			alert.setGraphic(new ImageView(this.getClass().getResource("/img/alert.jpg").toString()));
			alert.setContentText("File Corrupted");
			alert.setHeaderText(null);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
			alert.showAndWait();
		}

	}

	public void uploadFile(ActionEvent event)  {
		try {
		uploaded = file.getName();
		byte[] pdfData = new byte[(int) file.length()];
		DataInputStream dis = new DataInputStream(new FileInputStream(file));
		dis.readFully(pdfData); // read from file into byte[] array
		dis.close();

		PreparedStatement ps = connection
				.prepareStatement("INSERT INTO file (" + "name, " + "fileContent" + ") VALUES (?,?)");
		ps.setString(1, uploaded);
		ps.setBytes(2, pdfData); // byte[] array
		ps.executeUpdate();
		supervisorHomeController.ALERT_AUDIOCLIPY.play();
      
		/*PreparedStatement myStmt;

		FileInputStream input = null;
		String sql = "INSERT INTO file (" + "name, " + "fileContent" + ") VALUES (?,?)";
		myStmt = connection.prepareStatement(sql);
	    uploaded = file.getName();
		File theFile = new File(file.getAbsolutePath());
		input = new FileInputStream(theFile);
		myStmt.setString(1,uploaded);
		myStmt.setBinaryStream(2, input);
		
		myStmt.executeUpdate();
		*/
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("ALERT");
		alert.setGraphic(new ImageView(this.getClass().getResource("/img/folder.png").toString()));
		alert.setContentText(uploaded + " " + "Uploaded To The Database");
		alert.setHeaderText(null);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
		alert.showAndWait();
		}catch(Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("ALERT");
			alert.setGraphic(new ImageView(this.getClass().getResource("/img/Delete-icon.png").toString()));
			alert.setContentText(uploaded + " " + "Too Large To Upload");
			alert.setHeaderText(null);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
			alert.showAndWait();
		}
	}

	public void enrollFiles(ActionEvent event) throws Exception {
		Stage primaryStage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("fileEnroll.fxml"));
		Scene scene = new Scene(root);
		primaryStage.getIcons().add(new Image("/img/baga.png"));
		primaryStage.setTitle("Uploaded Files");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void deleteFile(ActionEvent event) throws Exception {
		Stage primaryStage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("deleteFile.fxml"));
		Scene scene = new Scene(root);
		primaryStage.getIcons().add(new Image("/img/baga.png"));
		primaryStage.setTitle("Delete Files");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void help(ActionEvent event) {
		fily = new File("resources/file/help.txt");
		try {
			desktop.open(fily);
		} catch (IOException e) {

			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("ALERT");
			alert.setGraphic(new ImageView(this.getClass().getResource("/img/Delete-icon.png").toString()));
			alert.setContentText("System Error");
			alert.setHeaderText(null);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
			alert.showAndWait();
		}

	}

	public void downloadFile(ActionEvent e) throws Exception {
		Stage primaryStage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("downloadFile.fxml"));
		Scene scene = new Scene(root);
		primaryStage.getIcons().add(new Image("/img/baga.png"));
		primaryStage.setTitle("Download A File");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void email(ActionEvent e) throws Exception {
		Stage primaryStage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("Mail.fxml"));
		Scene scene = new Scene(root);
		primaryStage.getIcons().add(new Image("/img/baga.png"));
		primaryStage.setTitle("Email Services");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	public void sampleExcell(ActionEvent e) throws IOException {
		HSSFWorkbook wb = new HSSFWorkbook();// 2007 MsExcell
		HSSFSheet sheet = wb.createSheet("Tange");
		HSSFRow header = sheet.createRow(0);// first row
		header.createCell(0).setCellValue("checkNo");// columns
		header.createCell(1).setCellValue("Jina La Kwanza");
		header.createCell(2).setCellValue("Jina La Kati");
		header.createCell(3).setCellValue("Jina La Mwisho");
		header.createCell(4).setCellValue("Jinsia");
		header.createCell(5).setCellValue("Cheo");
		header.createCell(6).setCellValue("Ngazi Ya Mshahara");
		header.createCell(7).setCellValue("TSD");
		header.createCell(8).setCellValue("Tarehe Ya Kuzaliwa");
		header.createCell(9).setCellValue("Tarehe Ya Ajira");
		header.createCell(10).setCellValue("Tarehe Ya Kuthibitishwa");
		header.createCell(11).setCellValue("Tarehe ya Daraja La Sasa");
		header.createCell(12).setCellValue("Kiwango Cha Elimu");
		header.createCell(13).setCellValue("Chuo Alichosoma");
		header.createCell(14).setCellValue("Mwaka Aliomaliza");
		header.createCell(15).setCellValue("Kituo Cha Sasa Cha Kazi ");
		header.createCell(16).setCellValue("Kituo Cha Awali Cha Kazi");
		header.createCell(17).setCellValue("Dini");
		header.createCell(18).setCellValue("Mahali Alipozaliwa");
		header.createCell(19).setCellValue("Namba Ya Simu");
		header.createCell(20).setCellValue("Somo La Kwanza");
		header.createCell(21).setCellValue("Somo La Pili");
		header.createCell(22).setCellValue("Kata");
		
		//column sizes
		sheet.autoSizeColumn(1);
		sheet.autoSizeColumn(2);
		sheet.autoSizeColumn(3);
		sheet.autoSizeColumn(6);
		sheet.autoSizeColumn(8);
		sheet.autoSizeColumn(9);
		sheet.autoSizeColumn(10);
		sheet.autoSizeColumn(11);
		sheet.autoSizeColumn(12);
		sheet.autoSizeColumn(13);
		sheet.autoSizeColumn(14);
		sheet.autoSizeColumn(15);
		sheet.autoSizeColumn(16);
		sheet.autoSizeColumn(18);
		sheet.autoSizeColumn(19);
		sheet.autoSizeColumn(20);
		sheet.autoSizeColumn(21);
		sheet.autoSizeColumn(22);
		
	
		  String fileName = "Sample Excell File."+".xls";
	        String home = System.getProperty("user.home");
	        File file = new File(home +"/Desktop/" +fileName);
			FileOutputStream fileOut = new FileOutputStream(file);// before 2007 <xlsx> 'output file to windows'
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("ALERT");
			alert.setGraphic(new ImageView(this.getClass().getResource("/img/folder.png").toString()));
			alert.setContentText("Sample File Exported To Desktop");
			alert.setHeaderText(null);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
			alert.showAndWait();
			try {
				wb.write(fileOut);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			fileOut.close();
			//wb.close();

	}
}
