package com.bagamoyo.bms.controller;

import com.bagamoyo.bms.model.Worker;
import com.bagamoyo.bms.service.WorkerService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.textfield.TextFields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Worker Table Controller for the Bagamoyo District Council Management System.
 * Displays all workers in a TableView with filtering, sorting, and Excel export.
 */
public class WorkerTableController {

    private static final Logger logger = LoggerFactory.getLogger(WorkerTableController.class);

    // TableView and columns
    @FXML
    private TableView<Worker> workerTableView;

    @FXML
    private TableColumn<Worker, String> colCheckNo;

    @FXML
    private TableColumn<Worker, String> colFirstName;

    @FXML
    private TableColumn<Worker, String> colMiddleName;

    @FXML
    private TableColumn<Worker, String> colLastName;

    @FXML
    private TableColumn<Worker, String> colSex;

    @FXML
    private TableColumn<Worker, String> colPosition;

    @FXML
    private TableColumn<Worker, String> colSalary;

    @FXML
    private TableColumn<Worker, String> colTsd;

    @FXML
    private TableColumn<Worker, LocalDate> colDateOfBirth;

    @FXML
    private TableColumn<Worker, LocalDate> colEmploymentDate;

    @FXML
    private TableColumn<Worker, LocalDate> colConfirmationDate;

    @FXML
    private TableColumn<Worker, String> colGrade;

    @FXML
    private TableColumn<Worker, String> colEducation;

    @FXML
    private TableColumn<Worker, String> colInstitution;

    @FXML
    private TableColumn<Worker, String> colGraduationYear;

    @FXML
    private TableColumn<Worker, String> colCurrentWork;

    @FXML
    private TableColumn<Worker, String> colPreviousWork;

    @FXML
    private TableColumn<Worker, String> colReligion;

    @FXML
    private TableColumn<Worker, String> colPlaceOfBirth;

    @FXML
    private TableColumn<Worker, String> colPhoneNumber;

    @FXML
    private TableColumn<Worker, String> colSubject1;

    @FXML
    private TableColumn<Worker, String> colSubject2;

    @FXML
    private TableColumn<Worker, String> colWard;

    // Filter controls
    @FXML
    private TextField txtSearch;

    @FXML
    private TextField txtFilterSex;

    @FXML
    private TextField txtFilterPosition;

    @FXML
    private TextField txtFilterEducation;

    @FXML
    private TextField txtFilterWard;

    @FXML
    private Button btnSearch;

    @FXML
    private Button btnApplyFilters;

    @FXML
    private Button btnClearFilters;

    // Export controls
    @FXML
    private TextField txtSaveAs;

    @FXML
    private Button btnExportExcel;

    @FXML
    private Button btnRefresh;

    @FXML
    private Button btnClose;

    @FXML
    private Label lblTotalRecords;

    private final WorkerService workerService;
    private ObservableList<Worker> workerList;
    private FilteredList<Worker> filteredWorkers;
    private SortedList<Worker> sortedWorkers;

    private static final String[] SEX_OPTIONS = {"KE", "ME"};

    public WorkerTableController() {
        this.workerService = new WorkerService();
    }

    @FXML
    public void initialize() {
        setupTableColumns();
        loadWorkers();
        setupAutoCompletion();
        setupSorting();
        logger.info("Worker Table initialized");
    }

