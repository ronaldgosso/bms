package com.bagamoyo.bms;

import javafx.animation.FadeTransition;
import javafx.application.Preloader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Modern JavaFX preloader for the Bagamoyo District Council Management System.
 * Displays a professional loading screen with progress indication during application startup.
 */
public class Preloader extends Preloader {

    private static final Logger logger = LoggerFactory.getLogger(Preloader.class);

    private static final double WIDTH = 500;
    private static final double HEIGHT = 300;

    private ProgressBar progressBar;
    private Stage stage;
    private Scene preloaderScene;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        logger.debug("Initializing preloader UI");

        preloaderScene = createPreloaderScene();

        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(preloaderScene);
        stage.setAlwaysOnTop(true);
        stage.show();

        logger.info("Preloader displayed");
    }

    private Scene createPreloaderScene() {
        // Background panel with rounded corners
        var background = new Rectangle(WIDTH, HEIGHT);
        background.setArcWidth(20);
        background.setArcHeight(20);
        background.setFill(Color.valueOf("#1a1a2e"));
        background.setStroke(Color.valueOf("#16213e"));
        background.setStrokeWidth(2);

        // Title text
        var titleText = new Text("Bagamoyo District Council");
        titleText.setFont(Font.font("Segoe UI", 22));
        titleText.setFill(Color.WHITE);

        var subtitleText = new Text("Management System");
        subtitleText.setFont(Font.font("Segoe UI", 20));
        subtitleText.setFill(Color.valueOf("#00d4ff"));

        // Version label
        var versionText = new Text("Version 2.0");
        versionText.setFont(Font.font("Segoe UI", 12));
        versionText.setFill(Color.valueOf("#a0a0a0"));

        // Progress bar
        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(WIDTH - 80);
        progressBar.getStyleClass().addAll("preloader-progress-bar");
        progressBar.setStyle(
                "-fx-accent: #00d4ff; " +
                "-fx-control-inner-background: #2a2a4a; " +
                "-fx-background-radius: 5; " +
                "-fx-padding: 5;");

        // Status text
        var statusText = new Text("Initializing...");
        statusText.setFont(Font.font("Segoe UI", 11));
        statusText.setFill(Color.valueOf("#888888"));
        statusText.getStyleClass().add("preloader-status-text");

        // Layout
        var titleBox = new VBox(4, titleText, subtitleText);
        titleBox.setAlignment(Pos.CENTER);

        var progressBox = new VBox(10, progressBar, statusText);
        progressBox.setAlignment(Pos.CENTER);

        var mainBox = new VBox(20, titleBox, progressBox, versionText);
        mainBox.setAlignment(Pos.CENTER);
        mainBox.setPadding(new javafx.geometry.Insets(30));

        var root = new StackPane(background, mainBox);
        root.setStyle("-fx-background-color: transparent;");

        var scene = new Scene(root, WIDTH, HEIGHT);
        scene.setFill(Color.TRANSPARENT);

        return scene;
    }

    @Override
    public void handleProgressNotification(ProgressNotification pn) {
        if (progressBar != null && pn.getProgress() >= 0) {
            progressBar.setProgress(pn.getProgress());
            logger.trace("Preloader progress updated: {}", String.format("%.0f%%", pn.getProgress() * 100));
        }
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification evt) {
        if (evt.getType() == StateChangeNotification.Type.BEFORE_START) {
            logger.debug("Application ready, hiding preloader");
            hidePreloader();
        }
    }

    private void hidePreloader() {
        if (stage != null && stage.isShowing()) {
            var fadeOut = new FadeTransition(Duration.millis(400), stage.getScene().getRoot());
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(event -> stage.hide());
            fadeOut.play();
        }
    }
}
