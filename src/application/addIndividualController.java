package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;
import org.controlsfx.control.textfield.TextFields;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import javafx.stage.Stage;

public class addIndividualController implements Initializable {

	@FXML
	private JFXTextField fName;
	@FXML
	private JFXTextField mName;
	@FXML
	private JFXTextField lName;
	@FXML
	private JFXTextField sex;
	@FXML
	private JFXTextField cheo;
	@FXML
	private JFXTextField mshahara;
	@FXML
	private JFXTextField check;
	@FXML
	private JFXTextField tsd;
	@FXML
	private JFXDatePicker kuzaliwa;
	@FXML
	private JFXDatePicker ajira;
	@FXML
	private JFXDatePicker thibitishwa;
	@FXML
	private JFXDatePicker daraja;
	@FXML
	private JFXTextField elimu;
	@FXML
	private JFXTextField chuo;
	@FXML
	private JFXTextField aliomaliza;
	@FXML
	private JFXTextField sasaKazi;
	@FXML
	private JFXTextField awaliKazi;
	@FXML
	private JFXTextField dini;
	@FXML
	private JFXTextField alipozaliwa;
	@FXML
	private JFXTextField somo1;
	@FXML
	private JFXTextField number;
	@FXML
	private ImageView picture;
	@FXML
	private Label word;
	@FXML
	private Label word1;
	@FXML
	private Label word2;

	// private Image image;
	@FXML
	private JFXTextField somo2;

	@FXML
	private TextField kata;

	@FXML
	private ScrollPane entryPane;
	@FXML
	private JFXToggleButton hidePane;
	@FXML
	private JFXToggleButton timeToggle;
	@FXML
	private JFXToggleButton showingDate;
	@FXML
	private Label date;
	private Notifications notify;
	private String kataWords[] = { "ZINGA", "KEREGE", "MAPINGA", "KIROMO", "DUNDA", "NIANJEMA", "MAGOMENI", "FUKAYOSI",
			"MAKURUNGE", "YOMBO", "KISUTU"

	};
	
	String jinsia []= {"KE","ME"}; 

	Stage primaryStage = new Stage();

	// private Desktop desktop = Desktop.getDesktop(); previewing to the desktop

	// private FileInputStream fis;
	Connection connection;

