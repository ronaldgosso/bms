package com.bagamoyo.bms.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Objects;

/**
 * Admin user model for the Bagamoyo District Council Management System.
 *
 * Extends the base User model with admin-specific properties such as
 * full name and email address for administrative accounts.
 */
public class Admin extends User {

    private final StringProperty fullName;
    private final StringProperty email;

    // ========================================================================
    // Constructors
    // ========================================================================

    /**
     * Default constructor with empty values.
     */
    public Admin() {
        super();
        this.fullName = new SimpleStringProperty("");
        this.email = new SimpleStringProperty("");
    }

    /**
     * Constructor with username, password, and admin role.
     */
    public Admin(String username, String password) {
        super(username, password, "ADMIN");
        this.fullName = new SimpleStringProperty("");
        this.email = new SimpleStringProperty("");
    }

    /**
     * Full constructor with all fields.
     */
    public Admin(String username, String password, String fullName, String email) {
        super(username, password, "ADMIN");
        this.fullName = new SimpleStringProperty(fullName != null ? fullName : "");
        this.email = new SimpleStringProperty(email != null ? email : "");
    }

    // ========================================================================
    // Getters
    // ========================================================================

    public String getFullName() {
        return fullName.get();
    }

    public String getEmail() {
        return email.get();
    }

    // ========================================================================
    // Setters
    // ========================================================================

    public void setFullName(String fullName) {
        this.fullName.set(fullName != null ? fullName : "");
    }

    public void setEmail(String email) {
        this.email.set(email != null ? email : "");
    }

    // ========================================================================
    // Property Accessors
    // ========================================================================

    public StringProperty fullNameProperty() {
        return fullName;
    }

    public StringProperty emailProperty() {
        return email;
    }

    // ========================================================================
    // Object Methods
    // ========================================================================

    @Override
    public String toString() {
        return "Admin{" +
                "username='" + getUsername() + '\'' +
                ", fullName='" + fullName.get() + '\'' +
                ", email='" + email.get() + '\'' +
                ", role='" + getRole() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Admin admin = (Admin) o;
        return Objects.equals(fullName.get(), admin.fullName.get()) &&
                Objects.equals(email.get(), admin.email.get());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), fullName.get(), email.get());
    }
}
