package com.bagamoyo.bms.controller;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Modern media player controller for video/audio playback.
 * Uses JavaFX Media controls with a modern player UI.
 */
public class MediaPlayerController {

    private static final Logger logger = LoggerFactory.getLogger(MediaPlayerController.class);

    // UI Components
    @FXML private MediaView mediaView;
    @FXML private BorderPane mediaPane;
    @FXML private Slider volumeSlider;
    @FXML private Slider seekSlider;
    @FXML private Label titleLabel;
    @FXML private Label timeLabel;
    @FXML private Button playBtn;
    @FXML private Button pauseBtn;
    @FXML private Button stopBtn;
    @FXML private Button chooseFileBtn;
    @FXML private Button fastBtn;
    @FXML private Button slowBtn;
    @FXML private Button repeatBtn;
    @FXML private Button startBtn;
    @FXML private Button endBtn;
    @FXML private ToggleButton muteToggle;

    // Media state
    private MediaPlayer mediaPlayer;
    private Media media;
    private File currentFile;
    private FileChooser fileChooser;

    // Playback state
    private boolean isPlaying;
    private boolean isRepeat;

    // Supported extensions
    private static final String[] VIDEO_EXTENSIONS = {"*.mp4", "*.flv", "*.m3u8"};
    private static final String[] AUDIO_EXTENSIONS = {"*.mp3", "*.wav", "*.aac", "*.aiff"};

    // ========================================================================
    // INITIALIZATION
    // ========================================================================

    @FXML
    public void initialize() {
        setupFileChooser();
        setupVolumeSlider();
        setupSeekSlider();
        setupMediaView();
        updatePlaybackButtons(false);
        logger.info("MediaPlayerController initialized");
    }

    private void setupFileChooser() {
        fileChooser = new FileChooser();
        fileChooser.setTitle("Open Media File");

        FileChooser.ExtensionFilter allMediaFilter = new FileChooser.ExtensionFilter(
                "Media Files", VIDEO_EXTENSIONS, AUDIO_EXTENSIONS);
        FileChooser.ExtensionFilter videoFilter = new FileChooser.ExtensionFilter(
                "Video Files", VIDEO_EXTENSIONS);
        FileChooser.ExtensionFilter audioFilter = new FileChooser.ExtensionFilter(
                "Audio Files", AUDIO_EXTENSIONS);
        FileChooser.ExtensionFilter allFilesFilter = new FileChooser.ExtensionFilter(
                "All Files", "*.*");

        fileChooser.getExtensionFilters().addAll(allMediaFilter, videoFilter, audioFilter, allFilesFilter);
    }