    /**
     * Setup TableView column cell value factories.
     */
    private void setupTableColumns() {
        colCheckNo.setCellValueFactory(new PropertyValueFactory<>("check"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colMiddleName.setCellValueFactory(new PropertyValueFactory<>("middleName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colSex.setCellValueFactory(new PropertyValueFactory<>("sex"));
        colPosition.setCellValueFactory(new PropertyValueFactory<>("position"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        colTsd.setCellValueFactory(new PropertyValueFactory<>("serviceDate"));
        colDateOfBirth.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        colEmploymentDate.setCellValueFactory(new PropertyValueFactory<>("employmentDate"));
        colConfirmationDate.setCellValueFactory(new PropertyValueFactory<>("confirmationDate"));
        colGrade.setCellValueFactory(new PropertyValueFactory<>("grade"));
        colEducation.setCellValueFactory(new PropertyValueFactory<>("educationLevel"));
        colInstitution.setCellValueFactory(new PropertyValueFactory<>("institution"));
        colGraduationYear.setCellValueFactory(new PropertyValueFactory<>("graduationYear"));
        colCurrentWork.setCellValueFactory(new PropertyValueFactory<>("currentWork"));
        colPreviousWork.setCellValueFactory(new PropertyValueFactory<>("previousWork"));
        colReligion.setCellValueFactory(new PropertyValueFactory<>("religion"));
        colPlaceOfBirth.setCellValueFactory(new PropertyValueFactory<>("placeOfBirth"));
        colPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colSubject1.setCellValueFactory(new PropertyValueFactory<>("subject1"));
        colSubject2.setCellValueFactory(new PropertyValueFactory<>("subject2"));
        colWard.setCellValueFactory(new PropertyValueFactory<>("ward"));

        // Format date columns
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        colDateOfBirth.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.format(dateFormatter));
                }
            }
        });

        colEmploymentDate.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.format(dateFormatter));
                }
            }
        });

        colConfirmationDate.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.format(dateFormatter));
                }
            }
        });
    }

    /**
     * Load all workers into the table.
     */
    private void loadWorkers() {
        try {
            List<Worker> workers = workerService.getAllWorkers();
            workerList = FXCollections.observableArrayList(workers);
            filteredWorkers = new FilteredList<>(workerList, p -> true);
            sortedWorkers = new SortedList<>(filteredWorkers);

            sortedWorkers.comparatorProperty().bind(workerTableView.comparatorProperty());
            workerTableView.setItems(sortedWorkers);

            updateTotalLabel();
            logger.debug("Loaded {} workers into table", workers.size());
        } catch (Exception e) {
            logger.error("Failed to load workers", e);
            showNotification("Error", "Failed to load worker records", NotificationType.ERROR);
        }
    }

    /**
     * Setup auto-completion for filter fields.
     */
    private void setupAutoCompletion() {
        if (txtFilterSex != null) {
            TextFields.bindAutoCompletion(txtFilterSex, SEX_OPTIONS);
        }
    }

    /**
     * Setup sorting for the TableView.
     */
    private void setupSorting() {
        // Sorting is handled via SortedList binding in loadWorkers()
    }

    /**
     * Update the total records label.
     */
    private void updateTotalLabel() {
        if (lblTotalRecords != null) {
            lblTotalRecords.setText("Total: " + filteredWorkers.size() + " records");
        }
    }

    /**
     * Handle Search button click - search by name.
     */
    @FXML
    public void handleSearch(ActionEvent event) {
        String query = txtSearch != null ? txtSearch.getText().trim() : "";

        if (query.isBlank()) {
            filteredWorkers.setPredicate(p -> true);
        } else {
            filteredWorkers.setPredicate(worker -> {
                String lowerQuery = query.toLowerCase();
                return worker.getFirstName().toLowerCase().contains(lowerQuery) ||
                        worker.getMiddleName().toLowerCase().contains(lowerQuery) ||
                        worker.getLastName().toLowerCase().contains(lowerQuery) ||
                        worker.getCheck().toLowerCase().contains(lowerQuery);
            });
        }

        updateTotalLabel();
        logger.debug("Name search executed: '{}'", query);
    }

    /**
     * Handle Apply Filters button click.
     */
    @FXML
    public void handleApplyFilters(ActionEvent event) {
        String sex = txtFilterSex != null ? txtFilterSex.getText().trim() : "";
        String position = txtFilterPosition != null ? txtFilterPosition.getText().trim() : "";
        String education = txtFilterEducation != null ? txtFilterEducation.getText().trim() : "";
        String ward = txtFilterWard != null ? txtFilterWard.getText().trim() : "";

        filteredWorkers.setPredicate(worker -> {
            boolean matchSex = sex.isBlank() || worker.getSex().equalsIgnoreCase(sex);
            boolean matchPosition = position.isBlank() || worker.getPosition().equalsIgnoreCase(position);
            boolean matchEducation = education.isBlank() || worker.getEducationLevel().equalsIgnoreCase(education);
            boolean matchWard = ward.isBlank() || worker.getWard().equalsIgnoreCase(ward);

            return matchSex && matchPosition && matchEducation && matchWard;
        });

        updateTotalLabel();
        showNotification("Filters Applied", "Showing " + filteredWorkers.size() + " records", NotificationType.INFORMATION);
        logger.debug("Filters applied - Sex: {}, Position: {}, Education: {}, Ward: {}", sex, position, education, ward);
    }

    /**
     * Handle Clear Filters button click.
     */
    @FXML
    public void handleClearFilters(ActionEvent event) {
        if (txtSearch != null) txtSearch.clear();
        if (txtFilterSex != null) txtFilterSex.clear();
        if (txtFilterPosition != null) txtFilterPosition.clear();
        if (txtFilterEducation != null) txtFilterEducation.clear();
        if (txtFilterWard != null) txtFilterWard.clear();

        filteredWorkers.setPredicate(p -> true);
        updateTotalLabel();
        showNotification("Cleared", "All filters cleared", NotificationType.INFORMATION);
        logger.debug("Filters cleared");
    }

    /**
     * Handle Export to Excel button click.
     */
    @FXML
    public void handleExportExcel(ActionEvent event) {
        String fileName = txtSaveAs != null ? txtSaveAs.getText().trim() : "";

        if (fileName.isBlank()) {
            showNotification("Validation Error", "Please enter a file name", NotificationType.ERROR);
            return;
        }

        if (fileName.contains(" ")) {
            showNotification("Validation Error", "File name cannot contain spaces", NotificationType.ERROR);
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Excel File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Excel Files", "*.xls", "*.xlsx")
        );
        fileChooser.setInitialFileName(fileName + "_enrolled.xlsx");

        Stage stage = new Stage();
        File file = fileChooser.showSaveDialog(stage);

        if (file == null) {
            return;
        }

        boolean success = workerService.exportToExcel(file.getAbsolutePath());

        if (success) {
            showNotification("Success", "Worker records exported to: " + file.getAbsolutePath(), NotificationType.INFORMATION);
            if (txtSaveAs != null) txtSaveAs.clear();
            logger.info("Workers exported to Excel: {}", file.getAbsolutePath());
        } else {
            showNotification("Error", "Failed to export worker records", NotificationType.ERROR);
            logger.error("Failed to export workers to Excel: {}", file.getAbsolutePath());
        }
    }

    /**
     * Handle Refresh button click.
     */
    @FXML
    public void handleRefresh(ActionEvent event) {
        loadWorkers();
        filteredWorkers.setPredicate(p -> true);
        handleClearFilters(null);
        showNotification("Refreshed", "Worker table refreshed", NotificationType.INFORMATION);
        logger.debug("Worker table refreshed");
    }

    /**
     * Handle Close button click.
     */
    @FXML
    public void handleClose(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        logger.debug("Worker Table closed");
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
