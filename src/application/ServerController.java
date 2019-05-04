package application;

import java.net.URL;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.TextFields;

import com.jfoenix.controls.JFXTextField;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

public class ServerController implements Initializable {
	@FXML
	private TextArea txtAreaChatMsg;
	@FXML
	private TextArea txtAreaEventLog;
	@FXML
	private ListView<String> listUsersConnected;
	@FXML
	private Button btnStartServer;
	@FXML
	private Button btnStopServer;

	public Server server;

	private ObservableList<String> users;

	private String[] portsMention = { "1025", "1026", "1027", "1028", "53769", "53770", "53771", "53772", "53773",
			"53774", "53775", "53776", "53777", "53779", "53780", "53781", "53782", "53783", "53784", "53785", "53786",
			"53787", "53788", "53789", "53790", "53778", "1600" };
	@FXML
	private JFXTextField txtPorts;
	public int port;
	private String getPorts;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		TextFields.bindAutoCompletion(txtPorts, portsMention);

	}

	public void setPort(int Port) {
		port = Port;
	}

	public void get(ActionEvent eve) {
		getPorts = txtPorts.getText();
	}

	public void startServer() {
		port = Integer.parseInt(getPorts);
		// create a new Server
		server = new Server(port, this);
		users = FXCollections.observableArrayList();
		listUsersConnected.setItems(users);
		new ServerRunning().start();
		btnStartServer.setDisable(true);
		btnStopServer.setDisable(false);
	}

	public void stopServer() {
		if (server != null) {
			server.stop();
			btnStopServer.setDisable(true);
			btnStartServer.setDisable(false);
			listUsersConnected.setItems(null);
			server = null;
			return;
		}
	}

	/*
	 * A thread to run the Server
	 */
	class ServerRunning extends Thread {
		public void run() {
			server.start(); // should execute until if fails
			// the server failed
			appendEvent("Server Stopped\n");
			server = null;
			users = null;
		}
	}

	public void addUser(String user) {
		Platform.runLater(() -> {
			users.add(user);
		});
	}

	public void appendEvent(String string) {
		txtAreaEventLog.appendText(string);
	}

	public void appendRoom(String messageLf) {
		txtAreaChatMsg.appendText(messageLf);
	}

	public void remove(String username) {
		Platform.runLater(() -> {
			users.remove(username);
		});
	}
	public void exit(ActionEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide();
	}

}
