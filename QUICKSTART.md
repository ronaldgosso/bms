# Quick Start Guide

Get the Bagamoyo District Council Management System running in 5 minutes!

---

## ⚡ Quick Setup (5 Minutes)

### Step 1: Prerequisites Check

Ensure you have:
- ✅ **Java 17+** installed: `java -version`
- ✅ **Maven 3.8+** installed: `mvn -version`

**Don't have them?**
- Java: https://adoptium.net/
- Maven: https://maven.apache.org/download.cgi

---

### Step 2: Get the Code

```bash
# Clone the repository
git clone <repository-url>
cd bms
```

---

### Step 3: Run the Application

```bash
# That's it!
mvn javafx:run
```

**First run will:**
- ✅ Download all dependencies (~2-3 minutes on first run)
- ✅ Create the H2 database automatically
- ✅ Set up all tables and indexes
- ✅ Insert default admin credentials

---

### Step 4: Login

**Default Credentials:**

| Role | Username | Password |
|------|----------|----------|
| Admin | `admin` | `admin123` |
| Manager | `manager` | `admin123` |

> ⚠️ **Important**: Change these passwords immediately after first login!

---

## 🎯 What You'll See

### Home Screen
- Department statistics pie chart
- Three login buttons (System Admin, Admin, Supervisor)
- Clean, modern interface

### Admin Dashboard
- Manage Supervisors button
- Manage Departments button
- Change Password option
- Real-time statistics

### Supervisor Dashboard
- Full worker management (Add/Edit/Delete/View)
- Excel export functionality
- File upload/download
- Email client
- Chat system
- Media player & web browser

---

## 🛠️ Common Commands

### Development

```bash
# Run the application
mvn javafx:run

# Clean and rebuild
mvn clean compile

# Run with debug logging
mvn javafx:run -Dorg.slf4j.simpleLogger.defaultLogLevel=debug
```

### Production Build

```bash
# Create executable JAR
mvn clean package

# Run the JAR
java -jar target/bms-2.0.0.jar
```

---

## 📁 Project Structure (Simplified)

```
bms/
├── src/main/java/com/bagamoyo/bms/
│   ├── controller/    → UI controllers
│   ├── model/         → Data models
│   ├── service/       → Business logic
│   └── util/          → Utilities
│
├── src/main/resources/
│   ├── fxml/          → UI layouts
│   ├── css/           → Stylesheets
│   └── db/migration/  → Database schema
│
└── pom.xml            → Maven configuration
```

---

## ❓ Quick Troubleshooting

### "JavaFX runtime components are missing"

**Fix**: Use Maven to run (includes JavaFX):
```bash
mvn javafx:run
```

### Database errors

**Fix**: Delete database files and restart:
```bash
# Windows
del resources\db\bms.*

# Linux/Mac
rm resources/db/bms.*

# Then restart
mvn javafx:run
```

### Build fails

**Fix**: Clean everything and rebuild:
```bash
mvn clean
mvn dependency:purge-local-repository
mvn javafx:run
```

---

## 📚 Next Steps

- 📖 Read the full [README.md](README.md)
- 🤝 Check [CONTRIBUTING.md](CONTRIBUTING.md) for development guidelines
- 📝 See [CHANGELOG.md](CHANGELOG.md) for version history
- 🐛 Report issues on GitHub

---

## 🆘 Need Help?

- Check the [Troubleshooting section](README.md#troubleshooting)
- Open an [Issue on GitHub](https://github.com/your-username/bms/issues)
- Review console logs (SLF4J output)

---

**Enjoy the modernized BMS! 🎉**
