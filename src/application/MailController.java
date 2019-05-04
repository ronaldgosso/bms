package application;

import java.io.File;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.AuthenticationFailedException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class MailController implements Initializable {

	@FXML
	private JFXButton mail;
	@FXML
	private JFXTextArea mailArea;
	@FXML
	private AnchorPane signInPane;
	@FXML
	private AnchorPane mailPane;
	@FXML
	private TextArea FileName;
	private FileChooser fileChooser;
	private File file;
	private Stage primaryStage;
	private String fileName;
	private String path;
	private String myEmail;
	private String myName;
	private String myPassword;
	@FXML
	private Text signText;
	@FXML
	private JFXTextField name;
	@FXML
	private JFXTextField email;
	@FXML
	private JFXPasswordField password;
	@FXML
	private JFXTextField from;
	@FXML
	private JFXTextField to;
	@FXML
	private JFXTextField subject;
	@FXML
	private Label labelName;
	@FXML
	private Label date;
	@FXML
	private Label day;


	private static final AudioClip ALERT_AUDIOCLIP = new AudioClip(
			MailController.class.getResource("/Music/deleted.mp3").toString());
	private static final AudioClip ALERT_AUDIOCLIPY = new AudioClip(
			MailController.class.getResource("/Music/success.wav").toString());

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
      
		mailPane.setVisible(false);
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

		// Get the date today using Calendar object.
		Date today = Calendar.getInstance().getTime();
		// Using DateFormat format method we can create a string
		// representation of a date with the defined format.
		String issuedDate = df.format(today);
		String issuedDay = new SimpleDateFormat("EEEE").format(new Date());

		date.setText(issuedDate);
		day.setText(issuedDay);

	}

	public void signIn(ActionEvent ebent) {

		myEmail = email.getText();
		myName = name.getText();
		myPassword = password.getText();

		if (emptySign() & spaceSign() & emailV()) {
			from.setText(myEmail);
			signText.setVisible(false);
			signInPane.setVisible(false);
			mailPane.setVisible(true);
			labelName.setText(myName);
		}

	}
	public boolean emptySign() {
		if (!myEmail.isEmpty() && !myName.isEmpty() && !myPassword.isEmpty()) {
			return true;
		} else {
			MailController.ALERT_AUDIOCLIP.play();
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("PROMPT");
			alert.setGraphic(new ImageView(this.getClass().getResource("/img/Delete-icon.png").toString()));
			alert.setContentText("Please Fill All Of The Fields");
			alert.setHeaderText(null);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
			alert.showAndWait();
			email.clear();
			name.clear();
			password.clear();
			return false;
		}
	}
	
	public boolean emailV() {
		String validator = "[a-zA-Z0-9][a-zA-Z0-9._]*@[a-zA-z0-9]+([.][a-zA-Z]+)+";
		Pattern p = Pattern.compile(validator);
		Matcher m = p.matcher(myEmail);
		if(m.find() && m.group().equals(myEmail)) {
			return true;
		}
		
	else {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("PROMPT");
		alert.setGraphic(new ImageView(this.getClass().getResource("/img/alert.jpg").toString()));
		alert.setContentText("Error Occured\n"
				+ "Please Enter A Valid Email");
		alert.setHeaderText(null);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
		alert.showAndWait();
		return false;
	}
	}
	
	public boolean spaceSign() {
		boolean space = myEmail.contains(" ");
		boolean space2 = myName.contains(" ");
		boolean space3= myPassword.contains(" ");
		if (!space && !space2 && !space3) {
			return true;
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("PROMPT");
			alert.setGraphic(new ImageView(this.getClass().getResource("/img/Delete-icon.png").toString()));
			alert.setContentText("No Space Submission");
			alert.setHeaderText(null);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
			alert.showAndWait();
			return false;
		}
	}
	public void mail(ActionEvent ev) {
		
		Properties pro = System.getProperties();
		pro.put("mail.smtp.user", myEmail);
		pro.put("mail.smtp.password", password);
		pro.put("mail.smtp.host", "smtp.gmail.com");
		
		pro.put("mail.smtp.socketFactory.port", "465");
		pro.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		pro.put("mail.smtp.auth", "true");
		pro.put("mail.smtp.port", 465);
		pro.put("mail.smtp.ssl.enable", "true");
		pro.put("mail.debug", false);
		
	/*	
		 pro.put("mail.smtp.starttls.enable", "true");
		 pro.put("mail.smtp.host", "smtp.mail.yahoo.com");
		   pro.put("mail.smtp.user",myEmail);
		   pro.put("mail.smtp.password",password);
		   pro.put("mail.smtp.port", "587");
		   pro.put("mail.smtp.auth", "true");
*/
		Session session = Session.getDefaultInstance(pro, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(myEmail, myPassword);// my email + password

			}
		}

		);

		Message sms = new MimeMessage(session);
		String wSms = mailArea.getText();
		if(emailSend() & emptySend() & space()) {
			
		try {
			
			sms.setFrom(new InternetAddress(myEmail));// my address
			sms.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to.getText()));// recipient
			sms.setSubject(subject.getText());// subject

			MimeBodyPart smsBodyPart = new MimeBodyPart();
			smsBodyPart.setText(wSms);

			// file attachment

			Multipart multi = new MimeMultipart();
			multi.addBodyPart(smsBodyPart);

			smsBodyPart = new MimeBodyPart();
			DataSource source = new FileDataSource(path);

			smsBodyPart.setDataHandler(new DataHandler(source));
			smsBodyPart.setFileName(fileName);
			multi.addBodyPart(smsBodyPart);
			sms.setContent(multi);

			Transport tr = session.getTransport("smtp");
			tr.connect(session.getProperty("mail.smtp.host"), session.getProperty("mail.smtp.user"),
					session.getProperty("mail.smtp.password"));

			// transport the whole message
			Transport.send(sms);

			MailController.ALERT_AUDIOCLIPY.play();
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("PROMPT");
			alert.setGraphic(new ImageView(this.getClass().getResource("/img/success.jpg").toString()));
			alert.setContentText(myName + " " + "Your Message Was Sent");
			alert.setHeaderText(null);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
			alert.showAndWait();

			

		
			to.clear();
			from.clear();
			subject.clear();
			FileName.clear();

		}	
		catch(AuthenticationFailedException e) {
			
			MailController.ALERT_AUDIOCLIP.play();
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("PROMPT");
			alert.setGraphic(new ImageView(this.getClass().getResource("/img/Delete-icon.png").toString()));
			alert.setContentText("Check Your Username with Password Colleration");
			alert.setHeaderText(null);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
			alert.showAndWait();
		
			
		}catch (AddressException e) {
		
			MailController.ALERT_AUDIOCLIP.play();
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("PROMPT");
			alert.setGraphic(new ImageView(this.getClass().getResource("/img/Delete-icon.png").toString()));
			alert.setContentText("Check your Address with Password Colleration");
			alert.setHeaderText(null);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
			alert.showAndWait();
			

		} catch(MessagingException e) {
			MailController.ALERT_AUDIOCLIP.play();
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("PROMPT");
			alert.setGraphic(new ImageView(this.getClass().getResource("/img/Delete-icon.png").toString()));
			alert.setContentText("*Messaging errors*\n"
					+ "Message not Sent");
			alert.setHeaderText(null);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
			alert.showAndWait();
		

		}

		}
	}
	public boolean emailSend() {
		String validator = "[a-zA-Z0-9][a-zA-Z0-9._]*@[a-zA-z0-9]+([.][a-zA-Z]+)+";
		Pattern p = Pattern.compile(validator);
		Matcher m = p.matcher(to.getText());
		if(m.find() && m.group().equals(to.getText())) {
			return true;
		}
		
	else {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("PROMPT");
		alert.setGraphic(new ImageView(this.getClass().getResource("/img/alert.jpg").toString()));
		alert.setContentText("Error Occured\n"
				+ "Please Enter A Valid Email");
		alert.setHeaderText(null);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
		alert.showAndWait();
		return false;
	}
	}
	public boolean emptySend() {
		if (!to.getText().isEmpty() && !subject.getText().isEmpty() ) {
			return true;
		} else {
			MailController.ALERT_AUDIOCLIP.play();
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("PROMPT");
			alert.setGraphic(new ImageView(this.getClass().getResource("/img/Delete-icon.png").toString()));
			alert.setContentText("The Recipient or The Subject Field Must Be Filled");
			alert.setHeaderText(null);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
			alert.showAndWait();
			email.clear();
			name.clear();
			password.clear();
			return false;
		}
	}
	public boolean space() {
		boolean space = subject.getText().contains(" ");
		if(!space) {
			return true;
		}else {
			MailController.ALERT_AUDIOCLIP.play();
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("PROMPT");
			alert.setGraphic(new ImageView(this.getClass().getResource("/img/Delete-icon.png").toString()));
			alert.setContentText("Subject Cannot Contain White Space For Security Purpose");
			alert.setHeaderText(null);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
			alert.showAndWait();
			return false;
			
		}
		
	}


	public void attachFile(ActionEvent event) {
		fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"),
				new ExtensionFilter("Video Files", "*txt"), new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
				new ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"),
				new ExtensionFilter("PDF Files", "*.pdf"), new ExtensionFilter("Excell Files", "*.xls"),
				new ExtensionFilter("Text Files", "*txt")
				);

		file = fileChooser.showOpenDialog(primaryStage);
		if (file != null) {
			fileName = file.getName();
			FileName.setText(fileName);
			path = file.getAbsolutePath();

			MailController.ALERT_AUDIOCLIPY.play();
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("PROMPT");
			alert.setGraphic(new ImageView(this.getClass().getResource("/img/success.jpg").toString()));
			alert.setContentText("File Attached Ready To Send");
			alert.setHeaderText(null);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
			alert.showAndWait();
		} else {

			MailController.ALERT_AUDIOCLIP.play();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("PROMPT");
			alert.setGraphic(new ImageView(this.getClass().getResource("/img/Delete-icon.png").toString()));
			alert.setContentText("File Corrupted");
			alert.setHeaderText(null);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
			alert.showAndWait();
		}
	}

	public void sendAgain(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide();
		Parent root = FXMLLoader.load(getClass().getResource("Mail.fxml"));
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		primaryStage.getIcons().add(new Image("/img/baga.png"));
		primaryStage.setTitle("Email Services");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	public void yahooMail(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide();
		Parent root = FXMLLoader.load(getClass().getResource("MailYahoo.fxml"));
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		primaryStage.getIcons().add(new Image("/img/baga.png"));
		primaryStage.setTitle("Email Services");
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	public void exit(ActionEvent n) {
		((Node) n.getSource()).getScene().getWindow().hide();
	}

}
