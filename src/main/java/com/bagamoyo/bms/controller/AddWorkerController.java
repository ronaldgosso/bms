package com.bagamoyo.bms.controller;

import com.bagamoyo.bms.model.Worker;
import com.bagamoyo.bms.service.WorkerService;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.textfield.TextFields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

/**
 * Add Worker Controller for the Bagamoyo District Council Management System.
 * Provides a form with all 23 fields to add a new worker record.
 */
public class AddWorkerController {

    private static final Logger logger = LoggerFactory.getLogger(AddWorkerController.class);

    // Required fields (marked with asterisk)
    @FXML
    private JFXTextField txtCheckNo;

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
    private Button btnAddWorker;

    @FXML
    private Button btnClear;

    @FXML
    private Button btnCancel;

    @FXML
    private VBox formContainer;

    private final WorkerService workerService;

    // Auto-completion data
    private static final String[] WARDS = {
            "ZINGA", "KEREGE", "MAPINGA", "KIROMO", "DUNDA", "NIANJEMA",
            "MAGOMENI", "FUKAYOSI", "MAKURUNGE", "YOMBO", "KISUTU"
    };

    private static final String[] SEX_OPTIONS = {"KE", "ME"};

    public AddWorkerController() {
        this.workerService = new WorkerService();
    }

    @FXML
    public void initialize() {
        setupAutoCompletion();
        setupDatePickerFormats();
        logger.info("Add Worker form initialized");
    }

    /**
     * Setup auto-completion for ward and sex fields.
     */
    private void setupAutoCompletion() {
        TextFields.bindAutoCompletion(txtWard, WARDS);
        TextFields.bindAutoCompletion(txtSex, SEX_OPTIONS);
    }

    /**
     * Setup date picker formats.
     */
    private void setupDatePickerFormats() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
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
     * Handle Add Worker button click.
     */
    @FXML
    public void handleAddWorker(ActionEvent event) {
        if (!validateRequiredFields()) {
            return;
        }

        if (!validateNoSpaces()) {
            return;
        }

        Worker worker = buildWorkerFromForm();

        boolean success = workerService.addWorker(worker);
        if (success) {
            showNotification("Success", "Individual Added successfully", NotificationType.INFORMATION);
            clearForm();
            logger.info("Worker added: {} {}", worker.getFirstName(), worker.getLastName());
        } else {
            showNotification("Error", "Failed to add individual. Record may already exist.", NotificationType.ERROR);
            logger.warn("Failed to add worker - possible duplicate: {}", worker.getCheck());
        }
    }

    /**
     * Handle Clear button click.
     */
    @FXML
    public void handleClear(ActionEvent event) {
        clearForm();
        showNotification("Cleared", "Form fields cleared", NotificationType.INFORMATION);
    }

    /**
     * Handle Cancel button click - close the window.
     */
    @FXML
    public void handleCancel(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        logger.debug("Add Worker form closed");
    }

    /**
     * Build a Worker object from form fields.
     */
    private Worker buildWorkerFromForm() {
        Worker worker = new Worker();
        worker.setCheck(getText(txtCheckNo));
        worker.setFirstName(getText(txtFirstName));
        worker.setMiddleName(getText(txtMiddleName));
        worker.setLastName(getText(txtLastName));
        worker.setSex(getText(txtSex));
        worker.setPosition(getText(txtPosition));
        worker.setSalary(getText(txtSalary));
        worker.setServiceDate(getText(txtTsd));
        worker.setDateOfBirth(getDatePickerValue(txtDateOfBirth));
        worker.setEmploymentDate(getDatePickerValue(txtEmploymentDate));
        worker.setConfirmationDate(getDatePickerValue(txtConfirmationDate));
        worker.setGrade(getDatePickerText(txtGradeDate));
        worker.setEducationLevel(getText(txtEducation));
        worker.setInstitution(getText(txtInstitution));
        worker.setGraduationYear(getText(txtGraduationYear));
        worker.setCurrentWork(getText(txtCurrentWork));
        worker.setPreviousWork(getText(txtPreviousWork));
        worker.setReligion(getText(txtReligion));
        worker.setPlaceOfBirth(getText(txtPlaceOfBirth));
        worker.setPhoneNumber(getText(txtPhoneNumber));
        worker.setSubject1(getText(txtSubject1));
        worker.setSubject2(getText(txtSubject2));
        worker.setWard(getText(txtWard));
        return worker;
    }

    /**
     * Validate that required fields are not empty.
     */
    private boolean validateRequiredFields() {
        // Core required fields
        String[] requiredFields = {
                getText(txtCheckNo), getText(txtFirstName), getText(txtMiddleName),
                getText(txtLastName), getText(txtSex), getText(txtPosition),
                getText(txtSalary), getText(txtTsd), getDatePickerText(txtDateOfBirth),
                getDatePickerText(txtEmploymentDate), getDatePickerText(txtConfirmationDate),
                getDatePickerText(txtGradeDate), getText(txtEducation), getText(txtInstitution),
                getText(txtGraduationYear), getText(txtCurrentWork), getText(txtPreviousWork),
                getText(txtReligion), getText(txtPlaceOfBirth), getText(txtPhoneNumber),
                getText(txtSubject1), getText(txtSubject2), getText(txtWard)
        };

        String[] fieldNames = {
                "Check No", "First Name", "Middle Name", "Last Name", "Sex",
                "Position", "Salary", "TSD", "Date of Birth", "Employment Date",
                "Confirmation Date", "Grade Date", "Education", "Institution",
                "Graduation Year", "Current Work", "Previous Work", "Religion",
                "Place of Birth", "Phone Number", "Subject 1", "Subject 2", "Ward"
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
     * Validate that text fields do not contain spaces.
     */
    private boolean validateNoSpaces() {
        TextField[] textFields = {
                txtFirstName, txtMiddleName, txtLastName, txtSex,
                txtCheckNo, txtPosition, txtSalary, txtTsd,
                txtEducation, txtInstitution, txtGraduationYear,
                txtCurrentWork, txtPreviousWork, txtReligion,
                txtPlaceOfBirth, txtPhoneNumber, txtSubject1, txtSubject2, txtWard
        };

        for (TextField field : textFields) {
            if (field.getText() != null && field.getText().contains(" ")) {
                showNotification("Validation Error",
                        field.getPromptText() + " cannot contain spaces",
                        NotificationType.ERROR);
                return false;
            }
        }

        return true;
    }

    /**
     * Clear all form fields.
     */
    private void clearForm() {
        txtCheckNo.clear();
        txtFirstName.clear();
        txtMiddleName.clear();
        txtLastName.clear();
        txtSex.clear();
        txtPosition.clear();
        txtSalary.clear();
        txtTsd.clear();
        txtDateOfBirth.setValue(null);
        txtEmploymentDate.setValue(null);
        txtConfirmationDate.setValue(null);
        txtGradeDate.setValue(null);
        txtEducation.clear();
        txtInstitution.clear();
        txtGraduationYear.clear();
        txtCurrentWork.clear();
        txtPreviousWork.clear();
        txtReligion.clear();
        txtPlaceOfBirth.clear();
        txtPhoneNumber.clear();
        txtSubject1.clear();
        txtSubject2.clear();
        txtWard.clear();
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
     * Helper to get LocalDate value from DatePicker.
     */
    private LocalDate getDatePickerValue(JFXDatePicker datePicker) {
        if (datePicker == null) {
            return null;
        }
        return datePicker.getValue();
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
