package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.TextFields;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class editIndividualController implements Initializable {

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
	private TextField check;
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
	private JFXTextField somo2;

	@FXML
	private TextField kata;
	Connection connection;
	ObservableList<String> datum;
	PreparedStatement state, autoLoad;
	private String suggest[] = { "KE", "ME" };

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		connection = Database.Connector();
		if (connection == null) {
			System.exit(1);
		}
		datum = FXCollections.observableArrayList();
		try {
			String need = "select checkNo from workers";
			state = connection.prepareStatement(need);
			ResultSet set = state.executeQuery();
			while (set.next()) {
				datum.add(set.getString("checkNo"));
			}
			TextFields.bindAutoCompletion(check, datum);
			TextFields.bindAutoCompletion(sex, suggest);

		} catch (Exception e) {
			e.printStackTrace();
		}

		// autoFILL
		check.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ENTER) {
				String auto = "select * from workers where checkNo = '" + check.getText().trim() + "'";

				try {
					autoLoad = connection.prepareStatement(auto);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				try {
					ResultSet set3 = autoLoad.executeQuery();
					while (set3.next()) {
						fName.setText(set3.getString("fName"));
						mName.setText(set3.getString("mName"));
						lName.setText(set3.getString("lName"));
						sex.setText(set3.getString("sex"));
						cheo.setText(set3.getString("cheo"));
						mshahara.setText(set3.getString("nMshahara"));
						tsd.setText(set3.getString("tsd"));
						((TextField) kuzaliwa.getEditor()).setText(set3.getString("tKuzaliwa"));
						((TextField) ajira.getEditor()).setText(set3.getString("tAjira"));
						((TextField) thibitishwa.getEditor()).setText(set3.getString("tKuthibitishwa"));
						((TextField) daraja.getEditor()).setText(set3.getString("tDaraja"));
						elimu.setText(set3.getString("kElimu"));
						chuo.setText(set3.getString("cAlisoma"));
						aliomaliza.setText(set3.getString("mAlimaliza"));
						sasaKazi.setText(set3.getString("kKaziSasa"));
						awaliKazi.setText(set3.getString("kKaziAwali"));
						dini.setText(set3.getString("dini"));
						alipozaliwa.setText(set3.getString("aMahali"));
						number.setText(set3.getString("mobile"));
						somo1.setText(set3.getString("sKwanza"));
						somo2.setText(set3.getString("sPili"));
						kata.setText(set3.getString("kata"));

					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}

			}
		});

	}

	public boolean ifDbIsConnected() {
		try {
			return !connection.isClosed();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public void updateIndividual(ActionEvent event) {
		PreparedStatement statement = null;
		if (empty() & space()) {

			String activate = "update  workers set fName =?,mName = ?,lName = ?,sex = ?,cheo = ?,nMshahara = ?"
					+ ",tsd = ?,tKuzaliwa = ? ,tAjira = ?,tKuthibitishwa = ? ,tDaraja = ? ,kElimu = ? ,cAlisoma = ?"
					+ ",mAlimaliza = ?,kKaziSasa = ?,kKaziAwali = ?,dini = ?,aMahali = ? ,mobile = ?,sKwanza = ?,sPili = ?,kata = ?";
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

				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("ALERT");
				alert.setGraphic(new ImageView(this.getClass().getResource("/img/success.jpg").toString()));
				alert.setContentText("User Updated");
				alert.setHeaderText(null);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
				alert.showAndWait();

				statement.execute();

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

				statement.close();

			} catch (Exception e) {
			
e.printStackTrace();
			}
		}

	}

	public boolean empty() {
		if (!fName.getText().isEmpty() || !somo2.getText().isEmpty() || !elimu.getText().isEmpty()
				|| !sex.getText().isEmpty() || !mName.getText().isEmpty() || !check.getText().isEmpty()
				|| !alipozaliwa.getText().isEmpty() || !kata.getText().isEmpty() || !chuo.getText().isEmpty()
				|| !lName.getText().isEmpty() || !awaliKazi.getText().isEmpty()
				|| !daraja.getEditor().getText().isEmpty() || !thibitishwa.getEditor().getText().isEmpty()
				|| !kuzaliwa.getEditor().getText().isEmpty() || !mshahara.getText().isEmpty()
				|| !cheo.getText().isEmpty() || !ajira.getEditor().getText().isEmpty() || !dini.getText().isEmpty()
				|| !number.getText().isEmpty() || !sasaKazi.getText().isEmpty() || !somo1.getText().isEmpty()
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
		if (!space && !space1 && !space2 && !space3 && !space4 && !space5 && !space6 && !space7 && !space8 && !space9
				&& !space10 && !space11 && !space12 && !space13 && !space14 && !space15 && !space16 && !space17
				&& !space18) {
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

	public void exit(ActionEvent e) {
		((Node) e.getSource()).getScene().getWindow().hide();
	}

}
