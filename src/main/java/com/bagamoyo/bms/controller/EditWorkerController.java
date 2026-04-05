package com.bagamoyo.bms.controller;

import com.bagamoyo.bms.model.Worker;
import com.bagamoyo.bms.service.WorkerService;
import com.jfoenix.controls.JFXDatePicker;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Edit Worker Controller for the Bagamoyo District Council Management System.
 * Allows searching for a worker by check number and updating all 23 fields.
 */
public class EditWorkerController {

    private static final Logger logger = LoggerFactory.getLogger(EditWorkerController.class);

    // Search field
    @FXML
    private TextField txtSearchCheckNo;

    // Worker fields (all 23)
    @FXML
    private JFXTextField txtFirstName;

    @FXML
    private JFXTextField txtMiddleName;

    @FXML
    private JFXTextField txtLastName;

    @FXML
    private JFXTextField txtSex;

    @FXML
    private JFXTextField txtPosition;

    @FXML
    private JFXTextField txtSalary;

    @FXML
    private TextField txtCheckNo;

    @FXML
    private JFXTextField txtTsd;

    @FXML
    private JFXDatePicker txtDateOfBirth;

    @FXML
    private JFXDatePicker txtEmploymentDate;

    @FXML
    private JFXDatePicker txtConfirmationDate;

    @FXML
    private JFXDatePicker txtGradeDate;

    @FXML
    private JFXTextField txtEducation;

    @FXML
    private JFXTextField txtInstitution;

    @FXML
    private JFXTextField txtGraduationYear;

    @FXML
    private JFXTextField txtCurrentWork;

    @FXML
    private JFXTextField txtPreviousWork;

    @FXML
    private JFXTextField txtReligion;

    @FXML
    private JFXTextField txtPlaceOfBirth;

    @FXML
    private JFXTextField txtPhoneNumber;

    @FXML
    private JFXTextField txtSubject1;

    @FXML
    private JFXTextField txtSubject2;

    @FXML
    private TextField txtWard;

    @FXML
    private Button btnSearch;

    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnClear;

    @FXML
    private Button btnCancel;

    private final WorkerService workerService;
    private ObservableList<String> checkNoList;
    private Worker currentWorker;

    private static final String[] SEX_OPTIONS = {"KE", "ME"};

    public EditWorkerController() {
        this.workerService = new WorkerService();
    }

