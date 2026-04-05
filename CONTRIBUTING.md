# Contributing to BMS

Thank you for your interest in contributing to the Bagamoyo District Council Management System!

## How to Contribute

### Reporting Bugs

Before creating bug reports, please check existing issues to avoid duplicates. When creating a bug report, include:

- **Clear title and description**
- **Steps to reproduce** the behavior
- **Expected vs actual behavior**
- **Screenshots** if applicable
- **Environment details** (OS, Java version, Maven version)

**Example:**
```markdown
**Describe the bug**
Worker export to Excel fails when worker name contains special characters.

**To Reproduce**
1. Add a worker with name "José García"
2. Click "Export to Excel"
3. See error in console

**Expected behavior**
Excel file should be created successfully with proper UTF-8 encoding.

**Environment**
- OS: Windows 11
- Java: 17.0.5
- Maven: 3.8.6
```

### Suggesting Features

Feature suggestions are always welcome! Please provide:

- **Use case**: Why is this feature needed?
- **Proposed solution**: How should it work?
- **Alternatives considered**: Other approaches you've thought about
- **Additional context**: Screenshots, mockups, or examples

### Pull Requests

1. **Fork** the repository
2. **Create a branch** from `main`:
   ```bash
   git checkout -b feature/your-feature-name
   ```
3. **Make your changes** following our coding standards
4. **Test thoroughly** with `mvn clean verify`
5. **Commit** with clear, descriptive messages:
   ```bash
   git commit -m "feat: Add bulk worker import from CSV"
   ```
6. **Push** to your fork:
   ```bash
   git push origin feature/your-feature-name
   ```
7. **Open a Pull Request** against `main`

## Coding Standards

### Java Code Style

- **Indentation**: 4 spaces (no tabs)
- **Line length**: Max 120 characters
- **Naming conventions**:
  - Classes: `PascalCase` (e.g., `WorkerService`)
  - Methods: `camelCase` (e.g., `getAllWorkers()`)
  - Constants: `UPPER_SNAKE_CASE` (e.g., `MAX_WORKERS`)
  - FXML IDs: `camelCase` (e.g., `workerTableView`)

### Code Organization

```
controller/     → JavaFX @FXML controllers only (UI logic)
model/          → Data models and POJOs
service/        → Business logic (no UI code here)
util/           → Helper classes and utilities
```

### Naming Conventions

✅ **Good:**
```java
public class WorkerService {
    public Optional<Worker> getWorkerById(int id) { ... }
}
```

❌ **Bad:**
```java
public class individualTable {
    public static String getworker(int x) { ... }
}
```

### Database Operations

**Always use try-with-resources:**

✅ **Good:**
```java
public List<Worker> getAllWorkers() {
    List<Worker> workers = new ArrayList<>();
    String sql = "SELECT * FROM workers ORDER BY f_name";
    
    try (Connection conn = Database.getInstance().getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {
        
        while (rs.next()) {
            workers.add(mapResultSetToWorker(rs));
        }
    } catch (SQLException e) {
        logger.error("Failed to fetch workers", e);
    }
    
    return workers;
}
```

❌ **Bad:**
```java
public static void getworkers() {
    Connection conn = Database.getInstance().getConnection();
    Statement stmt = conn.createStatement();
    ResultSet rs = stmt.executeQuery("SELECT * FROM workers");
    // Never closed! Resource leak!
}
```

### Error Handling

**Never use `System.exit()` in application code:**

✅ **Good:**
```java
try {
    workerService.addWorker(worker);
    Notifications.create()
        .title("Success")
        .text("Worker added successfully")
        .showInformation();
} catch (Exception e) {
    logger.error("Failed to add worker", e);
    Notifications.create()
        .title("Error")
        .text("Failed to add worker: " + e.getMessage())
        .showError();
}
```

❌ **Bad:**
```java
try {
    workerService.addWorker(worker);
} catch (Exception e) {
    System.exit(1);  // Don't do this!
}
```

### Logging

