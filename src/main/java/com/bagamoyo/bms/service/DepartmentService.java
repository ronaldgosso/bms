package com.bagamoyo.bms.service;

import com.bagamoyo.bms.model.Department;
import com.bagamoyo.bms.util.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Department CRUD service for the department table.
 * All database operations use try-with-resources via the Database utility.
 */
public class DepartmentService {

    private static final Logger logger = LoggerFactory.getLogger(DepartmentService.class);

    private final Database database;

    public DepartmentService() {
        this.database = Database.getInstance();
    }

    // ========================================================================
    // READ
    // ========================================================================

    /**
     * Retrieve all departments from the database.
     */
    public List<Department> getAllDepartments() {
        List<Department> departments = new ArrayList<>();
        String sql = "SELECT * FROM department ORDER BY name";

        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                departments.add(mapResultSetToDepartment(rs));
            }
            logger.debug("Retrieved {} departments", departments.size());
        } catch (SQLException e) {
            logger.error("Failed to retrieve departments", e);
        }
        return departments;
    }

    /**
     * Retrieve a department by its name.
     */
    public Optional<Department> getDepartmentByName(String name) {
        String sql = "SELECT * FROM department WHERE name = ?";

        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToDepartment(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Failed to retrieve department by name: {}", name, e);
        }
        return Optional.empty();
    }

    // ========================================================================
    // CREATE
    // ========================================================================

    /**
     * Add a new department to the database.
     */
    public boolean addDepartment(Department department) {
        String sql = "INSERT INTO department (name, description) VALUES (?, ?)";

        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, department.getName());
            stmt.setString(2, department.getDescription());

            stmt.execute();
            logger.info("Department added: {} (description: {})", department.getName(), department.getDescription());
            return true;
        } catch (SQLException e) {
            logger.error("Failed to add department: {}", department.getName(), e);
            return false;
        }
    }

    // ========================================================================
    // UPDATE
    // ========================================================================

    /**
     * Update an existing department.
     * Uses the department name as the identifier.
     */
    public boolean updateDepartment(Department department, String oldName) {
        String sql = "UPDATE department SET name=?, description=? WHERE name=?";

        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, department.getName());
            stmt.setString(2, department.getDescription());
            stmt.setString(3, oldName);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                logger.info("Department updated: {} -> {}", oldName, department.getName());
                return true;
            }
            logger.warn("No department found to update with name: {}", oldName);
            return false;
        } catch (SQLException e) {
            logger.error("Failed to update department with name: {}", oldName, e);
            return false;
        }
    }

    // ========================================================================
    // DELETE
    // ========================================================================

    /**
     * Delete a department by its name.
     */
    public boolean deleteDepartment(String name) {
        String sql = "DELETE FROM department WHERE name = ?";

        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                logger.info("Department deleted: {}", name);
                return true;
            }
            logger.warn("No department found to delete with name: {}", name);
            return false;
        } catch (SQLException e) {
            logger.error("Failed to delete department with name: {}", name, e);
            return false;
        }
    }

    // ========================================================================
    // COUNT
    // ========================================================================

    /**
     * Count total number of departments.
     */
    public long countDepartments() {
        String sql = "SELECT COUNT(*) FROM department";

        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            logger.error("Failed to count departments", e);
        }
        return 0;
    }

    // ========================================================================
    // PRIVATE HELPERS
    // ========================================================================

    private Department mapResultSetToDepartment(ResultSet rs) throws SQLException {
        Department department = new Department();
        department.setName(rs.getString("name"));
        department.setDescription(rs.getString("description"));

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            department.setCreateDate(createdAt.toLocalDateTime().toLocalDate());
        }

        return department;
    }
}
