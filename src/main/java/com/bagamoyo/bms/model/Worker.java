package com.bagamoyo.bms.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Modern model class for worker/employee records in the Bagamoyo District Council Management System.
 *
 * This class replaces the legacy individualTable.java with improved Java 17 features,
 * proper JavaFX properties for TableView binding, and complete equals/hashCode/toString.
 *
 * Fields mapping (Swahili to English):
 * - check: Selection checkbox / employee ID marker
 * - fName: First name
 * - mName: Middle name
 * - lName: Last name
 * - sex: Gender
 * - cheo: Position/Title
 * - mshahara: Salary
 * - tsd: TSD (Time Scale Date / Service Date)
 * - kuzaliwa: Date of birth
 * - ajira: Employment date
 * - thibitishwa: Confirmation date
 * - daraja: Grade/Level
 * - elimu: Education level
 * - chuo: Institution/College
 * - aliomaliza: Year of graduation
 * - sasaKazi: Current work assignment
 * - awaliKazi: Previous work assignment
 * - dini: Religion
 * - alipozaliwa: Place of birth
 * - number: Phone number
 * - somo1: Subject 1
 * - somo2: Subject 2
 * - kata: Ward/District
 */
public class Worker {

    // JavaFX Properties for data binding
    private final StringProperty check;
    private final StringProperty firstName;
    private final StringProperty middleName;
    private final StringProperty lastName;
    private final StringProperty sex;
    private final StringProperty position;        // cheo
    private final StringProperty salary;          // mshahara
    private final StringProperty serviceDate;     // tsd
    private final ObjectProperty<LocalDate> dateOfBirth;  // kuzaliwa
    private final ObjectProperty<LocalDate> employmentDate; // ajira
    private final ObjectProperty<LocalDate> confirmationDate; // thibitishwa
    private final StringProperty grade;           // daraja
    private final StringProperty educationLevel;  // elimu
    private final StringProperty institution;     // chuo
    private final StringProperty graduationYear;  // aliomaliza
    private final StringProperty currentWork;     // sasaKazi
    private final StringProperty previousWork;    // awaliKazi
    private final StringProperty religion;        // dini
    private final StringProperty placeOfBirth;    // alipozaliwa
    private final StringProperty phoneNumber;     // number
    private final StringProperty subject1;        // somo1
    private final StringProperty subject2;        // somo2
    private final StringProperty ward;            // kata

    // ========================================================================
    // Constructors
    // ========================================================================

    /**
     * Default constructor with all properties initialized to empty/default values.
     */
    public Worker() {
        this.check = new SimpleStringProperty("");
        this.firstName = new SimpleStringProperty("");
        this.middleName = new SimpleStringProperty("");
        this.lastName = new SimpleStringProperty("");
        this.sex = new SimpleStringProperty("");
        this.position = new SimpleStringProperty("");
        this.salary = new SimpleStringProperty("");
        this.serviceDate = new SimpleStringProperty("");
        this.dateOfBirth = new SimpleObjectProperty<>(null);
        this.employmentDate = new SimpleObjectProperty<>(null);
        this.confirmationDate = new SimpleObjectProperty<>(null);
        this.grade = new SimpleStringProperty("");
        this.educationLevel = new SimpleStringProperty("");
        this.institution = new SimpleStringProperty("");
        this.graduationYear = new SimpleStringProperty("");
        this.currentWork = new SimpleStringProperty("");
        this.previousWork = new SimpleStringProperty("");
        this.religion = new SimpleStringProperty("");
        this.placeOfBirth = new SimpleStringProperty("");
        this.phoneNumber = new SimpleStringProperty("");
        this.subject1 = new SimpleStringProperty("");
        this.subject2 = new SimpleStringProperty("");
        this.ward = new SimpleStringProperty("");
    }

