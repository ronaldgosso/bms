package com.bagamoyo.bms.controller;

import com.bagamoyo.bms.service.SupervisorService;
import com.bagamoyo.bms.service.WorkerService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * Supervisor Dashboard Controller for the Bagamoyo District Council Management System.
 * Provides navigation to worker management, file management, email, and chat features.
 */
public class SupervisorDashboardController {

    private static final Logger logger = LoggerFactory.getLogger(SupervisorDashboardController.class);

    @FXML
    private Label totalWorkersLabel;

    @FXML
    private ImageView leftView;

    @FXML
    private ImageView rightView;

    @FXML
    private ImageView backView;

    @FXML
    private Button btnAddWorker;

    @FXML
    private Button btnEditWorker;

    @FXML
    private Button btnDeleteWorker;

    @FXML
    private Button btnViewWorkers;

    @FXML
    private Button btnImportExcel;

    @FXML
    private Button btnExportExcel;

    @FXML
    private Button btnUploadFile;

    @FXML
    private Button btnDownloadFile;

    @FXML
    private Button btnViewFiles;

    @FXML
    private Button btnDeleteFile;

    @FXML
    private Button btnEmail;

    @FXML
    private Button btnChatServer;

    @FXML
    private Button btnChatClient;

    @FXML
    private Button btnChangePassword;

    @FXML
    private Button btnHelp;

    @FXML
    private Button btnLogout;

    private final WorkerService workerService;
    private final SupervisorService supervisorService;

    public SupervisorDashboardController() {
        this.workerService = new WorkerService();
        this.supervisorService = new SupervisorService();
    }

    @FXML
    public void initialize() {
        loadDashboardStats();
        logger.info("Supervisor dashboard initialized");
    }

    /**
     * Load dashboard statistics (total workers count).
     */
    private void loadDashboardStats() {
        try {
            long workerCount = workerService.countWorkers();
            if (totalWorkersLabel != null) {
                totalWorkersLabel.setText(String.valueOf(workerCount));
            }
            logger.debug("Total workers loaded: {}", workerCount);
        } catch (Exception e) {
            logger.error("Failed to load dashboard statistics", e);
        }
    }

    // ========================================================================
    // WORKER MANAGEMENT NAVIGATION
    // ========================================================================

    /**
     * Navigate to Add Worker screen.
     */
    @FXML
    public void handleAddWorker(ActionEvent event) {
        navigateTo("Add Individual", "/fxml/addIndividual.fxml", event);
    }

    /**
     * Navigate to Edit Worker screen.
     */
    @FXML
    public void handleEditWorker(ActionEvent event) {
        navigateTo("Edit Individual", "/fxml/editIndividual.fxml", event);
    }

    /**
     * Navigate to Delete Worker screen.
     */
    @FXML
    public void handleDeleteWorker(ActionEvent event) {
        navigateTo("Delete Individual", "/fxml/deleteIndividual.fxml", event);
    }

    /**
     * Navigate to Worker Table view (all workers).
     */
    @FXML
    public void handleViewWorkers(ActionEvent event) {
        navigateTo("Enroll Individuals", "/fxml/enrollIndividual.fxml", event);
    }

    // ========================================================================
    // FILE MANAGEMENT NAVIGATION
    // ========================================================================

    /**
     * Navigate to Import Excel screen.
     */
    @FXML
    public void handleImportExcel(ActionEvent event) {
        navigateTo("Excel File Upload", "/fxml/excellFile.fxml", event);
    }

    /**
     * Navigate to Export/Worker Table screen for Excel export.
     */
    @FXML
    public void handleExportExcel(ActionEvent event) {
        navigateTo("Export to Excel", "/fxml/enrollIndividual.fxml", event);
    }

    /**
     * Handle file upload - open file chooser and navigate to upload screen.
     */
    @FXML
    public void handleUploadFile(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf"),
                new FileChooser.ExtensionFilter("Doc Files", "*.doc", "*.docx"),
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        Stage stage = new Stage();
        File file = chooser.showOpenDialog(stage);
        if (file != null) {
            showNotification("File Selected", file.getName() + " uploaded to the system", NotificationType.INFORMATION);
            logger.info("File selected for upload: {}", file.getName());
        } else {
            showNotification("Error", "File corrupted or cancelled", NotificationType.ERROR);
        }
    }

    /**
     * Navigate to Download File screen.
     */
    @FXML
    public void handleDownloadFile(ActionEvent event) {
        navigateTo("Download A File", "/fxml/downloadFile.fxml", event);
    }