    private void setupVolumeSlider() {
        if (volumeSlider != null) {
            volumeSlider.setMin(0);
            volumeSlider.setMax(100);
            volumeSlider.setValue(50);

            volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
                if (mediaPlayer != null) {
                    mediaPlayer.setVolume(newVal.doubleValue() / 100.0);
                }
            });
        }
    }

    private void setupSeekSlider() {
        if (seekSlider != null) {
            seekSlider.setMin(0);
            seekSlider.setValue(0);

            seekSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
                if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                    double totalDuration = mediaPlayer.getTotalDuration().toMillis();
                    mediaPlayer.seek(javafx.util.Duration.millis(newVal.doubleValue() * totalDuration / 100.0));
                }
            });
        }
    }

    private void setupMediaView() {
        if (mediaView != null && mediaView.fitWidthProperty() != null) {
            mediaView.fitWidthProperty().bind(
                    Bindings.selectDouble(mediaView.sceneProperty(), "width"));
            mediaView.fitHeightProperty().bind(
                    Bindings.selectDouble(mediaView.sceneProperty(), "height"));
        }
    }

    // ========================================================================
    // FILE SELECTION
    // ========================================================================

    @FXML
    private void handleChooseFile() {
        Stage stage = (mediaView != null) ? (Stage) mediaView.getScene().getWindow() : null;
        if (stage == null) return;

        File file = fileChooser.showOpenDialog(stage);
        if (file == null) {
            return;
        }

        loadMedia(file);
    }

    private void loadMedia(File file) {
        try {
            currentFile = file;
            String uri = file.toURI().toString();

            // Stop current playback
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.dispose();
            }

            media = new Media(uri);
            mediaPlayer = new MediaPlayer(media);

            // Set up media player
            mediaPlayer.setVolume(volumeSlider != null ? volumeSlider.getValue() / 100.0 : 0.5);
            mediaPlayer.setAutoPlay(false);

            if (mediaView != null) {
                mediaView.setMediaPlayer(mediaPlayer);
            }

            // Update UI
            String fileName = file.getName();
            if (titleLabel != null) {
                titleLabel.setText(fileName);
            }

            // Set up seek slider updates
            setupSeekUpdates();

            // Handle errors
            media.errorProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    showErrorNotification("Media Error", "Failed to load media file");
                    logger.error("Media error: {}", newVal.getMessage());
                }
            });

            updatePlaybackButtons(true);
            showSuccessNotification("Media Loaded", "File: " + fileName);
            logger.info("Media loaded: {}", file.getAbsolutePath());

        } catch (Exception e) {
            logger.error("Error loading media file", e);
            showErrorNotification("Error", "Failed to load media file. Use .mp4 for video or .mp3 for audio");
        }
    }

    private void setupSeekUpdates() {
        if (mediaPlayer == null || seekSlider == null) return;

        mediaPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
            if (mediaPlayer.getTotalDuration() != null) {
                double totalDuration = mediaPlayer.getTotalDuration().toMillis();
                double currentTime = newTime.toMillis();
                if (totalDuration > 0) {
                    seekSlider.setValue((currentTime / totalDuration) * 100.0);
                }
            }

            // Update time label
            if (timeLabel != null) {
                timeLabel.setText(formatTime(newTime) + " / " + formatTime(mediaPlayer.getTotalDuration()));
            }
        });
    }

    private String formatTime(javafx.util.Duration duration) {
        if (duration == null || duration.isIndefinite() || duration.isUnknown()) {
            return "00:00";
        }
        int totalSeconds = (int) (duration.toMillis() / 1000);
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    // ========================================================================
    // PLAYBACK CONTROLS
    // ========================================================================

    @FXML
    private void handlePlay() {
        if (mediaPlayer == null) {
            showWarningNotification("No Media", "Please select a media file first");
            return;
        }
        mediaPlayer.play();
        mediaPlayer.setRate(1.0);
        isPlaying = true;
        updatePlaybackState();
        logger.debug("Media playing");
    }

    @FXML
    private void handlePause() {
        if (mediaPlayer == null) return;
        mediaPlayer.pause();
        isPlaying = false;
        updatePlaybackState();
        logger.debug("Media paused");
    }

    @FXML
    private void handleStop() {
        if (mediaPlayer == null) return;
        mediaPlayer.stop();
        isPlaying = false;
        if (seekSlider != null) {
            seekSlider.setValue(0);
        }
        updatePlaybackState();
        logger.debug("Media stopped");
    }

    @FXML
    private void handleFast() {
        if (mediaPlayer == null) return;
        mediaPlayer.setRate(2.0);
        showInfoNotification("Speed", "Playing at 2x speed");
    }

    @FXML
    private void handleSlow() {
        if (mediaPlayer == null) return;
        mediaPlayer.setRate(0.5);
        showInfoNotification("Speed", "Playing at 0.5x speed");
    }

    @FXML
    private void handleRepeat() {
        if (mediaPlayer == null) return;
        isRepeat = !isRepeat;
        mediaPlayer.setCycleCount(isRepeat ? MediaPlayer.INDEFINITE : 1);
        if (repeatBtn != null) {
            repeatBtn.setText(isRepeat ? "Repeat: ON" : "Repeat");
        }
        showInfoNotification("Repeat", isRepeat ? "Repeat enabled" : "Repeat disabled");
    }

    @FXML
    private void handleStart() {
        if (mediaPlayer == null) return;
        mediaPlayer.seek(mediaPlayer.getStartTime());
        mediaPlayer.play();
    }

    @FXML
    private void handleEnd() {
        if (mediaPlayer == null) return;
        mediaPlayer.seek(mediaPlayer.getTotalDuration());
        mediaPlayer.stop();
    }

    @FXML
    private void handleMute() {
        if (mediaPlayer == null || muteToggle == null) return;
        mediaPlayer.setMute(muteToggle.isSelected());
    }

    // ========================================================================
    // DRAG AND DROP SUPPORT
    // ========================================================================

    @FXML
    private void handleDragAndDrop() {
        if (currentFile != null) {
            loadMedia(currentFile);
            if (mediaPlayer != null) {
                mediaPlayer.play();
            }
        }
    }

    // ========================================================================
    // ERROR HANDLING
    // ========================================================================

    @FXML
    private void handleErrorInfo() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Media Player Help");
        alert.setHeaderText("Supported Formats");
        alert.setContentText("Video: .mp4, .flv, .m3u8\nAudio: .mp3, .wav, .aac, .aiff\n\n" +
                "If media fails to load, ensure the file format is supported.");
        alert.showAndWait();
    }

    // ========================================================================
    // VOLUME CONTROL
    // ========================================================================

    @FXML
    private void handleVolumeChanged() {
        if (volumeSlider != null && mediaPlayer != null) {
            mediaPlayer.setVolume(volumeSlider.getValue() / 100.0);
        }
    }

    // ========================================================================
    // UTILITY
    // ========================================================================

    private void updatePlaybackButtons(boolean hasMedia) {
        if (playBtn != null) playBtn.setDisable(!hasMedia);
        if (pauseBtn != null) pauseBtn.setDisable(!hasMedia);
        if (stopBtn != null) stopBtn.setDisable(!hasMedia);
        if (fastBtn != null) fastBtn.setDisable(!hasMedia);
        if (slowBtn != null) slowBtn.setDisable(!hasMedia);
        if (repeatBtn != null) repeatBtn.setDisable(!hasMedia);
        if (startBtn != null) startBtn.setDisable(!hasMedia);
        if (endBtn != null) endBtn.setDisable(!hasMedia);
        if (seekSlider != null) seekSlider.setDisable(!hasMedia);
    }

    private void updatePlaybackState() {
        // Update button states based on playing/paused
    }

    /**
     * Clean up media player resources
     */
    public void cleanup() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
        }
        logger.info("MediaPlayerController cleanup complete");
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
