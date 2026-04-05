package com.bagamoyo.bms.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.textfield.TextFields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Modern chat client controller using ScheduledExecutorService.
 * Handles connection to chat server, login, messaging, and user list.
 */
public class ChatClientController {

    private static final Logger logger = LoggerFactory.getLogger(ChatClientController.class);

    // UI Components
    @FXML private Button loginBtn;
    @FXML private Button logoutBtn;
    @FXML private TextArea serverMsgArea;
    @FXML private TextField hostIpField;
    @FXML private TextField usernameField;
    @FXML private TextField messageField;
    @FXML private ListView<String> userList;
    @FXML private TextField serverPortField;

    // Connection state
    private boolean connected;
    private String serverHost;
    private String username;
    private int serverPort;

    // Network streams
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket socket;

    // User list
    private final ObservableList<String> users = FXCollections.observableArrayList();

    // Scheduler for background tasks
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

    // Predefined hosts for auto-completion
    private static final String[] COMMON_HOSTS = {
            "localhost", "127.0.0.1", "192.168.1.1", "192.168.0.1"
    };

    private static final String[] COMMON_PORTS = {
            "1025", "1026", "1027", "1028", "1600", "53769", "53770"
    };

    // ========================================================================
    // INITIALIZATION
    // ========================================================================

    @FXML
    public void initialize() {
        setupAutoCompletion();
        setupUserList();
        updateButtonStates(false);
        logger.info("ChatClientController initialized");
    }

    private void setupAutoCompletion() {
        if (hostIpField != null) {
            TextFields.bindAutoCompletion(hostIpField, COMMON_HOSTS);
        }
        if (serverPortField != null) {
            TextFields.bindAutoCompletion(serverPortField, COMMON_PORTS);
        }
    }

    private void setupUserList() {
        if (userList != null) {
            userList.setItems(users);
        }
    }

    // ========================================================================
    // LOGIN
    // ========================================================================

    @FXML
    private void handleLogin() {
        String host = hostIpField != null ? hostIpField.getText().trim() : "";
        String user = usernameField != null ? usernameField.getText().trim() : "";
        String portText = serverPortField != null ? serverPortField.getText().trim() : "";

        if (!validateLogin(host, user, portText)) {
            return;
        }

        serverHost = host;
        username = user;
        serverPort = Integer.parseInt(portText);

        Task<Boolean> connectTask = new Task<>() {
            @Override
            protected Boolean call() {
                return establishConnection();
            }
        };

        connectTask.setOnSucceeded(e -> {
            boolean success = connectTask.getValue();
            if (success) {
                connected = true;
                updateButtonStates(true);
                showSuccessNotification("Connected", "Logged in as " + username);
                appendServerMessage("Connected to " + serverHost + ":" + serverPort);
                logger.info("Client logged in: {}@{}:{}", username, serverHost, serverPort);
            } else {
                showErrorNotification("Connection Failed", "Could not connect to server");
            }
        });

        connectTask.setOnFailed(e -> {
            logger.error("Connection failed", connectTask.getException());
            showErrorNotification("Connection Error", connectTask.getException().getMessage());
        });

        scheduler.submit(connectTask);
    }

