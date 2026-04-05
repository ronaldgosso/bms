package com.bagamoyo.bms.controller;

import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.textfield.TextFields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Modern web browser controller using JavaFX WebView.
 * Features navigation buttons, preset URLs, URL bar with auto-complete,
 * and a modern browser UI.
 */
public class WebBrowserController {

    private static final Logger logger = LoggerFactory.getLogger(WebBrowserController.class);

    // UI Components
    @FXML private WebView webView;
    @FXML private TextField urlField;
    @FXML private Button backBtn;
    @FXML private Button forwardBtn;
    @FXML private Button refreshBtn;
    @FXML private Button homeBtn;
    @FXML private Button searchBtn;
    @FXML private Label titleLabel;
    @FXML private ProgressBar progressBar;
    @FXML private ImageView logoView;

    // Browser state
    private WebEngine webEngine;
    private WebHistory history;
    private boolean isLoading;

    // Scheduler for background tasks
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    // Preset URLs
    private static final String HOME_URL = "https://www.google.com";
    private static final String[] PRESET_URLS = {
            "https://www.bbc.com/swahili",
            "https://globalpublishers.co.tz/",
            "http://pwani.go.tz/",
            "https://www.google.com",
            "http://www.ilovepdf.com/",
            "https://www.tanzania.go.tz/",
            "https://www.parliament.go.tz/",
            "https://www.irs.go.tz/",
            "https://www.bot.go.tz/"
    };

    // Auto-complete suggestions
    private static final String[] URL_SUGGESTIONS = {
            "google.com", "bbc.com/swahili", "government.go.tz",
            "pwani.go.tz", "parliament.go.tz", "tra.go.tz",
            "bot.go.tz", "ilovepdf.com", "tanzania.go.tz",
            "globalpublishers.co.tz", "yahoo.com", "bing.com"
    };

    // ========================================================================
    // INITIALIZATION
    // ========================================================================

    @FXML
    public void initialize() {
        setupWebView();
        setupAutoCompletion();
        setupNavigationButtons();
        setupLoadingListener();
        loadHomePage();
        logger.info("WebBrowserController initialized");
    }

    private void setupWebView() {
        if (webView == null) return;

        webEngine = webView.getEngine();
        history = webEngine.getHistory();

        // Enable JavaScript
        webEngine.setJavaScriptEnabled(true);

        // Handle page title changes
        webEngine.titleProperty().addListener((obs, oldTitle, newTitle) -> {
            if (titleLabel != null && newTitle != null) {
                Platform.runLater(() -> titleLabel.setText(newTitle));
            }
        });

        // Handle URL changes
        webEngine.locationProperty().addListener((obs, oldLocation, newLocation) -> {
            if (urlField != null && newLocation != null) {
                Platform.runLater(() -> urlField.setText(newLocation));
            }
        });

        // Handle errors
        webEngine.getLoadWorker().exceptionProperty().addListener((obs, oldException, newException) -> {
            if (newException != null) {
                logger.error("Web engine error", newException);
                showErrorNotification("Loading Error", "Failed to load the page: " + newException.getMessage());
            }
        });
    }

    private void setupAutoCompletion() {
        if (urlField != null) {
            TextFields.bindAutoCompletion(urlField, URL_SUGGESTIONS);
        }
    }

    private void setupNavigationButtons() {
        updateNavigationButtons();

        if (history != null) {
            history.currentIndexProperty().addListener((obs, oldIndex, newIndex) -> updateNavigationButtons());
        }
    }

