package com.bagamoyo.bms.controller;

import com.bagamoyo.bms.service.AdminService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.textfield.TextFields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * System Administrator Dashboard Controller for the Bagamoyo District Council Management System.
 * Handles creation/deletion of admin accounts and displays system statistics.
 */
public class SystemAdminDashboardController {

    private static final Logger logger = LoggerFactory.getLogger(SystemAdminDashboardController.class);

    @FXML
    private TextField txtCreateUsername;

    @FXML
    private PasswordField txtCreatePassword;

    @FXML
    private PasswordField txtVerifyPassword;

    @FXML
    private Button btnCreateAdmin;

    @FXML
    private TextField txtDeleteUsername;

    @FXML
    private Button btnDeleteAdmin;

    @FXML
    private TableView<String> adminTableView;

    @FXML
    private TableColumn<String, String> colUsername;

    @FXML
    private TextField txtSearchAdmin;

    @FXML
    private Label lblTotalAdmins;

    @FXML
    private Label lblStatus;

    @FXML
    private Button btnRefresh;

    @FXML
    private Button btnLogout;

    private final AdminService adminService;
    private ObservableList<String> adminList;
    private FilteredList<String> filteredAdmins;

    public SystemAdminDashboardController() {
        this.adminService = new AdminService();
    }

    @FXML
    public void initialize() {
        loadAdminTable();
        setupAutoCompletion();
        setupSearchFilter();
        updateStats();
        logger.info("System Admin dashboard initialized");
    }

    /**
     * Load all admin usernames into the TableView.
     */
    private void loadAdminTable() {
        try {
            List<String> usernames = adminService.getAllAdminUsernames();
            adminList = FXCollections.observableArrayList(usernames);
            filteredAdmins = new FilteredList<>(adminList, p -> true);

            colUsername.setCellValueFactory(param ->
                    new javafx.beans.property.SimpleStringProperty(param.getValue()));

            adminTableView.setItems(filteredAdmins);
            adminTableView.setEditable(false);

            logger.debug("Loaded {} admin accounts into table", usernames.size());
        } catch (Exception e) {
            logger.error("Failed to load admin table", e);
            showNotification("Error", "Failed to load admin accounts", NotificationType.ERROR);
        }
    }

    /**
     * Setup auto-completion for the delete username field.
     */
    private void setupAutoCompletion() {
        try {
            List<String> usernames = adminService.getAllAdminUsernames();
            TextFields.bindAutoCompletion(txtDeleteUsername, usernames);
        } catch (Exception e) {
            logger.error("Failed to setup auto-completion", e);
        }
    }

