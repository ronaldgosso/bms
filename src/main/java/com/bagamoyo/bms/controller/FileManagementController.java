package com.bagamoyo.bms.controller;

import com.bagamoyo.bms.model.FileEntry;
import com.bagamoyo.bms.service.FileService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.textfield.TextFields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Modern file management controller for upload/download/delete operations.
 * Uses FileService for BLOB storage/retrieval with TableView display.
 */
public class FileManagementController {

    private static final Logger logger = LoggerFactory.getLogger(FileManagementController.class);

    // Form fields
    @FXML private TextField fileNameField;
    @FXML private TextArea statusArea;
    @FXML private Label selectedFileLabel;

    // Table and data
    @FXML private TableView<FileEntry> fileTable;
    @FXML private TableColumn<FileEntry, String> nameColumn;
    @FXML private TableColumn<FileEntry, String> typeColumn;
    @FXML private TableColumn<FileEntry, LocalDateTime> dateColumn;
    @FXML private TableColumn<FileEntry, String> sizeColumn;

    // Service layer
    private final FileService fileService;

    // Auto-complete data
    private final ObservableList<String> fileNames = FXCollections.observableArrayList();

    // Selected file
    private FileEntry selectedFileEntry;

    // File chooser for upload
    private FileChooser fileChooser;

    public FileManagementController() {
        this.fileService = new FileService();
    }

    // ========================================================================
    // INITIALIZATION
    // ========================================================================

    @FXML
    public void initialize() {
        setupTableColumns();
        setupFileChooser();
        setupAutoCompletion();
        loadFiles();
        logger.info("FileManagementController initialized");
    }

    private void setupTableColumns() {
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        typeColumn.setCellValueFactory(cellData -> cellData.getValue().fileTypeProperty());
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().uploadDateProperty());
        sizeColumn.setCellValueFactory(cellData -> {
            FileEntry entry = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(entry.getFormattedFileSize());
        });

        fileTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    selectedFileEntry = newSelection;
                    if (newSelection != null) {
                        selectedFileLabel.setText("Selected: " + newSelection.getName());
                    }
                }
        );
    }

    private void setupFileChooser() {
        fileChooser = new FileChooser();
        fileChooser.setTitle("Select File to Upload");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*"),
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf"),
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif", "*.jpeg"),
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("Document Files", "*.doc", "*.docx"),
                new FileChooser.ExtensionFilter("Spreadsheet Files", "*.xls", "*.xlsx")
        );
    }

    private void setupAutoCompletion() {
        TextFields.bindAutoCompletion(fileNameField, fileNames);
    }

    // ========================================================================
    // DATA LOADING
    // ========================================================================

    private void loadFiles() {
        Task<List<FileEntry>> task = new Task<>() {
            @Override
            protected List<FileEntry> call() {
                return fileService.getAllFileEntries();
            }
        };

        task.setOnSucceeded(e -> {
            List<FileEntry> entries = task.getValue();
            ObservableList<FileEntry> data = FXCollections.observableArrayList(entries);
            fileTable.setItems(data);

            // Update auto-complete list
            List<String> names = entries.stream()
                    .map(FileEntry::getName)
                    .toList();
            fileNames.setAll(names);

            logger.debug("Loaded {} file entries", entries.size());
        });

        task.setOnFailed(e -> {
            logger.error("Failed to load files", task.getException());
            showErrorNotification("Failed to load files");
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    // ========================================================================
    // UPLOAD FILE
    // ========================================================================

    @FXML
    private void handleUploadFile() {
        Stage stage = (Stage) fileTable.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file == null) {
            showWarningNotification("No File Selected", "Please select a file to upload");
            return;
        }

        String fileName = file.getName();
        statusArea.setText("Uploading: " + fileName + "...");

        Task<Boolean> task = new Task<>() {
            @Override
            protected Boolean call() throws IOException {
                byte[] content = Files.readAllBytes(file.toPath());
                return fileService.uploadFile(fileName, content);
            }
        };

        task.setOnSucceeded(e -> {
            boolean success = task.getValue();
            if (success) {
                showSuccessNotification("File Uploaded", "File '" + fileName + "' uploaded successfully");
                statusArea.setText("File uploaded successfully: " + fileName);
                loadFiles();
                logger.info("File uploaded: {}", fileName);
            } else {
                showErrorNotification("Failed to upload file. File may already exist.");
                statusArea.setText("Failed to upload file");
            }
        });

        task.setOnFailed(e -> {
            logger.error("Error uploading file: {}", fileName, task.getException());
            showErrorNotification("Error uploading file");
            statusArea.setText("Error uploading file");
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    // ========================================================================
    // DOWNLOAD FILE
    // ========================================================================

    @FXML
    private void handleDownloadFile() {
        String fileName = fileNameField.getText().trim();

        if (fileName.isEmpty()) {
            if (selectedFileEntry != null) {
                fileName = selectedFileEntry.getName();
            } else {
                showWarningNotification("No File Selected", "Please select or enter a file name to download");
                return;
            }
        }

        // Prompt for save location
        Stage stage = (Stage) fileTable.getScene().getWindow();
        FileChooser saveChooser = new FileChooser();
        saveChooser.setTitle("Save File");
        saveChooser.setInitialFileName(fileName);

        File saveFile = saveChooser.showSaveDialog(stage);
        if (saveFile == null) {
            return;
        }

        statusArea.setText("Downloading: " + fileName + "...");

        Task<Boolean> task = new Task<>() {
            @Override
            protected Boolean call() throws IOException {
                Optional<byte[]> fileData = fileService.downloadFile(fileName);
                if (fileData.isPresent()) {
                    try (FileOutputStream fos = new FileOutputStream(saveFile)) {
                        fos.write(fileData.get());
                    }
                    return true;
                }
                return false;
            }
        };

        task.setOnSucceeded(e -> {
            boolean success = task.getValue();
            if (success) {
                showSuccessNotification("File Downloaded", "File '" + fileName + "' saved to " + saveFile.getAbsolutePath());
                statusArea.setText("File downloaded to: " + saveFile.getAbsolutePath());
                logger.info("File downloaded: {} -> {}", fileName, saveFile.getAbsolutePath());
            } else {
                showErrorNotification("File not found: " + fileName);
                statusArea.setText("File not found: " + fileName);
            }
        });

        task.setOnFailed(e -> {
            logger.error("Error downloading file: {}", fileName, task.getException());
            showErrorNotification("Error downloading file");
            statusArea.setText("Error downloading file");
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    // ========================================================================
    // DELETE FILE
    // ========================================================================

    @FXML
    private void handleDeleteFile() {
        String fileName = fileNameField.getText().trim();

        if (fileName.isEmpty()) {
            if (selectedFileEntry != null) {
                fileName = selectedFileEntry.getName();
            } else {
                showWarningNotification("No File Selected", "Please select or enter a file name to delete");
                return;
            }
        }

        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm Deletion");
        confirmDialog.setHeaderText("Delete File");
        confirmDialog.setContentText("Are you sure you want to delete '" + fileName + "'?");

        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            final String finalFileName = fileName;
            Task<Boolean> task = new Task<>() {
                @Override
                protected Boolean call() {
                    return fileService.deleteFile(finalFileName);
                }
            };

            task.setOnSucceeded(e -> {
                boolean success = task.getValue();
                if (success) {
                    showSuccessNotification("File Deleted", "File '" + finalFileName + "' has been deleted");
                    fileNameField.clear();
                    loadFiles();
                    statusArea.setText("File deleted: " + finalFileName);
                    logger.info("File deleted: {}", finalFileName);
                } else {
                    showErrorNotification("Failed to delete file: " + finalFileName);
                    statusArea.setText("Failed to delete file");
                }
            });

            task.setOnFailed(e -> {
                logger.error("Error deleting file: {}", fileName, task.getException());
                showErrorNotification("Error deleting file");
                statusArea.setText("Error deleting file");
            });

            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();
        }
    }

    // ========================================================================
    // DOWNLOAD SELECTED FROM TABLE
    // ========================================================================

    @FXML
    private void handleDownloadSelected() {
        if (selectedFileEntry == null) {
            showWarningNotification("No Selection", "Please select a file from the table");
            return;
        }

        fileNameField.setText(selectedFileEntry.getName());
        handleDownloadFile();
    }

    // ========================================================================
    // DELETE SELECTED FROM TABLE
    // ========================================================================

    @FXML
    private void handleDeleteSelected() {
        if (selectedFileEntry == null) {
            showWarningNotification("No Selection", "Please select a file from the table");
            return;
        }

        fileNameField.setText(selectedFileEntry.getName());
        handleDeleteFile();
    }

    // ========================================================================
    // REFRESH
    // ========================================================================

    @FXML
    private void handleRefresh() {
        loadFiles();
        fileNameField.clear();
        statusArea.clear();
        selectedFileLabel.setText("No file selected");
        showInfoNotification("Refreshed", "File list has been refreshed");
    }

    // ========================================================================
    // UTILITY
    // ========================================================================

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
