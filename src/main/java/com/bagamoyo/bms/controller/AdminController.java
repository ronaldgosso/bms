package com.bagamoyo.bms.controller;

import com.bagamoyo.bms.service.AuthService;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
 * Admin login screen controller.
 * Authenticates against the admin table using AuthService.validateAdmin().
 * Features modern UI with proper error handling and ControlsFX notifications.
 */
public class AdminController implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    // FXML-injected components
    @FXML
    private TextField user;

    @FXML
    private PasswordField pass;

    @FXML
    private ImageView adminView;

    @FXML
    private Label statusLabel;

    @FXML
    private Button loginButton;

    @FXML
    private AnchorPane rootPane;

    private AuthService authService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logger.info("Initializing AdminController");
        authService = new AuthService();

        // Apply fade-in animation for smooth appearance
        if (rootPane != null) {
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.8), rootPane);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();
        }

        // Enter key triggers login
        if (pass != null) {
            pass.setOnAction(event -> handleLogin());
        }
        if (user != null) {
            user.setOnAction(event -> handleLogin());
        }

        logger.debug("AdminController initialized");
    }

    /**
     * Handle the admin login button action.
     * Authenticates against the admin table using BCrypt password verification.
     */
    @FXML
    private void handleLogin() {
        String username = user != null ? user.getText().trim() : "";
        String password = pass != null ? pass.getText() : "";

        // Validate input
        if (username.isEmpty() || password.isEmpty()) {
            logger.warn("Admin login attempt with empty credentials");
            showErrorNotification("Login Failed", "Please enter both username and password.");
            return;
        }

        logger.info("Admin login attempt for user: {}", username);

        // Authenticate using AuthService with BCrypt verification
        boolean authenticated = authService.validateAdmin(username, password);

        if (authenticated) {
            logger.info("Successful admin login for user: {}", username);
            showSuccessNotification("Login Successful", "Welcome, " + username + "!");

            // Navigate to admin dashboard
            navigateToAdminHome();

            // Clear fields
            if (user != null) user.clear();
            if (pass != null) pass.clear();
        } else {
            logger.warn("Failed admin login for user: {}", username);
            showErrorNotification("Login Failed", "Invalid username or password. Please try again.");

            // Clear password field for retry
            if (pass != null) pass.clear();
        }
    }

    /**
     * Navigate to the admin home dashboard.
     */
    private void navigateToAdminHome() {
        try {
            // Close current window
            Stage currentStage = getCurrentStage();
            if (currentStage != null) {
                currentStage.close();
            }

            // Load admin home dashboard
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AdminHome.fxml"));
            Parent root = loader.load();

            Stage dashboardStage = new Stage();
            dashboardStage.setTitle("Administration Home");
            try {
                Image icon = new Image(getClass().getResourceAsStream("/img/baga.png"));
                dashboardStage.getIcons().add(icon);
            } catch (Exception e) {
                logger.debug("Could not load window icon", e);
            }

            Scene scene = new Scene(root);
            dashboardStage.setScene(scene);
            dashboardStage.show();

            logger.info("Navigated to Admin Home dashboard");
        } catch (Exception e) {
            logger.error("Failed to navigate to Admin Home dashboard", e);
            showErrorNotification("Navigation Error", "Could not open the dashboard page.");
        }
    }

    /**
     * Navigate back to the home screen.
     */
    @FXML
    private void goToHome() {
        logger.info("Navigating back to home screen from admin login");
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