    private void setupLoadingListener() {
        if (webEngine != null) {
            webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
                switch (newState) {
                    case SCHEDULED:
                    case RUNNING:
                        setIsLoading(true);
                        break;
                    case SUCCEEDED:
                        setIsLoading(false);
                        break;
                    case FAILED:
                        setIsLoading(false);
                        break;
                    default:
                        break;
                }
            });
        }
    }

    // ========================================================================
    // NAVIGATION
    // ========================================================================

    @FXML
    private void handleBack() {
        if (history != null && history.getCurrentIndex() > 0) {
            history.go(-1);
            logger.debug("Navigated back");
        }
    }

    @FXML
    private void handleForward() {
        if (history != null && history.getCurrentIndex() < history.getEntries().size() - 1) {
            history.go(1);
            logger.debug("Navigated forward");
        }
    }

    @FXML
    private void handleRefresh() {
        if (webEngine != null) {
            webEngine.reload();
            showInfoNotification("Refreshing", "Reloading current page");
            logger.debug("Page refreshed");
        }
    }

    @FXML
    private void handleHome() {
        navigateTo(HOME_URL);
    }

    @FXML
    private void handleSearch() {
        String input = urlField != null ? urlField.getText().trim() : "";

        if (input.isEmpty()) {
            navigateTo(HOME_URL);
            return;
        }

        // Check if input looks like a URL
        if (input.contains(".") && !input.contains(" ")) {
            // Add protocol if missing
            if (!input.startsWith("http://") && !input.startsWith("https://")) {
                input = "https://" + input;
            }
            navigateTo(input);
        } else {
            // Treat as search query
            String searchUrl = "https://www.google.com/search?q=" + input.replace(" ", "+");
            navigateTo(searchUrl);
        }
    }

    @FXML
    private void handleUrlEntered() {
        handleSearch();
    }

    // ========================================================================
    // PRESET URL NAVIGATION
    // ========================================================================

    @FXML
    private void handleBbcSwahili() {
        navigateTo("https://www.bbc.com/swahili");
    }

    @FXML
    private void handleGlobalPublishers() {
        navigateTo("https://globalpublishers.co.tz/");
    }

    @FXML
    private void handlePwaniGovernment() {
        navigateTo("http://pwani.go.tz/");
    }

    @FXML
    private void handlePdfConverter() {
        navigateTo("http://www.ilovepdf.com/");
    }

    @FXML
    private void handleTanzaniaGovernment() {
        navigateTo("https://www.tanzania.go.tz/");
    }

    @FXML
    private void handleParliament() {
        navigateTo("https://www.parliament.go.tz/");
    }

    @FXML
    private void handleRevenueAuthority() {
        navigateTo("https://www.irs.go.tz/");
    }

    @FXML
    private void handleCentralBank() {
        navigateTo("https://www.bot.go.tz/");
    }

    // ========================================================================
    // CORE NAVIGATION
    // ========================================================================

    private void navigateTo(String url) {
        if (webEngine == null) return;

        logger.info("Navigating to: {}", url);
        setIsLoading(true);

        // Schedule the actual navigation on the JavaFX Application Thread
        Platform.runLater(() -> {
            webEngine.load(url);
        });
    }

    private void loadHomePage() {
        navigateTo(HOME_URL);
        if (titleLabel != null) {
            titleLabel.setText("Bagamoyo Browser - Home");
        }
    }

    // ========================================================================
    // UI STATE
    // ========================================================================

    private void setIsLoading(boolean loading) {
        isLoading = loading;
        Platform.runLater(() -> {
            if (progressBar != null) {
                progressBar.setVisible(loading);
                if (loading) {
                    progressBar.setProgress(-1); // Indeterminate
                }
            }
            updateNavigationButtons();
        });
    }

    private void updateNavigationButtons() {
        Platform.runLater(() -> {
            if (backBtn != null) {
                backBtn.setDisable(history == null || history.getCurrentIndex() <= 0);
            }
            if (forwardBtn != null) {
                int currentIndex = history != null ? history.getCurrentIndex() : -1;
                int size = history != null ? history.getEntries().size() : 0;
                forwardBtn.setDisable(history == null || currentIndex >= size - 1);
            }
        });
    }

    // ========================================================================
    // EXIT
    // ========================================================================

    @FXML
    private void handleExit() {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Exit Browser");
        confirmDialog.setHeaderText("Close Browser");
        confirmDialog.setContentText("Are you sure you want to exit the browser?");

        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            logger.info("Browser closed by user");
            cleanup();
            // Hide the window (caller should handle stage closing)
            if (webView != null && webView.getScene() != null && webView.getScene().getWindow() != null) {
                webView.getScene().getWindow().hide();
            }
        }
    }

    // ========================================================================
    // SHUTDOWN
    // ========================================================================

    /**
     * Clean up resources
     */
    public void cleanup() {
        scheduler.shutdownNow();
        if (webEngine != null) {
            webEngine.load(null);
        }
        logger.info("WebBrowserController cleanup complete");
    }

    // ========================================================================
    // NOTIFICATIONS
    // ========================================================================

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
