package com.bagamoyo.bms.controller;

import com.bagamoyo.bms.model.Department;
import com.bagamoyo.bms.service.DepartmentService;
import com.bagamoyo.bms.service.SupervisorService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.textfield.TextFields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Modern department management controller with full CRUD operations.
 * Uses DepartmentService for data access and JFoenix/ControlsFX for modern UI.
 */
public class DepartmentManagementController {

    private static final Logger logger = LoggerFactory.getLogger(DepartmentManagementController.class);

    // Form fields
    @FXML private TextField nameField;
    @FXML private TextField serialField;
    @FXML private TextField supervisorField;
    @FXML private DatePicker datePicker;
    @FXML private TextArea statusArea;

    // Table and data
    @FXML private TableView<Department> departmentTable;
    @FXML private TableColumn<Department, String> nameColumn;
    @FXML private TableColumn<Department, String> serialColumn;
    @FXML private TableColumn<Department, String> supervisorColumn;
    @FXML private TableColumn<Department, LocalDate> dateColumn;

    // Service layer
    private final DepartmentService departmentService;
    private final SupervisorService supervisorService;

    // Auto-complete data
    private final ObservableList<String> supervisorNames = FXCollections.observableArrayList();

    // Selected department for view/edit
    private Department selectedDepartment;

    public DepartmentManagementController() {
        this.departmentService = new DepartmentService();
        this.supervisorService = new SupervisorService();
    }

    // ========================================================================
    // INITIALIZATION
    // ========================================================================

    @FXML
    public void initialize() {
        setupTableColumns();
        setupAutoCompletion();
        loadDepartments();
        loadSupervisorNames();
        logger.info("DepartmentManagementController initialized");
    }

    private void setupTableColumns() {
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        serialColumn.setCellValueFactory(cellData -> cellData.getValue().serialProperty());
        supervisorColumn.setCellValueFactory(cellData -> cellData.getValue().supervisorProperty());
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().createDateProperty());

        departmentTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> selectedDepartment = newSelection
        );
    }

    private void setupAutoCompletion() {
        TextFields.bindAutoCompletion(supervisorField, supervisorNames);
    }

    // ========================================================================
    // DATA LOADING
    // ========================================================================

    private void loadDepartments() {
        Task<List<Department>> task = new Task<>() {
            @Override
            protected List<Department> call() {
                return departmentService.getAllDepartments();
            }
        };

        task.setOnSucceeded(e -> {
            List<Department> departments = task.getValue();
            ObservableList<Department> data = FXCollections.observableArrayList(departments);
            departmentTable.setItems(data);
            logger.debug("Loaded {} departments", departments.size());
        });

        task.setOnFailed(e -> {
            logger.error("Failed to load departments", task.getException());
            showErrorNotification("Failed to load departments");
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private void loadSupervisorNames() {
        Task<List<String>> task = new Task<>() {
            @Override
            protected List<String> call() {
                return supervisorService.getAllSupervisors().stream()
                        .map(s -> s.getFirstName() + " " + s.getLastName())
                        .collect(Collectors.toList());
            }
        };

        task.setOnSucceeded(e -> {
            supervisorNames.setAll(task.getValue());
            logger.debug("Loaded {} supervisor names", supervisorNames.size());
        });

        task.setOnFailed(e -> logger.error("Failed to load supervisor names", task.getException()));

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    // ========================================================================
    // CREATE DEPARTMENT
    // ========================================================================

    @FXML
    private void handleCreateDepartment() {
        if (!validateCreateForm()) {
            return;
        }

        String name = nameField.getText().trim();
        String serial = serialField.getText().trim();
        String supervisor = supervisorField.getText().trim();
        LocalDate date = datePicker.getValue();

        Department department = new Department(name, serial, supervisor, date, "");

        Task<Boolean> task = new Task<>() {
            @Override
            protected Boolean call() {
                return departmentService.addDepartment(department);
            }
        };

        task.setOnSucceeded(e -> {
            boolean success = task.getValue();
            if (success) {
                showSuccessNotification("Department Created", "Department '" + name + "' has been created successfully");
                clearForm();
                loadDepartments();
                statusArea.setText("Department created successfully: " + name);
                logger.info("Department created: {}", name);
            } else {
                showErrorNotification("Failed to create department. Check for duplicate names.");
                statusArea.setText("Failed to create department");
            }
        });

        task.setOnFailed(e -> {
            logger.error("Error creating department", task.getException());
            showErrorNotification("Error creating department");
            statusArea.setText("Error creating department");
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    // ========================================================================
    // DELETE DEPARTMENT
    // ========================================================================

    @FXML
    private void handleDeleteDepartment() {
        if (selectedDepartment == null) {
            showWarningNotification("No Selection", "Please select a department to delete");
            return;
        }

        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm Deletion");
        confirmDialog.setHeaderText("Delete Department");
        confirmDialog.setContentText("Are you sure you want to delete '" + selectedDepartment.getName() + "'?");

        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Task<Boolean> task = new Task<>() {
                @Override
                protected Boolean call() {
                    return departmentService.deleteDepartment(selectedDepartment.getName());
                }
            };

            task.setOnSucceeded(e -> {
                boolean success = task.getValue();
                if (success) {
                    showSuccessNotification("Department Deleted", "Department '" + selectedDepartment.getName() + "' has been deleted");
                    clearForm();
                    loadDepartments();
                    statusArea.setText("Department deleted: " + selectedDepartment.getName());
                    logger.info("Department deleted: {}", selectedDepartment.getName());
                } else {
                    showErrorNotification("Failed to delete department");
                    statusArea.setText("Failed to delete department");
                }
            });

            task.setOnFailed(e -> {
                logger.error("Error deleting department", task.getException());
                showErrorNotification("Error deleting department");
            });

            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();
        }
    }

    // ========================================================================
    // VIEW DEPARTMENT
    // ========================================================================

    @FXML
    private void handleViewDepartment() {
        if (selectedDepartment == null) {
            showWarningNotification("No Selection", "Please select a department to view");
            return;
        }

        Department dept = selectedDepartment;
        StringBuilder details = new StringBuilder();
        details.append("Department Details\n");
        details.append("==================\n\n");
        details.append("Name: ").append(dept.getName()).append("\n");
        details.append("Serial: ").append(dept.getSerial()).append("\n");
        details.append("Supervisor: ").append(dept.getSupervisor()).append("\n");
        details.append("Created: ").append(dept.getCreateDate() != null ? dept.getCreateDate().toString() : "N/A").append("\n");
        details.append("Description: ").append(dept.getDescription() != null ? dept.getDescription() : "N/A");

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Department Details");
        alert.setHeaderText("Viewing: " + dept.getName());
        alert.setContentText(details.toString());
        alert.getDialogPane().setPrefWidth(400);
        alert.showAndWait();

        logger.debug("Viewed department: {}", dept.getName());
    }

    // ========================================================================
    // REFRESH
    // ========================================================================

    @FXML
    private void handleRefresh() {
        loadDepartments();
        loadSupervisorNames();
        clearForm();
        showInfoNotification("Refreshed", "Department list has been refreshed");
    }

    // ========================================================================
    // VALIDATION
    // ========================================================================

    private boolean validateCreateForm() {
        String name = nameField.getText().trim();
        String serial = serialField.getText().trim();
        String supervisor = supervisorField.getText().trim();
        LocalDate date = datePicker.getValue();

        if (name.isEmpty() || serial.isEmpty() || supervisor.isEmpty() || date == null) {
            showErrorNotification("Validation Error", "All fields are required");
            return false;
        }

        if (name.contains(" ") || serial.contains(" ") || supervisor.contains(" ")) {
            showErrorNotification("Validation Error", "Fields must not contain spaces");
            return false;
        }

        return true;
    }

    // ========================================================================
    // UTILITY
    // ========================================================================

    private void clearForm() {
        nameField.clear();
        serialField.clear();
        supervisorField.clear();
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