    @FXML
    public void initialize() {
        loadCheckNoSuggestions();
        setupAutoCompletion();
        setupSearchOnEnter();
        setupDatePickerFormats();
        logger.info("Edit Worker form initialized");
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
     * Setup auto-completion for search and sex fields.
     */
    private void setupAutoCompletion() {
        if (txtSearchCheckNo != null && checkNoList != null) {
            TextFields.bindAutoCompletion(txtSearchCheckNo, checkNoList);
        }
        TextFields.bindAutoCompletion(txtSex, SEX_OPTIONS);
    }

    /**
     * Setup search on Enter key press.
     */
    private void setupSearchOnEnter() {
        if (txtSearchCheckNo != null) {
            txtSearchCheckNo.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    handleSearch(null);
                }
            });
        }
    }

    /**
     * Setup date picker formats.
     */
    private void setupDatePickerFormats() {
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
        if (txtDateOfBirth != null) {
            txtDateOfBirth.setPromptText("dd/MM/yyyy");
        }
        if (txtEmploymentDate != null) {
            txtEmploymentDate.setPromptText("dd/MM/yyyy");
        }
        if (txtConfirmationDate != null) {
            txtConfirmationDate.setPromptText("dd/MM/yyyy");
        }
        if (txtGradeDate != null) {
            txtGradeDate.setPromptText("dd/MM/yyyy");
        }
    }

    /**
     * Handle search button click - find worker by check number.
     */
    @FXML
    public void handleSearch(ActionEvent event) {
        String checkNo = txtSearchCheckNo != null ? txtSearchCheckNo.getText().trim() : "";

        if (checkNo.isBlank()) {
            showNotification("Validation Error", "Please enter a Check Number to search", NotificationType.ERROR);
            return;
        }

        Optional<Worker> workerOpt = workerService.getWorkerById(checkNo);

        if (workerOpt.isPresent()) {
            currentWorker = workerOpt.get();
            populateForm(currentWorker);
            showNotification("Found", "Worker record loaded", NotificationType.INFORMATION);
            logger.info("Worker loaded for editing: {} {}", currentWorker.getFirstName(), currentWorker.getLastName());
        } else {
            showNotification("Not Found", "No worker found with Check No: " + checkNo, NotificationType.ERROR);
            clearForm();
            logger.warn("Worker not found with checkNo: {}", checkNo);
        }
    }

    /**
     * Handle Update button click.
     */
    @FXML
    public void handleUpdate(ActionEvent event) {
        if (currentWorker == null) {
            showNotification("Validation Error", "Please search and select a worker first", NotificationType.ERROR);
            return;
        }

        if (!validateFields()) {
            return;
        }

        // Update the worker object from form fields
        currentWorker.setFirstName(getText(txtFirstName));
        currentWorker.setMiddleName(getText(txtMiddleName));
        currentWorker.setLastName(getText(txtLastName));
        currentWorker.setSex(getText(txtSex));
        currentWorker.setPosition(getText(txtPosition));
        currentWorker.setSalary(getText(txtSalary));
        currentWorker.setServiceDate(getText(txtTsd));
        currentWorker.setDateOfBirth(txtDateOfBirth.getValue());
        currentWorker.setEmploymentDate(txtEmploymentDate.getValue());
        currentWorker.setConfirmationDate(txtConfirmationDate.getValue());
        currentWorker.setGrade(getDatePickerText(txtGradeDate));
        currentWorker.setEducationLevel(getText(txtEducation));
        currentWorker.setInstitution(getText(txtInstitution));
        currentWorker.setGraduationYear(getText(txtGraduationYear));
        currentWorker.setCurrentWork(getText(txtCurrentWork));
        currentWorker.setPreviousWork(getText(txtPreviousWork));
        currentWorker.setReligion(getText(txtReligion));
        currentWorker.setPlaceOfBirth(getText(txtPlaceOfBirth));
        currentWorker.setPhoneNumber(getText(txtPhoneNumber));
        currentWorker.setSubject1(getText(txtSubject1));
        currentWorker.setSubject2(getText(txtSubject2));
        currentWorker.setWard(getText(txtWard));

        boolean success = workerService.updateWorker(currentWorker);

        if (success) {
            showNotification("Success", "Individual updated successfully", NotificationType.INFORMATION);
            logger.info("Worker updated: {} {}", currentWorker.getFirstName(), currentWorker.getLastName());
        } else {
            showNotification("Error", "Failed to update individual", NotificationType.ERROR);
            logger.warn("Failed to update worker: {}", currentWorker.getCheck());
        }
    }

    /**
     * Handle Clear button click.
     */
    @FXML
    public void handleClear(ActionEvent event) {
        clearForm();
        currentWorker = null;
        if (txtSearchCheckNo != null) {
            txtSearchCheckNo.clear();
        }
    }

    /**
     * Handle Cancel button click - close the window.
     */
    @FXML
    public void handleCancel(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        logger.debug("Edit Worker form closed");
    }

    /**
     * Populate form fields with worker data.
     */
    private void populateForm(Worker worker) {
        if (txtCheckNo != null) {
            txtCheckNo.setText(worker.getCheck());
        }
        if (txtFirstName != null) {
            txtFirstName.setText(worker.getFirstName());
        }
        if (txtMiddleName != null) {
            txtMiddleName.setText(worker.getMiddleName());
        }
        if (txtLastName != null) {
            txtLastName.setText(worker.getLastName());
        }
        if (txtSex != null) {
            txtSex.setText(worker.getSex());
        }
        if (txtPosition != null) {
            txtPosition.setText(worker.getPosition());
        }
        if (txtSalary != null) {
            txtSalary.setText(worker.getSalary());
        }
        if (txtTsd != null) {
            txtTsd.setText(worker.getServiceDate());
        }
        if (txtDateOfBirth != null) {
            txtDateOfBirth.setValue(worker.getDateOfBirth());
        }
        if (txtEmploymentDate != null) {
            txtEmploymentDate.setValue(worker.getEmploymentDate());
        }
        if (txtConfirmationDate != null) {
            txtConfirmationDate.setValue(worker.getConfirmationDate());
        }
        if (txtGradeDate != null) {
            if (worker.getGrade() != null && !worker.getGrade().isBlank()) {
                try {
                    txtGradeDate.setValue(LocalDate.parse(worker.getGrade()));
                } catch (Exception e) {
                    // If grade is not a valid date, set null
                    txtGradeDate.setValue(null);
                }
            }
        }
        if (txtEducation != null) {
            txtEducation.setText(worker.getEducationLevel());
        }
        if (txtInstitution != null) {
            txtInstitution.setText(worker.getInstitution());
        }
        if (txtGraduationYear != null) {
            txtGraduationYear.setText(worker.getGraduationYear());
        }
        if (txtCurrentWork != null) {
            txtCurrentWork.setText(worker.getCurrentWork());
        }
        if (txtPreviousWork != null) {
            txtPreviousWork.setText(worker.getPreviousWork());
        }
        if (txtReligion != null) {
            txtReligion.setText(worker.getReligion());
        }
        if (txtPlaceOfBirth != null) {
            txtPlaceOfBirth.setText(worker.getPlaceOfBirth());
        }
        if (txtPhoneNumber != null) {
            txtPhoneNumber.setText(worker.getPhoneNumber());
        }
        if (txtSubject1 != null) {
            txtSubject1.setText(worker.getSubject1());
        }
        if (txtSubject2 != null) {
            txtSubject2.setText(worker.getSubject2());
        }
        if (txtWard != null) {
            txtWard.setText(worker.getWard());
        }
    }

    /**
     * Validate that form fields are not empty.
     */
    private boolean validateFields() {
        String[] requiredFields = {
                getText(txtFirstName), getText(txtMiddleName), getText(txtLastName),
                getText(txtSex), getText(txtPosition), getText(txtSalary),
                getText(txtTsd)
        };

        String[] fieldNames = {
                "First Name", "Middle Name", "Last Name", "Sex",
                "Position", "Salary", "TSD"
        };

        for (int i = 0; i < requiredFields.length; i++) {
            if (requiredFields[i] == null || requiredFields[i].isBlank()) {
                showNotification("Validation Error", fieldNames[i] + " is required", NotificationType.ERROR);
                return false;
            }
        }

        return true;
    }

    /**
     * Clear all form fields.
     */
    private void clearForm() {
        if (txtCheckNo != null) txtCheckNo.clear();
        if (txtFirstName != null) txtFirstName.clear();
        if (txtMiddleName != null) txtMiddleName.clear();
        if (txtLastName != null) txtLastName.clear();
        if (txtSex != null) txtSex.clear();
        if (txtPosition != null) txtPosition.clear();
        if (txtSalary != null) txtSalary.clear();
        if (txtTsd != null) txtTsd.clear();
        if (txtDateOfBirth != null) txtDateOfBirth.setValue(null);
        if (txtEmploymentDate != null) txtEmploymentDate.setValue(null);
        if (txtConfirmationDate != null) txtConfirmationDate.setValue(null);
        if (txtGradeDate != null) txtGradeDate.setValue(null);
        if (txtEducation != null) txtEducation.clear();
        if (txtInstitution != null) txtInstitution.clear();
        if (txtGraduationYear != null) txtGraduationYear.clear();
        if (txtCurrentWork != null) txtCurrentWork.clear();
        if (txtPreviousWork != null) txtPreviousWork.clear();
        if (txtReligion != null) txtReligion.clear();
        if (txtPlaceOfBirth != null) txtPlaceOfBirth.clear();
        if (txtPhoneNumber != null) txtPhoneNumber.clear();
        if (txtSubject1 != null) txtSubject1.clear();
        if (txtSubject2 != null) txtSubject2.clear();
        if (txtWard != null) txtWard.clear();
    }

    /**
     * Helper to safely get text from a TextField.
     */
    private String getText(TextField field) {
        return field != null ? field.getText().trim() : "";
    }

    /**
     * Helper to get text from DatePicker editor.
     */
    private String getDatePickerText(JFXDatePicker datePicker) {
        if (datePicker == null || datePicker.getEditor() == null) {
            return "";
        }
        return datePicker.getEditor().getText().trim();
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