    /**
     * Full constructor with all 23 fields.
     */
    public Worker(String check, String firstName, String middleName, String lastName,
                  String sex, String position, String salary, String serviceDate,
                  LocalDate dateOfBirth, LocalDate employmentDate, LocalDate confirmationDate,
                  String grade, String educationLevel, String institution, String graduationYear,
                  String currentWork, String previousWork, String religion, String placeOfBirth,
                  String phoneNumber, String subject1, String subject2, String ward) {
        this.check = new SimpleStringProperty(check != null ? check : "");
        this.firstName = new SimpleStringProperty(firstName != null ? firstName : "");
        this.middleName = new SimpleStringProperty(middleName != null ? middleName : "");
        this.lastName = new SimpleStringProperty(lastName != null ? lastName : "");
        this.sex = new SimpleStringProperty(sex != null ? sex : "");
        this.position = new SimpleStringProperty(position != null ? position : "");
        this.salary = new SimpleStringProperty(salary != null ? salary : "");
        this.serviceDate = new SimpleStringProperty(serviceDate != null ? serviceDate : "");
        this.dateOfBirth = new SimpleObjectProperty<>(dateOfBirth);
        this.employmentDate = new SimpleObjectProperty<>(employmentDate);
        this.confirmationDate = new SimpleObjectProperty<>(confirmationDate);
        this.grade = new SimpleStringProperty(grade != null ? grade : "");
        this.educationLevel = new SimpleStringProperty(educationLevel != null ? educationLevel : "");
        this.institution = new SimpleStringProperty(institution != null ? institution : "");
        this.graduationYear = new SimpleStringProperty(graduationYear != null ? graduationYear : "");
        this.currentWork = new SimpleStringProperty(currentWork != null ? currentWork : "");
        this.previousWork = new SimpleStringProperty(previousWork != null ? previousWork : "");
        this.religion = new SimpleStringProperty(religion != null ? religion : "");
        this.placeOfBirth = new SimpleStringProperty(placeOfBirth != null ? placeOfBirth : "");
        this.phoneNumber = new SimpleStringProperty(phoneNumber != null ? phoneNumber : "");
        this.subject1 = new SimpleStringProperty(subject1 != null ? subject1 : "");
        this.subject2 = new SimpleStringProperty(subject2 != null ? subject2 : "");
        this.ward = new SimpleStringProperty(ward != null ? ward : "");
    }

    /**
     * Convenience constructor for legacy compatibility (all String parameters).
     * Matches the legacy individualTable constructor signature.
     */
    public Worker(String check, String firstName, String middleName, String lastName,
                  String sex, String position, String salary, String serviceDate,
                  String dateOfBirth, String employmentDate, String confirmationDate,
                  String grade, String educationLevel, String institution, String graduationYear,
                  String currentWork, String previousWork, String religion, String placeOfBirth,
                  String phoneNumber, String subject1, String subject2, String ward) {
        this.check = new SimpleStringProperty(check != null ? check : "");
        this.firstName = new SimpleStringProperty(firstName != null ? firstName : "");
        this.middleName = new SimpleStringProperty(middleName != null ? middleName : "");
        this.lastName = new SimpleStringProperty(lastName != null ? lastName : "");
        this.sex = new SimpleStringProperty(sex != null ? sex : "");
        this.position = new SimpleStringProperty(position != null ? position : "");
        this.salary = new SimpleStringProperty(salary != null ? salary : "");
        this.serviceDate = new SimpleStringProperty(serviceDate != null ? serviceDate : "");
        this.dateOfBirth = new SimpleObjectProperty<>(parseDate(dateOfBirth));
        this.employmentDate = new SimpleObjectProperty<>(parseDate(employmentDate));
        this.confirmationDate = new SimpleObjectProperty<>(parseDate(confirmationDate));
        this.grade = new SimpleStringProperty(grade != null ? grade : "");
        this.educationLevel = new SimpleStringProperty(educationLevel != null ? educationLevel : "");
        this.institution = new SimpleStringProperty(institution != null ? institution : "");
        this.graduationYear = new SimpleStringProperty(graduationYear != null ? graduationYear : "");
        this.currentWork = new SimpleStringProperty(currentWork != null ? currentWork : "");
        this.previousWork = new SimpleStringProperty(previousWork != null ? previousWork : "");
        this.religion = new SimpleStringProperty(religion != null ? religion : "");
        this.placeOfBirth = new SimpleStringProperty(placeOfBirth != null ? placeOfBirth : "");
        this.phoneNumber = new SimpleStringProperty(phoneNumber != null ? phoneNumber : "");
        this.subject1 = new SimpleStringProperty(subject1 != null ? subject1 : "");
        this.subject2 = new SimpleStringProperty(subject2 != null ? subject2 : "");
        this.ward = new SimpleStringProperty(ward != null ? ward : "");
    }

