package com.bagamoyo.bms.controller;

import com.bagamoyo.bms.model.Supervisor;
import com.bagamoyo.bms.service.SupervisorService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import org.controlsfx.control.Notifications;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Modern supervisor management controller with full CRUD operations.
 * Uses SupervisorService for data access with modern form UI.
 */
public class SupervisorManagementController {

    private static final Logger logger = LoggerFactory.getLogger(SupervisorManagementController.class);

    // Form fields
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private DatePicker datePicker;
    @FXML private TextArea statusArea;

    // Table and data
    @FXML private TableView<Supervisor> supervisorTable;
    @FXML private TableColumn<Supervisor, String> usernameColumn;
    @FXML private TableColumn<Supervisor, String> firstNameColumn;
    @FXML private TableColumn<Supervisor, String> lastNameColumn;
    @FXML private TableColumn<Supervisor, String> emailColumn;
    @FXML private TableColumn<Supervisor, LocalDate> dateColumn;

    // Service layer
    private final SupervisorService supervisorService;

    // Selected supervisor for view/edit
    private Supervisor selectedSupervisor;

    public SupervisorManagementController() {
        this.supervisorService = new SupervisorService();
    }

    // ========================================================================
    // INITIALIZATION
    // ========================================================================

    @FXML
    public void initialize() {
        setupTableColumns();
        loadSupervisors();
        logger.info("SupervisorManagementController initialized");
    }

    private void setupTableColumns() {
        usernameColumn.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        emailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().registrationDateProperty());

        supervisorTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> selectedSupervisor = newSelection
        );
    }

    // ========================================================================
    // DATA LOADING
    // ========================================================================

    private void loadSupervisors() {
        Task<List<Supervisor>> task = new Task<>() {
            @Override
            protected List<Supervisor> call() {
                return supervisorService.getAllSupervisors();
            }
        };

        task.setOnSucceeded(e -> {
            List<Supervisor> supervisors = task.getValue();
            ObservableList<Supervisor> data = FXCollections.observableArrayList(supervisors);
            supervisorTable.setItems(data);
            logger.debug("Loaded {} supervisors", supervisors.size());
        });

        task.setOnFailed(e -> {
            logger.error("Failed to load supervisors", task.getException());
            showErrorNotification("Failed to load supervisors");
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    // ========================================================================
    // CREATE SUPERVISOR
    // ========================================================================

    @FXML
    private void handleCreateSupervisor() {
        if (!validateCreateForm()) {
            return;
        }

        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        LocalDate date = datePicker.getValue() != null ? datePicker.getValue() : LocalDate.now();

        Supervisor supervisor = new Supervisor(username, password, email, firstName, lastName, date);

        Task<Boolean> task = new Task<>() {
            @Override
            protected Boolean call() {
                return supervisorService.addSupervisor(supervisor);
            }
        };

        task.setOnSucceeded(e -> {
            boolean success = task.getValue();
            if (success) {
                showSuccessNotification("Supervisor Created",
                        "Supervisor '" + firstName + " " + lastName + "' has been created successfully");
                clearForm();
                loadSupervisors();
                statusArea.setText("Supervisor created successfully: " + username);
                logger.info("Supervisor created: {}", username);
            } else {
                showErrorNotification("Failed to create supervisor. Username may already exist.");
                statusArea.setText("Failed to create supervisor");
            }
        });

        task.setOnFailed(e -> {
            logger.error("Error creating supervisor", task.getException());
            showErrorNotification("Error creating supervisor");
            statusArea.setText("Error creating supervisor");
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    // ========================================================================
    // DELETE SUPERVISOR
    // ========================================================================

    @FXML
    private void handleDeleteSupervisor() {
        if (selectedSupervisor == null) {
            showWarningNotification("No Selection", "Please select a supervisor to delete");
            return;
        }

        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm Deletion");
        confirmDialog.setHeaderText("Delete Supervisor");
        confirmDialog.setContentText("Are you sure you want to delete '" + selectedSupervisor.getUsername() + "'?");

        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Task<Boolean> task = new Task<>() {
                @Override
                protected Boolean call() {
                    return supervisorService.deleteSupervisor(selectedSupervisor.getUsername());
                }
            };

            task.setOnSucceeded(e -> {
                boolean success = task.getValue();
                if (success) {
                    showSuccessNotification("Supervisor Deleted",
                            "Supervisor '" + selectedSupervisor.getUsername() + "' has been deleted");
                    clearForm();
                    loadSupervisors();
                    statusArea.setText("Supervisor deleted: " + selectedSupervisor.getUsername());
                    logger.info("Supervisor deleted: {}", selectedSupervisor.getUsername());
                } else {
                    showErrorNotification("Failed to delete supervisor");
                    statusArea.setText("Failed to delete supervisor");
                }
            });

            task.setOnFailed(e -> {
                logger.error("Error deleting supervisor", task.getException());
                showErrorNotification("Error deleting supervisor");
            });

            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();
        }
    }

    // ========================================================================
    // VIEW SUPERVISOR
    // ========================================================================

    @FXML
    private void handleViewSupervisor() {
        if (selectedSupervisor == null) {
            showWarningNotification("No Selection", "Please select a supervisor to view");
            return;
        }

        Supervisor sup = selectedSupervisor;
        StringBuilder details = new StringBuilder();
        details.append("Supervisor Details\n");
        details.append("==================\n\n");
        details.append("Username: ").append(sup.getUsername()).append("\n");
        details.append("First Name: ").append(sup.getFirstName()).append("\n");
        details.append("Last Name: ").append(sup.getLastName()).append("\n");
        details.append("Email: ").append(sup.getEmail()).append("\n");
        details.append("Registered: ").append(sup.getRegistrationDate() != null ? sup.getRegistrationDate().toString() : "N/A");

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Supervisor Details");
        alert.setHeaderText("Viewing: " + sup.getFirstName() + " " + sup.getLastName());
        alert.setContentText(details.toString());
        alert.getDialogPane().setPrefWidth(400);
        alert.showAndWait();

        logger.debug("Viewed supervisor: {}", sup.getUsername());
    }

    // ========================================================================
    // REFRESH
    // ========================================================================

    @FXML
    private void handleRefresh() {
        loadSupervisors();
        clearForm();
        showInfoNotification("Refreshed", "Supervisor list has been refreshed");
    }

    // ========================================================================
    // VALIDATION
    // ========================================================================

    private boolean validateCreateForm() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();

        if (username.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()) {
            showErrorNotification("Validation Error", "All fields are required");
            return false;
        }

        if (!isValidEmail(email)) {
            showErrorNotification("Validation Error", "Please enter a valid email address");
            return false;
        }

        if (username.contains(" ") || firstName.contains(" ") || lastName.contains(" ")) {
            showErrorNotification("Validation Error", "Name fields must not contain spaces");
            return false;
        }

        if (password.length() < 4) {
            showErrorNotification("Validation Error", "Password must be at least 4 characters");
            return false;
        }

        return true;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "[a-zA-Z0-9][a-zA-Z0-9._]*@[a-zA-Z0-9]+([.][a-zA-Z]+)+";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // ========================================================================
    // UTILITY
    // ========================================================================

    private void clearForm() {
        usernameField.clear();
        passwordField.clear();
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
        datePicker.setValue(null);
    }

    private void showSuccessNotification(String title, String message) {
        Notifications.create()
                .title(title)
                .text(message)
                .hideAfter(javafx.util.Duration.seconds(3))
                .position(Pos.TOP_RIGHT)
                .showInformation();
    }

    private void showErrorNotification(String message) {
        showErrorNotification("Error", message);
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
