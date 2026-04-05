package com.bagamoyo.bms;

import com.bagamoyo.bms.util.Database;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.application.Preloader.ProgressNotification;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;

/**
 * Main application entry point for the Bagamoyo District Council Management System.
 *
 * This class initializes the JavaFX application, loads the primary view,
 * applies modern styling, and manages the application lifecycle.
 *
 * @since 2.0
 */
public class BmsApplication extends Application {

    private static final Logger logger = LoggerFactory.getLogger(BmsApplication.class);

    private static final String APP_TITLE = "Bagamoyo District Council Management System v2.0";
    private static final double MIN_WIDTH = 1200;
    private static final double MIN_HEIGHT = 800;
    private static final String FXML_PATH = "/fxml/Home.fxml";
    private static final String STYLESHEET_APPLICATION = "/fxml/application.css";
    private static final String STYLESHEET_BASIC = "/fxml/BasicApplication.css";
    private static final String ICON_PATH = "/img/baga.png";

    private Scene scene;

    /**
     * JavaFX application entry point.
     *
     * @param primaryStage the primary stage for this application
     */
    @Override
    public void start(Stage primaryStage) {
        logger.info("Starting {}", APP_TITLE);

        try {
            var fxmlLocation = Objects.requireNonNull(
                    getClass().getResource(FXML_PATH),
                    "FXML file not found: " + FXML_PATH
            );

            var loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();

            scene = new Scene(root, MIN_WIDTH, MIN_HEIGHT);
            loadStylesheets(scene);

            configureStage(primaryStage);

            logger.info("Application window displayed successfully");

        } catch (IOException e) {
            logger.error("Failed to load application UI: {}", e.getMessage(), e);
            showErrorStage(primaryStage, e);

        } catch (NullPointerException e) {
            logger.error("Required resource not found: {}", e.getMessage(), e);
            showErrorStage(primaryStage, e);

        } catch (Exception e) {
            logger.error("Unexpected error during application startup", e);
            showErrorStage(primaryStage, e);
        }
    }

    /**
     * Load and apply all CSS stylesheets to the scene.
     */
    private void loadStylesheets(Scene scene) {
        var stylesheets = scene.getStylesheets();

        var stylesheetPaths = new String[]{STYLESHEET_APPLICATION, STYLESHEET_BASIC};

        for (var path : stylesheetPaths) {
            var resourceUrl = getClass().getResource(path);
            if (resourceUrl != null) {
                stylesheets.add(resourceUrl.toExternalForm());
                logger.debug("Stylesheet loaded: {}", path);
            } else {
                logger.warn("Stylesheet not found: {}", path);
            }
        }
    }

    /**
     * Configure the primary stage properties.
     */
    private void configureStage(Stage primaryStage) {
        primaryStage.setTitle(APP_TITLE);
        primaryStage.setMinWidth(MIN_WIDTH);
        primaryStage.setMinHeight(MIN_HEIGHT);
        primaryStage.setScene(scene);

        centerStageOnScreen(primaryStage);
        loadApplicationIcon(primaryStage);

        primaryStage.setOnCloseRequest(event -> {
            logger.info("Application shutdown requested");
            cleanupResources();
        });

        primaryStage.show();
    }

    /**
     * Center the stage on the primary screen.
     */
    private void centerStageOnScreen(Stage stage) {
        var screenBounds = Screen.getPrimary().getVisualBounds();
        var screenWidth = screenBounds.getWidth();
        var screenHeight = screenBounds.getHeight();

        var x = (screenWidth - MIN_WIDTH) / 2;
        var y = (screenHeight - MIN_HEIGHT) / 2;

        stage.setX(Math.max(0, x));
        stage.setY(Math.max(0, y));

        logger.debug("Stage centered on screen ({}x{})", screenWidth, screenHeight);
    }

    /**
     * Load the application icon.
     */
    private void loadApplicationIcon(Stage stage) {
        var iconResource = getClass().getResource(ICON_PATH);
        if (iconResource != null) {
            try {
                var icon = new Image(iconResource.toExternalForm());
                stage.getIcons().add(icon);
                logger.debug("Application icon loaded");
            } catch (Exception e) {
                logger.warn("Failed to load application icon: {}", e.getMessage());
            }
        } else {
            logger.warn("Application icon not found: {}", ICON_PATH);
        }
    }

    /**
     * Show an error stage when normal startup fails.
     */
    private void showErrorStage(Stage primaryStage, Exception error) {
        logger.error("Attempting to show error dialog");

        var errorLabel = new javafx.scene.control.Label("Failed to Start Application");
        errorLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #ff4444;");

        var detailLabel = new javafx.scene.control.Label(error.getMessage());
        detailLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #666666;");
        detailLabel.setWrapText(true);
        detailLabel.setMaxWidth(400);

        var layout = new javafx.scene.layout.VBox(15, errorLabel, detailLabel);
        layout.setStyle("-fx-background-color: #ffffff; -fx-padding: 30px; -fx-alignment: center;");

        var errorScene = new javafx.scene.Scene(layout, 500, 200);
        primaryStage.setTitle("Application Error");
        primaryStage.setScene(errorScene);
        primaryStage.setMinWidth(400);
        primaryStage.setMinHeight(150);
        primaryStage.show();
    }

    /**
     * Clean up resources on application shutdown.
     */
    private void cleanupResources() {
        logger.info("Cleaning up application resources");
        Platform.exit();
    }

    /**
     * Static main method - initializes database and launches JavaFX application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        var logger = LoggerFactory.getLogger(BmsApplication.class);
        logger.info("Bagamoyo District Council Management System v2.0 - Starting");

        logger.info("Initializing database connection");

        try {
            Database.getInstance();
            logger.info("Database initialized successfully");
        } catch (Exception e) {
            logger.error("Database initialization failed - application may not function correctly", e);
        }

        launch(args);
    }
}
