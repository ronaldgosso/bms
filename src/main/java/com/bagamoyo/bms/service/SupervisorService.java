package com.bagamoyo.bms.service;

import com.bagamoyo.bms.model.Supervisor;
import com.bagamoyo.bms.util.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Supervisor CRUD service for the supervisor table.
 * All database operations use try-with-resources via the Database utility.
 * Passwords are stored as BCrypt hashes.
 */
public class SupervisorService {

    private static final Logger logger = LoggerFactory.getLogger(SupervisorService.class);

    private final Database database;

    public SupervisorService() {
        this.database = Database.getInstance();
    }

    // ========================================================================
    // READ
    // ========================================================================

    /**
     * Retrieve all supervisors from the database.
     */
    public List<Supervisor> getAllSupervisors() {
        List<Supervisor> supervisors = new ArrayList<>();
        String sql = "SELECT * FROM supervisor ORDER BY firstname, lastname";

        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                supervisors.add(mapResultSetToSupervisor(rs));
            }
            logger.debug("Retrieved {} supervisors", supervisors.size());
        } catch (SQLException e) {
            logger.error("Failed to retrieve supervisors", e);
        }
        return supervisors;
    }

    /**
     * Retrieve a supervisor by username.
     */
    public Optional<Supervisor> getSupervisorByUsername(String username) {
        String sql = "SELECT * FROM supervisor WHERE username = ?";

        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToSupervisor(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Failed to retrieve supervisor by username: {}", username, e);
        }
        return Optional.empty();
    }

    // ========================================================================
    // CREATE
    // ========================================================================

    /**
     * Add a new supervisor to the database.
     * The password should already be BCrypt-hashed before calling this method.
     */
    public boolean addSupervisor(Supervisor supervisor) {
        String sql = "INSERT INTO supervisor (username, password, firstname, lastname, email, date) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, supervisor.getUsername());
            stmt.setString(2, supervisor.getPassword());
            stmt.setString(3, supervisor.getFirstName());
            stmt.setString(4, supervisor.getLastName());
            stmt.setString(5, supervisor.getEmail());
            setDateParam(stmt, 6, supervisor.getRegistrationDate());

            stmt.execute();
            logger.info("Supervisor added: {} {} ({})", supervisor.getFirstName(), supervisor.getLastName(), supervisor.getUsername());
            return true;
        } catch (SQLException e) {
            logger.error("Failed to add supervisor: {}", supervisor.getUsername(), e);
            return false;
        }
    }

    // ========================================================================
    // UPDATE
    // ========================================================================

    /**
     * Update an existing supervisor.
     * Uses username as the identifier.
     */
    public boolean updateSupervisor(Supervisor supervisor, String oldUsername) {
        String sql = "UPDATE supervisor SET username=?, password=?, firstname=?, lastname=?, email=?, date=? WHERE username=?";

        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, supervisor.getUsername());
            stmt.setString(2, supervisor.getPassword());
            stmt.setString(3, supervisor.getFirstName());
            stmt.setString(4, supervisor.getLastName());
            stmt.setString(5, supervisor.getEmail());
            setDateParam(stmt, 6, supervisor.getRegistrationDate());
            stmt.setString(7, oldUsername);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                logger.info("Supervisor updated: {} -> {}", oldUsername, supervisor.getUsername());
                return true;
            }
            logger.warn("No supervisor found to update with username: {}", oldUsername);
            return false;
        } catch (SQLException e) {
            logger.error("Failed to update supervisor: {}", oldUsername, e);
            return false;
        }
    }

    /**
     * Update a supervisor's password only.
     */
    public boolean updateSupervisorPassword(String username, String newHashedPassword) {
        String sql = "UPDATE supervisor SET password=? WHERE username=?";

        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newHashedPassword);
            stmt.setString(2, username);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                logger.info("Supervisor password updated: {}", username);
                return true;
            }
            logger.warn("No supervisor found to update password: {}", username);
            return false;
        } catch (SQLException e) {
            logger.error("Failed to update supervisor password: {}", username, e);
            return false;
        }
    }

    // ========================================================================
    // DELETE
    // ========================================================================

    /**
     * Delete a supervisor by username.
     */
    public boolean deleteSupervisor(String username) {
        String sql = "DELETE FROM supervisor WHERE username = ?";

        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                logger.info("Supervisor deleted: {}", username);
                return true;
            }
            logger.warn("No supervisor found to delete with username: {}", username);
            return false;
        } catch (SQLException e) {
            logger.error("Failed to delete supervisor: {}", username, e);
            return false;
        }
    }

    // ========================================================================
    // COUNT
    // ========================================================================

    /**
     * Count total number of supervisors.
     */
    public long countSupervisors() {
        String sql = "SELECT COUNT(*) FROM supervisor";

        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            logger.error("Failed to count supervisors", e);
        }
        return 0;
    }

    // ========================================================================
    // PRIVATE HELPERS
    // ========================================================================

    private Supervisor mapResultSetToSupervisor(ResultSet rs) throws SQLException {
        Supervisor supervisor = new Supervisor();
        supervisor.setUsername(rs.getString("username"));
        supervisor.setPassword(rs.getString("password"));
        supervisor.setFirstName(rs.getString("firstname"));
        supervisor.setLastName(rs.getString("lastname"));
        supervisor.setEmail(rs.getString("email"));

        Timestamp timestamp = rs.getTimestamp("date");
        if (timestamp != null) {
            supervisor.setRegistrationDate(timestamp.toLocalDateTime().toLocalDate());
        }

        return supervisor;
    }

    /**
     * Safely set a DATE parameter on a PreparedStatement.
     */
    private void setDateParam(PreparedStatement stmt, int index, LocalDate date) throws SQLException {
        if (date != null) {
            stmt.setDate(index, Date.valueOf(date));
        } else {
            stmt.setNull(index, java.sql.Types.DATE);
        }
    }
}