    /**
     * Setup search filter for the admin table.
     */
    private void setupSearchFilter() {
        if (txtSearchAdmin != null) {
            txtSearchAdmin.textProperty().addListener((observable, oldValue, newValue) -> {
                if (filteredAdmins != null) {
                    filteredAdmins.setPredicate(username -> {
                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }
                        return username.toLowerCase().contains(newValue.toLowerCase());
                    });
                }
            });
        }
    }

    /**
     * Update system statistics labels.
     */
    private void updateStats() {
        try {
            long totalAdmins = adminService.countAdmins();
            if (lblTotalAdmins != null) {
                lblTotalAdmins.setText(String.valueOf(totalAdmins));
            }
        } catch (Exception e) {
            logger.error("Failed to update statistics", e);
        }
    }

    /**
     * Create a new admin account.
     */
    @FXML
    public void handleCreateAdmin(ActionEvent event) {
        String username = txtCreateUsername.getText();
        String password = txtCreatePassword.getText();
        String verify = txtVerifyPassword.getText();

        if (!validateCreateInputs(username, password, verify)) {
            return;
        }

        boolean success = adminService.createAdmin(username, password);
        if (success) {
            showNotification("Success", "System Administrator '" + username + "' created successfully", NotificationType.INFORMATION);
            if (lblStatus != null) {
                lblStatus.setText("User Added Successfully");
                lblStatus.setStyle("-fx-text-fill: green;");
            }
            clearCreateFields();
            loadAdminTable();
            updateStats();
            setupAutoCompletion();
            logger.info("New admin account created: {}", username);
        } else {
            showNotification("Error", "Username '" + username + "' already exists", NotificationType.ERROR);
            if (lblStatus != null) {
                lblStatus.setText("User Not Added - Username Exists");
                lblStatus.setStyle("-fx-text-fill: red;");
            }
            logger.warn("Failed to create admin - username exists: {}", username);
        }
    }

    /**
     * Delete an admin account.
     */
    @FXML
    public void handleDeleteAdmin(ActionEvent event) {
        String username = txtDeleteUsername.getText();

        if (!validateDeleteInput(username)) {
            return;
        }

        boolean confirmed = showConfirmation("Delete Admin",
                "Are you sure you want to delete admin account '" + username + "'?");

        if (!confirmed) {
            return;
        }

        boolean success = adminService.deleteAdmin(username);
        if (success) {
            showNotification("Success", "System Administrator '" + username + "' deleted successfully", NotificationType.INFORMATION);
            if (lblStatus != null) {
                lblStatus.setText("User Deleted Successfully");
                lblStatus.setStyle("-fx-text-fill: green;");
            }
            txtDeleteUsername.clear();
            loadAdminTable();
            updateStats();
            setupAutoCompletion();
            logger.info("Admin account deleted: {}", username);
        } else {
            showNotification("Error", "Failed to delete admin '" + username + "'", NotificationType.ERROR);
            if (lblStatus != null) {
                lblStatus.setText("User Not Deleted");
                lblStatus.setStyle("-fx-text-fill: red;");
            }
        }
    }

    /**
     * Refresh the dashboard data.
     */
    @FXML
    public void handleRefresh(ActionEvent event) {
        loadAdminTable();
        updateStats();
        setupAutoCompletion();
        clearAllFields();
        showNotification("Refreshed", "Dashboard data refreshed", NotificationType.INFORMATION);
        logger.debug("Dashboard refreshed");
    }

    /**
     * Handle logout - return to home screen.
     */
    @FXML
    public void handleLogout(ActionEvent event) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/Home.fxml")));
            stage.setTitle("Welcome to Bagamoyo District Council Management System");
            stage.setScene(new Scene(root));
            stage.show();
            logger.info("System Admin logged out successfully");
        } catch (IOException e) {
            logger.error("Failed to navigate to Home screen", e);
            showNotification("Error", "Failed to return to home screen", NotificationType.ERROR);
        }
    }

    /**
     * Validate inputs for creating an admin.
     */
    private boolean validateCreateInputs(String username, String password, String verify) {
        if (username == null || username.isBlank()) {
            showNotification("Validation Error", "Username cannot be empty", NotificationType.ERROR);
            return false;
        }

        if (username.contains(" ")) {
            showNotification("Validation Error", "Username cannot contain spaces", NotificationType.ERROR);
            return false;
        }

        if (password == null || password.isBlank()) {
            showNotification("Validation Error", "Password cannot be empty", NotificationType.ERROR);
            return false;
        }

        if (!password.equals(verify)) {
            showNotification("Validation Error", "Passwords do not match", NotificationType.ERROR);
            txtCreatePassword.clear();
            txtVerifyPassword.clear();
            return false;
        }

        return true;
    }

    /**
     * Validate input for deleting an admin.
     */
    private boolean validateDeleteInput(String username) {
        if (username == null || username.isBlank()) {
            showNotification("Validation Error", "You cannot delete a null person", NotificationType.ERROR);
            return false;
        }

        if (username.contains(" ")) {
            showNotification("Validation Error", "Username cannot contain spaces", NotificationType.ERROR);
            return false;
        }

        return true;
    }

    /**
     * Show a confirmation dialog.
     */
    private boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new javafx.scene.image.Image(
                Objects.requireNonNull(getClass().getResourceAsStream("/img/baga.png"))));

        return alert.showAndWait().filter(response -> response == ButtonType.OK).isPresent();
    }

    /**
     * Clear create form fields.
     */
    private void clearCreateFields() {
        txtCreateUsername.clear();
        txtCreatePassword.clear();
        txtVerifyPassword.clear();
    }

    /**
     * Clear all input fields.
     */
    private void clearAllFields() {
        clearCreateFields();
        txtDeleteUsername.clear();
        if (txtSearchAdmin != null) {
            txtSearchAdmin.clear();
        }
        if (lblStatus != null) {
            lblStatus.setText("");
        }
    }

    /**
     * Show a ControlsFX notification.
     */
    private void showNotification(String title, String message, NotificationType type) {
        Notifications notificationBuilder = Notifications.create()
                .title(title)
                .text(message)
                .hideAfter(javafx.util.Duration.seconds(3));

        switch (type) {
            case ERROR -> notificationBuilder.showError();
            case WARNING -> notificationBuilder.showWarning();
            case INFORMATION -> notificationBuilder.showInformation();
            default -> notificationBuilder.show();
        }
    }

    /**
     * Enum for notification types.
     */
    public enum NotificationType {
        INFORMATION, WARNING, ERROR
    }
}
