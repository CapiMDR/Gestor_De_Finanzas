# Application Flow — Gestor de Finanzas

> Technical document describing the internal workings of the application:
> startup sequence, data persistence, architecture, and UI lifecycle.
> Updated for v2.5.4.

---

## 1. Entry Point

The application starts from `AppLauncher.java`, which first checks that no other instance is already
running via `SingleInstanceGuard.checkAndLock()`, then delegates to `Main.java`.
The `AppLauncher` wrapper exists to avoid the JavaFX module detection error that occurs when
a class extending `Application` is used directly as the fat-jar or installer main class.

The startup sequence executes in strict order:

```
AppLauncher.main()
  ├── SingleInstanceGuard.checkAndLock()           → Binds port 49152; exits if already running
  └── Main.main() / Main.start()
        │
        ├── 1. AppConfig.ensureDataDirExists()           → Guarantees the data folder exists
        ├── 2. AccountManager.initAccountManager()       → Registers the central model
        ├── 3. AccountManager.loadInitialData()          → Deserializes accounts/movements from JSON
        ├── 4. RemindersModule.initGlobalReminders()     → Starts the background reminder scheduler
        ├── 5. RecurringsModule.initGlobalRecurrings()   → Starts the recurring-payment daemon thread
        ├── 6. SystemTrayManager.enableTray() (conditional)
        │                                               → Only if settings = SEGUNDO_PLANO
        └── 7. Loads main_shell.fxml → builds and shows MainShell (main Stage)
```

No JSON file is read before the folder is guaranteed (step 1).
No data is displayed in the UI before it is loaded into `AccountManager` (steps 2–3).

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
| `settings.json` | User preferences (notification mode, autostart, tutorial shown) | `AppSettings.java` |
| `notifications.json` | Unread notifications persisted across sessions | `NotificationManager.java` |

---

## 3. Architecture — MVC + Observer

The project follows the **Model-View-Controller** pattern combined with the **Observer pattern**
to decouple the data layer from the UI.

### 3.1 Layer responsibilities

| Layer | Package | Responsibility |
|---|---|---|
| **Model** | `*_model/` | Data, business logic, JSON persistence |
| **View** | `*_view/` | Visual presentation (JavaFX — FXML + CSS) |
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
  └── notifies → AccountDashboardController (Observer)

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
│   ├── account_view/        ← AccountViewFX.java + AccountsModule.java (JavaFX)
│   └── account_controller/  ← AccountController
│
├── movements/
│   ├── movement_model/      ← Movement, MovementCategory
│   ├── movement_view/       ← MovementsViewFX.java (JavaFX)
│   └── movement_controller/ ← MovementController
│
├── goals/
│   ├── goals_model/         ← Goal
│   ├── goals_view/          ← GoalsViewFX.java (JavaFX)
│   └── goals_controller/    ← GoalsController
│
├── recurrings/
│   ├── recurring_model/     ← RecurringMove, RecurringJSONHandler
│   ├── recurring_view/      ← RecurringsViewFX.java (JavaFX)
│   └── recurring_controller/← RecurringsController (daemon thread)
│
├── reminders/
│   ├── reminder_model/      ← Reminder, ReminderJSONHandler
│   ├── reminder_view/       ← RemindersViewFX.java (JavaFX)
│   └── reminder_controller/ ← RemindersController (daemon thread scheduler)
│
├── notifications/
│   ├── notification_model/  ← AppNotification (types: RECORDATORIO, META_CUMPLIDA, RECURRENTE_VENCIDO)
│   ├── notification_controller/ ← NotificationManager (singleton, persists to notifications.json)
│   └── SystemTrayManager.java   ← System tray icon; keeps JVM alive in SEGUNDO_PLANO mode
│
├── reports/
│   └── report_model/        ← ReportGenerator, ReportSubject
│
├── filters/
│   └── filter_view/         ← FilterViewFX (Category filters)
│
├── tutorial/
│   ├── tutorial_model/      ← TutorialStep
│   └── tutorial_controller/ ← TutorialManager (shows interactive onboarding on first run)
│
├── utils/
│   └── UIUtils.java         ← Shared UI utilities
│
└── config/
    ├── AppConfig.java           ← Centralized data paths
    ├── AppSettings.java         ← Persistent user preferences (singleton)
    ├── SingleInstanceGuard.java ← Socket lock on port 49152 prevents duplicate instances
    ├── SettingsPanelController.java ← Settings dialog controller
    └── WinRegistryHelper.java   ← Windows registry autostart integration
