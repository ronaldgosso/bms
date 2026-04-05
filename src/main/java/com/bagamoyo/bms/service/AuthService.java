package com.bagamoyo.bms.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.bagamoyo.bms.util.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Authentication service handling login validation and password hashing.
 * Uses BCrypt for secure password storage and validation.
 * All database operations use try-with-resources via the Database utility.
 */
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private static final int BCRYPT_COST = 12;

    private final Database database;

    public AuthService() {
        this.database = Database.getInstance();
    }

    // ========================================================================
    // BCrypt Password Utilities
    // ========================================================================

    /**
     * Hash a plaintext password using BCrypt.
     *
     * @param plainPassword the password to hash
     * @return BCrypt hashed password string
     */
    public static String hashPassword(String plainPassword) {
        return BCrypt.withDefaults().hashToString(BCRYPT_COST, plainPassword.toCharArray());
    }

    /**
     * Verify a plaintext password against a BCrypt hash.
     *
     * @param plainPassword the plaintext password to verify
     * @param bcryptHash    the stored BCrypt hash
     * @return true if the password matches
     */
    public static boolean verifyPassword(String plainPassword, String bcryptHash) {
        BCrypt.Result result = BCrypt.verifyer().verify(plainPassword.toCharArray(), bcryptHash);
        return result.verified;
    }

    // ========================================================================
    // Role-based Login Methods
    // ========================================================================

    /**
     * Attempt login for any role. Returns true if credentials match.
     *
     * @param username the username
     * @param password the plaintext password
     * @param role     one of "admin", "manager", "supervisor"
     * @return true if login succeeds
     */
    public boolean login(String username, String password, String role) {
        String table = getTableForRole(role);
        if (table == null) {
            logger.warn("Login attempt with invalid role: {}", role);
            return false;
        }

        String sql = "SELECT password FROM " + table + " WHERE username = ?";

        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("password");
                    boolean matches = verifyPassword(password, storedPassword);
                    if (matches) {
                        logger.info("Successful {} login for user: {}", role, username);
                    } else {
                        logger.warn("Failed {} login for user: {} - incorrect password", role, username);
                    }
                    return matches;
                } else {
                    logger.warn("Failed {} login - user not found: {}", role, username);
                    return false;
                }
            }
        } catch (SQLException e) {
            logger.error("Database error during {} login for user: {}", role, username, e);
            return false;
        }
    }

    /**
     * Validate manager credentials.
     */
    public boolean validateManager(String username, String password) {
        return login(username, password, "manager");
    }

    /**
     * Validate admin credentials.
     */
    public boolean validateAdmin(String username, String password) {
        return login(username, password, "admin");
    }

    /**
     * Validate supervisor credentials.
     */
    public boolean validateSupervisor(String username, String password) {
        return login(username, password, "supervisor");
    }

    // ========================================================================
    // User Existence & Password Retrieval
    // ========================================================================

    /**
     * Check if a username exists in the given role table.
     */
    public boolean userExists(String username, String role) {
        String table = getTableForRole(role);
        if (table == null) {
            return false;
        }

        String sql = "SELECT COUNT(*) FROM " + table + " WHERE username = ?";

        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            logger.error("Error checking if user exists: {}", username, e);
        }
        return false;
    }

    /**
     * Get the stored BCrypt password hash for a user.
     */
    public Optional<String> getPasswordHash(String username, String role) {
        String table = getTableForRole(role);
        if (table == null) {
            return Optional.empty();
        }

        String sql = "SELECT password FROM " + table + " WHERE username = ?";

        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.ofNullable(rs.getString("password"));
                }
            }
        } catch (SQLException e) {
            logger.error("Error retrieving password hash for user: {}", username, e);
        }
        return Optional.empty();
    }

    // ========================================================================
    // Private Helpers
    // ========================================================================

    /**
     * Map a role name to its database table name.
     */
    private String getTableForRole(String role) {
        String lowerRole = role.toLowerCase();
        switch (lowerRole) {
            case "admin":
            case "manager":
            case "supervisor":
                return lowerRole;
            default:
                logger.warn("Invalid role: {}", role);
                return null;
        }
    }
}
