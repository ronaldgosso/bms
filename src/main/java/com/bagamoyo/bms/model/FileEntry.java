package com.bagamoyo.bms.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

/**
 * File entry model for file storage in the Bagamoyo District Council Management System.
 *
 * Represents a stored file with its metadata and binary content.
 * Used for document management, file uploads, and attachments within the system.
 */
public class FileEntry {

    private final StringProperty name;
    private final StringProperty fileType;
    private final ObjectProperty<byte[]> content;
    private final ObjectProperty<LocalDateTime> uploadDate;
    private final StringProperty uploadedBy;
    private final StringProperty description;

    // ========================================================================
    // Constructors
    // ========================================================================

    /**
     * Default constructor with empty/default values.
     */
    public FileEntry() {
        this.name = new SimpleStringProperty("");
        this.fileType = new SimpleStringProperty("");
        this.content = new SimpleObjectProperty<>(new byte[0]);
        this.uploadDate = new SimpleObjectProperty<>(LocalDateTime.now());
        this.uploadedBy = new SimpleStringProperty("");
        this.description = new SimpleStringProperty("");
    }

    /**
     * Constructor with name and content.
     */
    public FileEntry(String name, byte[] content) {
        this.name = new SimpleStringProperty(name != null ? name : "");
        this.fileType = new SimpleStringProperty(detectFileType(name));
        this.content = new SimpleObjectProperty<>(content != null ? content : new byte[0]);
        this.uploadDate = new SimpleObjectProperty<>(LocalDateTime.now());
        this.uploadedBy = new SimpleStringProperty("");
        this.description = new SimpleStringProperty("");
    }

    /**
     * Full constructor with all fields.
     */
    public FileEntry(String name, String fileType, byte[] content,
                     LocalDateTime uploadDate, String uploadedBy, String description) {
        this.name = new SimpleStringProperty(name != null ? name : "");
        this.fileType = new SimpleStringProperty(fileType != null ? fileType : detectFileType(name));
        this.content = new SimpleObjectProperty<>(content != null ? content : new byte[0]);
        this.uploadDate = new SimpleObjectProperty<>(uploadDate != null ? uploadDate : LocalDateTime.now());
        this.uploadedBy = new SimpleStringProperty(uploadedBy != null ? uploadedBy : "");
        this.description = new SimpleStringProperty(description != null ? description : "");
    }

    // ========================================================================
    // Getters
    // ========================================================================

    public String getName() {
        return name.get();
    }

    public String getFileType() {
        return fileType.get();
    }

    public byte[] getContent() {
        return content.get();
    }

    public LocalDateTime getUploadDate() {
        return uploadDate.get();
    }

    public String getUploadedBy() {
        return uploadedBy.get();
    }

    public String getDescription() {
        return description.get();
    }

    // ========================================================================
    // Setters
    // ========================================================================

    public void setName(String name) {
        this.name.set(name != null ? name : "");
    }

    public void setFileType(String fileType) {
        this.fileType.set(fileType != null ? fileType : "");
    }

    public void setContent(byte[] content) {
        this.content.set(content != null ? content : new byte[0]);
    }

    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate.set(uploadDate);
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy.set(uploadedBy != null ? uploadedBy : "");
    }

    public void setDescription(String description) {
        this.description.set(description != null ? description : "");
    }

    // ========================================================================
    // Property Accessors
    // ========================================================================

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty fileTypeProperty() {
        return fileType;
    }

    public ObjectProperty<byte[]> contentProperty() {
        return content;
    }

    public ObjectProperty<LocalDateTime> uploadDateProperty() {
        return uploadDate;
    }

    public StringProperty uploadedByProperty() {
        return uploadedBy;
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    // ========================================================================
    // Utility Methods
    // ========================================================================

    /**
     * Returns the file size in bytes.
     */
    public long getFileSize() {
        byte[] data = content.get();
        return data != null ? data.length : 0;
    }

    /**
     * Returns a human-readable file size (e.g., "1.5 MB").
     */
    public String getFormattedFileSize() {
        long bytes = getFileSize();
        if (bytes < 1024) return bytes + " B";
        long kb = bytes / 1024;
        if (kb < 1024) return kb + " KB";
        long mb = kb / 1024;
        if (mb < 1024) return mb + " MB";
        long gb = mb / 1024;
        return gb + " GB";
    }

    // ========================================================================
    // Object Methods
    // ========================================================================

    @Override
    public String toString() {
        return "FileEntry{" +
                "name='" + name.get() + '\'' +
                ", fileType='" + fileType.get() + '\'' +
                ", size=" + getFormattedFileSize() +
                ", uploadDate=" + uploadDate.get() +
                ", uploadedBy='" + uploadedBy.get() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileEntry fileEntry = (FileEntry) o;
        return Objects.equals(name.get(), fileEntry.name.get()) &&
                Objects.equals(fileType.get(), fileEntry.fileType.get()) &&
                Arrays.equals(content.get(), fileEntry.content.get()) &&
                Objects.equals(uploadDate.get(), fileEntry.uploadDate.get()) &&
                Objects.equals(uploadedBy.get(), fileEntry.uploadedBy.get()) &&
                Objects.equals(description.get(), fileEntry.description.get());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(
                name.get(), fileType.get(), uploadDate.get(),
                uploadedBy.get(), description.get()
        );
        result = 31 * result + Arrays.hashCode(content.get());
        return result;
    }

    // ========================================================================
    // Private Helper Methods
    // ========================================================================

    /**
     * Detects file type from file extension.
     */
    private static String detectFileType(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            return "unknown";
        }
        int lastDot = fileName.lastIndexOf('.');
        if (lastDot > 0 && lastDot < fileName.length() - 1) {
            return fileName.substring(lastDot + 1).toLowerCase();
        }
        return "unknown";
    }
}
