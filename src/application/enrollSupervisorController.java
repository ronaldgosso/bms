package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;

public class enrollSupervisorController implements Initializable {
	@FXML
	private TableView<enrollSupervisor> departmentTable;
	@FXML
	private TableColumn<enrollSupervisor, String> columnFName;
	@FXML
	private TableColumn<enrollSupervisor, String> columnLName;
	@FXML
	private TableColumn<enrollSupervisor, String> columnUsername;
	@FXML
	private TableColumn<enrollSupervisor, String> columnDate;
	@FXML
	private TableColumn<enrollSupervisor, String> columnEmail;
	@FXML
	private TableColumn<enrollSupervisor, String> columnPassword;

	private ObservableList<enrollSupervisor> data;
	Connection connection;
	private PreparedStatement statement;
	private ResultSet set;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		connection = Database.Connector();
		if (connection == null) {
			System.exit(1);
		}

	}

	public boolean ifDbIsConnected() {
		try {
			return !connection.isClosed();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	// Event Listener on JFXButton.onAction

	public void loading(ActionEvent event) {
		data = FXCollections.observableArrayList();
		try {
			String query = "SELECT * FROM supervisor";
			statement = connection.prepareStatement(query);
			set = statement.executeQuery();

			while (set.next()) {
				data.add(new enrollSupervisor(set.getString("username"), set.getString("password"),
						set.getString("email"), set.getString("firstname"), set.getString("lastname"),
						set.getString("date")

				));
			}

		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("PROMPT");
			alert.setGraphic(new ImageView(this.getClass().getResource("/img/Delete-icon.png").toString()));
			alert.setContentText("System Error");
			alert.setHeaderText(null);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
			alert.showAndWait();
		}

		columnUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
		columnPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
		columnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		columnFName.setCellValueFactory(new PropertyValueFactory<>("Fname"));
		columnLName.setCellValueFactory(new PropertyValueFactory<>("Lname"));
		columnDate.setCellValueFactory(new PropertyValueFactory<>("date"));

		departmentTable.setItems(null);

		departmentTable.setItems(data);
	}
	// Event Listener on JFXButton.onAction

	public void exiting(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide();
		Stage primaryStage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("AdminHome.fxml"));

		Scene scene = new Scene(root);
		primaryStage.getIcons().add(new Image("/img/baga.png"));
		primaryStage.setTitle("Delete The Department");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
