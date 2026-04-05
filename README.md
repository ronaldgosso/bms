# 🏛️ Bagamoyo District Council Management System (BMS)

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/projects/jdk/17/)
[![JavaFX](https://img.shields.io/badge/JavaFX-21.0.2-green.svg)](https://openjfx.io/)
[![Maven](https://img.shields.io/badge/Maven-3.8+-blue.svg)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![H2 Database](https://img.shields.io/badge/Database-H2-lightgrey.svg)](https://www.h2database.com/)

> **Modern HR and Employee Records Management System** for Bagamoyo District Council, Tanzania  
> President's Office, Regional Administration and Local Government

---

## 📋 Table of Contents

- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Project Structure](#-project-structure)
- [Prerequisites](#-prerequisites)
- [Installation & Setup](#-installation--setup)
- [Running the Application](#-running-the-application)
- [Building for Production](#-building-for-production)
- [Database](#-database)
- [User Roles & Access](#-user-roles--access)
- [Architecture](#-architecture)
- [Modernization](#-modernization-from-legacy)
- [Troubleshooting](#-troubleshooting)
- [Contributing](#-contributing)
- [License](#-license)
- [Credits](#-credits)

---

## ✨ Features

### 👥 Employee Management
- **Complete Worker Records**: Add, edit, delete, and view employee information (23 fields)
- **Advanced Search & Filtering**: Filter by name, sex, position, education, ward
- **TableView with Sorting**: Sortable columns with multi-criteria filtering
- **Excel Export**: Export worker data to Excel (.xls) format

### 🏢 Department & Supervisor Management
- **Department CRUD**: Create, view, and delete departments
- **Supervisor Enrollment**: Add and manage supervisor accounts
- **Real-time Statistics**: Dashboard cards showing counts and metrics

### 🔐 Authentication & Security
- **Three-Tier Role System**: System Administrator, Admin, Supervisor
- **BCrypt Password Hashing**: Industry-standard password security
- **Secure Login**: Role-based authentication with session management

### 📁 File & Email Services
- **File Upload/Download**: Store and retrieve files from database (BLOB storage)
- **Email Client**: Send emails with attachments via Gmail SMTP
- **Excel Import**: Bulk import worker data from Excel files

### 💬 Communication Tools
- **Chat Server/Client**: Built-in TCP multi-user chat system
- **Media Player**: Video and audio playback with full controls
- **Web Browser**: Integrated WebView browser with preset URLs

### 🎨 User Interface
- **Material Design**: Modern UI with JFoenix controls
- **Responsive Layouts**: Clean, professional design
- **Smooth Animations**: Fade-in transitions and hover effects
- **Notifications**: In-app toast notifications for user feedback

---

## 🛠️ Tech Stack

| Category | Technology | Version |
|----------|-----------|---------|
| **Language** | Java | 17 (LTS) |
| **UI Framework** | JavaFX | 21.0.2 |
| **UI Controls** | JFoenix | 9.0.10 |
| **Additional Controls** | ControlsFX | 11.2.1 |
| **Database** | H2 Embedded | 2.2.224 |
| **Build Tool** | Maven | 3.8+ |
| **Password Security** | BCrypt | 0.10.2 |
| **Excel Operations** | Apache POI | 5.2.5 |
| **Email** | JavaMail | 1.6.2 |
| **QR Codes** | ZXing | 3.5.3 |
| **Logging** | SLF4J | 2.0.11 |

---

## 📁 Project Structure

```
bms/
├── pom.xml                          # Maven build configuration
├── .gitignore                       # Git ignore rules
├── README.md                        # This file
│
├── src/
│   ├── main/
│   │   ├── java/com/bagamoyo/bms/
│   │   │   ├── BmsApplication.java       # Main application entry point
│   │   │   ├── Preloader.java            # Loading screen
│   │   │   │
│   │   │   ├── controller/               # JavaFX Controllers (19 files)
│   │   │   │   ├── HomeController.java
│   │   │   │   ├── LoginController.java
│   │   │   │   ├── AdminController.java
│   │   │   │   ├── SupervisorLoginController.java
│   │   │   │   ├── AdminDashboardController.java
│   │   │   │   ├── SystemAdminDashboardController.java
│   │   │   │   ├── SupervisorDashboardController.java
│   │   │   │   ├── AddWorkerController.java
│   │   │   │   ├── EditWorkerController.java
│   │   │   │   ├── DeleteWorkerController.java
│   │   │   │   ├── WorkerTableController.java
│   │   │   │   ├── DepartmentManagementController.java
│   │   │   │   ├── SupervisorManagementController.java
│   │   │   │   ├── FileManagementController.java
│   │   │   │   ├── EmailController.java
│   │   │   │   ├── ChatServerController.java
│   │   │   │   ├── ChatClientController.java
│   │   │   │   ├── MediaPlayerController.java
│   │   │   │   └── WebBrowserController.java
│   │   │   │
│   │   │   ├── model/                    # Data models (6 files)
│   │   │   │   ├── Worker.java           # Worker/employee model (23 fields)
│   │   │   │   ├── User.java             # Base user model
│   │   │   │   ├── Admin.java            # Admin user model
│   │   │   │   ├── Supervisor.java       # Supervisor model
│   │   │   │   ├── Department.java       # Department model
│   │   │   │   └── FileEntry.java        # File storage model
│   │   │   │
│   │   │   ├── service/                  # Business logic layer (6 files)
│   │   │   │   ├── AuthService.java      # Authentication & BCrypt
│   │   │   │   ├── WorkerService.java    # Worker CRUD operations
│   │   │   │   ├── DepartmentService.java
│   │   │   │   ├── SupervisorService.java
│   │   │   │   ├── FileService.java
│   │   │   │   └── AdminService.java
│   │   │   │
│   │   │   └── util/                     # Utility classes
│   │   │       └── Database.java         # H2 database connection utility
│   │   │
│   │   └── resources/
│   │       ├── fxml/                     # FXML view files (19 files)
│   │       │   ├── Home.fxml
│   │       │   ├── Login.fxml
│   │       │   ├── AdminLogin.fxml
│   │       │   ├── SupervisorLogin.fxml
│   │       │   ├── AdminDashboard.fxml
│   │       │   ├── SystemAdminDashboard.fxml
│   │       │   ├── SupervisorDashboard.fxml
│   │       │   ├── AddWorker.fxml
│   │       │   ├── EditWorker.fxml
│   │       │   ├── DeleteWorker.fxml
│   │       │   ├── WorkerTable.fxml
│   │       │   ├── DepartmentManagement.fxml
│   │       │   ├── SupervisorManagement.fxml
│   │       │   ├── FileManagement.fxml
│   │       │   ├── Email.fxml
│   │       │   ├── ChatServer.fxml
│   │       │   ├── ChatClient.fxml
│   │       │   ├── MediaPlayer.fxml
│   │       │   └── WebBrowser.fxml
│   │       │
│   │       ├── css/                      # Stylesheets
│   │       │   ├── application.css       # Main stylesheet (750+ lines)
│   │       │   └── BasicApplication.css  # Utility classes (600+ lines)
│   │       │
│   │       ├── db/
│   │       │   └── migration/
│   │       │       └── V1__Initial_Schema.sql  # H2 schema migration
│   │       │
│   │       └── img/                      # Images and icons
│   │
│   └── test/                            # Unit tests (future)
│
├── resources/                           # Legacy resources (images, video, audio)
│   ├── img/                             # Application images
│   ├── Music/                           # Sound effects
│   └── db/                              # H2 database files (auto-created)
│
└── src/application/                     # Legacy code (reference only)
    └── [41 legacy Java files]
```

---

## 📦 Prerequisites

Before running the application, ensure you have:

- **Java Development Kit (JDK) 17 or higher**  
  Download from [Adoptium](https://adoptium.net/) or [Oracle JDK](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)

- **Maven 3.8+**  
  Download from [Apache Maven](https://maven.apache.org/download.cgi)

- **Git** (for cloning the repository)  
  Download from [git-scm.com](https://git-scm.com/)

### Verify Installation

```bash
# Check Java version
java -version

# Check Maven version
mvn -version

# Check Git
git --version
```

---

## 🚀 Installation & Setup

### 1. Clone the Repository

```bash
git clone https://github.com/your-username/bms.git
cd bms
```

### 2. Build the Project

```bash
# Clean and compile
mvn clean compile

# Run tests (if available)
mvn test
```

### 3. First-Time Setup

The H2 database is automatically created on first run. The schema migration script (`V1__Initial_Schema.sql`) sets up all tables and indexes automatically.

Default credentials:
- **Manager**: username `manager`, password `admin123`
- **Admin**: username `admin`, password `admin123`

> ⚠️ **Important**: Change default passwords immediately after first login!

---

## 🎮 Running the Application

### Development Mode

```bash
# Using Maven JavaFX plugin
mvn javafx:run
```

### From IDE

1. Import the project as a Maven project in your IDE (IntelliJ IDEA, Eclipse, VS Code)
2. Run `BmsApplication.java` (contains `main` method)

### Standalone JAR

```bash
# Build executable JAR
mvn clean package

# Run the JAR
java -jar target/bms-2.0.0.jar
```

### Custom Database Location

```bash
# Run with custom DB path
mvn javafx:run -Ddb.path=/path/to/database
```

---

## 🏗️ Building for Production

### Create Executable JAR

```bash
mvn clean package
```

The shaded JAR will be in `target/bms-2.0.0.jar` with all dependencies bundled.

### Create Distribution Package

```bash
# Windows
mvn javafx:jlink

# Output in target/jlink-image/
```

### Native Image (Optional)

For native executables, use [GraalVM](https://www.graalvm.org/):

```bash
# Install GraalVM native-image
gu install native-image

# Build native image
mvn -Pnative package
```

---

## 💾 Database

### H2 Embedded Database

BMS uses **H2 Database Engine**, a modern, lightweight, embedded SQL database:

- **Zero configuration**: No separate database server needed
- **Auto-server mode**: Supports multiple connections
- **SQL compliant**: Standard SQL syntax
- **Automatic migration**: Schema created on first run

### Database Location

```
resources/db/bms.mv.db
```

### Access H2 Console (Debug Mode)

To view database contents directly:

```bash
# Start H2 Console
java -cp target/bms-2.0.0.jar org.h2.tools.Console

# Open browser to http://localhost:8082
# JDBC URL: jdbc:h2:./resources/db/bms
# User: sa
# Password: (empty)
```

### Database Schema

Tables:
- `manager` - System manager credentials
- `admin` - Administrator accounts
- `supervisor` - Supervisor accounts
- `workers` - Employee records (23 fields)
- `department` - Department information
- `file_storage` - Uploaded files (BLOB)

See [`src/main/resources/db/migration/V1__Initial_Schema.sql`](src/main/resources/db/migration/V1__Initial_Schema.sql) for full schema.

---

## 👤 User Roles & Access

### 1. System Administrator
**Access**: Full system control
- Create and delete admin accounts
- System configuration
- Manage all users

### 2. Admin
**Access**: Department and supervisor management
- Create/delete supervisors
- Create/delete departments
- Enroll supervisors and departments
- Change admin password
- View statistics

### 3. Supervisor
**Access**: Worker records and file management
- Add/edit/delete worker records
- View and search all workers
- Export to Excel
- Upload/download files
- Send emails with attachments
- Use chat system
- Access media player and web browser

---

## 🏛️ Architecture

### MVC Pattern

```
┌─────────────────────────────────────────┐
│          Model-View-Controller          │
├─────────────────────────────────────────┤
│                                         │
│  Model      →  Data objects (POJOs)    │
│  View       →  FXML files              │
│  Controller →  JavaFX Controllers      │
│  Service    →  Business logic layer    │
│  Util       →  Database & helpers      │
│                                         │
└─────────────────────────────────────────┘
```

### Layer Separation

```
Presentation Layer (FXML + Controllers)
         ↓
Service Layer (Business Logic)
         ↓
Data Access Layer (Database)
```

### Key Design Decisions

- **Separation of Concerns**: Controllers handle UI, Services handle business logic
- **Dependency Injection**: JavaFX FXML injection via `@FXML` annotations
- **Immutability**: Java 17 records and final fields where possible
- **Resource Management**: Try-with-resources for all database operations
- **Security**: BCrypt password hashing, no plaintext passwords
- **Logging**: SLF4J throughout for debugging and monitoring

---

## 🔄 Modernization from Legacy

This project has been completely modernized from a 2017-era Java 8 Eclipse project:

| Aspect | Legacy (2017) | Modern (2026) |
|--------|---------------|---------------|
| **Java Version** | JavaSE-1.8 | Java 17 LTS |
| **Build System** | Eclipse (.classpath) | Maven |
| **Database** | MS Access (.accdb) | H2 Embedded |
| **Package Structure** | Flat (all in `application`) | Layered (controller/model/service/util) |
| **Password Security** | Plaintext | BCrypt hashing |
| **DB Operations** | Manual connection management | Try-with-resources |
| **Error Handling** | `System.exit(1)` | Proper exception handling |
| **Logging** | `System.out.println` | SLF4J |
| **Concurrency** | Raw `Thread` classes | ExecutorService, JavaFX Task |
| **Naming** | Inconsistent (e.g., `NaivgationDrawer`) | Professional conventions |
| **UI Controls** | Basic JavaFX | JFoenix Material Design |
| **CSS** | Basic styles | Modern Material Design (1350+ lines) |
| **Code Quality** | Mixed concerns in controllers | MVC pattern with service layer |
| **Dependencies** | Manual JARs | Maven dependency management |

### Migration Highlights

✅ **41 legacy Java files** → **33 modern files** (better organized)  
✅ **Zero tests** → **Test-ready structure** (add tests in `src/test/`)  
✅ **SQL injection risk** → **Parameterized queries only**  
✅ **Resource leaks** → **All DB operations use try-with-resources**  
✅ **Hardcoded paths** → **Configurable, platform-independent**  
✅ **No documentation** → **Comprehensive README + code comments**

---

## 🔧 Troubleshooting

### Common Issues

#### 1. JavaFX Not Found

**Error**: `Error: JavaFX runtime components are missing`

**Solution**: Ensure you're using Java 17+ with JavaFX:
```bash
# Use Maven to run (includes JavaFX dependencies)
mvn javafx:run
```

#### 2. Database Connection Failed

**Error**: `Failed to initialize database`

**Solution**:
- Ensure `resources/db/` directory exists and is writable
- Check file permissions
- Delete `resources/db/bms.*` files and restart (database will be recreated)

#### 3. Maven Build Fails

**Error**: Compilation errors

**Solution**:
```bash
# Clean Maven cache and rebuild
mvn clean
mvn dependency:purge-local-repository
mvn install
```

#### 4. FXML Loading Error

**Error**: `Location is not set` or `FXML file not found`

**Solution**:
- Ensure FXML files are in `src/main/resources/fxml/`
- Check `fx:controller` attribute matches actual controller class
- Verify path in FXMLLoader: `/fxml/Home.fxml` (leading slash)

#### 5. CSS Not Applied

**Error**: Styles not loading

**Solution**:
- Check CSS path in controller: `getClass().getResource("/css/application.css")`
- Ensure CSS selectors match FXML component IDs and style classes
- Open CSS file and verify syntax (no unclosed braces)

### Getting Help

- Check the [Issues page](https://github.com/your-username/bms/issues)
- Review logs in console (SLF4J output)
- Enable debug logging: Add `-Dorg.slf4j.simpleLogger.defaultLogLevel=debug`

---

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. **Fork the repository**
2. **Create a feature branch**: `git checkout -b feature/amazing-feature`
3. **Commit your changes**: `git commit -m 'Add amazing feature'`
4. **Push to the branch**: `git push origin feature/amazing-feature`
5. **Open a Pull Request**

### Code Style

- Follow Java 17 conventions
- Use meaningful variable and method names
- Add JavaDoc for public methods
- Write unit tests for new features
- Ensure Maven build passes: `mvn clean verify`

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 🙏 Credits

**Original Developer (2017)**: Gosso Ronald - UDSM (University of Dar es Salaam)  
**Modernization (2026)**: Comprehensive refactoring to Java 17, Maven, H2, and modern architecture

**Special Thanks**:
- Bagamoyo District Council, Tanzania
- JavaFX Community
- Apache Software Foundation
- H2 Database Team

---

## 📞 Contact

For questions or support:
- 📧 Email: [your-email@example.com](mailto:your-email@example.com)
- 🌐 Website: [Bagamoyo District Council](http://www.bagamoyo.go.tz)

---

**Made with ❤️ for the People of Bagamoyo District**

*The United Republic Of Tanzania*  
*President's Office, Regional Administration and Local Government*
