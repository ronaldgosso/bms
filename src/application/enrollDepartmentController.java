package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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

public class enrollDepartmentController implements Initializable {
	@FXML
	private TableView<enrolldepartment> departmentTable;
	@FXML
	private TableColumn<enrolldepartment, String> columnName;
	@FXML
	private TableColumn<enrolldepartment, String> columnSerial;
	@FXML
	private TableColumn<enrolldepartment, String> columnSupervisor;
	@FXML
	private TableColumn<enrolldepartment, String> columnDate;

	private ObservableList<enrolldepartment> data;
	Connection connection;
	private PreparedStatement statement;
	private ResultSet set;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		connection = Database.Connector();
		if(connection == null){
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

	public void load(ActionEvent event) {
		data = FXCollections.observableArrayList();
		try {
			String query = "SELECT * FROM department";
			statement = connection.prepareStatement(query);
			set = statement.executeQuery();

			while (set.next()) {
				data.add(new enrolldepartment(set.getString("name"), set.getString("serial"),
						set.getString("supervisor"), set.getString("date")

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

		columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		columnSerial.setCellValueFactory(new PropertyValueFactory<>("serial"));
		columnSupervisor.setCellValueFactory(new PropertyValueFactory<>("supervisor"));
		columnDate.setCellValueFactory(new PropertyValueFactory<>("date"));

		departmentTable.setItems(null);

		departmentTable.setItems(data);

	}
	public void exit(ActionEvent event)throws Exception{
		((Node)event.getSource()).getScene().getWindow().hide();
		Stage primaryStage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("AdminHome.fxml"));

		Scene scene = new Scene(root);
		primaryStage.getIcons().add(new Image("/img/baga.png"));
		primaryStage.setTitle("Delete The Department");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
