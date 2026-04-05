package com.bagamoyo.bms.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Modern database connection utility using H2 embedded database.
 * Replaces the legacy MS Access connection with a more robust and scalable solution.
 */
public class Database {
    private static final Logger logger = LoggerFactory.getLogger(Database.class);
    private static final String DB_URL = "jdbc:h2:./resources/db/bms;AUTO_SERVER=TRUE;INIT=RUNSCRIPT FROM 'src/main/resources/db/migration/V1__Initial_Schema.sql'";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";
    
    private static Database instance;
    
    private Database() {
        initializeDatabase();
    }
    
    /**
     * Get singleton instance of Database utility
     */
    public static synchronized Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }
    
    /**
     * Initialize database - ensures schema is created on first run
     */
    private void initializeDatabase() {
        try {
            // Load H2 driver
            Class.forName("org.h2.Driver");
            logger.info("H2 Database Driver loaded successfully");
            
            // Test connection
            try (Connection conn = getConnection()) {
                logger.info("Database connection established: {}", DB_URL);
            }
        } catch (ClassNotFoundException e) {
            logger.error("H2 Database Driver not found", e);
            throw new RuntimeException("Failed to load H2 Database Driver", e);
        } catch (SQLException e) {
            logger.error("Failed to initialize database connection", e);
            throw new RuntimeException("Failed to initialize database", e);
        }
    }
    
    /**
     * Get a new database connection
     */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
    
    /**
     * Close database connection safely
     */
    public void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                logger.error("Error closing database connection", e);
            }
        }
    }
}
