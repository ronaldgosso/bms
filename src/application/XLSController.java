package application;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import com.jfoenix.controls.JFXTextField;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;

public class XLSController implements Initializable {

	Connection connection;

	@FXML
	private TableView<individualTable> filterTable;
	@FXML
	private TableColumn<individualTable, String> a;
	@FXML
	private TableColumn<individualTable, String> b;
	@FXML
	private TableColumn<individualTable, String> c;
	@FXML
	private TableColumn<individualTable, String> d;
	@FXML
	private TableColumn<individualTable, String> e;
	@FXML
	private TableColumn<individualTable, String> f;
	@FXML
	private TableColumn<individualTable, String> g;
	@FXML
	private TableColumn<individualTable, String> h;
	@FXML
	private TableColumn<individualTable, String> i;
	@FXML
	private TableColumn<individualTable, String> j;
	@FXML
	private TableColumn<individualTable, String> k;
	@FXML
	private TableColumn<individualTable, String> l;
	@FXML
	private TableColumn<individualTable, String> m;
	@FXML
	private TableColumn<individualTable, String> n;
	@FXML
	private TableColumn<individualTable, String> o;
	@FXML
	private TableColumn<individualTable, String> p;
	@FXML
	private TableColumn<individualTable, String> q;
	@FXML
	private TableColumn<individualTable, String> r;
	@FXML
	private TableColumn<individualTable, String> s;
	@FXML
	private TableColumn<individualTable, String> t;
	@FXML
	private TableColumn<individualTable, String> u;
	@FXML
	private TableColumn<individualTable, String> v;
	@FXML
	private TableColumn<individualTable, String> w;
	@FXML
	private JFXTextField saveAs;
	private ResultSet statement;
	private PreparedStatement set;
	private ObservableList<individualTable> data;

