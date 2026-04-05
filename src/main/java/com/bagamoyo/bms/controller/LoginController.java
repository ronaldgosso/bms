package com.bagamoyo.bms.controller;

import com.bagamoyo.bms.service.AuthService;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Unified login controller that handles authentication for all three roles:
 * System Admin (manager), Admin, and Supervisor.
 * Uses AuthService with BCrypt password verification.
 */
public class LoginController implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    // FXML-injected components
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Label statusLabel;

    @FXML
    private Label roleLabel;

    @FXML
    private ImageView logoView;

    @FXML
    private AnchorPane rootPane;

    private AuthService authService;
    private String currentRole = "admin"; // Default role; set externally

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logger.info("Initializing LoginController");
        authService = new AuthService();

        // Apply fade-in animation for smooth appearance
        if (rootPane != null) {
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.8), rootPane);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();
        }

        // Enter key triggers login
        if (passwordField != null) {
            passwordField.setOnAction(event -> handleLogin());
        }
        if (usernameField != null) {
            usernameField.setOnAction(event -> handleLogin());
        }

        logger.debug("LoginController initialized with role: {}", currentRole);
    }

    /**
     * Set the current login role. Call this before showing the view.
     *
     * @param role one of "admin", "manager", "supervisor"
     */
    public void setRole(String role) {
        if (role != null && (role.equalsIgnoreCase("admin")
                || role.equalsIgnoreCase("manager")
                || role.equalsIgnoreCase("supervisor"))) {
            this.currentRole = role.toLowerCase();
            logger.info("Login role set to: {}", currentRole);

            // Update the UI to reflect the role
            Platform.runLater(() -> {
                if (roleLabel != null) {
                    roleLabel.setText(currentRole.toUpperCase() + " LOGIN");
                }
            });
        } else {
            logger.warn("Invalid role attempted: {}. Defaulting to admin.", role);
        }
    }

    /**
     * Handle the login button action.
     */
    @FXML
    private void handleLogin() {
        String username = usernameField != null ? usernameField.getText().trim() : "";
        String password = passwordField != null ? passwordField.getText() : "";

        // Validate input
        if (username.isEmpty() || password.isEmpty()) {
            logger.warn("Login attempt with empty credentials");
            showErrorNotification("Login Failed", "Please enter both username and password.");
            return;
        }

        logger.info("Login attempt for role: {}, user: {}", currentRole, username);

        // Authenticate using AuthService
        boolean authenticated = authService.login(username, password, currentRole);

        if (authenticated) {
            logger.info("Successful {} login for user: {}", currentRole, username);
            showSuccessNotification("Login Successful", "Welcome, " + username + "!");

            // Navigate to the appropriate dashboard
            navigateToDashboard(currentRole, username);

            // Clear fields
            if (usernameField != null) usernameField.clear();
            if (passwordField != null) passwordField.clear();
        } else {
            logger.warn("Failed {} login for user: {}", currentRole, username);
            showErrorNotification("Login Failed", "Invalid username or password. Please try again.");

            // Clear password field for retry
            if (passwordField != null) passwordField.clear();
        }
    }

    /**
     * Navigate to the appropriate dashboard based on the authenticated role.
     *
     * @param role     the authenticated role
     * @param username the authenticated username
     */
    private void navigateToDashboard(String role, String username) {
        String fxmlPath;
        String windowTitle;

        switch (role) {
            case "manager":
                fxmlPath = "/fxml/SysAdmHome.fxml";
                windowTitle = "System Administrator Home";
                break;
            case "admin":
                fxmlPath = "/fxml/AdminHome.fxml";
                windowTitle = "Administration Home";
                break;
            case "supervisor":
                fxmlPath = "/fxml/supervisorHome.fxml";
                windowTitle = "Supervisor's Panel";
                break;
            default:
                logger.error("Unknown role for dashboard navigation: {}", role);
                return;
        }

        // Close current window and open dashboard
        try {
            Stage currentStage = getCurrentStage();
            if (currentStage != null) {
                currentStage.close();
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Stage dashboardStage = new Stage();
            dashboardStage.setTitle(windowTitle);
            try {
                Image icon = new Image(getClass().getResourceAsStream("/img/baga.png"));
                dashboardStage.getIcons().add(icon);
            } catch (Exception e) {
                logger.debug("Could not load window icon", e);
            }

            Scene scene = new Scene(root);
            dashboardStage.setScene(scene);
            dashboardStage.show();

            logger.info("Navigated to {} dashboard", role);
        } catch (Exception e) {
            logger.error("Failed to navigate to dashboard for role: {}", role, e);
            showErrorNotification("Navigation Error", "Could not open the dashboard page.");
        }
    }

    /**
     * Navigate back to the home screen.
     */
    @FXML
    private void goToHome() {
        logger.info("Navigating back to home screen");
        try {
            Stage currentStage = getCurrentStage();
            if (currentStage != null) {
                currentStage.close();
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Home.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Welcome to Bagamoyo District Council Management System");
            try {
                Image icon = new Image(getClass().getResourceAsStream("/img/baga.png"));
                stage.getIcons().add(icon);
            } catch (Exception e) {
                logger.debug("Could not load window icon", e);
            }

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            logger.error("Failed to navigate to home screen", e);
        }
    }

    /**
     * Get the current stage from any FXML-injected node.
     */
    private Stage getCurrentStage() {
        if (rootPane != null && rootPane.getScene() != null) {
            return (Stage) rootPane.getScene().getWindow();
        }
        return null;
    }

    // ========================================================================
    // Notification Helpers
    // ========================================================================

    /**
     * Show a success notification.
     */
    private void showSuccessNotification(String title, String message) {
        Platform.runLater(() -> {
            Image icon = loadImage("/img/baga.png");
            Notifications.create()
                    .graphic(new ImageView(icon))
                    .title(title)
                    .text(message)
                    .hideAfter(Duration.seconds(3))
                    .position(Pos.TOP_RIGHT)
                    .darkStyle()
                    .show();
        });
    }

    /**
     * Show an error notification.
     */
    private void showErrorNotification(String title, String message) {
        Platform.runLater(() -> {
            Image icon = loadImage("/img/baga.png");
            Notifications.create()
                    .graphic(new ImageView(icon))
                    .title(title)
                    .text(message)
                    .hideAfter(Duration.seconds(5))
                    .position(Pos.TOP_RIGHT)
                    .show();
        });
    }

    /**
     * Safely load an image from resources.
     */
    private Image loadImage(String resourcePath) {
        try {
            return new Image(getClass().getResourceAsStream(resourcePath));
        } catch (Exception e) {
            logger.debug("Could not load image: {}", resourcePath);
            return new Image(getClass().getResourceAsStream("/img/baga.png"));
        }
    }
}
