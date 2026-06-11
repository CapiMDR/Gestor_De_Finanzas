# Flujo de Funcionamiento — Gestor de Finanzas

> Documento técnico que describe cómo funciona la aplicación internamente:
> arranque, persistencia de datos, arquitectura y ciclo de vida de la UI.

---

## 1. Punto de Entrada

La aplicación arranca desde `Main.java` (o `MainApp.java` tras la migración a JavaFX).
El método `main()` ejecuta cuatro pasos en orden estricto:

```
main()
  │
  ├── 1. AppConfig.ensureDataDirExists()   → Garantiza que la carpeta de datos existe
  ├── 2. AccountManager.initAccountManager()  → Inicializa el modelo central en memoria
  ├── 3. AccountView / MainApp (JavaFX)    → Construye la UI principal
  └── 4. AccountManager.loadInitialData() → Carga los datos desde disco a memoria
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

Esta ruta la calcula `AppConfig.java` usando la propiedad del sistema `user.home`, que Java
lee directamente del sistema operativo. Es completamente independiente de dónde esté instalada
o ejecutada la aplicación.

### 2.2 ¿Quién crea la carpeta?

`AppConfig.ensureDataDirExists()` — llamado una sola vez al arrancar `main()`:

```java
// AppConfig.java
public static void ensureDataDirExists() {
    new File(DATA_DIR).mkdirs();  // mkdirs() crea la carpeta Y todas las intermedias
}                                  // Si ya existe, no hace nada (idempotente)
```

### 2.3 ¿Quién crea los archivos JSON?

Los archivos JSON **no se crean al arrancar**. Se crean la primera vez que el usuario
guarda datos. Los métodos `load*()` de los handlers verifican primero si el archivo existe:

```java
// JsonDataHandler.java — loadAccounts()
File file = new File(AppConfig.getAccountsFilePath());
if (!file.exists() || file.length() == 0) {
    return new ArrayList<>();  // Primera ejecución: lista vacía, sin crash
}
```

Si el archivo no existe → la app devuelve una lista vacía y arranca limpia.
Si el archivo existe → lo lee y deserializa el estado guardado previamente.

### 2.4 Flujo completo de persistencia

```
PRIMERA EJECUCIÓN EN UNA MÁQUINA NUEVA
───────────────────────────────────────────────────────────────────
  Arranque  →  se crea ~/.gestor-finanzas/        (solo la carpeta)
  Carga     →  no hay JSONs → listas vacías → app limpia en blanco
  Usuario agrega cuentas, movimientos, metas...
  Guardado  →  se crean los archivos JSON por primera vez:
                  ~/.gestor-finanzas/accounts_data.json
                  ~/.gestor-finanzas/categories_data.json
                  ~/.gestor-finanzas/recurrings.json
                  ~/.gestor-finanzas/reminders.json

EJECUCIONES POSTERIORES
───────────────────────────────────────────────────────────────────
  Arranque  →  carpeta ya existe, mkdirs() no hace nada
  Carga     →  lee los JSONs → restaura el estado anterior
  Usuario trabaja con sus datos...
  Guardado  →  sobreescribe los JSONs con el nuevo estado
```

### 2.5 Escritura atómica (protección contra corrupción)

Los métodos de guardado usan escritura atómica para evitar JSON corruptos
si la aplicación se cierra inesperadamente durante una escritura:

```
1. Escribir el JSON en un archivo temporal (.tmp)
2. Si la escritura fue exitosa → mover .tmp al archivo final (operación atómica del SO)
3. Si la escritura falla → el archivo original permanece intacto
```

### 2.6 Archivos de datos

| Archivo | Contenido | Handler |
|---|---|---|
| `accounts_data.json` | Cuentas, movimientos y metas financieras | `JsonDataHandler.java` |
| `categories_data.json` | Categorías de movimientos del usuario | `JsonDataHandler.java` |
| `recurrings.json` | Movimientos recurrentes configurados | `RecurringJSONHandler.java` |
| `reminders.json` | Recordatorios configurados | `ReminderJSONHandler.java` |

---

## 3. Arquitectura — MVC + Observer

El proyecto sigue el patrón **Modelo-Vista-Controlador** combinado con el **patrón Observer**
para desacoplar la capa de datos de la UI.

### 3.1 Responsabilidades

| Capa | Paquete | Responsabilidad |
|---|---|---|
| **Modelo** | `*_model/` | Datos, lógica de negocio, persistencia JSON |
| **Vista** | `*_view/` | Presentación visual (Swing → migración a JavaFX) |
| **Controlador** | `*_controller/` | Orquesta modelo y vista; responde a eventos del usuario |

**Regla crítica:** La lógica de negocio **nunca** va en la Vista. La Vista **nunca** accede
directamente al Modelo. Toda comunicación pasa por el Controlador.

### 3.2 Patrón Observer

`AccountManager` actúa como `Subject` (sujeto observable). Las vistas que necesitan
reflejar cambios en los datos se registran como `Observer`:

```
AccountManager (Subject)
  │
  ├── notifica a → AccountView (Observer)
  ├── notifica a → MovementsView (Observer)
  └── notifica a → ReportsView (Observer)

Flujo de un cambio:
  Usuario agrega cuenta
      → AccountController llama a AccountManager.addAccount()
      → AccountManager guarda en JSON
      → AccountManager notifica a todos sus Observers
      → Las vistas registradas se actualizan automáticamente
```

### 3.3 Estructura de módulos

Cada dominio funcional sigue la misma estructura de tres capas:

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
    └── AppConfig.java       ← Rutas de datos centralizadas
```

---

## 4. Ciclo de Vida de la UI

### 4.1 Apertura de una ventana

```
Usuario hace clic en un botón de la AccountView (ej: "Ver Movimientos")
    │
    ▼
AccountController maneja el evento
    │
    ▼
Crea una instancia de MovementsView y MovementsController
    │
    ├── MovementsController se registra como Observer en AccountManager
    └── MovementsView se muestra al usuario
```

### 4.2 Cierre de una ventana

Al cerrar una ventana, el Controller se **desregistra** del Subject para
evitar memory leaks (lista `static` de observers en `AccountManagerSubject`):

```java
// En cada vista que implemente Observer:
addWindowListener(new WindowAdapter() {
    @Override
    public void windowClosed(WindowEvent e) {
        AccountManager.removeObserver(MyController.this);
    }
});
```

### 4.3 Guardado automático

El guardado no ocurre en intervalos de tiempo. Se dispara cada vez que el
Controlador realiza una operación de escritura (agregar, editar, eliminar).
No existe un botón "Guardar" explícito — los datos se persisten inmediatamente.

---

## 5. CI/CD y Workflows

| Workflow | Trigger | Acción |
|---|---|---|
| `sonarcloud.yml` | Push a cualquier rama | Análisis estático de código con SonarCloud + cobertura JaCoCo |
| `release.yml` | Push de tag `v*` | Genera el JAR ejecutable y crea un GitHub Release |

El workflow de release usa `jreleaser.yml` para empaquetar y publicar el artefacto.

---

*Mantener este documento actualizado cuando cambie la arquitectura, los módulos o el sistema de persistencia.*
