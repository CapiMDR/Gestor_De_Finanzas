# Application Flow — Gestor de Finanzas

> Technical document describing the internal workings of the application:
> startup sequence, data persistence, architecture, and UI lifecycle.

---

## 1. Entry Point

The application starts from `Main.java` (or `MainApp.java` after the JavaFX migration).
The `main()` method executes four steps in strict order:

```
main()
  │
  ├── 1. AppConfig.ensureDataDirExists()      → Guarantees the data folder exists
  ├── 2. AccountManager.initAccountManager()  → Initializes the central model in memory
  ├── 3. AccountView / MainApp (JavaFX)       → Builds the main UI
  └── 4. AccountManager.loadInitialData()     → Loads data from disk into memory
```

No JSON file is read before the folder is guaranteed (step 1).
No data is displayed in the UI before it is loaded into `AccountManager` (step 4).

---

## 2. Data Management — JSON File Lifecycle

### 2.1 Where is data stored?

Data is **not stored in the folder where the `.exe` is installed or where the `.jar` is executed**.
It is stored in a fixed folder inside the operating system user's home directory:

| Operating System | Data path |
|---|---|
| Windows | `C:\Users\<username>\.gestor-finanzas\` |
| macOS   | `/Users/<username>/.gestor-finanzas/` |
| Linux   | `/home/<username>/.gestor-finanzas/` |

This path is computed by `AppConfig.java` using the system property `user.home`, which Java
reads directly from the OS. It is completely independent of where the application is installed
or executed from.

### 2.2 Who creates the folder?

`AppConfig.ensureDataDirExists()` — called once at startup inside `main()`:

```java
// AppConfig.java
public static void ensureDataDirExists() {
    new File(DATA_DIR).mkdirs();  // mkdirs() creates the folder AND all missing parents
}                                  // If it already exists, does nothing (idempotent)
```

### 2.3 Who creates the JSON files?

JSON files are **not created at startup**. They are created the first time the user saves data.
The `load*()` methods in each handler check whether the file exists first:

```java
// JsonDataHandler.java — loadAccounts()
File file = new File(AppConfig.getAccountsFilePath());
if (!file.exists() || file.length() == 0) {
    return new ArrayList<>();  // First run: empty list, no crash
}
```

If the file does not exist → the app returns an empty list and starts fresh.
If the file exists → it reads and deserializes the previously saved state.

### 2.4 Full persistence flow

```
FIRST RUN ON A NEW MACHINE
───────────────────────────────────────────────────────────────────
  Startup   →  creates ~/.gestor-finanzas/          (folder only)
  Load      →  no JSONs found → empty lists → clean app
  User adds accounts, movements, goals...
  Save      →  JSON files are created for the first time:
                  ~/.gestor-finanzas/accounts_data.json
                  ~/.gestor-finanzas/categories_data.json
                  ~/.gestor-finanzas/recurrings.json
                  ~/.gestor-finanzas/reminders.json

SUBSEQUENT RUNS
───────────────────────────────────────────────────────────────────
  Startup   →  folder already exists, mkdirs() does nothing
  Load      →  reads JSONs → restores previous state
  User works with their data...
  Save      →  overwrites JSONs with the new state
```

### 2.5 Atomic writes (corruption protection)

Save methods use atomic writes to prevent corrupted JSON files
if the application closes unexpectedly during a write operation:

```
1. Write the JSON to a temporary file (.tmp)
2. If write succeeded → move .tmp to the final file (atomic OS operation)
3. If write failed   → the original file remains intact
```

### 2.6 Data files

| File | Contents | Handler |
|---|---|---|
| `accounts_data.json` | Accounts, movements and financial goals | `JsonDataHandler.java` |
| `categories_data.json` | User-defined movement categories | `JsonDataHandler.java` |
| `recurrings.json` | Configured recurring movements | `RecurringJSONHandler.java` |
| `reminders.json` | Configured reminders | `ReminderJSONHandler.java` |

---

## 3. Architecture — MVC + Observer

The project follows the **Model-View-Controller** pattern combined with the **Observer pattern**
to decouple the data layer from the UI.

### 3.1 Layer responsibilities

| Layer | Package | Responsibility |
|---|---|---|
| **Model** | `*_model/` | Data, business logic, JSON persistence |
| **View** | `*_view/` | Visual presentation (Swing → migrating to JavaFX) |
| **Controller** | `*_controller/` | Orchestrates model and view; handles user events |

**Critical rule:** Business logic **never** goes in the View. The View **never** accesses
the Model directly. All communication goes through the Controller.

### 3.2 Observer pattern

`AccountManager` acts as the central `Subject`. Controllers that need to react to data changes
register themselves as `Observer` and are notified automatically when the model changes:

```
AccountManager (Subject)
  │
  ├── notifies → AccountController (Observer)
  ├── notifies → MovementsController (Observer)
  └── notifies → ReportsController (Observer)