Use SLF4J for all logging:

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkerService {
    private static final Logger logger = LoggerFactory.getLogger(WorkerService.class);
    
    public void addWorker(Worker worker) {
        logger.debug("Adding worker: {}", worker.getFullName());
        try {
            // ... database operations
            logger.info("Worker added successfully, ID: {}", worker.getId());
        } catch (SQLException e) {
            logger.error("Failed to add worker", e);
        }
    }
}
```

**Log levels:**
- `TRACE`: Very detailed debugging
- `DEBUG`: Diagnostic information (method entry/exit, variable values)
- `INFO`: Significant events (operations completed, user actions)
- `WARN`: Unexpected situations that don't break functionality
- `ERROR`: Error events that might still allow the application to continue

### Security

**Password handling:**
- Always use BCrypt for password hashing
- Never store or log passwords in plaintext
- Verify passwords with BCrypt's constant-time comparison

```java
import at.favre.lib.crypto.bcrypt.BCrypt;

// Hash password
String hash = BCrypt.withDefaults().hashToString(12, password.toCharArray());

// Verify password
BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), storedHash);
if (result.verified) {
    // Password matches
}
```

**SQL injection prevention:**
- Always use `PreparedStatement` with parameters
- Never concatenate user input into SQL strings

```java
// ✅ Good - Parameterized query
String sql = "SELECT * FROM workers WHERE f_name = ?";
try (PreparedStatement stmt = conn.prepareStatement(sql)) {
    stmt.setString(1, userInput);
    ResultSet rs = stmt.executeQuery();
}

// ❌ Bad - String concatenation (SQL injection risk!)
String sql = "SELECT * FROM workers WHERE f_name = '" + userInput + "'";
```

### FXML Guidelines

**Controller references:**
```xml
<?xml version="1.0" encoding="UTF-8"?>

<?import com.jfoenix.controls.JFXButton?>
<?import com.jfoenix.controls.JFXTextField?>

<VBox xmlns="http://javafx.com/javafx/21"
      xmlns:fx="http://javafx.com/fxml/1"
      fx:controller="com.bagamoyo.bms.controller.AddWorkerController"
      stylesheets="@/css/application.css">
    
    <JFXTextField fx:id="firstNameField" promptText="First Name"/>
    <JFXButton fx:id="saveButton" onAction="#handleSave" text="Save"/>
</VBox>
```

- Always use `fx:controller` with full package path
- Match `fx:id` attributes exactly with `@FXML` fields in controller
- Use `onAction` for button click handlers
- Include stylesheet reference

### CSS Guidelines

**Naming convention:**
- Use camelCase for custom CSS classes
- Prefix with component name to avoid conflicts

```css
/* Good */
.workerTableView { ... }
.workerTableView .column-header { ... }
.primaryButton { ... }

/* Avoid */
.button { ... }  /* Too generic, overrides JavaFX default */
```

## Testing

### Running Tests

```bash
mvn test
```

### Writing Tests

Place tests in `src/test/java/com/bagamoyo/bms/`:

```java
package com.bagamoyo.bms.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {
    
    @Test
    void testPasswordHashing() {
        String password = "testPassword123";
        String hash = AuthService.hashPassword(password);
        
        assertNotNull(hash);
        assertNotEquals(password, hash);
        assertTrue(AuthService.verifyPassword(password, hash));
    }
}
```

## Commit Messages

Use conventional commits:

```
<type>(<scope>): <description>

[optional body]

[optional footer]
```

**Types:**
- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation changes
- `style`: Code style changes (formatting, no logic change)
- `refactor`: Code refactoring (no feature change)
- `test`: Adding or modifying tests
- `chore`: Maintenance tasks, dependencies

**Examples:**
```
feat(worker): Add bulk import from CSV
fix(auth): Fix BCrypt verification for long passwords
docs(readme): Update installation instructions
refactor(service): Extract worker validation to separate method
```

## Review Process

1. All PRs require at least one review
2. CI checks must pass (Maven build, tests)
3. Address review comments promptly
4. Keep PRs focused and reasonably sized

## Questions?

Feel free to open an issue with your question or reach out to the maintainers.

---

Thank you for contributing to BMS! 🎉
