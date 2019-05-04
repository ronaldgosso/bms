package application;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class excellFileController implements Initializable{
	
	
	private PreparedStatement statement;
	private FileChooser chooser;
	Stage primaryStage = new Stage();
	private File file;
	private Desktop desktop = Desktop.getDesktop();
	private String visible;
	@FXML 
	private TextArea fileName;
    

	private static final AudioClip ALERT_AUDIOCLIP = new AudioClip(
			MainController.class.getResource("/Music/success.wav").toString());
	
	

	Connection connection;
	
	public boolean ifDbIsConnected() {
		try {
			return !connection.isClosed();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void chooseFile(ActionEvent eve) {
		chooser = new FileChooser();
		chooser.getExtensionFilters().addAll(new ExtensionFilter("Excell  Files", "*.xls"),
				new ExtensionFilter("All Files", "*.*")
				);

				 file = chooser.showOpenDialog(primaryStage);
				 if(file != null) {
					 visible = file.getName();
					fileName.setText(visible);
				 
				 excellFileController.ALERT_AUDIOCLIP.play();

					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("ALERT");
					alert.setGraphic(new ImageView(this.getClass().getResource("/img/success.jpg").toString()));
					alert.setContentText("File"+" " +visible +" "+"Added To The System");
					alert.setHeaderText(null);
					Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
					stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
					alert.showAndWait();
				 }	else {
					 Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("PROMPT");
						alert.setGraphic(new ImageView(this.getClass().getResource("/img/alert.jpg").toString()));
						alert.setContentText("File NULL");
						alert.setHeaderText(null);
						Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
						stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
						alert.showAndWait();
				 }
			
	}
	
	public void viewFile(ActionEvent event) {
		try { 
			if(file != null) {
				desktop.open(file); 
				}else {
					 Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("PROMPT");
						alert.setGraphic(new ImageView(this.getClass().getResource("/img/alert.jpg").toString()));
						alert.setContentText("File NULL");
						alert.setHeaderText(null);
						Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
						stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
						alert.showAndWait();
				}
			}
		catch(IOException e) {
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
	
	
	public void importFile(ActionEvent event) {
		try{
			String activate = "INSERT INTO workers (checkNo,fName,mName,lName,sex,cheo,nMshahara,tsd,tKuzaliwa,tAjira,tKuthibitishwa,tDaraja,kElimu,cAlisoma,mAlimaliza,kKaziSasa,kKaziAwali,dini,aMahali,mobile,sKwanza,sPili,kata) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			statement = connection.prepareStatement(activate);
			
			FileInputStream fin = new FileInputStream(file);
			
			HSSFWorkbook wb = new HSSFWorkbook(fin); 
			HSSFSheet sheet = wb.getSheetAt(0);
			Row row;
			for(int i =1; i <= sheet.getLastRowNum();i++) {
				row = sheet.getRow(i);
				
				statement.setString(1, row.getCell(0).getStringCellValue());
				statement.setString(2, row.getCell(1).getStringCellValue());
				statement.setString(3, row.getCell(2).getStringCellValue());
				statement.setString(4, row.getCell(3).getStringCellValue());
				statement.setString(5, row.getCell(4).getStringCellValue());
				statement.setString(6, row.getCell(5).getStringCellValue());
				statement.setString(7, row.getCell(6).getStringCellValue());
				statement.setString(8, row.getCell(7).getStringCellValue());
				statement.setString(9, row.getCell(8).getStringCellValue());
				statement.setString(10, row.getCell(9).getStringCellValue());
				statement.setString(11, row.getCell(10).getStringCellValue());
				statement.setString(12, row.getCell(11).getStringCellValue());
				statement.setString(13, row.getCell(12).getStringCellValue());
				statement.setString(14, row.getCell(13).getStringCellValue());
				statement.setString(15, row.getCell(14).getStringCellValue());
				statement.setString(16, row.getCell(15).getStringCellValue());
				statement.setString(17, row.getCell(16).getStringCellValue());
				statement.setString(18, row.getCell(17).getStringCellValue());
				statement.setString(19, row.getCell(18).getStringCellValue());
				statement.setString(20, row.getCell(19).getStringCellValue());
				statement.setString(21, row.getCell(20).getStringCellValue());
				statement.setString(22, row.getCell(21).getStringCellValue());
				statement.setString(23, row.getCell(22).getStringCellValue());
				statement.execute();
			
			}
			
			excellFileController.ALERT_AUDIOCLIP.play();

			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("ALERT");
			alert.setGraphic(new ImageView(this.getClass().getResource("/img/success.jpg").toString()));
			alert.setContentText("File"+" " +visible +"Imported To Database");
			alert.setHeaderText(null);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
			alert.showAndWait();
			
			statement.close();
			fin.close();
			fileName.clear();
		//	wb.close();
			
		}catch(Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("ALERT");
			alert.setGraphic(new ImageView(this.getClass().getResource("/img/Delete-icon.png").toString()));
			alert.setContentText("Make Sure The File Matches The Required Format");
			alert.setHeaderText(null);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
			alert.showAndWait();
		}
		
	}
	
	public void exit(ActionEvent en) {
		((Node)en.getSource()).getScene().getWindow().hide();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		connection = Database.Connector();
		if (connection == null) {
			System.exit(1);
		}

		
	}
	
	
}
