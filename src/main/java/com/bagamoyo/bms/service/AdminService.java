package com.bagamoyo.bms.service;

import com.bagamoyo.bms.model.Admin;
import com.bagamoyo.bms.util.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Admin account management service.
 * Handles admin creation, deletion, and password changes with BCrypt hashing.
 * All database operations use try-with-resources via the Database utility.
 */
public class AdminService {

    private static final Logger logger = LoggerFactory.getLogger(AdminService.class);

    private final Database database;
    private final AuthService authService;

    public AdminService() {
        this.database = Database.getInstance();
        this.authService = new AuthService();
    }

    // ========================================================================
    // CREATE
    // ========================================================================

    /**
     * Create a new admin account with a BCrypt-hashed password.
     *
     * @param username      the admin username
     * @param plainPassword the plaintext password (will be hashed)
     * @return true if successfully created
     */
    public boolean createAdmin(String username, String plainPassword) {
        return createAdmin(username, plainPassword, null, null);
    }

    /**
     * Create a new admin account with full details and a BCrypt-hashed password.
     *
     * @param username      the admin username
     * @param plainPassword the plaintext password (will be hashed)
     * @param fullName      the admin's full name
     * @param email         the admin's email address
     * @return true if successfully created
     */
    public boolean createAdmin(String username, String plainPassword, String fullName, String email) {
        if (adminExists(username)) {
            logger.warn("Cannot create admin: username already exists: {}", username);
            return false;
        }

        String sql = "INSERT INTO admin (username, password) VALUES (?, ?)";
        String hashedPassword = AuthService.hashPassword(plainPassword);

        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);

            stmt.execute();
            logger.info("Admin account created: {} (fullName: {}, email: {})", username, fullName, email);
            return true;
        } catch (SQLException e) {
            logger.error("Failed to create admin account: {}", username, e);
            return false;
        }
    }

    // ========================================================================
    // READ
    // ========================================================================

    /**
     * Retrieve all admin usernames from the database.
     */
    public List<String> getAllAdminUsernames() {
        List<String> usernames = new ArrayList<>();
        String sql = "SELECT username FROM admin ORDER BY username";

        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                usernames.add(rs.getString("username"));
            }
            logger.debug("Retrieved {} admin usernames", usernames.size());
        } catch (SQLException e) {
            logger.error("Failed to retrieve admin usernames", e);
        }
        return usernames;
    }

    // ========================================================================
    // DELETE
    // ========================================================================

    /**
     * Delete an admin account by username.
     *
     * @return true if successfully deleted
     */
    public boolean deleteAdmin(String username) {
        String sql = "DELETE FROM admin WHERE username = ?";

        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                logger.info("Admin account deleted: {}", username);
                return true;
            }
            logger.warn("No admin found to delete: {}", username);
            return false;
        } catch (SQLException e) {
            logger.error("Failed to delete admin account: {}", username, e);
            return false;
        }
    }

    // ========================================================================
    // PASSWORD MANAGEMENT
    // ========================================================================

    /**
     * Change an admin's password. Verifies the old password before updating.
     *
     * @param username         the admin username
     * @param oldPlainPassword the current plaintext password
     * @param newPlainPassword the new plaintext password
     * @return true if password was changed successfully
     */
    public boolean changePassword(String username, String oldPlainPassword, String newPlainPassword) {
        // Verify old password first
        if (!authService.validateAdmin(username, oldPlainPassword)) {
            logger.warn("Password change failed for {}: incorrect old password", username);
            return false;
        }

        String sql = "UPDATE admin SET password = ? WHERE username = ?";
        String hashedNewPassword = AuthService.hashPassword(newPlainPassword);

        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, hashedNewPassword);
            stmt.setString(2, username);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                logger.info("Password changed for admin: {}", username);
                return true;
            }
            logger.warn("Password change failed: admin not found: {}", username);
            return false;
        } catch (SQLException e) {
            logger.error("Failed to change password for admin: {}", username, e);
            return false;
        }
    }

    /**
     * Force-reset an admin's password without verifying the old one.
     * Useful for admin-level password recovery.
     *
     * @param username         the admin username
     * @param newPlainPassword the new plaintext password
     * @return true if password was reset successfully
     */
    public boolean resetPassword(String username, String newPlainPassword) {
        String sql = "UPDATE admin SET password = ? WHERE username = ?";
        String hashedNewPassword = AuthService.hashPassword(newPlainPassword);

        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, hashedNewPassword);
            stmt.setString(2, username);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                logger.info("Password reset for admin: {}", username);
                return true;
            }
            logger.warn("Password reset failed: admin not found: {}", username);
            return false;
        } catch (SQLException e) {
            logger.error("Failed to reset password for admin: {}", username, e);
            return false;
        }
    }

    // ========================================================================
    // EXISTENCE CHECK
    // ========================================================================

    /**
     * Check if an admin username exists.
     */
    public boolean adminExists(String username) {
        String sql = "SELECT COUNT(*) FROM admin WHERE username = ?";

        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            logger.error("Failed to check admin existence: {}", username, e);
        }
        return false;
    }

    /**
     * Count total number of admin accounts.
     */
    public long countAdmins() {
        String sql = "SELECT COUNT(*) FROM admin";

        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            logger.error("Failed to count admins", e);
        }
        return 0;
    }
}