```

---

## 4. UI Lifecycle

### 4.1 Opening a window

```
User clicks a button in AccountViewFX (e.g. "View Movements")
    │
    ▼
AccountController handles the event
    │
    ▼
Calls MovementsModule.initMovements(account)
    │
    ├── Loads movements.fxml via FXMLLoader
    ├── Creates and wires MovementsViewFX and MovementController
    └── Shows the new Stage
```

### 4.2 Closing a window

In JavaFX, each `Stage` can register an `onCloseRequest` handler to unregister from
the Observer list when the window is closed, preventing memory leaks:

```java
stage.setOnCloseRequest(event -> {
    AccountManager.removeObserver(myController);
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
| `release.yml` | Push of tag `v*` | Builds the fat-jar, generates the Windows `.exe` installer, and publishes it as a GitHub Release |

The release workflow uses `jreleaser.yml` to package and publish the artifact.
The fat-jar is produced by `maven-shade-plugin` with `ServicesResourceTransformer`.
The installer is generated by `jpackage-maven-plugin` and bundles a private JRE.

---

---

# Flujo de la Aplicación — Gestor de Finanzas *(Español)*

> Documento técnico que describe cómo funciona la aplicación internamente:
> arranque, persistencia de datos, arquitectura y ciclo de vida de la UI.

---

## 1. Punto de Entrada

La aplicación arranca desde `AppLauncher.java`, que primero comprueba que no haya otra instancia
en ejecución mediante `SingleInstanceGuard.checkAndLock()`, y luego delega a `Main.java`.

```
AppLauncher.main()
  ├── SingleInstanceGuard.checkAndLock()            → Bloquea el puerto 49152; termina si ya está corriendo
  └── Main.main() / Main.start()
        │
        ├── 1. AppConfig.ensureDataDirExists()        → Garantiza que la carpeta de datos existe
        ├── 2. AccountManager.initAccountManager()    → Registra el modelo central
        ├── 3. AccountManager.loadInitialData()       → Deserializa cuentas/movimientos del JSON
        ├── 4. RemindersModule.initGlobalReminders()  → Arranca el scheduler de recordatorios
        ├── 5. RecurringsModule.initGlobalRecurrings() → Arranca el hilo de pagos recurrentes
        ├── 6. SystemTrayManager.enableTray() (condicional)
        │                                              → Solo si configuración = SEGUNDO_PLANO
        └── 7. Carga main_shell.fxml → construye y muestra MainShell (Stage principal)
```

Ningún archivo JSON se lee antes de que la carpeta esté garantizada (paso 1).

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
| `settings.json` | Preferencias del usuario (modo notificaciones, autostart, tutorial mostrado) | `AppSettings.java` |
| `notifications.json` | Notificaciones no leídas persistidas entre sesiones | `NotificationManager.java` |

---

## 3. Arquitectura — MVC + Observer

Patrón MVC combinado con Observer para desacoplar datos y UI.
La lógica de negocio nunca va en la Vista. La Vista nunca accede directamente al Modelo.

`AccountManager` es el `Subject` central. Los Controladores se registran como `Observer`
y son notificados automáticamente cuando el modelo cambia.

---

## 4. Ciclo de Vida de la UI

- **Apertura:** El Controlador carga el FXML, crea el Stage y se registra como Observer.
- **Cierre:** El Stage llama al handler `onCloseRequest` que desregistra el Controlador del Subject.
- **Guardado:** Automático tras cada operación de escritura — sin botón "Guardar" explícito.

---

*Keep this document updated when the architecture, modules or persistence system changes.*
