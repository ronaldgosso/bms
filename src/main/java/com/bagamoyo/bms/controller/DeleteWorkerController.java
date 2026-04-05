package com.bagamoyo.bms.controller;

import com.bagamoyo.bms.model.Worker;
import com.bagamoyo.bms.service.WorkerService;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.textfield.TextFields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * Delete Worker Controller for the Bagamoyo District Council Management System.
 * Allows searching for a worker by check number and deleting the record with confirmation.
 */
public class DeleteWorkerController {

    private static final Logger logger = LoggerFactory.getLogger(DeleteWorkerController.class);

    @FXML
    private JFXTextField txtCheckNo;

    @FXML
    private JFXTextField txtWorkerName;

    @FXML
    private Button btnSearch;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnClear;

    @FXML
    private Button btnCancel;

    @FXML
    private Label lblStatus;

    private final WorkerService workerService;
    private ObservableList<String> checkNoList;
    private Worker currentWorker;

    public DeleteWorkerController() {
        this.workerService = new WorkerService();
    }

    @FXML
    public void initialize() {
        loadCheckNoSuggestions();
        setupAutoCompletion();
        setupSearchOnEnter();
        logger.info("Delete Worker form initialized");
    }

    /**
     * Load all check numbers for auto-completion.
     */
    private void loadCheckNoSuggestions() {
        try {
            List<Worker> workers = workerService.getAllWorkers();
            checkNoList = FXCollections.observableArrayList(
                    workers.stream().map(Worker::getCheck).toList()
            );
            logger.debug("Loaded {} check numbers for auto-completion", checkNoList.size());
        } catch (Exception e) {
            logger.error("Failed to load check numbers", e);
        }
    }

    /**
     * Setup auto-completion for the check number field.
     */
    private void setupAutoCompletion() {
        if (txtCheckNo != null && checkNoList != null) {
            TextFields.bindAutoCompletion(txtCheckNo, checkNoList);
        }
    }

    /**
     * Setup search on Enter key press.
     */
    private void setupSearchOnEnter() {
        if (txtCheckNo != null) {
            txtCheckNo.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    handleSearch(null);
                }
            });
        }
    }

    /**
     * Handle Search button click - find worker by check number.
     */
    @FXML
    public void handleSearch(ActionEvent event) {
        String checkNo = txtCheckNo != null ? txtCheckNo.getText().trim() : "";

        if (checkNo.isBlank()) {
            showNotification("Validation Error", "Please enter a Check Number to search", NotificationType.ERROR);
            return;
        }

        Optional<Worker> workerOpt = workerService.getWorkerById(checkNo);

        if (workerOpt.isPresent()) {
            currentWorker = workerOpt.get();
            String fullName = currentWorker.getFirstName() + " " +
                    currentWorker.getMiddleName() + " " +
                    currentWorker.getLastName();

            if (txtWorkerName != null) {
                txtWorkerName.setText(fullName);
            }

            logger.info("Worker found for deletion: {}", fullName);
        } else {
            showNotification("Not Found", "No worker found with Check No: " + checkNo, NotificationType.ERROR);
            if (txtWorkerName != null) {
                txtWorkerName.clear();
            }
            currentWorker = null;
        }
    }

    /**
     * Handle Delete button click.
     */
    @FXML
    public void handleDelete(ActionEvent event) {
        String checkNo = txtCheckNo != null ? txtCheckNo.getText().trim() : "";
        String workerName = txtWorkerName != null ? txtWorkerName.getText().trim() : "";

        if (!validateInputs(checkNo, workerName)) {
            return;
        }

        boolean confirmed = showConfirmation("Delete Worker",
                "Are you sure you want to delete worker:\n\n" +
                        "Check No: " + checkNo + "\n" +
                        "Name: " + workerName + "\n\n" +
                        "This action cannot be undone.");

        if (!confirmed) {
            return;
        }

        boolean success = workerService.deleteWorker(checkNo);

        if (success) {
            showNotification("Success", "Individual '" + workerName + "' deleted successfully", NotificationType.INFORMATION);
            if (lblStatus != null) {
                lblStatus.setText("Worker Deleted Successfully");
                lblStatus.setStyle("-fx-text-fill: green;");
            }
            clearForm();
            loadCheckNoSuggestions();
            logger.info("Worker deleted: checkNo={}, name={}", checkNo, workerName);
        } else {
            showNotification("Error", "Failed to delete individual", NotificationType.ERROR);
            if (lblStatus != null) {
                lblStatus.setText("Worker Not Deleted");
                lblStatus.setStyle("-fx-text-fill: red;");
            }
            logger.warn("Failed to delete worker: {}", checkNo);
        }
    }

    /**
     * Handle Clear button click.
     */
    @FXML
    public void handleClear(ActionEvent event) {
        clearForm();
    }

    /**
     * Handle Cancel button click - close the window.
     */
    @FXML
    public void handleCancel(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        logger.debug("Delete Worker form closed");
    }

    /**
     * Validate inputs for deletion.
     */
    private boolean validateInputs(String checkNo, String workerName) {
        if (checkNo.isBlank()) {
            showNotification("Validation Error", "Check Number is required", NotificationType.ERROR);
            return false;
        }

        if (checkNo.contains(" ")) {
            showNotification("Validation Error", "Check Number cannot contain spaces", NotificationType.ERROR);
            return false;
        }

        if (workerName.isBlank()) {
            showNotification("Validation Error", "You cannot delete a null person. Search first.", NotificationType.ERROR);
            return false;
        }

        if (workerName.contains("  ")) {
            showNotification("Validation Error", "Name cannot contain double spaces", NotificationType.ERROR);
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
        try {
            stage.getIcons().add(new javafx.scene.image.Image(
                    getClass().getResourceAsStream("/img/baga.png")));
        } catch (Exception e) {
            logger.debug("Could not load window icon", e);
        }

        return alert.showAndWait().filter(response -> response == ButtonType.OK).isPresent();
    }

    /**
     * Clear all form fields.
     */
    private void clearForm() {
        if (txtCheckNo != null) txtCheckNo.clear();
        if (txtWorkerName != null) txtWorkerName.clear();
        if (lblStatus != null) {
            lblStatus.setText("");
        }
        currentWorker = null;
    }

    /**
     * Refresh the check number suggestions.
     */
    @FXML
    public void handleRefresh(ActionEvent event) {
        loadCheckNoSuggestions();
        setupAutoCompletion();
        clearForm();
        showNotification("Refreshed", "Data refreshed", NotificationType.INFORMATION);
        logger.debug("Delete Worker form refreshed");
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
            case ERROR:
                notificationBuilder.showError();
                break;
            case WARNING:
                notificationBuilder.showWarning();
                break;
            case INFORMATION:
                notificationBuilder.showInformation();
                break;
            default:
                notificationBuilder.show();
                break;
        }
    }

    /**
     * Enum for notification types.
     */
    public enum NotificationType {
        INFORMATION, WARNING, ERROR
    }
}
