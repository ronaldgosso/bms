package com.bagamoyo.bms.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Objects;

/**
 * Base user model for the Bagamoyo District Council Management System.
 *
 * Represents a system user with authentication credentials and role-based access control.
 * Uses JavaFX properties for data binding in UI components.
 */
public class User {

    private final StringProperty username;
    private final StringProperty password;
    private final StringProperty role;

    // ========================================================================
    // Constructors
    // ========================================================================

    /**
     * Default constructor with empty values.
     */
    public User() {
        this.username = new SimpleStringProperty("");
        this.password = new SimpleStringProperty("");
        this.role = new SimpleStringProperty("");
    }

    /**
     * Constructor with username and password.
     */
    public User(String username, String password) {
        this.username = new SimpleStringProperty(username != null ? username : "");
        this.password = new SimpleStringProperty(password != null ? password : "");
        this.role = new SimpleStringProperty("");
    }

    /**
     * Full constructor with all fields.
     */
    public User(String username, String password, String role) {
        this.username = new SimpleStringProperty(username != null ? username : "");
        this.password = new SimpleStringProperty(password != null ? password : "");
        this.role = new SimpleStringProperty(role != null ? role : "");
    }

    // ========================================================================
    // Getters
    // ========================================================================

    public String getUsername() {
        return username.get();
    }

    public String getPassword() {
        return password.get();
    }

    public String getRole() {
        return role.get();
    }

    // ========================================================================
    // Setters
    // ========================================================================

    public void setUsername(String username) {
        this.username.set(username != null ? username : "");
    }

    public void setPassword(String password) {
        this.password.set(password != null ? password : "");
    }

    public void setRole(String role) {
        this.role.set(role != null ? role : "");
    }

    // ========================================================================
    // Property Accessors
    // ========================================================================

    public StringProperty usernameProperty() {
        return username;
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public StringProperty roleProperty() {
        return role;
    }

    // ========================================================================
    // Object Methods
    // ========================================================================

    @Override
    public String toString() {
        return "User{" +
                "username='" + username.get() + '\'' +
                ", role='" + role.get() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username.get(), user.username.get()) &&
                Objects.equals(password.get(), user.password.get()) &&
                Objects.equals(role.get(), user.role.get());
    }

    @Override
    public int hashCode() {
        return Objects.hash(username.get(), password.get(), role.get());
    }
}