    // ========================================================================
    // Getters (value accessors)
    // ========================================================================

    public String getCheck() {
        return check.get();
    }

    public String getFirstName() {
        return firstName.get();
    }

    public String getMiddleName() {
        return middleName.get();
    }

    public String getLastName() {
        return lastName.get();
    }

    public String getSex() {
        return sex.get();
    }

    public String getPosition() {
        return position.get();
    }

    public String getSalary() {
        return salary.get();
    }

    public String getServiceDate() {
        return serviceDate.get();
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth.get();
    }

    public LocalDate getEmploymentDate() {
        return employmentDate.get();
    }

    public LocalDate getConfirmationDate() {
        return confirmationDate.get();
    }

    public String getGrade() {
        return grade.get();
    }

    public String getEducationLevel() {
        return educationLevel.get();
    }

    public String getInstitution() {
        return institution.get();
    }

    public String getGraduationYear() {
        return graduationYear.get();
    }

    public String getCurrentWork() {
        return currentWork.get();
    }

    public String getPreviousWork() {
        return previousWork.get();
    }

    public String getReligion() {
        return religion.get();
    }

    public String getPlaceOfBirth() {
        return placeOfBirth.get();
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public String getSubject1() {
        return subject1.get();
    }

    public String getSubject2() {
        return subject2.get();
    }

    public String getWard() {
        return ward.get();
    }

    // ========================================================================
    // Setters
    // ========================================================================

    public void setCheck(String check) {
        this.check.set(check != null ? check : "");
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName != null ? firstName : "");
    }

