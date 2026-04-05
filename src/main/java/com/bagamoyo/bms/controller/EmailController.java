package com.bagamoyo.bms.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Modern email client controller using JavaMail (Gmail SMTP).
 * Supports sending emails with attachments and modern form UI.
 */
public class EmailController {

    private static final Logger logger = LoggerFactory.getLogger(EmailController.class);

    // Sign-in fields
    @FXML private AnchorPane signInPane;
    @FXML private TextField signInNameField;
    @FXML private TextField signInEmailField;
    @FXML private PasswordField signInPasswordField;

    // Email form fields
    @FXML private AnchorPane mailPane;
    @FXML private Label nameLabel;
    @FXML private Label dateLabel;
    @FXML private Label dayLabel;
    @FXML private TextField fromField;
    @FXML private TextField toField;
    @FXML private TextField subjectField;
    @FXML private TextArea messageArea;
    @FXML private TextField attachmentField;
    @FXML private TextArea statusArea;

    // SMTP settings
    @FXML private TextField smtpHostField;
    @FXML private TextField smtpPortField;

    // Service state
    private String userEmail;
    private String userName;
    private String userPassword;

    // Attachment state
    private File attachedFile;
    private FileChooser fileChooser;

    // SMTP defaults
    private static final String DEFAULT_SMTP_HOST = "smtp.gmail.com";
    private static final String DEFAULT_SMTP_PORT = "465";

    public EmailController() {
    }

    // ========================================================================
    // INITIALIZATION
    // ========================================================================

    @FXML
    public void initialize() {
        setupFileChooser();
        setupDateDisplay();
        configureSMTPDefaults();

        // Initially show sign-in pane
        if (mailPane != null) {
            mailPane.setVisible(false);
        }
        if (signInPane != null) {
            signInPane.setVisible(true);
        }

        logger.info("EmailController initialized");
    }