	// private FileChooser fileChooser;
	// private File file;
	@FXML
	private ImageView a;
	@FXML
	private ImageView b;
	@FXML
	private ImageView c;
	@FXML
	private ImageView d;
	@FXML
	private ImageView e;
	@FXML
	private ImageView f;
	@FXML
	private ImageView g;
	@FXML
	private ImageView h;
	@FXML
	private ImageView i;
	@FXML
	private ImageView j;
	@FXML
	private ImageView k;
	@FXML
	private ImageView l;
	@FXML
	private ImageView m;
	@FXML
	private ImageView n;
	@FXML
	private ImageView o;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		connection = Database.Connector();
		if (connection == null) {
			System.exit(1);
		}
		// autoComplete
		TextFields.bindAutoCompletion(kata, kataWords);
        TextFields.bindAutoCompletion(sex, jinsia);
		/*
		 * fileChooser = new FileChooser(); fileChooser.getExtensionFilters().addAll(//
		 * new ExtensionFilter("Text Files", "*txt"), new ExtensionFilter("Image Files",
		 * "*.png", "*.jpg", "*.gif"), // new ExtensionFilter("Audio Files", "*.wav",
		 * "*.mp3", "*.aac"), new ExtensionFilter("All Files", "*.*")
		 * 
		 * );
		 */
        a.setImage(new Image(getClass().getResource("/img/must.png").toString()));
        b.setImage(new Image(getClass().getResource("/img/must.png").toString()));
        c.setImage(new Image(getClass().getResource("/img/must.png").toString()));
        d.setImage(new Image(getClass().getResource("/img/must.png").toString()));
        e.setImage(new Image(getClass().getResource("/img/must.png").toString()));
        f.setImage(new Image(getClass().getResource("/img/must.png").toString()));
        g.setImage(new Image(getClass().getResource("/img/must.png").toString()));
        h.setImage(new Image(getClass().getResource("/img/must.png").toString()));
        i.setImage(new Image(getClass().getResource("/img/must.png").toString()));
        j.setImage(new Image(getClass().getResource("/img/must.png").toString()));
        k.setImage(new Image(getClass().getResource("/img/must.png").toString()));
        l.setImage(new Image(getClass().getResource("/img/must.png").toString()));
        m.setImage(new Image(getClass().getResource("/img/must.png").toString()));
        n.setImage(new Image(getClass().getResource("/img/must.png").toString()));
        o.setImage(new Image(getClass().getResource("/img/must.png").toString()));

	}

	public boolean ifDbIsConnected() {
		try {
			return !connection.isClosed();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public void addUser(ActionEvent event) {
		PreparedStatement statement = null;
		if (empty() & space()){

			String activate = "INSERT INTO workers (checkNo,fName,mName,lName,sex,cheo,nMshahara,tsd,tKuzaliwa,tAjira,tKuthibitishwa,tDaraja,kElimu,cAlisoma,mAlimaliza,kKaziSasa,kKaziAwali,dini,aMahali,mobile,sKwanza,sPili,kata) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			try {
				statement = connection.prepareStatement(activate);
				statement.setString(1, check.getText());
				statement.setString(2, fName.getText());
				statement.setString(3, mName.getText());
				statement.setString(4, lName.getText());
				statement.setString(5, sex.getText());
				statement.setString(6, cheo.getText());
				statement.setString(7, mshahara.getText());
				statement.setString(8, tsd.getText());
				statement.setString(9, ((TextField) kuzaliwa.getEditor()).getText());
				statement.setString(10, ((TextField) ajira.getEditor()).getText());
				statement.setString(11, ((TextField) thibitishwa.getEditor()).getText());
				statement.setString(12, ((TextField) daraja.getEditor()).getText());
				statement.setString(13, elimu.getText());
				statement.setString(14, chuo.getText());
				statement.setString(15, aliomaliza.getText());
				statement.setString(16, sasaKazi.getText());
				statement.setString(17, awaliKazi.getText());
				statement.setString(18, dini.getText());
				statement.setString(19, alipozaliwa.getText());
				statement.setString(20, number.getText());
				statement.setString(21, somo1.getText());
				statement.setString(22, somo2.getText());
				statement.setString(23, kata.getText());

				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("PROMPT");
				alert.setGraphic(new ImageView(this.getClass().getResource("/img/success.jpg").toString()));
				alert.setContentText("Individual Added");
				alert.setHeaderText(null);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
				alert.showAndWait();

				// fis = new FileInputStream(file);
				// statement.setBinaryStream(24, (InputStream) fis, (int) file.length());

				statement.execute();
				statement.close();

				fName.clear();
				somo2.clear();
				elimu.clear();
				sex.clear();
				mName.clear();
				check.clear();
				alipozaliwa.clear();
				kata.clear();
				chuo.clear();
				lName.clear();
				awaliKazi.clear();
				daraja.setValue(null);
				thibitishwa.setValue(null);
				kuzaliwa.setValue(null);
				mshahara.clear();
				cheo.clear();
				ajira.setValue(null);
				dini.clear();
				number.clear();
				sasaKazi.clear();
				somo1.clear();
				aliomaliza.clear();
				tsd.clear();
				// picture.setVisible(false);

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

	}

	public boolean space() {
		boolean space = fName.getText().contains(" ");
	
		boolean space1 = somo2.getText().contains(" ");
		boolean space9 = elimu.getText().contains(" ");
		boolean space2 = sex.getText().contains(" ");
		boolean space10 = mName.getText().contains(" ");
		boolean space14 = check.getText().contains(" ");
		boolean space3 = alipozaliwa.getText().contains(" ");
		boolean space11 = kata.getText().contains(" ");
		boolean space15 = chuo.getText().contains(" ");
		boolean space4 = lName.getText().contains(" ");
		boolean space12 = awaliKazi.getText().contains(" ");
		boolean space5 = mshahara.getText().contains(" ");
		boolean space6 = cheo.getText().contains(" ");
		boolean space13 = dini.getText().contains(" ");
		boolean space7 = number.getText().contains(" ");
		boolean space16 = sasaKazi.getText().contains(" ");
		boolean space18 = somo1.getText().contains(" ");
		boolean space8 = aliomaliza.getText().contains(" ");
		boolean space17 = tsd.getText().contains(" ");
		if (!space  || !space1 && !space2  && !space3 && !space4 || !space5
				&& !space6  && !space7  || !space8  || !space9  && !space10 
				&& !space11  || !space12  || !space13  && !space14  || !space15
				&& !space16  || !space17  || !space18) {
			return true;
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("PROMPT");
			alert.setGraphic(new ImageView(this.getClass().getResource("/img/Delete-icon.png").toString()));
			alert.setContentText("No Empty Space Submission");
			alert.setHeaderText(null);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
			alert.showAndWait();
			return false;
		}
	}

	public boolean empty() {
		if (!fName.getText().isEmpty() || !somo2.getText().isEmpty() || !elimu.getText().isEmpty()
				&& !sex.getText().isEmpty() && !mName.getText().isEmpty() && !check.getText().isEmpty()
				&& !alipozaliwa.getText().isEmpty() && !kata.getText().isEmpty() || !chuo.getText().isEmpty()
				&& !lName.getText().isEmpty() || !awaliKazi.getText().isEmpty()
				&& !daraja.getEditor().getText().isEmpty() || !thibitishwa.getEditor().getText().isEmpty()
				&& !kuzaliwa.getEditor().getText().isEmpty() || !mshahara.getText().isEmpty()
				&& !cheo.getText().isEmpty() && !ajira.getEditor().getText().isEmpty() || !dini.getText().isEmpty()
				&& !number.getText().isEmpty() && !sasaKazi.getText().isEmpty() || !somo1.getText().isEmpty()
				|| !aliomaliza.getText().isEmpty() || !tsd.getText().isEmpty()) {
			return true;
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("ALERT");
			alert.setGraphic(new ImageView(this.getClass().getResource("/img/Delete-icon.png").toString()));
			alert.setContentText("Fill In The Required Fields");
			alert.setHeaderText(null);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
			alert.showAndWait();
			return false;
		}
	}

	/*
	 * public void fileChooser(ActionEvent ec) { word1.setVisible(false);
	 * word.setVisible(false); word2.setVisible(false);
	 * 
	 * file = fileChooser.showOpenDialog(primaryStage); if (file != null) {
	 * 
	 * BufferedImage bufferedImage; try { bufferedImage = ImageIO.read(file); image
	 * = SwingFXUtils.toFXImage(bufferedImage, null); picture.setImage(image); }
	 * catch (IOException e) {
	 * 
	 * e.printStackTrace(); }
	 * 
	 * /* image = new Image(file.toURI().toString());//
	 * path,prefW,prefH,presRat,Smooth picture = new ImageView(image); /* try {
	 * desktop.open(file); } catch (IOException e) {
	 * 
	 * e.printStackTrace(); }
	 */
	// }

	// }

	public void enrollIndividuals(ActionEvent event) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("enrollIndividual.fxml"));
		Scene scene = new Scene(root);
		primaryStage.getIcons().add(new Image("img/baga.png"));
		primaryStage.setTitle("Enroll Individuals");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void hideEntryPane(ActionEvent event) {
		hidePane.selectedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
				if (hidePane.isSelected() == true) {
					entryPane.setVisible(false);
				} else {
					entryPane.setVisible(true);
				}

			}

		});
	}

	public void showDate(ActionEvent event) {

		showingDate.selectedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
				if (showingDate.isSelected() == true) {

					DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

					// Get the date today using Calendar object.
					Date today = Calendar.getInstance().getTime();
					// Using DateFormat format method we can create a string
					// representation of a date with the defined format.
					String issuedDate = df.format(today);

					date.setText(issuedDate);

				} else {
					date.setText(null);
				}

			}

		});

	}

	public void timeResponse(ActionEvent event) {
		timeToggle.selectedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
				if (timeToggle.isSelected() == true) {

					DateFormat df = new SimpleDateFormat("HH : mm : ss");

					// Get the date today using Calendar object.
					Date today = Calendar.getInstance().getTime();
					// Using DateFormat format method we can create a string
					// representation of a date with the defined format.
					String issuedDate = df.format(today);

					Image icon = new Image("img/baga.png");
					notify = Notifications.create().graphic(new ImageView(icon)).title("THE TIME IS").text(issuedDate)
							.position(Pos.TOP_RIGHT);
					notify.darkStyle();
					notify.show();

				} else {
					notify.hideAfter(Duration.seconds(1));
				}

			}

		});

	}

	public void entryStatus(ActionEvent e) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("currentEntry.fxml"));
		Scene scene = new Scene(root);
		primaryStage.getIcons().add(new Image("img/baga.png"));
		primaryStage.setTitle("Entry Status");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void filter(ActionEvent event) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("XLS.fxml"));
		Scene scene = new Scene(root);
		primaryStage.getIcons().add(new Image("img/baga.png"));
		primaryStage.setTitle("Filtering Panel");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void email(ActionEvent event) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("Mail.fxml"));
		Scene scene = new Scene(root);
		primaryStage.getIcons().add(new Image("img/baga.png"));
		primaryStage.setTitle("Email Services Panel");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void exit(ActionEvent n) {
		((Node) n.getSource()).getScene().getWindow().hide();
	}

}