    public void setMiddleName(String middleName) {
        this.middleName.set(middleName != null ? middleName : "");
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName != null ? lastName : "");
    }

    public void setSex(String sex) {
        this.sex.set(sex != null ? sex : "");
    }

    public void setPosition(String position) {
        this.position.set(position != null ? position : "");
    }

    public void setSalary(String salary) {
        this.salary.set(salary != null ? salary : "");
    }

    public void setServiceDate(String serviceDate) {
        this.serviceDate.set(serviceDate != null ? serviceDate : "");
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth.set(dateOfBirth);
    }

    public void setEmploymentDate(LocalDate employmentDate) {
        this.employmentDate.set(employmentDate);
    }

    public void setConfirmationDate(LocalDate confirmationDate) {
        this.confirmationDate.set(confirmationDate);
    }

    public void setGrade(String grade) {
        this.grade.set(grade != null ? grade : "");
    }

    public void setEducationLevel(String educationLevel) {
        this.educationLevel.set(educationLevel != null ? educationLevel : "");
    }

    public void setInstitution(String institution) {
        this.institution.set(institution != null ? institution : "");
    }

    public void setGraduationYear(String graduationYear) {
        this.graduationYear.set(graduationYear != null ? graduationYear : "");
    }

    public void setCurrentWork(String currentWork) {
        this.currentWork.set(currentWork != null ? currentWork : "");
    }

    public void setPreviousWork(String previousWork) {
        this.previousWork.set(previousWork != null ? previousWork : "");
    }

    public void setReligion(String religion) {
        this.religion.set(religion != null ? religion : "");
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth.set(placeOfBirth != null ? placeOfBirth : "");
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.set(phoneNumber != null ? phoneNumber : "");
    }

    public void setSubject1(String subject1) {
        this.subject1.set(subject1 != null ? subject1 : "");
    }

    public void setSubject2(String subject2) {
        this.subject2.set(subject2 != null ? subject2 : "");
    }

    public void setWard(String ward) {
        this.ward.set(ward != null ? ward : "");
    }

    // ========================================================================
    // Property Accessors (for JavaFX TableView binding)
    // ========================================================================

    public StringProperty checkProperty() {
        return check;
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public StringProperty middleNameProperty() {
        return middleName;
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public StringProperty sexProperty() {
        return sex;
    }

    public StringProperty positionProperty() {
        return position;
    }

    public StringProperty salaryProperty() {
        return salary;
    }

    public StringProperty serviceDateProperty() {
        return serviceDate;
    }

    public ObjectProperty<LocalDate> dateOfBirthProperty() {
        return dateOfBirth;
    }

    public ObjectProperty<LocalDate> employmentDateProperty() {
        return employmentDate;
    }

    public ObjectProperty<LocalDate> confirmationDateProperty() {
        return confirmationDate;
    }

    public StringProperty gradeProperty() {
        return grade;
    }

    public StringProperty educationLevelProperty() {
        return educationLevel;
    }

    public StringProperty institutionProperty() {
        return institution;
    }

    public StringProperty graduationYearProperty() {
        return graduationYear;
    }

    public StringProperty currentWorkProperty() {
        return currentWork;
    }

    public StringProperty previousWorkProperty() {
        return previousWork;
    }

    public StringProperty religionProperty() {
        return religion;
    }

    public StringProperty placeOfBirthProperty() {
        return placeOfBirth;
    }

    public StringProperty phoneNumberProperty() {
        return phoneNumber;
    }

    public StringProperty subject1Property() {
        return subject1;
    }

    public StringProperty subject2Property() {
        return subject2;
    }

    public StringProperty wardProperty() {
        return ward;
    }

    // ========================================================================
    // Object Methods
    // ========================================================================

    @Override
    public String toString() {
        return "Worker{" +
                "firstName='" + firstName.get() + '\'' +
                ", middleName='" + middleName.get() + '\'' +
                ", lastName='" + lastName.get() + '\'' +
                ", sex='" + sex.get() + '\'' +
                ", position='" + position.get() + '\'' +
                ", salary='" + salary.get() + '\'' +
                ", grade='" + grade.get() + '\'' +
                ", educationLevel='" + educationLevel.get() + '\'' +
                ", phoneNumber='" + phoneNumber.get() + '\'' +
                ", ward='" + ward.get() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Worker worker = (Worker) o;
        return Objects.equals(check.get(), worker.check.get()) &&
                Objects.equals(firstName.get(), worker.firstName.get()) &&
                Objects.equals(middleName.get(), worker.middleName.get()) &&
                Objects.equals(lastName.get(), worker.lastName.get()) &&
                Objects.equals(sex.get(), worker.sex.get()) &&
                Objects.equals(position.get(), worker.position.get()) &&
                Objects.equals(salary.get(), worker.salary.get()) &&
                Objects.equals(serviceDate.get(), worker.serviceDate.get()) &&
                Objects.equals(dateOfBirth.get(), worker.dateOfBirth.get()) &&
                Objects.equals(employmentDate.get(), worker.employmentDate.get()) &&
                Objects.equals(confirmationDate.get(), worker.confirmationDate.get()) &&
                Objects.equals(grade.get(), worker.grade.get()) &&
                Objects.equals(educationLevel.get(), worker.educationLevel.get()) &&
                Objects.equals(institution.get(), worker.institution.get()) &&
                Objects.equals(graduationYear.get(), worker.graduationYear.get()) &&
                Objects.equals(currentWork.get(), worker.currentWork.get()) &&
                Objects.equals(previousWork.get(), worker.previousWork.get()) &&
                Objects.equals(religion.get(), worker.religion.get()) &&
                Objects.equals(placeOfBirth.get(), worker.placeOfBirth.get()) &&
                Objects.equals(phoneNumber.get(), worker.phoneNumber.get()) &&
                Objects.equals(subject1.get(), worker.subject1.get()) &&
                Objects.equals(subject2.get(), worker.subject2.get()) &&
                Objects.equals(ward.get(), worker.ward.get());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                check.get(), firstName.get(), middleName.get(), lastName.get(), sex.get(),
                position.get(), salary.get(), serviceDate.get(), dateOfBirth.get(),
                employmentDate.get(), confirmationDate.get(), grade.get(), educationLevel.get(),
                institution.get(), graduationYear.get(), currentWork.get(), previousWork.get(),
                religion.get(), placeOfBirth.get(), phoneNumber.get(), subject1.get(),
                subject2.get(), ward.get()
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
            // Try common date formats
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
