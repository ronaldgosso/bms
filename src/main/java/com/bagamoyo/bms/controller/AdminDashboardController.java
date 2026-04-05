package com.bagamoyo.bms.controller;

import com.bagamoyo.bms.service.AdminService;
import com.bagamoyo.bms.service.DepartmentService;
import com.bagamoyo.bms.service.SupervisorService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;

/**
 * Admin Dashboard Controller for the Bagamoyo District Council Management System.
 * Provides navigation to supervisor/department management, enrollment, and password change.
 */
public class AdminDashboardController {

    private static final Logger logger = LoggerFactory.getLogger(AdminDashboardController.class);

    @FXML
    private Label adminUsernameLabel;

    @FXML
    private Label totalSupervisorsLabel;

    @FXML
    private Label totalDepartmentsLabel;

    @FXML
    private VBox statsCard;

    @FXML
    private Button btnAddSupervisor;

    @FXML
    private Button btnDeleteSupervisor;

    @FXML
    private Button btnEnrollSupervisor;

    @FXML
    private Button btnAddDepartment;

    @FXML
    private Button btnDeleteDepartment;

    @FXML
    private Button btnEnrollDepartment;

    @FXML
    private Button btnChangePassword;

    @FXML
    private Button btnLogout;

    private final AdminService adminService;
    private final SupervisorService supervisorService;
    private final DepartmentService departmentService;

    public AdminDashboardController() {
        this.adminService = new AdminService();
        this.supervisorService = new SupervisorService();
        this.departmentService = new DepartmentService();
    }

    @FXML
    public void initialize() {
        loadDashboardStats();
        logger.info("Admin dashboard initialized");
    }

    /**
     * Load and display dashboard statistics.
     */
    private void loadDashboardStats() {
        try {
            long supervisorCount = supervisorService.countSupervisors();
            long departmentCount = departmentService.countDepartments();

            if (totalSupervisorsLabel != null) {
                totalSupervisorsLabel.setText(String.valueOf(supervisorCount));
            }
            if (totalDepartmentsLabel != null) {
                totalDepartmentsLabel.setText(String.valueOf(departmentCount));
            }
            logger.debug("Dashboard stats loaded - Supervisors: {}, Departments: {}", supervisorCount, departmentCount);
        } catch (Exception e) {
            logger.error("Failed to load dashboard statistics", e);
            showNotification("Error", "Failed to load statistics", NotificationType.ERROR);
        }
    }

    /**
     * Navigate to Add Supervisor screen.
     */
    @FXML
    public void handleAddSupervisor(ActionEvent event) {
        navigateTo("Add Supervisor", "/fxml/Supervisor.fxml", event);
    }

    /**
     * Navigate to Delete Supervisor screen.
     */
    @FXML
    public void handleDeleteSupervisor(ActionEvent event) {
        navigateTo("Delete Supervisor", "/fxml/SupvsorDelete.fxml", event);
    }

    /**
     * Navigate to Enroll Supervisor screen.
     */
    @FXML
    public void handleEnrollSupervisor(ActionEvent event) {
        navigateTo("Enroll Supervisor", "/fxml/enrollSupervisor.fxml", event);
    }

    /**
     * Navigate to Add Department screen.
     */
    @FXML
    public void handleAddDepartment(ActionEvent event) {
        navigateTo("Add Department", "/fxml/crDepartment.fxml", event);
    }

    /**
     * Navigate to Delete Department screen.
     */
    @FXML
    public void handleDeleteDepartment(ActionEvent event) {
        navigateTo("Delete Department", "/fxml/delDepartment.fxml", event);
    }

    /**
     * Navigate to Enroll Department screen.
     */
    @FXML
    public void handleEnrollDepartment(ActionEvent event) {
        navigateTo("Enroll Department", "/fxml/enrollDepartment.fxml", event);
    }

    /**
     * Navigate to Change Admin Password screen.
     */
    @FXML
    public void handleChangePassword(ActionEvent event) {
        navigateTo("Change Password", "/fxml/changePwdAdmin.fxml", event);
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
            logger.info("Admin logged out successfully");
        } catch (IOException e) {
            logger.error("Failed to navigate to Home screen", e);
            showNotification("Error", "Failed to return to home screen", NotificationType.ERROR);
        }
    }

    /**
     * Navigate to a new FXML screen, hiding the current window.
     */
    private void navigateTo(String title, String fxmlPath, ActionEvent event) {
        try {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.hide();

            Stage newStage = new Stage();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlPath)));
            newStage.setTitle(title);
            newStage.setScene(new Scene(root));
            newStage.show();
            logger.debug("Navigated to: {}", title);
        } catch (IOException e) {
            logger.error("Failed to navigate to {}", title, e);
            showNotification("Error", "Failed to open " + title, NotificationType.ERROR);
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