Flow of a change:
  User adds account
      → AccountController calls AccountManager.addAccount()
      → AccountManager saves to JSON
      → AccountManager notifies all registered Observers
      → Registered views update automatically
```

### 3.3 Module structure

Each functional domain follows the same three-layer structure:

```
gestorFinanzas/src/main/java/
│
├── accounts/
│   ├── account_model/       ← Account, AccountManager, JsonDataHandler
│   ├── account_view/        ← AccountView (Swing) → AccountViewFX (JavaFX)
│   └── account_controller/  ← AccountController
│
├── movements/
│   ├── movement_model/      ← Movement, MovementCategory
│   ├── movement_view/       ← MovementsView → MovementsViewFX
│   └── movement_controller/ ← MovementController
│
├── goals/
│   ├── goals_model/         ← Goal
│   ├── goals_view/          ← GoalsView → GoalsViewFX
│   └── goals_controller/    ← GoalsController
│
├── recurringMoves/
│   ├── recurring_model/     ← RecurringMove, RecurringJSONHandler
│   ├── recurring_view/      ← RecurringsView → RecurringsViewFX
│   └── recurring_controller/← RecurringsController
│
├── reminders/
│   ├── reminder_model/      ← Reminder, ReminderJSONHandler
│   ├── reminder_view/       ← RemindersView → RemindersViewFX
│   └── reminder_controller/ ← RemindersController
│
├── reports/
│   └── modelReport/         ← ReportGenerator, ReportSubject
│
├── filters/
│   └── controllerFilter/    ← FilterController
│
└── config/
    └── AppConfig.java       ← Centralized data paths
```

---

## 4. UI Lifecycle

### 4.1 Opening a window

```
User clicks a button in AccountView (e.g. "View Movements")
    │
    ▼
AccountController handles the event
    │
    ▼
Creates an instance of MovementsView and MovementsController
    │
    ├── MovementsController registers itself as Observer in AccountManager
    └── MovementsView is shown to the user
```

### 4.2 Closing a window

When a window closes, the Controller **unregisters** from the Subject to
prevent memory leaks (the `static` observer list in `AccountManagerSubject`):

```java
// Required in every Controller that implements Observer:
addWindowListener(new WindowAdapter() {
    @Override
    public void windowClosed(WindowEvent e) {
        AccountManager.removeObserver(MyController.this);
    }
});
```

### 4.3 Auto-save

Saving is not time-based. It is triggered every time the Controller performs
a write operation (add, edit, delete). There is no explicit "Save" button —
data is persisted immediately after each change.

---

## 5. CI/CD Workflows

| Workflow | Trigger | Action |
|---|---|---|
| `sonarcloud.yml` | Push to any branch | Static code analysis with SonarCloud + JaCoCo coverage |
| `release.yml` | Push of tag `v*` | Builds the executable JAR and creates a GitHub Release |

The release workflow uses `jreleaser.yml` to package and publish the artifact.

---

---

# Flujo de la Aplicación — Gestor de Finanzas *(Español)*

> Documento técnico que describe cómo funciona la aplicación internamente:
> arranque, persistencia de datos, arquitectura y ciclo de vida de la UI.

---

## 1. Punto de Entrada

La aplicación arranca desde `Main.java` (o `MainApp.java` tras la migración a JavaFX).
El método `main()` ejecuta cuatro pasos en orden estricto:

```
main()
  │
  ├── 1. AppConfig.ensureDataDirExists()      → Garantiza que la carpeta de datos existe
  ├── 2. AccountManager.initAccountManager()  → Inicializa el modelo central en memoria
  ├── 3. AccountView / MainApp (JavaFX)       → Construye la UI principal
  └── 4. AccountManager.loadInitialData()     → Carga los datos desde disco a memoria