	private static final AudioClip ALERT_AUDIOCLIP = new AudioClip(
			MainController.class.getResource("/Music/success.wav").toString());

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		connection = Database.Connector();
		if (connection == null) {
			System.exit(1);
		}

	}

	public boolean ifDbIsConnected() {
		try {
			return !connection.isClosed();
		} catch (Exception e) {
			
			return false;
		}
	}

	public void jinsiaFilter(ActionEvent event) {
		data = FXCollections.observableArrayList();
		try {
			String query = "SELECT * FROM workers ORDER BY workers.sex ASC";
			set = connection.prepareStatement(query);
			statement = set.executeQuery();

			while (statement.next()) {
				data.add(new individualTable(statement.getString("checkNo"), statement.getString("fName"),
						statement.getString("mName"), statement.getString("lName"), statement.getString("sex"),
						statement.getString("cheo"), statement.getString("nMshahara"), statement.getString("tsd"),
						statement.getString("tKuzaliwa"), statement.getString("tAjira"),
						statement.getString("TKuthibitishwa"), statement.getString("tDaraja"),
						statement.getString("kElimu"), statement.getString("cAlisoma"),
						statement.getString("mAlimaliza"), statement.getString("kKaziSasa"),
						statement.getString("kKaziAwali"), statement.getString("dini"), statement.getString("aMahali"),
						statement.getString("mobile"), statement.getString("sKwanza"), statement.getString("sPili"),
						statement.getString("kata")));
			}

		} catch (Exception e) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("ALERT");
			alert.setGraphic(new ImageView(this.getClass().getResource("/img/Delete-icon.png").toString()));
			alert.setContentText("System Error");
			alert.setHeaderText(null);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
			alert.showAndWait();
		}

		a.setCellValueFactory(new PropertyValueFactory<>("check"));
		b.setCellValueFactory(new PropertyValueFactory<>("fName"));
		c.setCellValueFactory(new PropertyValueFactory<>("mName"));
		d.setCellValueFactory(new PropertyValueFactory<>("lName"));
		e.setCellValueFactory(new PropertyValueFactory<>("sex"));
		f.setCellValueFactory(new PropertyValueFactory<>("cheo"));
		g.setCellValueFactory(new PropertyValueFactory<>("mshahara"));
		h.setCellValueFactory(new PropertyValueFactory<>("tsd"));
		i.setCellValueFactory(new PropertyValueFactory<>("kuzaliwa"));
		j.setCellValueFactory(new PropertyValueFactory<>("ajira"));
		k.setCellValueFactory(new PropertyValueFactory<>("thibitishwa"));
		l.setCellValueFactory(new PropertyValueFactory<>("daraja"));
		m.setCellValueFactory(new PropertyValueFactory<>("elimu"));
		n.setCellValueFactory(new PropertyValueFactory<>("chuo"));
		o.setCellValueFactory(new PropertyValueFactory<>("aliomaliza"));
		p.setCellValueFactory(new PropertyValueFactory<>("sasaKazi"));
		q.setCellValueFactory(new PropertyValueFactory<>("awaliKazi"));
		r.setCellValueFactory(new PropertyValueFactory<>("dini"));
		s.setCellValueFactory(new PropertyValueFactory<>("alipozaliwa"));
		t.setCellValueFactory(new PropertyValueFactory<>("number"));
		u.setCellValueFactory(new PropertyValueFactory<>("somo1"));
		v.setCellValueFactory(new PropertyValueFactory<>("somo2"));
		w.setCellValueFactory(new PropertyValueFactory<>("kata"));

		filterTable.setItems(null);
		filterTable.setItems(data);

	}

	public void sasaKaziFilter(ActionEvent event) {
		data = FXCollections.observableArrayList();
		try {
			String query = "SELECT * FROM workers ORDER BY workers.kKaziSasa ASC";
			set = connection.prepareStatement(query);
			statement = set.executeQuery();

			while (statement.next()) {
				data.add(new individualTable(statement.getString("checkNo"), statement.getString("fName"),
						statement.getString("mName"), statement.getString("lName"), statement.getString("sex"),
						statement.getString("cheo"), statement.getString("nMshahara"), statement.getString("tsd"),
						statement.getString("tKuzaliwa"), statement.getString("tAjira"),
						statement.getString("TKuthibitishwa"), statement.getString("tDaraja"),
						statement.getString("kElimu"), statement.getString("cAlisoma"),
						statement.getString("mAlimaliza"), statement.getString("kKaziSasa"),
						statement.getString("kKaziAwali"), statement.getString("dini"), statement.getString("aMahali"),
						statement.getString("mobile"), statement.getString("sKwanza"), statement.getString("sPili"),
						statement.getString("kata")));
			}

		} catch (Exception e) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("ALERT");
			alert.setGraphic(new ImageView(this.getClass().getResource("/img/Delete-icon.png").toString()));
			alert.setContentText("System Error");
			alert.setHeaderText(null);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
			alert.showAndWait();
		}

		a.setCellValueFactory(new PropertyValueFactory<>("check"));
		b.setCellValueFactory(new PropertyValueFactory<>("fName"));
		c.setCellValueFactory(new PropertyValueFactory<>("mName"));
		d.setCellValueFactory(new PropertyValueFactory<>("lName"));
		e.setCellValueFactory(new PropertyValueFactory<>("sex"));
		f.setCellValueFactory(new PropertyValueFactory<>("cheo"));
		g.setCellValueFactory(new PropertyValueFactory<>("mshahara"));
		h.setCellValueFactory(new PropertyValueFactory<>("tsd"));
		i.setCellValueFactory(new PropertyValueFactory<>("kuzaliwa"));
		j.setCellValueFactory(new PropertyValueFactory<>("ajira"));
		k.setCellValueFactory(new PropertyValueFactory<>("thibitishwa"));
		l.setCellValueFactory(new PropertyValueFactory<>("daraja"));
		m.setCellValueFactory(new PropertyValueFactory<>("elimu"));
		n.setCellValueFactory(new PropertyValueFactory<>("chuo"));
		o.setCellValueFactory(new PropertyValueFactory<>("aliomaliza"));
		p.setCellValueFactory(new PropertyValueFactory<>("sasaKazi"));
		q.setCellValueFactory(new PropertyValueFactory<>("awaliKazi"));
		r.setCellValueFactory(new PropertyValueFactory<>("dini"));
		s.setCellValueFactory(new PropertyValueFactory<>("alipozaliwa"));
		t.setCellValueFactory(new PropertyValueFactory<>("number"));
		u.setCellValueFactory(new PropertyValueFactory<>("somo1"));
		v.setCellValueFactory(new PropertyValueFactory<>("somo2"));
		w.setCellValueFactory(new PropertyValueFactory<>("kata"));

		filterTable.setItems(null);
		filterTable.setItems(data);

	}

	public void elimuFilter(ActionEvent event) {
		data = FXCollections.observableArrayList();
		try {
			String query = "SELECT * FROM workers ORDER BY workers.kElimu ASC";
			set = connection.prepareStatement(query);
			statement = set.executeQuery();

			while (statement.next()) {
				data.add(new individualTable(statement.getString("checkNo"), statement.getString("fName"),
						statement.getString("mName"), statement.getString("lName"), statement.getString("sex"),
						statement.getString("cheo"), statement.getString("nMshahara"), statement.getString("tsd"),
						statement.getString("tKuzaliwa"), statement.getString("tAjira"),
						statement.getString("TKuthibitishwa"), statement.getString("tDaraja"),
						statement.getString("kElimu"), statement.getString("cAlisoma"),
						statement.getString("mAlimaliza"), statement.getString("kKaziSasa"),
						statement.getString("kKaziAwali"), statement.getString("dini"), statement.getString("aMahali"),
						statement.getString("mobile"), statement.getString("sKwanza"), statement.getString("sPili"),
						statement.getString("kata")));
			}

		} catch (Exception e) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("ALERT");
			alert.setGraphic(new ImageView(this.getClass().getResource("/img/Delete-icon.png").toString()));
			alert.setContentText("System Error");
			alert.setHeaderText(null);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
			alert.showAndWait();
		}

		a.setCellValueFactory(new PropertyValueFactory<>("check"));
		b.setCellValueFactory(new PropertyValueFactory<>("fName"));
		c.setCellValueFactory(new PropertyValueFactory<>("mName"));
		d.setCellValueFactory(new PropertyValueFactory<>("lName"));
		e.setCellValueFactory(new PropertyValueFactory<>("sex"));
		f.setCellValueFactory(new PropertyValueFactory<>("cheo"));
		g.setCellValueFactory(new PropertyValueFactory<>("mshahara"));
		h.setCellValueFactory(new PropertyValueFactory<>("tsd"));
		i.setCellValueFactory(new PropertyValueFactory<>("kuzaliwa"));
		j.setCellValueFactory(new PropertyValueFactory<>("ajira"));
		k.setCellValueFactory(new PropertyValueFactory<>("thibitishwa"));
		l.setCellValueFactory(new PropertyValueFactory<>("daraja"));
		m.setCellValueFactory(new PropertyValueFactory<>("elimu"));
		n.setCellValueFactory(new PropertyValueFactory<>("chuo"));
		o.setCellValueFactory(new PropertyValueFactory<>("aliomaliza"));
		p.setCellValueFactory(new PropertyValueFactory<>("sasaKazi"));
		q.setCellValueFactory(new PropertyValueFactory<>("awaliKazi"));
		r.setCellValueFactory(new PropertyValueFactory<>("dini"));
		s.setCellValueFactory(new PropertyValueFactory<>("alipozaliwa"));
		t.setCellValueFactory(new PropertyValueFactory<>("number"));
		u.setCellValueFactory(new PropertyValueFactory<>("somo1"));
		v.setCellValueFactory(new PropertyValueFactory<>("somo2"));
		w.setCellValueFactory(new PropertyValueFactory<>("kata"));

		filterTable.setItems(null);
		filterTable.setItems(data);

	}

	public void mshaharaFilter(ActionEvent event) {
		data = FXCollections.observableArrayList();
		try {
			String query = "SELECT * FROM workers ORDER BY workers.nMshahara ASC";
			set = connection.prepareStatement(query);
			statement = set.executeQuery();

			while (statement.next()) {
				data.add(new individualTable(statement.getString("checkNo"), statement.getString("fName"),
						statement.getString("mName"), statement.getString("lName"), statement.getString("sex"),
						statement.getString("cheo"), statement.getString("nMshahara"), statement.getString("tsd"),
						statement.getString("tKuzaliwa"), statement.getString("tAjira"),
						statement.getString("TKuthibitishwa"), statement.getString("tDaraja"),
						statement.getString("kElimu"), statement.getString("cAlisoma"),
						statement.getString("mAlimaliza"), statement.getString("kKaziSasa"),
						statement.getString("kKaziAwali"), statement.getString("dini"), statement.getString("aMahali"),
						statement.getString("mobile"), statement.getString("sKwanza"), statement.getString("sPili"),
						statement.getString("kata")));
			}

		} catch (Exception e) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("ALERT");
			alert.setGraphic(new ImageView(this.getClass().getResource("/img/Delete-icon.png").toString()));
			alert.setContentText("System Error");
			alert.setHeaderText(null);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
			alert.showAndWait();
		}

		a.setCellValueFactory(new PropertyValueFactory<>("check"));
		b.setCellValueFactory(new PropertyValueFactory<>("fName"));
		c.setCellValueFactory(new PropertyValueFactory<>("mName"));
		d.setCellValueFactory(new PropertyValueFactory<>("lName"));
		e.setCellValueFactory(new PropertyValueFactory<>("sex"));
		f.setCellValueFactory(new PropertyValueFactory<>("cheo"));
		g.setCellValueFactory(new PropertyValueFactory<>("mshahara"));
		h.setCellValueFactory(new PropertyValueFactory<>("tsd"));
		i.setCellValueFactory(new PropertyValueFactory<>("kuzaliwa"));
		j.setCellValueFactory(new PropertyValueFactory<>("ajira"));
		k.setCellValueFactory(new PropertyValueFactory<>("thibitishwa"));
		l.setCellValueFactory(new PropertyValueFactory<>("daraja"));
		m.setCellValueFactory(new PropertyValueFactory<>("elimu"));
		n.setCellValueFactory(new PropertyValueFactory<>("chuo"));
		o.setCellValueFactory(new PropertyValueFactory<>("aliomaliza"));
		p.setCellValueFactory(new PropertyValueFactory<>("sasaKazi"));
		q.setCellValueFactory(new PropertyValueFactory<>("awaliKazi"));
		r.setCellValueFactory(new PropertyValueFactory<>("dini"));
		s.setCellValueFactory(new PropertyValueFactory<>("alipozaliwa"));
		t.setCellValueFactory(new PropertyValueFactory<>("number"));
		u.setCellValueFactory(new PropertyValueFactory<>("somo1"));
		v.setCellValueFactory(new PropertyValueFactory<>("somo2"));
		w.setCellValueFactory(new PropertyValueFactory<>("kata"));

		filterTable.setItems(null);
		filterTable.setItems(data);

	}

	public void darajaFilter(ActionEvent event) {
		data = FXCollections.observableArrayList();
		try {
			String query = "SELECT * FROM workers ORDER BY workers.tDaraja ASC";
			set = connection.prepareStatement(query);
			statement = set.executeQuery();

			while (statement.next()) {
				data.add(new individualTable(statement.getString("checkNo"), statement.getString("fName"),
						statement.getString("mName"), statement.getString("lName"), statement.getString("sex"),
						statement.getString("cheo"), statement.getString("nMshahara"), statement.getString("tsd"),
						statement.getString("tKuzaliwa"), statement.getString("tAjira"),
						statement.getString("TKuthibitishwa"), statement.getString("tDaraja"),
						statement.getString("kElimu"), statement.getString("cAlisoma"),
						statement.getString("mAlimaliza"), statement.getString("kKaziSasa"),
						statement.getString("kKaziAwali"), statement.getString("dini"), statement.getString("aMahali"),
						statement.getString("mobile"), statement.getString("sKwanza"), statement.getString("sPili"),
						statement.getString("kata")));
			}

		} catch (Exception e) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("ALERT");
			alert.setGraphic(new ImageView(this.getClass().getResource("/img/Delete-icon.png").toString()));
			alert.setContentText("System Error");
			alert.setHeaderText(null);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
			alert.showAndWait();
		}

		a.setCellValueFactory(new PropertyValueFactory<>("check"));
		b.setCellValueFactory(new PropertyValueFactory<>("fName"));
		c.setCellValueFactory(new PropertyValueFactory<>("mName"));
		d.setCellValueFactory(new PropertyValueFactory<>("lName"));
		e.setCellValueFactory(new PropertyValueFactory<>("sex"));
		f.setCellValueFactory(new PropertyValueFactory<>("cheo"));
		g.setCellValueFactory(new PropertyValueFactory<>("mshahara"));
		h.setCellValueFactory(new PropertyValueFactory<>("tsd"));
		i.setCellValueFactory(new PropertyValueFactory<>("kuzaliwa"));
		j.setCellValueFactory(new PropertyValueFactory<>("ajira"));
		k.setCellValueFactory(new PropertyValueFactory<>("thibitishwa"));
		l.setCellValueFactory(new PropertyValueFactory<>("daraja"));
		m.setCellValueFactory(new PropertyValueFactory<>("elimu"));
		n.setCellValueFactory(new PropertyValueFactory<>("chuo"));
		o.setCellValueFactory(new PropertyValueFactory<>("aliomaliza"));
		p.setCellValueFactory(new PropertyValueFactory<>("sasaKazi"));
		q.setCellValueFactory(new PropertyValueFactory<>("awaliKazi"));
		r.setCellValueFactory(new PropertyValueFactory<>("dini"));
		s.setCellValueFactory(new PropertyValueFactory<>("alipozaliwa"));
		t.setCellValueFactory(new PropertyValueFactory<>("number"));
		u.setCellValueFactory(new PropertyValueFactory<>("somo1"));
		v.setCellValueFactory(new PropertyValueFactory<>("somo2"));
		w.setCellValueFactory(new PropertyValueFactory<>("kata"));

		filterTable.setItems(null);
		filterTable.setItems(data);

	}

	public void cheoFilter(ActionEvent event) {
		data = FXCollections.observableArrayList();
		try {
			String query = "SELECT * FROM workers ORDER BY workers.cheo ASC";
			set = connection.prepareStatement(query);
			statement = set.executeQuery();

			while (statement.next()) {
				data.add(new individualTable(statement.getString("checkNo"), statement.getString("fName"),
						statement.getString("mName"), statement.getString("lName"), statement.getString("sex"),
						statement.getString("cheo"), statement.getString("nMshahara"), statement.getString("tsd"),
						statement.getString("tKuzaliwa"), statement.getString("tAjira"),
						statement.getString("TKuthibitishwa"), statement.getString("tDaraja"),
						statement.getString("kElimu"), statement.getString("cAlisoma"),
						statement.getString("mAlimaliza"), statement.getString("kKaziSasa"),
						statement.getString("kKaziAwali"), statement.getString("dini"), statement.getString("aMahali"),
						statement.getString("mobile"), statement.getString("sKwanza"), statement.getString("sPili"),
						statement.getString("kata")));
			}

		} catch (Exception e) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("ALERT");
			alert.setGraphic(new ImageView(this.getClass().getResource("/img/Delete-icon.png").toString()));
			alert.setContentText("System Error");
			alert.setHeaderText(null);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
			alert.showAndWait();
		}

		a.setCellValueFactory(new PropertyValueFactory<>("check"));
		b.setCellValueFactory(new PropertyValueFactory<>("fName"));
		c.setCellValueFactory(new PropertyValueFactory<>("mName"));
		d.setCellValueFactory(new PropertyValueFactory<>("lName"));
		e.setCellValueFactory(new PropertyValueFactory<>("sex"));
		f.setCellValueFactory(new PropertyValueFactory<>("cheo"));
		g.setCellValueFactory(new PropertyValueFactory<>("mshahara"));
		h.setCellValueFactory(new PropertyValueFactory<>("tsd"));
		i.setCellValueFactory(new PropertyValueFactory<>("kuzaliwa"));
		j.setCellValueFactory(new PropertyValueFactory<>("ajira"));
		k.setCellValueFactory(new PropertyValueFactory<>("thibitishwa"));
		l.setCellValueFactory(new PropertyValueFactory<>("daraja"));
		m.setCellValueFactory(new PropertyValueFactory<>("elimu"));
		n.setCellValueFactory(new PropertyValueFactory<>("chuo"));
		o.setCellValueFactory(new PropertyValueFactory<>("aliomaliza"));
		p.setCellValueFactory(new PropertyValueFactory<>("sasaKazi"));
		q.setCellValueFactory(new PropertyValueFactory<>("awaliKazi"));
		r.setCellValueFactory(new PropertyValueFactory<>("dini"));
		s.setCellValueFactory(new PropertyValueFactory<>("alipozaliwa"));
		t.setCellValueFactory(new PropertyValueFactory<>("number"));
		u.setCellValueFactory(new PropertyValueFactory<>("somo1"));
		v.setCellValueFactory(new PropertyValueFactory<>("somo2"));
		w.setCellValueFactory(new PropertyValueFactory<>("kata"));

		filterTable.setItems(null);
		filterTable.setItems(data);

	}

	public void ajiraFilter(ActionEvent event) {
		data = FXCollections.observableArrayList();
		try {
			String query = "SELECT * FROM workers ORDER BY workers.tAjira ASC";
			set = connection.prepareStatement(query);
			statement = set.executeQuery();

			while (statement.next()) {
				data.add(new individualTable(statement.getString("checkNo"), statement.getString("fName"),
						statement.getString("mName"), statement.getString("lName"), statement.getString("sex"),
						statement.getString("cheo"), statement.getString("nMshahara"), statement.getString("tsd"),
						statement.getString("tKuzaliwa"), statement.getString("tAjira"),
						statement.getString("TKuthibitishwa"), statement.getString("tDaraja"),
						statement.getString("kElimu"), statement.getString("cAlisoma"),
						statement.getString("mAlimaliza"), statement.getString("kKaziSasa"),
						statement.getString("kKaziAwali"), statement.getString("dini"), statement.getString("aMahali"),
						statement.getString("mobile"), statement.getString("sKwanza"), statement.getString("sPili"),
						statement.getString("kata")));
			}

		} catch (Exception e) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("ALERT");
			alert.setGraphic(new ImageView(this.getClass().getResource("/img/Delete-icon.png").toString()));
			alert.setContentText("System Error");
			alert.setHeaderText(null);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
			alert.showAndWait();
		}

		a.setCellValueFactory(new PropertyValueFactory<>("check"));
		b.setCellValueFactory(new PropertyValueFactory<>("fName"));
		c.setCellValueFactory(new PropertyValueFactory<>("mName"));
		d.setCellValueFactory(new PropertyValueFactory<>("lName"));
		e.setCellValueFactory(new PropertyValueFactory<>("sex"));
		f.setCellValueFactory(new PropertyValueFactory<>("cheo"));
		g.setCellValueFactory(new PropertyValueFactory<>("mshahara"));
		h.setCellValueFactory(new PropertyValueFactory<>("tsd"));
		i.setCellValueFactory(new PropertyValueFactory<>("kuzaliwa"));
		j.setCellValueFactory(new PropertyValueFactory<>("ajira"));
		k.setCellValueFactory(new PropertyValueFactory<>("thibitishwa"));
		l.setCellValueFactory(new PropertyValueFactory<>("daraja"));
		m.setCellValueFactory(new PropertyValueFactory<>("elimu"));
		n.setCellValueFactory(new PropertyValueFactory<>("chuo"));
		o.setCellValueFactory(new PropertyValueFactory<>("aliomaliza"));
		p.setCellValueFactory(new PropertyValueFactory<>("sasaKazi"));
		q.setCellValueFactory(new PropertyValueFactory<>("awaliKazi"));
		r.setCellValueFactory(new PropertyValueFactory<>("dini"));
		s.setCellValueFactory(new PropertyValueFactory<>("alipozaliwa"));
		t.setCellValueFactory(new PropertyValueFactory<>("number"));
		u.setCellValueFactory(new PropertyValueFactory<>("somo1"));
		v.setCellValueFactory(new PropertyValueFactory<>("somo2"));
		w.setCellValueFactory(new PropertyValueFactory<>("kata"));

		filterTable.setItems(null);
		filterTable.setItems(data);

	}

	public void checkFilter(ActionEvent event) {
		data = FXCollections.observableArrayList();
		try {
			String query = "SELECT * FROM workers ORDER BY workers.checkNo ASC";
			set = connection.prepareStatement(query);
			statement = set.executeQuery();

			while (statement.next()) {
				data.add(new individualTable(statement.getString("checkNo"), statement.getString("fName"),
						statement.getString("mName"), statement.getString("lName"), statement.getString("sex"),
						statement.getString("cheo"), statement.getString("nMshahara"), statement.getString("tsd"),
						statement.getString("tKuzaliwa"), statement.getString("tAjira"),
						statement.getString("TKuthibitishwa"), statement.getString("tDaraja"),
						statement.getString("kElimu"), statement.getString("cAlisoma"),
						statement.getString("mAlimaliza"), statement.getString("kKaziSasa"),
						statement.getString("kKaziAwali"), statement.getString("dini"), statement.getString("aMahali"),
						statement.getString("mobile"), statement.getString("sKwanza"), statement.getString("sPili"),
						statement.getString("kata")));
			}

		} catch (Exception e) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("ALERT");
			alert.setGraphic(new ImageView(this.getClass().getResource("/img/Delete-icon.png").toString()));
			alert.setContentText("System Error");
			alert.setHeaderText(null);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
			alert.showAndWait();
		}

		a.setCellValueFactory(new PropertyValueFactory<>("check"));
		b.setCellValueFactory(new PropertyValueFactory<>("fName"));
		c.setCellValueFactory(new PropertyValueFactory<>("mName"));
		d.setCellValueFactory(new PropertyValueFactory<>("lName"));
		e.setCellValueFactory(new PropertyValueFactory<>("sex"));
		f.setCellValueFactory(new PropertyValueFactory<>("cheo"));
		g.setCellValueFactory(new PropertyValueFactory<>("mshahara"));
		h.setCellValueFactory(new PropertyValueFactory<>("tsd"));
		i.setCellValueFactory(new PropertyValueFactory<>("kuzaliwa"));
		j.setCellValueFactory(new PropertyValueFactory<>("ajira"));
		k.setCellValueFactory(new PropertyValueFactory<>("thibitishwa"));
		l.setCellValueFactory(new PropertyValueFactory<>("daraja"));
		m.setCellValueFactory(new PropertyValueFactory<>("elimu"));
		n.setCellValueFactory(new PropertyValueFactory<>("chuo"));
		o.setCellValueFactory(new PropertyValueFactory<>("aliomaliza"));
		p.setCellValueFactory(new PropertyValueFactory<>("sasaKazi"));
		q.setCellValueFactory(new PropertyValueFactory<>("awaliKazi"));
		r.setCellValueFactory(new PropertyValueFactory<>("dini"));
		s.setCellValueFactory(new PropertyValueFactory<>("alipozaliwa"));
		t.setCellValueFactory(new PropertyValueFactory<>("number"));
		u.setCellValueFactory(new PropertyValueFactory<>("somo1"));
		v.setCellValueFactory(new PropertyValueFactory<>("somo2"));
		w.setCellValueFactory(new PropertyValueFactory<>("kata"));

		filterTable.setItems(null);
		filterTable.setItems(data);

	}

	public void kataFilter(ActionEvent event) {
		data = FXCollections.observableArrayList();
		try {
			String query = "SELECT * FROM workers ORDER BY workers.kata ASC";
			set = connection.prepareStatement(query);
			statement = set.executeQuery();

			while (statement.next()) {
				data.add(new individualTable(statement.getString("checkNo"), statement.getString("fName"),
						statement.getString("mName"), statement.getString("lName"), statement.getString("sex"),
						statement.getString("cheo"), statement.getString("nMshahara"), statement.getString("tsd"),
						statement.getString("tKuzaliwa"), statement.getString("tAjira"),
						statement.getString("TKuthibitishwa"), statement.getString("tDaraja"),
						statement.getString("kElimu"), statement.getString("cAlisoma"),
						statement.getString("mAlimaliza"), statement.getString("kKaziSasa"),
						statement.getString("kKaziAwali"), statement.getString("dini"), statement.getString("aMahali"),
						statement.getString("mobile"), statement.getString("sKwanza"), statement.getString("sPili"),
						statement.getString("kata")));
			}

		} catch (Exception e) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("ALERT");
			alert.setGraphic(new ImageView(this.getClass().getResource("/img/Delete-icon.png").toString()));
			alert.setContentText("System Error");
			alert.setHeaderText(null);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
			alert.showAndWait();
		}

		a.setCellValueFactory(new PropertyValueFactory<>("check"));
		b.setCellValueFactory(new PropertyValueFactory<>("fName"));
		c.setCellValueFactory(new PropertyValueFactory<>("mName"));
		d.setCellValueFactory(new PropertyValueFactory<>("lName"));
		e.setCellValueFactory(new PropertyValueFactory<>("sex"));
		f.setCellValueFactory(new PropertyValueFactory<>("cheo"));
		g.setCellValueFactory(new PropertyValueFactory<>("mshahara"));
		h.setCellValueFactory(new PropertyValueFactory<>("tsd"));
		i.setCellValueFactory(new PropertyValueFactory<>("kuzaliwa"));
		j.setCellValueFactory(new PropertyValueFactory<>("ajira"));
		k.setCellValueFactory(new PropertyValueFactory<>("thibitishwa"));
		l.setCellValueFactory(new PropertyValueFactory<>("daraja"));
		m.setCellValueFactory(new PropertyValueFactory<>("elimu"));
		n.setCellValueFactory(new PropertyValueFactory<>("chuo"));
		o.setCellValueFactory(new PropertyValueFactory<>("aliomaliza"));
		p.setCellValueFactory(new PropertyValueFactory<>("sasaKazi"));
		q.setCellValueFactory(new PropertyValueFactory<>("awaliKazi"));
		r.setCellValueFactory(new PropertyValueFactory<>("dini"));
		s.setCellValueFactory(new PropertyValueFactory<>("alipozaliwa"));
		t.setCellValueFactory(new PropertyValueFactory<>("number"));
		u.setCellValueFactory(new PropertyValueFactory<>("somo1"));
		v.setCellValueFactory(new PropertyValueFactory<>("somo2"));
		w.setCellValueFactory(new PropertyValueFactory<>("kata"));

		filterTable.setItems(null);
		filterTable.setItems(data);

	}

	public void toExcell(ActionEvent event)  {
		if(empty() & space()) {
		try {
		String query = "SELECT * FROM workers";
		set = connection.prepareStatement(query);
		statement = set.executeQuery();

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
		
	
		
		
		//row executions
		
		int index = 1;

		while (statement.next()) {
			HSSFRow row = sheet.createRow(index);// create a row each time the set is executed
			row.createCell(0).setCellValue(statement.getString("checkNo"));// rows with cell values
			row.createCell(1).setCellValue(statement.getString("fName"));
			row.createCell(2).setCellValue(statement.getString("mName"));
			row.createCell(3).setCellValue(statement.getString("lName"));
			row.createCell(4).setCellValue(statement.getString("sex"));
			row.createCell(5).setCellValue(statement.getString("cheo"));
			row.createCell(6).setCellValue(statement.getString("nMshahara"));
			row.createCell(7).setCellValue(statement.getString("tsd"));
			row.createCell(8).setCellValue(statement.getString("tKuzaliwa"));
			row.createCell(9).setCellValue(statement.getString("tAjira"));
			row.createCell(10).setCellValue(statement.getString("TKuthibitishwa"));
			row.createCell(11).setCellValue(statement.getString("tDaraja"));
			row.createCell(12).setCellValue(statement.getString("kElimu"));
			row.createCell(13).setCellValue(statement.getString("cAlisoma"));
			row.createCell(14).setCellValue(statement.getString("mAlimaliza"));
			row.createCell(15).setCellValue(statement.getString("kKaziSasa"));
			row.createCell(16).setCellValue(statement.getString("kKaziAwali"));
			row.createCell(17).setCellValue(statement.getString("dini"));
			row.createCell(18).setCellValue(statement.getString("aMahali"));
			row.createCell(19).setCellValue(statement.getString("mobile"));
			row.createCell(20).setCellValue(statement.getString("sKwanza"));
			row.createCell(21).setCellValue(statement.getString("sPili"));
			row.createCell(22).setCellValue(statement.getString("kata"));

			index++;
		}
		
        String fileName = saveAs.getText()+" "+"sorted."+".xls";
        String home = System.getProperty("user.home");
        File file = new File(home +"/Documents/" +fileName);
		FileOutputStream fileOut = new FileOutputStream(file);// before 2007 <xlsx> 'output file to windows'
		wb.write(fileOut);
		fileOut.close();

		XLSController.ALERT_AUDIOCLIP.play();

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("ALERT");
		alert.setGraphic(new ImageView(this.getClass().getResource("/img/success.jpg").toString()));
		alert.setContentText("Filtered File Exported To DOCUMENTS");
		alert.setHeaderText(null);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
		alert.showAndWait();
		
		set.close();
		statement.close();
		saveAs.clear();
		//wb.close();
	
	}catch(Exception e) {
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
}

		public boolean empty() {
			if(!saveAs.getText().isEmpty()) {
				return true;
			}else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("ALERT");
				alert.setGraphic(new ImageView(this.getClass().getResource("/img/Delete-icon.png").toString()));
				alert.setContentText("Empty Submission is Prohibited");
				alert.setHeaderText(null);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
				alert.showAndWait();
				return false;
			}
		}
		
		public boolean space() {
			boolean spx = saveAs.getText().contains(" ");
			if(!spx) {
				return true;
			}
			else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("ALERT");
				alert.setGraphic(new ImageView(this.getClass().getResource("/img/Delete-icon.png").toString()));
				alert.setContentText("No Spaced Names Allowed");
				alert.setHeaderText(null);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
				alert.showAndWait();
				return false;
			}
		}

	public void exit(ActionEvent en) {
		((Node)en.getSource()).getScene().getWindow().hide();
	}
}
