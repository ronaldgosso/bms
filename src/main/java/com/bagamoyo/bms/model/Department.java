package com.bagamoyo.bms.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Department model for the Bagamoyo District Council Management System.
 *
 * Represents a department within the council organization.
 * Based on the legacy enrolldepartment.java with modern Java 17 features
 * and proper JavaFX properties for TableView binding.
 */
public class Department {

    private final StringProperty name;
    private final StringProperty serial;
    private final StringProperty supervisor;
    private final ObjectProperty<LocalDate> createDate;
    private final StringProperty description;

    // ========================================================================
    // Constructors
    // ========================================================================

    /**
     * Default constructor with empty/default values.
     */
    public Department() {
        this.name = new SimpleStringProperty("");
        this.serial = new SimpleStringProperty("");
        this.supervisor = new SimpleStringProperty("");
        this.createDate = new SimpleObjectProperty<>(null);
        this.description = new SimpleStringProperty("");
    }

    /**
     * Constructor with name and description.
     */
    public Department(String name, String description) {
        this.name = new SimpleStringProperty(name != null ? name : "");
        this.serial = new SimpleStringProperty("");
        this.supervisor = new SimpleStringProperty("");
        this.createDate = new SimpleObjectProperty<>(LocalDate.now());
        this.description = new SimpleStringProperty(description != null ? description : "");
    }

    /**
     * Full constructor with all fields using LocalDate for date.
     */
    public Department(String name, String serial, String supervisor,
                      LocalDate createDate, String description) {
        this.name = new SimpleStringProperty(name != null ? name : "");
        this.serial = new SimpleStringProperty(serial != null ? serial : "");
        this.supervisor = new SimpleStringProperty(supervisor != null ? supervisor : "");
        this.createDate = new SimpleObjectProperty<>(createDate);
        this.description = new SimpleStringProperty(description != null ? description : "");
    }

    /**
     * Convenience constructor for legacy compatibility (String date parameter).
     * Matches the legacy enrolldepartment constructor signature.
     */
    public Department(String name, String serial, String supervisor, String createDate) {
        this.name = new SimpleStringProperty(name != null ? name : "");
        this.serial = new SimpleStringProperty(serial != null ? serial : "");
        this.supervisor = new SimpleStringProperty(supervisor != null ? supervisor : "");
        this.createDate = new SimpleObjectProperty<>(parseDate(createDate));
        this.description = new SimpleStringProperty("");
    }

    /**
     * Full constructor with String date for legacy compatibility.
     */
    public Department(String name, String serial, String supervisor,
                      String createDate, String description) {
        this.name = new SimpleStringProperty(name != null ? name : "");
        this.serial = new SimpleStringProperty(serial != null ? serial : "");
        this.supervisor = new SimpleStringProperty(supervisor != null ? supervisor : "");
        this.createDate = new SimpleObjectProperty<>(parseDate(createDate));
        this.description = new SimpleStringProperty(description != null ? description : "");
    }

    // ========================================================================
    // Getters
    // ========================================================================

    public String getName() {
        return name.get();
    }

    public String getSerial() {
        return serial.get();
    }

    public String getSupervisor() {
        return supervisor.get();
    }

    public LocalDate getCreateDate() {
        return createDate.get();
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

    public void setSerial(String serial) {
        this.serial.set(serial != null ? serial : "");
    }

    public void setSupervisor(String supervisor) {
        this.supervisor.set(supervisor != null ? supervisor : "");
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate.set(createDate);
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

    public StringProperty serialProperty() {
        return serial;
    }

    public StringProperty supervisorProperty() {
        return supervisor;
    }

    public ObjectProperty<LocalDate> createDateProperty() {
        return createDate;
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    // ========================================================================
    // Object Methods
    // ========================================================================

    @Override
    public String toString() {
        return "Department{" +
                "name='" + name.get() + '\'' +
                ", serial='" + serial.get() + '\'' +
                ", supervisor='" + supervisor.get() + '\'' +
                ", createDate=" + createDate.get() +
                ", description='" + description.get() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Department that = (Department) o;
        return Objects.equals(name.get(), that.name.get()) &&
                Objects.equals(serial.get(), that.serial.get()) &&
                Objects.equals(supervisor.get(), that.supervisor.get()) &&
                Objects.equals(createDate.get(), that.createDate.get()) &&
                Objects.equals(description.get(), that.description.get());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                name.get(), serial.get(), supervisor.get(),
                createDate.get(), description.get()
        );
    }

    // ========================================================================
    // Private Helper Methods
    // ========================================================================

    /**
     * Parses a date string into a LocalDate. Returns null if parsing fails.
     */
    private static LocalDate parseDate(String dateString) {
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
                    return null;
                }
            }
        }
    }
}