```

Ningún archivo JSON se lee antes de que la carpeta esté garantizada (paso 1).
Ningún dato se muestra en la UI antes de que esté cargado en el `AccountManager` (paso 4).

---

## 2. Gestión de Datos — Ciclo de Vida de los Archivos JSON

### 2.1 ¿Dónde se guardan los datos?

Los datos **no se guardan en la carpeta donde está instalado el `.exe` ni donde se ejecuta el `.jar`**.
Se guardan en una carpeta fija dentro del directorio `home` del usuario del sistema operativo:

| Sistema Operativo | Ruta de datos |
|---|---|
| Windows | `C:\Users\<nombre_usuario>\.gestor-finanzas\` |
| macOS   | `/Users/<nombre_usuario>/.gestor-finanzas/` |
| Linux   | `/home/<nombre_usuario>/.gestor-finanzas/` |

Esta ruta la calcula `AppConfig.java` usando la propiedad del sistema `user.home`.
Es completamente independiente de dónde esté instalada o ejecutada la aplicación.

### 2.2 ¿Quién crea la carpeta?

`AppConfig.ensureDataDirExists()` — llamado una sola vez al arrancar `main()`:

```java
public static void ensureDataDirExists() {
    new File(DATA_DIR).mkdirs();  // Crea la carpeta Y todas las intermedias si faltan
}                                  // Si ya existe, no hace nada (idempotente)
```

### 2.3 ¿Quién crea los archivos JSON?

Los archivos JSON **no se crean al arrancar**. Se crean la primera vez que el usuario guarda datos.
Los métodos `load*()` verifican primero si el archivo existe:

```java
File file = new File(AppConfig.getAccountsFilePath());
if (!file.exists() || file.length() == 0) {
    return new ArrayList<>();  // Primera ejecución: lista vacía, sin crash
}
```

### 2.4 Flujo completo de persistencia

```
PRIMERA EJECUCIÓN EN UNA MÁQUINA NUEVA
  Arranque  →  se crea ~/.gestor-finanzas/   (solo la carpeta)
  Carga     →  no hay JSONs → listas vacías → app limpia
  Usuario agrega datos → Guardado → se crean los JSONs por primera vez

EJECUCIONES POSTERIORES
  Arranque  →  carpeta ya existe, mkdirs() no hace nada
  Carga     →  lee los JSONs → restaura el estado anterior
```

### 2.5 Escritura atómica

Los guardados usan un archivo `.tmp` intermedio. Si la escritura falla,
el archivo original permanece intacto.

### 2.6 Archivos de datos

| Archivo | Contenido | Handler |
|---|---|---|
| `accounts_data.json` | Cuentas, movimientos y metas | `JsonDataHandler.java` |
| `categories_data.json` | Categorías de movimientos | `JsonDataHandler.java` |
| `recurrings.json` | Movimientos recurrentes | `RecurringJSONHandler.java` |
| `reminders.json` | Recordatorios | `ReminderJSONHandler.java` |

---

## 3. Arquitectura — MVC + Observer

Patrón MVC combinado con Observer para desacoplar datos y UI.
La lógica de negocio nunca va en la Vista. La Vista nunca accede directamente al Modelo.

`AccountManager` es el `Subject` central. Los Controladores se registran como `Observer`
y son notificados automáticamente cuando el modelo cambia.

---

## 4. Ciclo de Vida de la UI

- **Apertura:** El Controlador se registra como Observer al crear la ventana.
- **Cierre:** El Controlador se desregistra del Subject para evitar memory leaks.
- **Guardado:** Automático tras cada operación de escritura — sin botón "Guardar" explícito.

---

*Keep this document updated when the architecture, modules or persistence system changes.*