    /**
     * Navigate to View Uploaded Files screen.
     */
    @FXML
    public void handleViewFiles(ActionEvent event) {
        navigateTo("Uploaded Files", "/fxml/fileEnroll.fxml", event);
    }

    /**
     * Navigate to Delete File screen.
     */
    @FXML
    public void handleDeleteFile(ActionEvent event) {
        navigateTo("Delete Files", "/fxml/deleteFile.fxml", event);
    }

    // ========================================================================
    // EMAIL & CHAT NAVIGATION
    // ========================================================================

    /**
     * Navigate to Email Services screen.
     */
    @FXML
    public void handleEmail(ActionEvent event) {
        navigateTo("Email Services", "/fxml/Mail.fxml", event);
    }

    /**
     * Navigate to Chat Server screen.
     */
    @FXML
    public void handleChatServer(ActionEvent event) {
        navigateTo("nChat Server Panel", "/fxml/ServerPanel.fxml", event);
    }

    /**
     * Navigate to Chat Client screen.
     */
    @FXML
    public void handleChatClient(ActionEvent event) {
        navigateTo("nChat Client Panel", "/fxml/ClientPanel.fxml", event);
    }

    // ========================================================================
    // UTILITY NAVIGATION
    // ========================================================================

    /**
     * Navigate to Change Password screen (supervisor).
     */
    @FXML
    public void handleChangePassword(ActionEvent event) {
        showNotification("Contact Administrator", "Password change requires administrator contact", NotificationType.WARNING);
        logger.debug("Supervisor requested password change");
    }

    /**
     * Open help file.
     */
    @FXML
    public void handleHelp(ActionEvent event) {
        try {
            File helpFile = new File("resources/file/help.txt");
            if (helpFile.exists()) {
                java.awt.Desktop.getDesktop().open(helpFile);
                logger.debug("Opened help file");
            } else {
                showNotification("Error", "Help file not found", NotificationType.ERROR);
            }
        } catch (IOException e) {
            logger.error("Failed to open help file", e);
            showNotification("Error", "System error opening help file", NotificationType.ERROR);
        }
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
            logger.info("Supervisor logged out successfully");
        } catch (IOException e) {
            logger.error("Failed to navigate to Home screen", e);
            showNotification("Error", "Failed to return to home screen", NotificationType.ERROR);
        }
    }

    /**
     * Generate sample Excel template for bulk worker import.
     */
    @FXML
    public void handleSampleExcel(ActionEvent event) {
        try {
            org.apache.poi.hssf.usermodel.HSSFWorkbook wb = new org.apache.poi.hssf.usermodel.HSSFWorkbook();
            org.apache.poi.hssf.usermodel.HSSFSheet sheet = wb.createSheet("Tange");
            org.apache.poi.hssf.usermodel.HSSFRow header = sheet.createRow(0);

            String[] columns = {
                    "checkNo", "Jina La Kwanza", "Jina La Kati", "Jina La Mwisho", "Jinsia",
                    "Cheo", "Ngazi Ya Mshahara", "TSD", "Tarehe Ya Kuzaliwa", "Tarehe Ya Ajira",
                    "Tarehe Ya Kuthibitishwa", "Tarehe ya Daraja La Sasa", "Kiwango Cha Elimu",
                    "Chuo Alichosoma", "Mwaka Aliomaliza", "Kituo Cha Sasa Cha Kazi",
                    "Kituo Cha Awali Cha Kazi", "Dini", "Mahali Alipozaliwa", "Namba Ya Simu",
                    "Somo La Kwanza", "Somo La Pili", "Kata"
            };

            for (int i = 0; i < columns.length; i++) {
                header.createCell(i).setCellValue(columns[i]);
            }

            // Auto-size columns
            for (int i = 1; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            String fileName = "Sample Excell File.xls";
            String home = System.getProperty("user.home");
            File file = new File(home + "/Desktop/" + fileName);

            try (java.io.FileOutputStream fileOut = new java.io.FileOutputStream(file)) {
                wb.write(fileOut);
            }

            showNotification("Success", "Sample file exported to Desktop", NotificationType.INFORMATION);
            logger.info("Sample Excel file exported to Desktop");
        } catch (IOException e) {
            logger.error("Failed to export sample Excel file", e);
            showNotification("Error", "Failed to export sample file", NotificationType.ERROR);
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
