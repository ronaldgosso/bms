package com.bagamoyo.bms.service;

import com.bagamoyo.bms.model.Worker;
import com.bagamoyo.bms.util.Database;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Worker CRUD service for the workers table.
 * All database operations use try-with-resources via the Database utility.
 */
public class WorkerService {

    private static final Logger logger = LoggerFactory.getLogger(WorkerService.class);

    private final Database database;

    public WorkerService() {
        this.database = Database.getInstance();
    }

    // ========================================================================
    // CREATE
    // ========================================================================

    /**
     * Add a new worker to the database.
     */
    public boolean addWorker(Worker worker) {
        String sql = "INSERT INTO workers (check_no, f_name, m_name, l_name, sex, cheo, n_mshahara, tsd, " +
                "t_kuzaliwa, t_ajira, t_kuthibitishwa, t_daraja, k_elimu, c_alisoma, m_alimaliza, " +
                "k_kazi_sasa, k_kazi_awali, dini, a_mahali, mobile, s_kwanza, s_pili, kata) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            populateWorkerStatement(stmt, worker);
            stmt.execute();
            logger.info("Worker added: {} {}", worker.getFirstName(), worker.getLastName());
            return true;
        } catch (SQLException e) {
            logger.error("Failed to add worker: {} {}", worker.getFirstName(), worker.getLastName(), e);
            return false;
        }
    }

    // ========================================================================
    // READ
    // ========================================================================

    /**
     * Retrieve all workers from the database.
     */
    public List<Worker> getAllWorkers() {
        List<Worker> workers = new ArrayList<>();
        String sql = "SELECT * FROM workers ORDER BY f_name, l_name";

        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                workers.add(mapResultSetToWorker(rs));
            }
            logger.debug("Retrieved {} workers from database", workers.size());
        } catch (SQLException e) {
            logger.error("Failed to retrieve all workers", e);
        }
        return workers;
    }

    /**
     * Retrieve a worker by their check number (ID).
     */
    public Optional<Worker> getWorkerById(String checkNo) {
        String sql = "SELECT * FROM workers WHERE check_no = ?";

        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, checkNo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToWorker(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Failed to retrieve worker by ID: {}", checkNo, e);
        }
        logger.warn("Worker not found with checkNo: {}", checkNo);
        return Optional.empty();
    }

    /**
     * Search workers by sex.
     */
    public List<Worker> searchBySex(String sex) {
        return searchByColumn("sex", sex);
    }

    /**
     * Search workers by position.
     */
    public List<Worker> searchByPosition(String position) {
        return searchByColumn("cheo", position);
    }

    /**
     * Search workers by education level.
     */
    public List<Worker> searchByEducation(String education) {
        return searchByColumn("k_elimu", education);
    }

    /**
     * Search workers by ward/department.
     */
    public List<Worker> searchByDepartment(String department) {
        return searchByColumn("kata", department);
    }

    /**
     * Search workers by name (matches first, middle, or last name).
     */
    public List<Worker> searchByName(String name) {
        String sql = "SELECT * FROM workers WHERE f_name LIKE ? OR m_name LIKE ? OR l_name LIKE ?";
        String pattern = "%" + name + "%";

        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, pattern);
            stmt.setString(2, pattern);
            stmt.setString(3, pattern);

            List<Worker> workers = executeQuery(stmt);
            logger.debug("Name search '{}' returned {} workers", name, workers.size());
            return workers;
        } catch (SQLException e) {
            logger.error("Failed to search workers by name: {}", name, e);
            return Collections.emptyList();
        }
    }

    /**
     * Filter workers by multiple criteria simultaneously.
     * Any parameter can be null or blank to skip that filter.
     */
    public List<Worker> filterWorkers(String sex, String position, String education, String department) {
        StringBuilder sql = new StringBuilder("SELECT * FROM workers WHERE 1=1");
        List<String> params = new ArrayList<>();

        if (sex != null && !sex.isBlank()) {
            sql.append(" AND sex = ?");
            params.add(sex);
        }
        if (position != null && !position.isBlank()) {
            sql.append(" AND cheo = ?");
            params.add(position);
        }
        if (education != null && !education.isBlank()) {
            sql.append(" AND k_elimu = ?");
            params.add(education);
        }
        if (department != null && !department.isBlank()) {
            sql.append(" AND kata = ?");
            params.add(department);
        }

        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setString(i + 1, params.get(i));
            }

            List<Worker> workers = executeQuery(stmt);
            logger.debug("Filter returned {} workers", workers.size());
            return workers;
        } catch (SQLException e) {
            logger.error("Failed to filter workers", e);
            return Collections.emptyList();
        }
    }

    // ========================================================================
    // UPDATE
    // ========================================================================

    /**
     * Update an existing worker record.
     */
    public boolean updateWorker(Worker worker) {
        String sql = "UPDATE workers SET f_name=?, m_name=?, l_name=?, sex=?, cheo=?, " +
                "n_mshahara=?, tsd=?, t_kuzaliwa=?, t_ajira=?, t_kuthibitishwa=?, t_daraja=?, " +
                "k_elimu=?, c_alisoma=?, m_alimaliza=?, k_kazi_sasa=?, k_kazi_awali=?, " +
                "dini=?, a_mahali=?, mobile=?, s_kwanza=?, s_pili=?, kata=? " +
                "WHERE check_no=?";

        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, worker.getFirstName());
            stmt.setString(2, worker.getMiddleName());
            stmt.setString(3, worker.getLastName());
            stmt.setString(4, worker.getSex());
            stmt.setString(5, worker.getPosition());
            stmt.setString(6, worker.getSalary());
            setDateParam(stmt, 7, parseDate(worker.getServiceDate()));
            setDateParam(stmt, 8, worker.getDateOfBirth());
            setDateParam(stmt, 9, worker.getEmploymentDate());
            setDateParam(stmt, 10, worker.getConfirmationDate());
            stmt.setString(11, worker.getGrade());
            stmt.setString(12, worker.getEducationLevel());
            stmt.setString(13, worker.getInstitution());
            stmt.setString(14, worker.getGraduationYear());
            stmt.setString(15, worker.getCurrentWork());
            stmt.setString(16, worker.getPreviousWork());
            stmt.setString(17, worker.getReligion());
            stmt.setString(18, worker.getPlaceOfBirth());
            stmt.setString(19, worker.getPhoneNumber());
            stmt.setString(20, worker.getSubject1());
            stmt.setString(21, worker.getSubject2());
            stmt.setString(22, worker.getWard());
            stmt.setString(23, worker.getCheck());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                logger.info("Worker updated: {} {}", worker.getFirstName(), worker.getLastName());
                return true;
            }
            logger.warn("No worker found to update with checkNo: {}", worker.getCheck());
            return false;
        } catch (SQLException e) {
            logger.error("Failed to update worker: {}", worker.getCheck(), e);
            return false;
        }
    }

    // ========================================================================
    // DELETE
    // ========================================================================

    /**
     * Delete a worker by their check number.
     */
    public boolean deleteWorker(String checkNo) {
        String sql = "DELETE FROM workers WHERE check_no = ?";

        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, checkNo);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                logger.info("Worker deleted with checkNo: {}", checkNo);
                return true;
            }
            logger.warn("No worker found to delete with checkNo: {}", checkNo);
            return false;
        } catch (SQLException e) {
            logger.error("Failed to delete worker with checkNo: {}", checkNo, e);
            return false;
        }
    }

    // ========================================================================
    // EXPORT
    // ========================================================================

    /**
     * Export all workers to an Excel (.xlsx) file at the given path.
     */
    public boolean exportToExcel(String filePath) {
        List<Worker> workers = getAllWorkers();
        if (workers.isEmpty()) {
            logger.warn("No workers to export");
            return false;
        }

        String[] headers = {
                "Check No", "First Name", "Middle Name", "Last Name", "Sex",
                "Position", "Salary", "Service Date", "Date of Birth", "Employment Date",
                "Confirmation Date", "Grade", "Education", "Institution", "Graduation Year",
                "Current Work", "Previous Work", "Religion", "Birth Place", "Phone",
                "Subject 1", "Subject 2", "Ward"
        };

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Workers");

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }

            for (int i = 0; i < workers.size(); i++) {
                Worker w = workers.get(i);
                Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(w.getCheck());
                row.createCell(1).setCellValue(w.getFirstName());
                row.createCell(2).setCellValue(w.getMiddleName());
                row.createCell(3).setCellValue(w.getLastName());
                row.createCell(4).setCellValue(w.getSex());
                row.createCell(5).setCellValue(w.getPosition());
                row.createCell(6).setCellValue(w.getSalary());
                row.createCell(7).setCellValue(w.getServiceDate());
                row.createCell(8).setCellValue(formatDate(w.getDateOfBirth()));
                row.createCell(9).setCellValue(formatDate(w.getEmploymentDate()));
                row.createCell(10).setCellValue(formatDate(w.getConfirmationDate()));
                row.createCell(11).setCellValue(w.getGrade());
                row.createCell(12).setCellValue(w.getEducationLevel());
                row.createCell(13).setCellValue(w.getInstitution());
                row.createCell(14).setCellValue(w.getGraduationYear());
                row.createCell(15).setCellValue(w.getCurrentWork());
                row.createCell(16).setCellValue(w.getPreviousWork());
                row.createCell(17).setCellValue(w.getReligion());
                row.createCell(18).setCellValue(w.getPlaceOfBirth());
                row.createCell(19).setCellValue(w.getPhoneNumber());
                row.createCell(20).setCellValue(w.getSubject1());
                row.createCell(21).setCellValue(w.getSubject2());
                row.createCell(22).setCellValue(w.getWard());
            }

            try (OutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }
            logger.info("Exported {} workers to Excel: {}", workers.size(), filePath);
            return true;
        } catch (IOException e) {
            logger.error("Failed to export workers to Excel: {}", filePath, e);
            return false;
        }
    }

    // ========================================================================
    // COUNT
    // ========================================================================

    /**
     * Count total number of workers.
     */
    public long countWorkers() {
        String sql = "SELECT COUNT(*) FROM workers";

        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            logger.error("Failed to count workers", e);
        }
        return 0;
    }

    // ========================================================================
    // PRIVATE HELPERS
    // ========================================================================

    private List<Worker> searchByColumn(String column, String value) {
        String sql = "SELECT * FROM workers WHERE " + column + " = ?";

        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, value);
            return executeQuery(stmt);
        } catch (SQLException e) {
            logger.error("Failed to search workers by {}: {}", column, value, e);
            return Collections.emptyList();
        }
    }

    private List<Worker> executeQuery(PreparedStatement stmt) throws SQLException {
        List<Worker> workers = new ArrayList<>();
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                workers.add(mapResultSetToWorker(rs));
            }
        }
        return workers;
    }

    private Worker mapResultSetToWorker(ResultSet rs) throws SQLException {
        Worker worker = new Worker();
        worker.setCheck(rs.getString("check_no"));
        worker.setFirstName(rs.getString("f_name"));
        worker.setMiddleName(rs.getString("m_name"));
        worker.setLastName(rs.getString("l_name"));
        worker.setSex(rs.getString("sex"));
        worker.setPosition(rs.getString("cheo"));
        worker.setSalary(rs.getString("n_mshahara"));
        worker.setServiceDate(rs.getString("tsd"));

        Date dob = rs.getDate("t_kuzaliwa");
        worker.setDateOfBirth(dob != null ? dob.toLocalDate() : null);

        Date empDate = rs.getDate("t_ajira");
        worker.setEmploymentDate(empDate != null ? empDate.toLocalDate() : null);

        Date confDate = rs.getDate("t_kuthibitishwa");
        worker.setConfirmationDate(confDate != null ? confDate.toLocalDate() : null);

        worker.setGrade(rs.getString("t_daraja"));
        worker.setEducationLevel(rs.getString("k_elimu"));
        worker.setInstitution(rs.getString("c_alisoma"));

        int gradYear = rs.getInt("m_alimaliza");
        worker.setGraduationYear(rs.wasNull() ? "" : String.valueOf(gradYear));

        worker.setCurrentWork(rs.getString("k_kazi_sasa"));
        worker.setPreviousWork(rs.getString("k_kazi_awali"));
        worker.setReligion(rs.getString("dini"));
        worker.setPlaceOfBirth(rs.getString("a_mahali"));
        worker.setPhoneNumber(rs.getString("mobile"));
        worker.setSubject1(rs.getString("s_kwanza"));
        worker.setSubject2(rs.getString("s_pili"));
        worker.setWard(rs.getString("kata"));

        return worker;
    }

    private void populateWorkerStatement(PreparedStatement stmt, Worker worker) throws SQLException {
        stmt.setString(1, worker.getCheck());
        stmt.setString(2, worker.getFirstName());
        stmt.setString(3, worker.getMiddleName());
        stmt.setString(4, worker.getLastName());
        stmt.setString(5, worker.getSex());
        stmt.setString(6, worker.getPosition());
        stmt.setString(7, worker.getSalary());
        stmt.setString(8, worker.getServiceDate());
        setDateParam(stmt, 9, worker.getDateOfBirth());
        setDateParam(stmt, 10, worker.getEmploymentDate());
        setDateParam(stmt, 11, worker.getConfirmationDate());
        stmt.setString(12, worker.getGrade());
        stmt.setString(13, worker.getEducationLevel());
        stmt.setString(14, worker.getInstitution());

        try {
            stmt.setInt(15, worker.getGraduationYear() != null && !worker.getGraduationYear().isBlank()
                    ? Integer.parseInt(worker.getGraduationYear())
                    : 0);
        } catch (NumberFormatException e) {
            stmt.setNull(15, java.sql.Types.INTEGER);
        }

        stmt.setString(16, worker.getCurrentWork());
        stmt.setString(17, worker.getPreviousWork());
        stmt.setString(18, worker.getReligion());
        stmt.setString(19, worker.getPlaceOfBirth());
        stmt.setString(20, worker.getPhoneNumber());
        stmt.setString(21, worker.getSubject1());
        stmt.setString(22, worker.getSubject2());
        stmt.setString(23, worker.getWard());
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

    /**
     * Parse a date string into a LocalDate.
     */
    private LocalDate parseDate(String dateString) {
        if (dateString == null || dateString.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(dateString);
        } catch (Exception e) {
            try {
                return LocalDate.parse(dateString, java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } catch (Exception e2) {
                try {
                    return LocalDate.parse(dateString, java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                } catch (Exception e3) {
                    logger.debug("Failed to parse date: {}", dateString);
                    return null;
                }
            }
        }
    }

    /**
     * Format a LocalDate to string for Excel export.
     */
    private String formatDate(LocalDate date) {
        return date != null ? date.toString() : "";
    }
}
