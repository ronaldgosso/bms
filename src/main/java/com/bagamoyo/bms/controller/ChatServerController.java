package com.bagamoyo.bms.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.textfield.TextFields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Modern chat server controller using ScheduledExecutorService instead of raw threads.
 * Manages client connections, message broadcasting, and server lifecycle.
 */
public class ChatServerController {

    private static final Logger logger = LoggerFactory.getLogger(ChatServerController.class);

    // UI Components
    @FXML private TextArea chatMsgArea;
    @FXML private TextArea eventLogArea;
    @FXML private ListView<String> connectedUsersList;
    @FXML private Button startServerBtn;
    @FXML private Button stopServerBtn;
    @FXML private TextField portField;

    // Server state
    private ServerSocket serverSocket;
    private volatile boolean keepGoing;
    private final List<ClientHandler> connectedClients = new ArrayList<>();
    private final ObservableList<String> connectedUsers = FXCollections.observableArrayList();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4);

    private int port;
    private final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

    // Predefined ports for auto-completion
    private static final String[] COMMON_PORTS = {
            "1025", "1026", "1027", "1028", "53769", "53770", "53771", "53772",
            "53773", "53774", "53775", "53776", "53777", "53779", "53780",
            "53781", "53782", "53783", "53784", "53785", "53786", "53787",
            "53788", "53789", "53790", "53778", "1600"
    };

    // ========================================================================
    // INITIALIZATION
    // ========================================================================

    @FXML
    public void initialize() {
        setupAutoCompletion();
        setupUserList();
        updateButtonStates(false);
        logger.info("ChatServerController initialized");
    }

    private void setupAutoCompletion() {
        if (portField != null) {
            TextFields.bindAutoCompletion(portField, COMMON_PORTS);
        }
    }

    private void setupUserList() {
        if (connectedUsersList != null) {
            connectedUsersList.setItems(connectedUsers);
        }
    }

    // ========================================================================
    // SERVER LIFECYCLE
    // ========================================================================

    @FXML
    private void handleStartServer() {
        String portText = portField != null ? portField.getText().trim() : "";

        if (portText.isEmpty()) {
            showWarningNotification("Port Required", "Please enter a port number to start the server");
            return;
        }

        try {
            port = Integer.parseInt(portText);
        } catch (NumberFormatException e) {
            showErrorNotification("Invalid Port", "Please enter a valid port number");
            return;
        }

        if (port < 1024 || port > 65535) {
            showErrorNotification("Invalid Port", "Port must be between 1024 and 65535");
            return;
        }

        keepGoing = true;
        updateButtonStates(true);

        // Start server in background thread
        scheduler.execute(this::runServer);

        appendEvent("Server starting on port " + port + "...\n");
        showSuccessNotification("Server Starting", "Chat server is starting on port " + port);
        logger.info("Server starting on port {}", port);
    }

    private void runServer() {
        try {
            serverSocket = new ServerSocket(port);
            appendEvent("Server is listening on port " + port + "\n");

            while (keepGoing) {
                try {
                    Socket socket = serverSocket.accept();
                    if (!keepGoing) break;

                    ClientHandler client = new ClientHandler(socket);
                    synchronized (connectedClients) {
                        connectedClients.add(client);
                    }
                    client.start();

                } catch (IOException e) {
                    if (keepGoing) {
                        appendEvent("Error accepting connection: " + e.getMessage() + "\n");
                    }
                }
            }
        } catch (IOException e) {
            appendEvent("Failed to start server: " + e.getMessage() + "\n");
            logger.error("Failed to start server", e);
            Platform.runLater(() -> updateButtonStates(false));
        } finally {
            cleanup();
        }
    }

    @FXML
    private void handleStopServer() {
        keepGoing = false;
        appendEvent("Stopping server...\n");

        // Close all client connections
        synchronized (connectedClients) {
            for (ClientHandler client : connectedClients) {
                client.close();
            }
            connectedClients.clear();
        }

        // Close server socket
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            logger.error("Error closing server socket", e);
        }

        showInfoNotification("Server Stopped", "Chat server has been stopped");
        logger.info("Server stopped");
    }

    private void cleanup() {
        Platform.runLater(() -> {
            updateButtonStates(false);
            connectedUsers.clear();
            appendEvent("Server stopped\n");
        });
    }

    // ========================================================================
    // CLIENT HANDLER
    // ========================================================================

    private class ClientHandler {
        private final Socket socket;
        private ObjectInputStream input;
        private ObjectOutputStream output;
        private String username;
        private final int id;
        private static int idCounter = 0;

        ClientHandler(Socket socket) {
            this.socket = socket;
            this.id = ++idCounter;
        }

        void start() {
            scheduler.execute(this::run);
        }

        private void run() {
            try {
                // Create streams
                output = new ObjectOutputStream(socket.getOutputStream());
                input = new ObjectInputStream(socket.getInputStream());

                // Read username
                username = (String) input.readObject();

                // Add to user list
                Platform.runLater(() -> connectedUsers.add(username));
                appendEvent(username + " connected\n");

                // Broadcast new user
                broadcast(username + ":WHOISIN");

                // Send existing users to new client
                synchronized (connectedClients) {
                    for (ClientHandler client : connectedClients) {
                        if (client.username != null && !client.username.equals(username)) {
                            writeMessage(client.username + ":WHOISIN");
                        }
                    }
                }

                // Listen for messages
                boolean listening = true;
                while (listening && keepGoing) {
                    try {
                        ChatMessage msg = (ChatMessage) input.readObject();

                        switch (msg.getType()) {
                            case ChatMessage.MESSAGE:
                                broadcast(username + ": " + msg.getMessage());
                                break;
                            case ChatMessage.LOGOUT:
                                appendEvent(username + " disconnected (logout)\n");
                                broadcast(username + ":REMOVE");
                                listening = false;
                                break;
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        if (keepGoing) {
                            appendEvent(username + " disconnected unexpectedly\n");
                        }
                        listening = false;
                    }
                }

            } catch (IOException | ClassNotFoundException e) {
                appendEvent("Error handling client: " + e.getMessage() + "\n");
                logger.error("Error handling client", e);
            } finally {
                removeClient(this);
                close();
            }
        }

        boolean writeMessage(String msg) {
            if (!socket.isConnected()) {
                return false;
            }
            try {
                output.writeObject(msg);
                output.flush();
                return true;
            } catch (IOException e) {
                return false;
            }
        }

        void close() {
            try {
                if (output != null) output.close();
                if (input != null) input.close();
                if (socket != null && !socket.isClosed()) socket.close();
            } catch (IOException e) {
                logger.error("Error closing client connection", e);
            }
        }
    }

    // ========================================================================
    // BROADCAST & MANAGEMENT
    // ========================================================================

    private synchronized void broadcast(String message) {
        String timestamp = sdf.format(new Date());
        String formattedMessage;

        if (message.contains("WHOISIN") || message.contains("REMOVE")) {
            formattedMessage = message;
        } else {
            formattedMessage = timestamp + " " + message + "\n";
            final String msg = formattedMessage;
            Platform.runLater(() -> {
                if (chatMsgArea != null) {
                    chatMsgArea.appendText(msg);
                }
            });
        }

        synchronized (connectedClients) {
            List<ClientHandler> toRemove = new ArrayList<>();
            for (ClientHandler client : connectedClients) {
                if (!client.writeMessage(formattedMessage)) {
                    toRemove.add(client);
                }
            }
            for (ClientHandler client : toRemove) {
                removeClient(client);
            }
        }
    }

    private void removeClient(ClientHandler client) {
        synchronized (connectedClients) {
            connectedClients.remove(client);
        }
        if (client.username != null) {
            Platform.runLater(() -> connectedUsers.remove(client.username));
        }
    }

    // ========================================================================
    // UI UPDATES
    // ========================================================================

    private void appendEvent(String message) {
        String timestamp = sdf.format(new Date()) + " " + message;
        final String msg = timestamp;
        Platform.runLater(() -> {
            if (eventLogArea != null) {
                eventLogArea.appendText(msg);
            }
        });
    }

    private void updateButtonStates(boolean running) {
        if (startServerBtn != null) startServerBtn.setDisable(running);
        if (stopServerBtn != null) stopServerBtn.setDisable(!running);
    }

    // ========================================================================
    // SHUTDOWN HOOK
    // ========================================================================

    public void shutdown() {
        keepGoing = false;
        synchronized (connectedClients) {
            for (ClientHandler client : connectedClients) {
                client.close();
            }
            connectedClients.clear();
        }
        scheduler.shutdownNow();
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            logger.error("Error during shutdown", e);
        }
        logger.info("ChatServerController shutdown complete");
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
