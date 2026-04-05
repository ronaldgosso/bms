package com.bagamoyo.bms.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Supervisor model for the Bagamoyo District Council Management System.
 *
 * Represents a supervisor user who can manage departments and workers.
 * Based on the legacy enrollSupervisor.java with modern Java 17 features
 * and proper JavaFX properties for TableView binding.
 */
public class Supervisor {

    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty username;
    private final StringProperty password;
    private final StringProperty email;
    private final ObjectProperty<LocalDate> registrationDate;

    // ========================================================================
    // Constructors
    // ========================================================================

    /**
     * Default constructor with empty/default values.
     */
    public Supervisor() {
        this.firstName = new SimpleStringProperty("");
        this.lastName = new SimpleStringProperty("");
        this.username = new SimpleStringProperty("");
        this.password = new SimpleStringProperty("");
        this.email = new SimpleStringProperty("");
        this.registrationDate = new SimpleObjectProperty<>(null);
    }

    /**
     * Full constructor with all fields using LocalDate for date.
     */
    public Supervisor(String username, String password, String email,
                      String firstName, String lastName, LocalDate registrationDate) {
        this.firstName = new SimpleStringProperty(firstName != null ? firstName : "");
        this.lastName = new SimpleStringProperty(lastName != null ? lastName : "");
        this.username = new SimpleStringProperty(username != null ? username : "");
        this.password = new SimpleStringProperty(password != null ? password : "");
        this.email = new SimpleStringProperty(email != null ? email : "");
        this.registrationDate = new SimpleObjectProperty<>(registrationDate);
    }

    /**
     * Convenience constructor for legacy compatibility (String date parameter).
     * Matches the legacy enrollSupervisor constructor signature.
     */
    public Supervisor(String username, String password, String email,
                      String firstName, String lastName, String registrationDate) {
        this.firstName = new SimpleStringProperty(firstName != null ? firstName : "");
        this.lastName = new SimpleStringProperty(lastName != null ? lastName : "");
        this.username = new SimpleStringProperty(username != null ? username : "");
        this.password = new SimpleStringProperty(password != null ? password : "");
        this.email = new SimpleStringProperty(email != null ? email : "");
        this.registrationDate = new SimpleObjectProperty<>(parseDate(registrationDate));
    }

    // ========================================================================
    // Getters
    // ========================================================================

    public String getFirstName() {
        return firstName.get();
    }

    public String getLastName() {
        return lastName.get();
    }

    public String getUsername() {
        return username.get();
    }

    public String getPassword() {
        return password.get();
    }

    public String getEmail() {
        return email.get();
    }

    public LocalDate getRegistrationDate() {
        return registrationDate.get();
    }

    // ========================================================================
    // Setters
    // ========================================================================

    public void setFirstName(String firstName) {
        this.firstName.set(firstName != null ? firstName : "");
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName != null ? lastName : "");
    }

    public void setUsername(String username) {
        this.username.set(username != null ? username : "");
    }

    public void setPassword(String password) {
        this.password.set(password != null ? password : "");
    }

    public void setEmail(String email) {
        this.email.set(email != null ? email : "");
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate.set(registrationDate);
    }

    // ========================================================================
    // Property Accessors
    // ========================================================================

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public StringProperty emailProperty() {
        return email;
    }

    public ObjectProperty<LocalDate> registrationDateProperty() {
        return registrationDate;
    }

    // ========================================================================
    // Object Methods
    // ========================================================================

    @Override
    public String toString() {
        return "Supervisor{" +
                "firstName='" + firstName.get() + '\'' +
                ", lastName='" + lastName.get() + '\'' +
                ", username='" + username.get() + '\'' +
                ", email='" + email.get() + '\'' +
                ", registrationDate=" + registrationDate.get() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Supervisor supervisor = (Supervisor) o;
        return Objects.equals(firstName.get(), supervisor.firstName.get()) &&
                Objects.equals(lastName.get(), supervisor.lastName.get()) &&
                Objects.equals(username.get(), supervisor.username.get()) &&
                Objects.equals(password.get(), supervisor.password.get()) &&
                Objects.equals(email.get(), supervisor.email.get()) &&
                Objects.equals(registrationDate.get(), supervisor.registrationDate.get());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                firstName.get(), lastName.get(), username.get(), password.get(),
                email.get(), registrationDate.get()
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