    private void setupFileChooser() {
        fileChooser = new FileChooser();
        fileChooser.setTitle("Attach File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*"),
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf"),
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
                new FileChooser.ExtensionFilter("Document Files", "*.doc", "*.docx"),
                new FileChooser.ExtensionFilter("Spreadsheet Files", "*.xls", "*.xlsx"),
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );
    }

    private void setupDateDisplay() {
        if (dateLabel != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            dateLabel.setText(dateFormat.format(new Date()));
        }
        if (dayLabel != null) {
            dayLabel.setText(new SimpleDateFormat("EEEE").format(new Date()));
        }
    }

    private void configureSMTPDefaults() {
        if (smtpHostField != null) {
            smtpHostField.setText(DEFAULT_SMTP_HOST);
        }
        if (smtpPortField != null) {
            smtpPortField.setText(DEFAULT_SMTP_PORT);
        }
    }

    // ========================================================================
    // SIGN IN
    // ========================================================================

    @FXML
    private void handleSignIn() {
        String name = signInNameField.getText().trim();
        String email = signInEmailField.getText().trim();
        String password = signInPasswordField.getText();

        if (!validateSignIn(name, email, password)) {
            return;
        }

        this.userName = name;
        this.userEmail = email;
        this.userPassword = password;

        // Switch to mail pane
        if (signInPane != null) {
            signInPane.setVisible(false);
        }
        if (mailPane != null) {
            mailPane.setVisible(true);
        }
        if (fromField != null) {
            fromField.setText(userEmail);
        }
        if (nameLabel != null) {
            nameLabel.setText(userName);
        }

        showSuccessNotification("Signed In", "Welcome, " + userName + "!");
        logger.info("User signed in: {}", userEmail);
    }

    private boolean validateSignIn(String name, String email, String password) {
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showErrorNotification("Validation Error", "Please fill in all sign-in fields");
            return false;
        }

        if (!isValidEmail(email)) {
            showErrorNotification("Validation Error", "Please enter a valid email address");
            return false;
        }

        return true;
    }

    // ========================================================================
    // SEND EMAIL
    // ========================================================================

    @FXML
    private void handleSendEmail() {
        String to = toField.getText().trim();
        String subject = subjectField.getText().trim();
        String body = messageArea.getText();

        if (!validateEmailForm(to, subject)) {
            return;
        }

        statusArea.setText("Sending email...");

        Task<Boolean> task = new Task<>() {
            @Override
            protected Boolean call() {
                return sendEmail(to, subject, body);
            }
        };

        task.setOnSucceeded(e -> {
            boolean success = task.getValue();
            if (success) {
                showSuccessNotification("Email Sent", "Your message has been sent successfully");
                clearEmailForm();
                statusArea.setText("Email sent successfully");
                logger.info("Email sent to: {}", to);
            } else {
                showErrorNotification("Failed to Send Email", "Check your credentials and try again");
                statusArea.setText("Failed to send email");
            }
        });

        task.setOnFailed(e -> {
            logger.error("Error sending email", task.getException());
            showErrorNotification("Error Sending Email", task.getException().getMessage());
            statusArea.setText("Error: " + task.getException().getMessage());
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private boolean sendEmail(String to, String subject, String body) {
        Properties props = System.getProperties();
        props.put("mail.smtp.host", smtpHostField != null ? smtpHostField.getText() : DEFAULT_SMTP_HOST);
        props.put("mail.smtp.socketFactory.port", smtpPortField != null ? smtpPortField.getText() : DEFAULT_SMTP_PORT);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", smtpPortField != null ? smtpPortField.getText() : DEFAULT_SMTP_PORT);
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.debug", "false");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userEmail, userPassword);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(userEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);

            Multipart multipart = new MimeMultipart();

            // Text body part
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(body);
            multipart.addBodyPart(textPart);

            // Attachment (if any)
            if (attachedFile != null && attachedFile.exists()) {
                MimeBodyPart attachmentPart = new MimeBodyPart();
                DataSource source = new FileDataSource(attachedFile);
                attachmentPart.setDataHandler(new DataHandler(source));
                attachmentPart.setFileName(attachedFile.getName());
                multipart.addBodyPart(attachmentPart);
            }

            message.setContent(multipart);

            Transport.send(message);
            return true;

        } catch (AuthenticationFailedException e) {
            logger.error("Authentication failed for {}", userEmail, e);
            return false;
        } catch (MessagingException e) {
            logger.error("Messaging error", e);
            return false;
        }
    }

    // ========================================================================
    // ATTACH FILE
    // ========================================================================

    @FXML
    private void handleAttachFile() {
        Stage stage = (attachmentField != null) ? (Stage) attachmentField.getScene().getWindow() : null;
        if (stage == null && attachmentField != null) {
            stage = (Stage) attachmentField.getScene().getWindow();
        }

        if (stage == null) return;

        File file = fileChooser.showOpenDialog(stage);
        if (file == null) {
            return;
        }

        attachedFile = file;
        if (attachmentField != null) {
            attachmentField.setText(file.getName());
        }

        showSuccessNotification("File Attached", "File '" + file.getName() + "' is ready to send");
        logger.info("File attached: {}", file.getName());
    }

    // ========================================================================
    // REFRESH
    // ========================================================================

    @FXML
    private void handleRefresh() {
        clearEmailForm();
        attachedFile = null;
        showInfoNotification("Refreshed", "Email form has been cleared");
    }

    // ========================================================================
    // VALIDATION
    // ========================================================================

    private boolean validateEmailForm(String to, String subject) {
        if (to.isEmpty()) {
            showErrorNotification("Validation Error", "Recipient email is required");
            return false;
        }

        if (!isValidEmail(to)) {
            showErrorNotification("Validation Error", "Please enter a valid recipient email address");
            return false;
        }

        if (subject.isEmpty()) {
            showErrorNotification("Validation Error", "Email subject is required");
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

    private void clearEmailForm() {
        if (toField != null) toField.clear();
        if (subjectField != null) subjectField.clear();
        if (messageArea != null) messageArea.clear();
        if (attachmentField != null) attachmentField.clear();
        attachedFile = null;
    }

    private void showSuccessNotification(String title, String message) {
        Notifications.create()
                .title(title)
                .text(message)
                .hideAfter(javafx.util.Duration.seconds(3))
                .position(Pos.TOP_RIGHT)
                .showInformation();
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