    private boolean establishConnection() {
        try {
            socket = new Socket(serverHost, serverPort);

            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());

            // Send username
            output.writeObject(username);
            output.flush();

            // Start listening for server messages
            scheduler.execute(this::listenForMessages);

            return true;
        } catch (IOException e) {
            logger.error("Failed to connect to server", e);
            return false;
        }
    }

    private boolean validateLogin(String host, String user, String port) {
        if (host.isEmpty()) {
            showErrorNotification("Validation Error", "Server IP is required");
            return false;
        }

        if (user.isEmpty()) {
            showErrorNotification("Validation Error", "Username is required");
            return false;
        }

        if (port.isEmpty()) {
            showErrorNotification("Validation Error", "Server port is required");
            return false;
        }

        try {
            int p = Integer.parseInt(port);
            if (p < 1024 || p > 65535) {
                showErrorNotification("Invalid Port", "Port must be between 1024 and 65535");
                return false;
            }
        } catch (NumberFormatException e) {
            showErrorNotification("Invalid Port", "Please enter a valid port number");
            return false;
        }

        return true;
    }

    // ========================================================================
    // LOGOUT
    // ========================================================================

    @FXML
    private void handleLogout() {
        if (!connected) return;

        try {
            ChatMessage msg = new ChatMessage(ChatMessage.LOGOUT, "");
            output.writeObject(msg);
            output.flush();
        } catch (IOException e) {
            logger.error("Error sending logout message", e);
        }

        disconnect();
        showInfoNotification("Logged Out", "Disconnected from server");
        logger.info("Client logged out: {}", username);
    }

    // ========================================================================
    // SEND MESSAGE
    // ========================================================================

    @FXML
    private void handleSendMessage() {
        if (!connected) {
            showWarningNotification("Not Connected", "Please connect to the server first");
            return;
        }

        String message = messageField != null ? messageField.getText() : "";
        if (message.isEmpty()) {
            return;
        }

        Task<Void> sendTask = new Task<>() {
            @Override
            protected Void call() throws IOException {
                ChatMessage msg = new ChatMessage(ChatMessage.MESSAGE, message);
                output.writeObject(msg);
                output.flush();
                return null;
            }
        };

        sendTask.setOnFailed(e -> {
            logger.error("Error sending message", sendTask.getException());
            appendServerMessage("Error sending message: " + sendTask.getException().getMessage());
        });

        scheduler.submit(sendTask);

        messageField.clear();
    }

    @FXML
    private void handleEnterPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER && !event.isShiftDown()) {
            handleSendMessage();
            event.consume();
        }
    }

    // ========================================================================
    // LISTEN FOR MESSAGES
    // ========================================================================

    private void listenForMessages() {
        users.clear();

        while (connected) {
            try {
                String msg = (String) input.readObject();
                String[] parts = msg.split(":", 2);

                if (parts.length == 2) {
                    String sender = parts[0];
                    String action = parts[1];

                    switch (action) {
                        case "WHOISIN":
                            final String userName = sender;
                            Platform.runLater(() -> {
                                if (!users.contains(userName)) {
                                    users.add(userName);
                                }
                            });
                            break;
                        case "REMOVE":
                            final String removeUser = sender;
                            Platform.runLater(() -> users.remove(removeUser));
                            break;
                        default:
                            final String message = msg;
                            Platform.runLater(() -> appendServerMessage(message));
                            break;
                    }
                } else {
                    final String message = msg;
                    Platform.runLater(() -> appendServerMessage(message));
                }

            } catch (IOException e) {
                appendServerMessage("Server connection lost");
                connectionLost();
                break;
            } catch (ClassNotFoundException e) {
                logger.error("Unexpected object type from server", e);
                break;
            }
        }
    }

    private void appendServerMessage(String message) {
        Platform.runLater(() -> {
            if (serverMsgArea != null) {
                serverMsgArea.appendText(message + "\n");
            }
        });
    }

    // ========================================================================
    // DISCONNECT
    // ========================================================================

    private void disconnect() {
        connected = false;
        updateButtonStates(false);

        try {
            if (input != null) input.close();
            if (output != null) output.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            logger.error("Error during disconnect", e);
        }

        Platform.runLater(() -> users.clear());
    }

    private void connectionLost() {
        Platform.runLater(() -> {
            connected = false;
            updateButtonStates(false);
            users.clear();
            showErrorNotification("Connection Lost", "The server connection was lost");
        });
    }

    private void updateButtonStates(boolean loggedIn) {
        if (loginBtn != null) loginBtn.setDisable(loggedIn);
        if (logoutBtn != null) logoutBtn.setDisable(!loggedIn);
        if (usernameField != null) usernameField.setEditable(!loggedIn);
        if (hostIpField != null) hostIpField.setEditable(!loggedIn);
        if (serverPortField != null) serverPortField.setEditable(!loggedIn);
    }

    // ========================================================================
    // SHUTDOWN
    // ========================================================================

    public void shutdown() {
        if (connected) {
            handleLogout();
        }
        scheduler.shutdownNow();
        logger.info("ChatClientController shutdown complete");
    }

    // ========================================================================
    // NOTIFICATIONS
    // ========================================================================

    private void showSuccessNotification(String title, String message) {
        Notifications.create()
                .title(title)
                .text(message)
                .hideAfter(javafx.util.Duration.seconds(3))
                .position(Pos.TOP_RIGHT)
                .showInformation();
    }

    private void showErrorNotification(String title, String message) {
        Notifications.create()
                .title(title)
                .text(message)
                .hideAfter(javafx.util.Duration.seconds(5))
                .position(Pos.TOP_RIGHT)
                .showError();
    }

    private void showWarningNotification(String title, String message) {
        Notifications.create()
                .title(title)
                .text(message)
                .hideAfter(javafx.util.Duration.seconds(3))
                .position(Pos.TOP_RIGHT)
                .showWarning();
    }

    private void showInfoNotification(String title, String message) {
        Notifications.create()
                .title(title)
                .text(message)
                .hideAfter(javafx.util.Duration.seconds(2))
                .position(Pos.TOP_RIGHT)
                .showInformation();
    }
}
