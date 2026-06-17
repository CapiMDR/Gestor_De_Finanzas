# CONTRIBUTING — Gestor de Finanzas
> Development guide and project standards.  
> **Read this file before writing or modifying any file in the project.**

---

## 1. Project Identity

| Field | Value |
|---|---|
| Name | Gestor de Finanzas |
| Domain | Personal finance desktop application |
| Architecture | Java Desktop · MVC + Observer · Local JSON persistence |
| Current stage | Active development — JavaFX (migration complete) |
| Distribution | Executable JAR · Native installer (.exe) via jpackage |
| Repository | [github.com/CapiMDR/Gestor_De_Finanzas](https://github.com/CapiMDR/Gestor_De_Finanzas) |

Gestor de Finanzas is a local desktop application for tracking accounts, movements,
financial goals, recurring movements, and reminders. All data is stored locally in the
user's home directory — no servers, no cloud, no authentication.

---

## 2. Project Structure

Do not create files outside this structure without prior agreement.

```
gestorFinanzas/
│
├── docs/
│   ├── CONTRIBUTING.md          ← This file
│   └── APP_FLOW.md              ← Technical app flow (startup, data, architecture)
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── accounts/        ← Accounts module (model / view / controller)
│   │   │   ├── movements/       ← Movements module
│   │   │   ├── goals/           ← Financial goals module
│   │   │   ├── recurrings/      ← Recurring movements module
│   │   │   ├── reminders/       ← Reminders module
│   │   │   ├── notifications/   ← In-app notification system (NotificationManager, SystemTrayManager)
│   │   │   ├── reports/         ← Reports and data generation module (model only)
│   │   │   ├── filters/         ← Category and date filters module
│   │   │   ├── tutorial/        ← Interactive onboarding guide (TutorialManager, TutorialStep)
│   │   │   ├── utils/           ← Shared utilities (UIUtils, etc.)
│   │   │   ├── config/          ← Central configuration (AppConfig, AppSettings, SingleInstanceGuard, WinRegistryHelper)
│   │   │   └── com/mycompany/construccion/  ← Entry point (Main.java / MainShell.java / AppLauncher.java)
│   │   └── resources/
│   │       ├── fxml/            ← Declarative JavaFX layouts (*.fxml)
│   │       ├── styles/          ← JavaFX stylesheets (app.css)
│   │       ├── fonts/           ← Application fonts (Poppins-Bold.ttf)
│   │       └── images/          ← Image resources and icons
│   └── test/
│       └── java/                ← Unit tests (mirrors src/main/java)
│           ├── accounts/
│           ├── goals/
│           ├── movements/
│           ├── recurrings/
│           ├── reminders/
│           ├── reports/
│           ├── notifications/
│           └── config/
│
├── lib/                         ← Local dependency JARs (legacy, kept for reference only)
├── .github/workflows/           ← CI/CD pipelines (SonarCloud, Release)
├── pom.xml                      ← Maven project definition
└── jreleaser.yml                ← Packaging and release configuration
```

### 2.1 Internal module structure

Every domain module follows the same three-subfolder convention:

```
<module>/
├── <module>_model/      ← Entities, business logic, data access (JSON)
├── <module>_view/       ← Visual presentation (JavaFX: *ViewFX.java + *.fxml)
└── <module>_controller/ ← Orchestrates model and view; handles user events
```

> **Rule:** Business logic **never** goes in the View. The View **never** accesses
> the Model directly. All communication goes through the Controller.

---

## 3. Architecture — MVC + Observer

The project implements the **Model-View-Controller** pattern combined with the
**Observer pattern** to decouple the data layer from the user interface.

### 3.1 Observer pattern

`AccountManager` is the central `Subject`. Controllers that need to react to account state
changes register themselves as `Observer` and are notified automatically when the model changes.

**Critical rule when implementing a new view:** If a Controller registers as an Observer,
it **must** unregister when the JavaFX stage/scene is disposed to prevent memory leaks:

```java
// ✅ Required in every Controller that implements AccountObserver
// Use JavaFX's Platform.runLater or stage.setOnHidden to unregister:
stage.setOnHidden(e -> AccountManager.removeObserver(this));
```

### 3.2 SOLID principles applied

| Principle | How it applies in this project |
|---|---|
| **S** — Single Responsibility | Each class has one responsibility. `JsonDataHandler` only serializes/deserializes. `AppConfig` only provides paths. |
| **O** — Open/Closed | New views are added without modifying `AccountManager`. |
| **L** — Liskov Substitution | Observers are interchangeable: any class implementing the `Observer` interface can register. |
| **I** — Interface Segregation | Modules expose only the operations they need. |
| **D** — Dependency Inversion | Controllers depend on interfaces, not concrete implementations. Constructors accept dependency injection to facilitate testing. |

---

## 4. Code Conventions

### 4.1 Language

```
Code (classes, methods, variables, constants)  →  English
Javadoc comments and inline comments           →  English
End-user messages displayed in the UI          →  Spanish
Git commits                                    →  Spanish
```

> This project uses **Javadoc** as its documentation standard. Every public class and
> method must have a `/** ... */` block written in English.

### 4.2 Java naming

| Element | Convention | Example |
|---|---|---|
| Class | `PascalCase` | `AccountManager`, `GoalsController` |
| Interface | `PascalCase` | `Observer`, `AccountSubject` |
| Method | `camelCase` | `loadAccounts()`, `ensureDataDirExists()` |
| Variable | `camelCase` | `initialBalance`, `targetAmount` |
| Constant | `UPPER_SNAKE_CASE` | `DATA_DIR`, `FILE_PATH` |
| Package | lowercase with underscores | `account_model`, `goals_controller` |

### 4.3 Javadoc

Every public class and method **must** have Javadoc. Minimum standard:

```java
/**
 * Manages the list of accounts and notifies registered observers when data changes.
 * Acts as the central Subject in the Observer pattern.
 *
 * @author RoastWare
 */
public class AccountManager {

    /**
     * Adds a new account to the list and persists the change to disk.
     *
     * @param account the account to add; must not be null
     * @throws IllegalArgumentException if account is null
     */
    public static void addAccount(Account account) { ... }
}
```

### 4.4 Logging

**Never use `System.out.println()` in production code.** Use SLF4J with Logback:

```java
// ✅ Correct
private static final Logger log = LoggerFactory.getLogger(MyClass.class);
log.info("Accounts saved successfully — {} account(s).", count);
log.error("Error reading accounts file: {}", e.getMessage(), e);

// ❌ Forbidden
System.out.println("Cuentas guardadas: " + count);
```

Log level guidelines:
- `log.info()` → Relevant successful operations (save, load, startup)
- `log.warn()` → Unexpected but recoverable situations
- `log.error()` → Errors that prevent an operation; always include the exception

### 4.5 Encapsulation

- Methods that return internal lists must return `Collections.unmodifiableList(...)` to protect internal state.
- Never expose mutable internal collections from the model.

---

## 5. Technology Stack

| Technology | Version | Role |
|---|---|---|
| Java | 21 (LTS) | Primary language |
| JavaFX | 21.0.3 | UI framework (Swing migration complete) |
| Ikonli + Material Design 2 | 12.3.1 | Scalable vector icons for JavaFX |
| Maven | 3.9+ | Dependency management and build |
| SLF4J + Logback | 2.0.13 / 1.5.6 | Logging system |
| org.json | 20250517 | JSON serialization/deserialization |
| JUnit Jupiter | 5.11.4 | Unit testing |
| Mockito | 5.15.2 | Mocking in tests |
| TestFX | 4.0.18 | JavaFX UI testing |
| JaCoCo | 0.8.12 | Code coverage |
| SonarCloud | — | Static code quality analysis |

### 5.1 Dependency version management

Key JavaFX versions are centralized in a `pom.xml` property:

```xml
<properties>
    <javafx.version>21.0.3</javafx.version>
</properties>
```

To update JavaFX, change **only this property** — not the individual artifact versions.

---

## 6. Data Persistence

User data is **never** stored in the project folder or the installation folder.
It is stored in `~/.gestor-finanzas/` (see `AppConfig.java` and `docs/APP_FLOW.md §2`).

**Therefore:**
- `*.json` files are in `.gitignore` and **must never** be committed to the repository.
- When adding a new persistent data type, add its path to `AppConfig.java` following the existing pattern.
- Load methods must always handle the case of a missing file by returning an empty collection — never throw an unhandled exception to the user.

---

## 7. Git Workflow

Do not commit directly to `main`. Every change enters through its own branch
and is integrated into `main` via Pull Request.

```
main                         ← Only approved stable code
  └── <type>/<description>   ← Individual development branch
```

### 7.1 Branch naming

```
feature/<short-description>      ← New feature
fix/<short-description>          ← Bug fix
migration/<module>               ← Swing to JavaFX migration
refactor/<short-description>     ← Refactor without functional change
docs/<short-description>         ← Documentation only

Examples:
  feature/export-csv
  fix/report-amount-total
  migration/accounts-javafx
  refactor/json-handler-atomic-write
```

### 7.2 Commit format

Messages in **Spanish**, imperative mode, with the affected module as prefix:

```
[module] short description of the change

Examples:
  [accounts] Corregir bug de balance cero en ReportGenerator
  [javafx] Agregar AccountViewFX con FXML y hoja de estilos
  [config] Centralizar rutas de datos en AppConfig
  [tests] Agregar tests de round-trip para JsonDataHandler
  [deps] Agregar dependencias JavaFX e Ikonli en pom.xml
  [docs] Crear CONTRIBUTING y APP_FLOW
```

### 7.3 Pull Request process

1. Create the branch from an updated `main`: `git checkout -b feature/my-change`
2. Make commits following the format in section 7.2
3. Verify the checklist in Section 9 before opening the PR
4. Open the PR targeting `main` with a clear description of what changes and why
5. Wait for the SonarCloud pipeline to pass without new critical code smells
6. Merge to `main` only after review

---

## 8. Tests

### 8.1 Location

Tests must be placed in `src/test/java/` in the exact package equivalent
to the code they test:

```
src/main/java/accounts/account_model/JsonDataHandler.java
         ↕ same package
src/test/java/accounts/account_model/JsonDataHandlerTest.java
```

### 8.2 Test standards

- Use **JUnit Jupiter** (`@Test`, `@BeforeEach`, `@ExtendWith`)
- Use **Mockito** (`@Mock`, `@InjectMocks`, `when(...).thenReturn(...)`) to isolate dependencies
- Use JUnit's `@TempDir` for tests that involve file I/O
- Use `@ExtendWith(MockitoExtension.class)` in tests with mocks

```java
// ✅ Well-structured test example
@ExtendWith(MockitoExtension.class)
class GoalsControllerTest {

    @Mock
    private GoalsView view;

    @InjectMocks
    private GoalsController controller;

    @Test
    void testHandleAddGoalWithEmptyNameDoesNotSave() {
        // given
        when(view.getGoalName()).thenReturn("");

        // when
        controller.handleAddGoal();

        // then
        verify(view, never()).refreshGoalsList(any());
    }
}
```

### 8.3 Running tests

```bash
mvn test          # Run all tests
mvn verify        # Tests + JaCoCo coverage report
```

The coverage report is generated at `target/site/jacoco/index.html`.

---

## 9. Pull Request Checklist

Verify every point before opening a PR:

- [ ] Do class, method and variable names follow the conventions in Section 4.2?
- [ ] Is the code in English and are Javadoc comments also in English?
- [ ] Does every public class and method have Javadoc with `@param` and `@return` where applicable?
- [ ] Is there no `System.out.println()` in production code?
- [ ] Are errors handled with `try/catch` and logged via SLF4J?
- [ ] Do new persistent data types use `AppConfig` for their paths?
- [ ] Is the file in the correct folder according to Section 2?
- [ ] If the Controller implements Observer, does it unregister correctly on window close?
- [ ] Are new tests in `src/test/java/` in the correct package?
- [ ] Does the SonarCloud pipeline report no new critical or blocker issues?
- [ ] Does the commit message follow the `[module] description` format from Section 7.2?

---

## 10. Forbidden Practices

- ❌ Committing directly to `main`.
- ❌ Uploading `*.json` data files to the repository (they are in `.gitignore`).
- ❌ Using `System.out.println()` in any code merged to `main`.
- ❌ Writing business logic in the View.
- ❌ Accessing the Model directly from the View (bypassing the Controller).
- ❌ Exposing mutable internal collections from the Model.
- ❌ Hardcoding file paths outside `AppConfig.java`.
- ❌ Adding dependencies to `pom.xml` without prior agreement and documentation.
- ❌ Mixing Swing and JavaFX in the same view — the application uses JavaFX exclusively.

---

---

# CONTRIBUTING — Gestor de Finanzas *(Español)*
> Guía de desarrollo y estándares del proyecto.  
> **Leer este archivo antes de escribir o modificar cualquier archivo del proyecto.**

---

## 1. Identidad del Proyecto

Gestor de Finanzas es una aplicación de escritorio local para registrar cuentas, movimientos,
metas financieras, movimientos recurrentes y recordatorios. Todos los datos se guardan
localmente en el directorio `home` del usuario — sin servidores, sin nube, sin autenticación.

**Arquitectura:** Java Desktop · MVC + Observer · Persistencia JSON local  
**Etapa actual:** Desarrollo activo — JavaFX (migración completa)

---

## 2. Estructura del Proyecto

Cada módulo de dominio sigue la convención de tres subcarpetas: `_model/`, `_view/`, `_controller/`.  
La lógica de negocio nunca va en la Vista. La Vista nunca accede directamente al Modelo.

---

## 3. Arquitectura — MVC + Observer

`AccountManager` es el `Subject` central. Los Controladores se registran como `Observer`
y son notificados automáticamente cuando el modelo cambia.  
Al cerrar una ventana, el Controlador **debe** desregistrarse para evitar memory leaks.

Principios SOLID aplicados: cada clase tiene una responsabilidad única, los Observers son
intercambiables, y los Controladores dependen de interfaces (facilitando tests con inyección de dependencias).

---

## 4. Convenciones de Código

- **Idioma del código y Javadoc:** inglés. **Mensajes al usuario:** español. **Commits:** español.
- **Javadoc** obligatorio en toda clase y método público.
- **Nunca** usar `System.out.println()` — usar SLF4J (`log.info`, `log.error`).
- Los métodos que devuelven listas internas deben usar `Collections.unmodifiableList(...)`.

---

## 5. Persistencia de Datos

Los JSON **nunca** se guardan en la carpeta del proyecto. Se guardan en `~/.gestor-finanzas/`
(ver `AppConfig.java` y `docs/APP_FLOW.md §2`). Los archivos `*.json` están en `.gitignore`
y nunca deben subirse al repositorio.

---

## 6. Flujo de Trabajo Git

No hacer commits directamente a `main`. Todo cambio entra por rama propia → Pull Request.

**Formato de rama:** `feature/`, `fix/`, `migration/`, `refactor/`, `docs/`  
**Formato de commit:** `[módulo] descripción en español, modo imperativo`

---

## 7. Tests

Tests en `src/test/java/` en el paquete exactamente equivalente al código que prueban.
Usar JUnit Jupiter + Mockito. Usar `@TempDir` para tests con archivos.

---

## 8. Prácticas Prohibidas

Ver Sección 10 de la versión en inglés — aplican exactamente igual.

---

*Maintained by RoastWare*  
*Update this file when the stack, architecture, conventions or workflow change.*
