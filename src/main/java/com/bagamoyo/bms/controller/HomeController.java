package com.bagamoyo.bms.controller;

import com.bagamoyo.bms.service.WorkerService;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * Main home/landing page controller for the Bagamoyo District Council Management System.
 * Displays institution statistics via a pie chart and provides navigation to login screens
 * for three roles: System Admin, Admin, and Supervisor.
 */
public class HomeController implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    // FXML-injected components
    @FXML
    private MediaView media;

    @FXML
    private ToggleButton turnOffSlider;

    @FXML
    private Label date;

    @FXML
    private PieChart pie;

    @FXML
    private AnchorPane root;

    @FXML
    private BorderPane videoPane;

    private MediaPlayer mediaPlayer;
    private WorkerService workerService;
    private boolean isVideoPaused = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logger.info("Initializing HomeController");
        workerService = new WorkerService();

        setupCurrentDate();
        setupPieChart();
        setupVideoPlayer();
        setupToggleButton();
    }

    /**
     * Display the current date on the home screen.
     */
    private void setupCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
        Date today = Calendar.getInstance().getTime();
        String formattedDate = dateFormat.format(today) + "  " + dayFormat.format(today);
        date.setText(formattedDate);
        logger.debug("Current date displayed: {}", formattedDate);
    }

    /**
     * Populate the pie chart with institution statistics from the database.
     */
    private void setupPieChart() {
        try {
            long totalWorkers = workerService.countWorkers();
            logger.info("Total workers retrieved for statistics: {}", totalWorkers);

            // Fetch workers by department to build statistics
            ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();

            // Search by common departments/wards and add to pie chart
            addDepartmentSlice(pieData, "Secondary School");
            addDepartmentSlice(pieData, "Primary School");
            addDepartmentSlice(pieData, "Ward");
            addDepartmentSlice(pieData, "Hospital");
            addDepartmentSlice(pieData, "Dispensary");

            // If no data found, show placeholder
            if (pieData.isEmpty()) {
                pieData.addAll(
                        new PieChart.Data("Secondary Schools", 0),
                        new PieChart.Data("Primary Schools", 0),
                        new PieChart.Data("Wards", 0),
                        new PieChart.Data("Hospital", 0),
                        new PieChart.Data("Dispensaries", 0)
                );
                logger.warn("No department data found; showing empty pie chart");
            }

            pie.setData(pieData);
            pie.setTitle("Statistics Summary");
            logger.debug("Pie chart populated with {} categories", pieData.size());
        } catch (Exception e) {
            logger.error("Failed to load statistics for pie chart", e);
            showErrorNotification("Statistics Error", "Could not load department statistics.");
        }
    }

    /**
     * Helper to add a department slice to the pie chart.
     */
    private void addDepartmentSlice(ObservableList<PieChart.Data> pieData, String department) {
        try {
            long count = workerService.searchByDepartment(department).size();
            if (count > 0) {
                pieData.add(new PieChart.Data(department + "s", count));
            }
        } catch (Exception e) {
            logger.debug("No data for department: {}", department);
        }
    }

    /**
     * Set up the background video player (optional feature).
     * Video will autoplay in a loop if the file exists.
     */
    private void setupVideoPlayer() {
        try {
            String videoPath = new java.io.File("resources/video/baga.mp4").getAbsolutePath();
            java.io.File videoFile = new java.io.File(videoPath);

            if (!videoFile.exists()) {
                logger.info("Background video file not found at: {}. Video feature disabled.", videoPath);
                return;
            }

            Media mediaFile = new Media(videoFile.toURI().toString());
            mediaPlayer = new MediaPlayer(mediaFile);
            media.setMediaPlayer(mediaPlayer);
            mediaPlayer.setAutoPlay(true);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

            // Fade in animation for media view
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(1.5), media);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();

            logger.info("Background video player initialized");
        } catch (Exception e) {
            logger.warn("Failed to initialize background video player. This is optional.", e);
        }
    }

    /**
     * Set up the toggle button to pause/resume the background video.
     */
    private void setupToggleButton() {
        if (turnOffSlider != null) {
            turnOffSlider.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (mediaPlayer != null) {
                    if (newValue) {
                        mediaPlayer.pause();
                        isVideoPaused = true;
                        logger.debug("Video paused");
                    } else {
                        mediaPlayer.seek(Duration.ZERO);
                        mediaPlayer.play();
                        isVideoPaused = false;
                        logger.debug("Video resumed");
                    }
                }
            });
        }
    }

    // ========================================================================
    // Navigation Handlers
    // ========================================================================

    /**
     * Navigate to the System Admin login screen.
     */
    @FXML
    private void sysAdmin(javafx.event.ActionEvent event) {
        logger.info("Navigating to System Admin login");
        navigateTo("/fxml/SysAdm.fxml", "System Administrator Log In Panel");
    }

    /**
     * Navigate to the Admin login screen.
     */
    @FXML
    private void admin(javafx.event.ActionEvent event) {
        logger.info("Navigating to Admin login");
        navigateTo("/fxml/Admin.fxml", "Administration Log In Panel");
    }

    /**
     * Navigate to the Supervisor login screen.
     */
    @FXML
    private void supervisor(javafx.event.ActionEvent event) {
        logger.info("Navigating to Supervisor login");
        navigateTo("/fxml/LogSql.fxml", "Supervisor Log In Panel");
    }

    /**
     * Open the contacts page.
     */
    @FXML
    private void contactsPage(javafx.event.ActionEvent event) {
        logger.info("Opening contacts page");
        openNewWindow("/fxml/Contacts.fxml", "Feel Free, Make Contacts With Us");
    }

    /**
     * Launch the system web browser.
     */
    @FXML
    private void webLauncher(javafx.event.ActionEvent event) {
        logger.info("Launching system web browser");
        openNewWindow("/fxml/Webview.fxml", "Bagamoyo System Web Browser");
        showInfoNotification("Web Browser", "Welcome to the System Web Browser");
    }

    /**
     * Launch the media player.
     */
    @FXML
    private void mediaPlayer(javafx.event.ActionEvent event) {
        logger.info("Launching media player");
        openNewWindow("/fxml/Player.fxml", "System Media Player");
        showInfoNotification("Media Player", "Welcome to the System Media Player");
    }

    /**
     * Exit the application gracefully.
     */
    @FXML
    private void exitApp(javafx.event.ActionEvent event) {
        logger.info("Application exit requested by user");
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }
        Platform.exit();
    }

    /**
     * Navigate back to the home screen from another view.
     */
    @FXML
    private void logOutToHome(javafx.event.ActionEvent event) {
        logger.info("Returning to home screen");
        navigateTo("/fxml/Home.fxml", "Welcome to Bagamoyo District Council Management System");
    }

    // ========================================================================
    // Private Navigation Helpers
    // ========================================================================

    /**
     * Navigate to a new FXML view, closing the current window.
     *
     * @param fxmlPath the FXML resource path
     * @param title    the window title
     */
    private void navigateTo(String fxmlPath, String title) {
        try {
            Stage currentStage = (Stage) pie.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            logger.debug("Could not close current window, opening new one instead", e);
        }

        openNewWindow(fxmlPath, title);
    }

    /**
     * Open a new window with the given FXML view.
     *
     * @param fxmlPath the FXML resource path
     * @param title    the window title
     */
    private void openNewWindow(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle(title);

            // Try to load the app icon
            try {
                Image icon = new Image(getClass().getResourceAsStream("/img/baga.png"));
                stage.getIcons().add(icon);
            } catch (Exception e) {
                logger.debug("Could not load window icon", e);
            }

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            logger.debug("New window opened: {}", title);
        } catch (IOException e) {
            logger.error("Failed to load FXML: {}", fxmlPath, e);
            showErrorNotification("Navigation Error", "Could not open the requested page: " + fxmlPath);
        }
    }

    // ========================================================================
    // Notification Helpers
    // ========================================================================

    /**
     * Show a dark-style notification at the top-right of the screen.
     */
    private void showInfoNotification(String title, String message) {
        Platform.runLater(() -> {
            Image icon = loadImage("/img/baga.png");
            Notifications.create()
                    .graphic(new ImageView(icon))
                    .title(title)
                    .text(message)
                    .hideAfter(Duration.seconds(5))
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
                    .hideAfter(Duration.seconds(8))
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

    /**
     * Show an alert dialog (fallback when notifications are not sufficient).
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(type);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}
