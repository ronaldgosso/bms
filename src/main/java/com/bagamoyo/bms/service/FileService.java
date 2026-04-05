package com.bagamoyo.bms.service;

import com.bagamoyo.bms.model.FileEntry;
import com.bagamoyo.bms.util.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * File upload/download/delete service.
 * Stores files as BLOBs in the database file_storage table.
 * All database operations use try-with-resources via the Database utility.
 */
public class FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileService.class);

    private final Database database;

    public FileService() {
        this.database = Database.getInstance();
    }

    // ========================================================================
    // CREATE
    // ========================================================================

    /**
     * Upload a file to the database.
     *
     * @param fileName    the name of the file
     * @param inputStream the file content as an InputStream
     * @return true if successfully uploaded
     */
    public boolean uploadFile(String fileName, InputStream inputStream) {
        String sql = "INSERT INTO file_storage (name, file_content) VALUES (?, ?)";

        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, fileName);
            stmt.setBinaryStream(2, inputStream);

            stmt.execute();
            logger.info("File uploaded: {}", fileName);
            return true;
        } catch (SQLException e) {
            logger.error("Failed to upload file: {}", fileName, e);
            return false;
        }
    }

    /**
     * Upload a file from a byte array.
     *
     * @param fileName the name of the file
     * @param content  the file content as bytes
     * @return true if successfully uploaded
     */
    public boolean uploadFile(String fileName, byte[] content) {
        try (InputStream inputStream = new ByteArrayInputStream(content)) {
            return uploadFile(fileName, inputStream);
        } catch (IOException e) {
            logger.error("Failed to create input stream for file: {}", fileName, e);
            return false;
        }
    }

    // ========================================================================
    // READ
    // ========================================================================

    /**
     * Download a file from the database as a byte array.
     *
     * @param fileName the name of the file to download
     * @return Optional containing the file bytes, or empty if not found
     */
    public Optional<byte[]> downloadFile(String fileName) {
        String sql = "SELECT file_content FROM file_storage WHERE name = ?";

        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, fileName);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Blob blob = rs.getBlob("file_content");
                    if (blob != null) {
                        byte[] bytes = blob.getBytes(1, (int) blob.length());
                        logger.info("File downloaded: {} ({} bytes)", fileName, bytes.length);
                        return Optional.of(bytes);
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Failed to download file: {}", fileName, e);
        }
        logger.warn("File not found: {}", fileName);
        return Optional.empty();
    }

    /**
     * Download a file and write it directly to an OutputStream.
     *
     * @param fileName     the name of the file
     * @param outputStream the destination output stream
     * @return true if the file was found and written
     */
    public boolean downloadFileToStream(String fileName, OutputStream outputStream) {
        String sql = "SELECT file_content FROM file_storage WHERE name = ?";

        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, fileName);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    try (InputStream inputStream = rs.getBinaryStream("file_content")) {
                        if (inputStream != null) {
                            byte[] buffer = new byte[8192];
                            int bytesRead;
                            while ((bytesRead = inputStream.read(buffer)) > 0) {
                                outputStream.write(buffer, 0, bytesRead);
                            }
                            logger.info("File streamed to output: {}", fileName);
                            return true;
                        }
                    }
                }
            }
        } catch (SQLException | IOException e) {
            logger.error("Failed to stream file: {}", fileName, e);
        }
        logger.warn("File not found for streaming: {}", fileName);
        return false;
    }

    /**
     * Get a file entry with all metadata.
     */
    public Optional<FileEntry> getFileEntry(String fileName) {
        String sql = "SELECT name, file_content, upload_date FROM file_storage WHERE name = ?";

        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, fileName);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    FileEntry entry = mapResultSetToFileEntry(rs);
                    return Optional.of(entry);
                }
            }
        } catch (SQLException e) {
            logger.error("Failed to retrieve file entry: {}", fileName, e);
        }
        return Optional.empty();
    }

    /**
     * Get all file entries with metadata.
     */
    public List<FileEntry> getAllFileEntries() {
        List<FileEntry> entries = new ArrayList<>();
        String sql = "SELECT name, upload_date FROM file_storage ORDER BY upload_date DESC";

        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                FileEntry entry = new FileEntry();
                entry.setName(rs.getString("name"));
                Timestamp uploadTimestamp = rs.getTimestamp("upload_date");
                if (uploadTimestamp != null) {
                    entry.setUploadDate(uploadTimestamp.toLocalDateTime());
                }
                entries.add(entry);
            }
            logger.debug("Retrieved {} file entries from database", entries.size());
        } catch (SQLException e) {
            logger.error("Failed to retrieve file entries", e);
        }
        return entries;
    }

    /**
     * Get all file names stored in the database.
     */
    public List<String> getAllFiles() {
        List<String> fileNames = new ArrayList<>();
        String sql = "SELECT name FROM file_storage ORDER BY upload_date DESC";

        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                fileNames.add(rs.getString("name"));
            }
            logger.debug("Retrieved {} file names from database", fileNames.size());
        } catch (SQLException e) {
            logger.error("Failed to retrieve file list", e);
        }
        return fileNames;
    }

    // ========================================================================
    // DELETE
    // ========================================================================

    /**
     * Delete a file from the database by name.
     *
     * @return true if successfully deleted
     */
    public boolean deleteFile(String fileName) {
        String sql = "DELETE FROM file_storage WHERE name = ?";

        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, fileName);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                logger.info("File deleted: {}", fileName);
                return true;
            }
            logger.warn("No file found to delete: {}", fileName);
            return false;
        } catch (SQLException e) {
            logger.error("Failed to delete file: {}", fileName, e);
            return false;
        }
    }

    // ========================================================================
    // UTILITY
    // ========================================================================

    /**
     * Check if a file with the given name exists.
     */
    public boolean fileExists(String fileName) {
        String sql = "SELECT COUNT(*) FROM file_storage WHERE name = ?";

        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, fileName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            logger.error("Failed to check file existence: {}", fileName, e);
        }
        return false;
    }

    /**
     * Get the size of a file in bytes.
     */
    public Optional<Long> getFileSize(String fileName) {
        String sql = "SELECT file_content FROM file_storage WHERE name = ?";

        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, fileName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Blob blob = rs.getBlob("file_content");
                    if (blob != null) {
                        return Optional.of(blob.length());
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Failed to get file size: {}", fileName, e);
        }
        return Optional.empty();
    }

    /**
     * Count total number of files.
     */
    public long countFiles() {
        String sql = "SELECT COUNT(*) FROM file_storage";

        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            logger.error("Failed to count files", e);
        }
        return 0;
    }

    // ========================================================================
    // PRIVATE HELPERS
    // ========================================================================

    private FileEntry mapResultSetToFileEntry(ResultSet rs) throws SQLException {
        FileEntry entry = new FileEntry();
        entry.setName(rs.getString("name"));

        Blob blob = rs.getBlob("file_content");
        if (blob != null) {
            byte[] content = blob.getBytes(1, (int) blob.length());
            entry.setContent(content);
            entry.setFileType(detectFileType(entry.getName()));
        }

        Timestamp uploadTimestamp = rs.getTimestamp("upload_date");
        if (uploadTimestamp != null) {
            entry.setUploadDate(uploadTimestamp.toLocalDateTime());
        }

        return entry;
    }

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
