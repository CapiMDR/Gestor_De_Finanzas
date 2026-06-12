# JavaFX Migration — Gestor de Finanzas

> Technical document covering all changes made during Phase 3 of the improvement plan:
> complete migration of the presentation layer from Java Swing to JavaFX.
> Version: 2.0.0 — June 2026

---

## Table of Contents

1. [Motivation](#motivation)
2. [Migration Scope](#migration-scope)
3. [New Dependencies](#new-dependencies)
4. [Architecture Changes](#architecture-changes)
5. [New Files and Resources](#new-files-and-resources)
6. [Module-by-Module Changes](#module-by-module-changes)
7. [Build & Packaging Changes](#build--packaging-changes)
8. [Known Bugs Fixed During Migration](#known-bugs-fixed-during-migration)
9. [Version History](#version-history)

---

## Motivation

The original application used **Java Swing** as its GUI framework. While functional, Swing is a
legacy technology with several limitations:

- No hardware-accelerated rendering — UI feels dated and sluggish on modern displays.
- No native CSS styling support — custom themes required complex, error-prone `UIManager` hacks.
- No vector icon support — icons were either emoji characters or low-resolution PNGs.
- No FXML declarative layouts — all UI was constructed programmatically, mixing layout with logic.

**JavaFX** was chosen as the replacement because it is the modern, officially supported Java GUI
framework that solves all of the above problems while preserving the existing MVC architecture.

> This migration is classified as a **Major version** change (`1.x → 2.0.0`) under Semantic
> Versioning because the underlying rendering engine, styling system, and FXML component model are
> fundamentally incompatible with the previous Swing implementation.

---

## Migration Scope

| Aspect | Status |
|---|---|
| Business logic (controllers, models) | ✅ Unchanged |
| Data persistence (JSON handlers) | ✅ Unchanged |
| Color palette | ✅ Preserved and ported to CSS |
| Views / Windows | ✅ Fully rewritten in JavaFX + FXML |
| Icons | ✅ Replaced with Ikonli Material Design vectors |
| Swing `.form` files | ✅ Removed — replaced by `.fxml` files |

---

## New Dependencies

The following dependencies were added to `pom.xml`:

### JavaFX 21.0.3

```xml
<dependency>
    <groupId>org.openjfx</groupId>
    <artifactId>javafx-controls</artifactId>
    <version>21.0.3</version>
</dependency>
<dependency>
    <groupId>org.openjfx</groupId>
    <artifactId>javafx-fxml</artifactId>
    <version>21.0.3</version>
</dependency>
```

### Ikonli 12.3.1 (Vector Icons)

```xml
<dependency>
    <groupId>org.kordamp.ikonli</groupId>
    <artifactId>ikonli-javafx</artifactId>
    <version>12.3.1</version>
</dependency>
<dependency>
    <groupId>org.kordamp.ikonli</groupId>
    <artifactId>ikonli-materialdesign2-pack</artifactId>
    <version>12.3.1</version>
</dependency>
```

### JavaFX Maven Plugin

```xml
<plugin>
    <groupId>org.openjfx</groupId>
    <artifactId>javafx-maven-plugin</artifactId>
    <version>0.0.8</version>
    <configuration>
        <mainClass>com.mycompany.construccion.Main</mainClass>
    </configuration>
</plugin>
```

> **Note on Ikonli and fat-jar packaging:** Ikonli uses Java's `ServiceLoader` mechanism
> (`META-INF/services/org.kordamp.ikonli.IkonHandler`) to discover icon packs at runtime.
> The legacy `maven-assembly-plugin` would overwrite these service files when merging JARs,
> causing a fatal `Cannot resolve 'mdi2p-...'` crash. This is why the build was migrated
> to `maven-shade-plugin` with `ServicesResourceTransformer` (see [Build & Packaging Changes](#build--packaging-changes)).

---

## Architecture Changes

### Entry Point Wrapper (`AppLauncher.java`)

A critical issue with modern JavaFX fat-jars is that if the `Main` class directly extends
`javafx.application.Application`, the JVM throws:
`Error: JavaFX runtime components are missing, and are required to run this application`

This is a known limitation of how the module system detects JavaFX in unnamed modules.
The solution is to add a thin **launcher wrapper** that does not extend `Application`:

**New file:** `src/main/java/com/mycompany/construccion/AppLauncher.java`

```java
public class AppLauncher {
    public static void main(String[] args) {
        Main.main(args);  // delegates to the real Application
    }
}
```

Both the fat-jar manifest and the `jpackage` configuration point to `AppLauncher` as the
main class.

### View Layer (Swing → JavaFX)

| Before (Swing) | After (JavaFX) |
|---|---|
| `JFrame` / `JPanel` | `Stage` / `Scene` |
| `JTable` | `TableView<T>` |
| `JButton` | `Button` |
| `JLabel` | `Label` / `Text` |
| `JTextField` | `TextField` |
| `JComboBox` | `ComboBox<T>` |
| `JOptionPane` alerts | `Alert` / `Dialog` |
| UI constructed in Java code | UI declared in `.fxml` files |
| Styling via `UIManager` | Styling via `app.css` |

### CSS Design System

A centralized stylesheet was created at `src/main/resources/styles/app.css` containing
all color tokens, button styles, table styles, form input styles and component-level rules.

| CSS Variable / Class | Value / Purpose |
|---|---|
| `#F66B0E` | Primary orange — main buttons, highlights |
| `#205375` | Dark blue — sidebar, headers |
| `#F5C518` | Accent yellow — active states, badges |
| `#1a1a2e` | Dark background |
| `.btn-primary` | Orange filled button |
| `.btn-danger` | Red destructive action button |
| `.form-input` | Styled text fields and combo boxes |
| `.data-table` | Styled `TableView` component |

---

## New Files and Resources

### FXML Layout Files

| File | Module |
|---|---|
| `src/main/resources/fxml/accounts/account.fxml` | Account main view |
| `src/main/resources/fxml/movements/movements.fxml` | Movements list |
| `src/main/resources/fxml/movements/movement_edit.fxml` | Add/edit movement form |
| `src/main/resources/fxml/goals/goals.fxml` | Goals list |
| `src/main/resources/fxml/goals/goal_edit.fxml` | Add/edit goal form |
| `src/main/resources/fxml/recurringMoves/recurring.fxml` | Recurring moves view |
| `src/main/resources/fxml/recurringMoves/recurring_edit.fxml` | Add/edit recurring form |
| `src/main/resources/fxml/reminders/reminders.fxml` | Reminders list |
| `src/main/resources/fxml/reminders/reminder_edit.fxml` | Add/edit reminder form |

### New Java View Classes

| File | Replaces |
|---|---|
| `accounts/account_view/AccountViewFX.java` | `AccountView.java` (Swing) |
| `movements/movement_view/MovementsViewFX.java` | `MovementsView.java` (Swing) |
| `goals/goals_view/GoalsViewFX.java` | `GoalsView.java` (Swing) |
| `recurringMoves/recurring_view/RecurringsViewFX.java` | `RecurringsView.java` (Swing) |
| `reminders/reminder_view/RemindersViewFX.java` | `RemindersView.java` (Swing) |

### Stylesheet

`src/main/resources/styles/app.css` — Centralized CSS design system for all JavaFX views.

### Icon Asset

`src/main/resources/images/piggy.ico` — Application icon for the Windows installer.

---

## Module-by-Module Changes

### Accounts Module

- `AccountViewFX.java`: New JavaFX controller class linked to `account.fxml`. Displays the
  account list using `ListView`, the balance summary using `Label`, and provides buttons for
  all account operations. The `AccountsModule.java` acts as the factory that creates and
  shows the stage.
- `AccountsModule.java`: New static factory class that loads the FXML, applies the
  stylesheet, wires the controller, and creates the primary `Stage`.

### Movements Module

- `MovementsViewFX.java`: New JavaFX controller linked to `movements.fxml`. Displays
  movements in a `TableView` with columns for date, description, category, and amount.
- `movement_edit.fxml` and its controller handle the add/edit form with validation.

### Goals Module

- `GoalsViewFX.java`: New JavaFX view for savings goals. Displays a progress bar per goal
  alongside the target amount and current progress.

### Recurring Moves Module

- `RecurringsViewFX.java`: New JavaFX view. Displays the list of configured recurring
  payments with their frequency and next trigger date.

### Reminders Module

- `RemindersViewFX.java`: New JavaFX view using `ComboBox<Integer>` for hour and minute
  selection instead of a `Spinner`, which provided better visual consistency with the
  application's color palette and CSS styling.
- `RemindersController.java`: Background scheduler configured with **daemon threads** to
  prevent zombie processes when the main window is closed (see [Known Bugs Fixed](#known-bugs-fixed-during-migration)).

---

## Build & Packaging Changes

### Replaced `maven-assembly-plugin` with `maven-shade-plugin`

| | `maven-assembly-plugin` | `maven-shade-plugin` |
|---|---|---|
| When same-named files exist in multiple JARs | Overwrites (last one wins) | Merges with Transformer |
| Ikonli `ServiceLoader` files | ❌ Broken — only one pack survives | ✅ All packs merged and discoverable |
| Output artifact name | `gestor-finanzas-x.x.x-jar-with-dependencies.jar` | `gestor-finanzas-x.x.x.jar` |

The `maven-shade-plugin` configuration uses two transformers:

```xml
<transformer implementation="...ServicesResourceTransformer"/>
<!-- Merges all META-INF/services/* files to preserve all Ikonli icon packs -->

<transformer implementation="...ManifestResourceTransformer">
    <mainClass>com.mycompany.construccion.AppLauncher</mainClass>
</transformer>
<!-- Sets the correct main class in the JAR manifest -->
```

### `jpackage` Configuration

The `jpackage-maven-plugin` (1.6.5) is configured to generate a Windows `.exe` installer:

```xml
<mainJar>gestor-finanzas-${project.version}.jar</mainJar>
<mainClass>com.mycompany.construccion.AppLauncher</mainClass>
<type>EXE</type>
<winDirChooser>true</winDirChooser>
<winMenu>true</winMenu>
<winShortcut>true</winShortcut>
```

The installer bundles a private JRE so the end user does not need Java installed.

### GitHub Actions Release Workflow

No changes required to `.github/workflows/release.yml`. The workflow's
`mvn clean package -DskipTests` produces `gestor-finanzas-{version}.jar` which is
exactly what `jpackage` expects. The `app.version` override via `-Dapp.version=$VERSION`
continues to work correctly.

---

## Known Bugs Fixed During Migration

### 1. Zombie processes after closing the application

**Root cause:** `RemindersController` used a `ScheduledExecutorService` with a default
(non-daemon) thread. Java's JVM will not exit while non-daemon threads are still running.
When the user closed the main window, the scheduler kept running in the background indefinitely,
creating a "zombie" process.

**Fix in `RemindersController.java`:**
```java
// Before:
private final ScheduledExecutorService scheduler =
    Executors.newSingleThreadScheduledExecutor();

// After (daemon thread — JVM exits cleanly when the window closes):
private final ScheduledExecutorService scheduler =
    Executors.newSingleThreadScheduledExecutor(r -> {
        Thread thread = new Thread(r, "Reminders-Scheduler");
        thread.setDaemon(true);
        return thread;
    });
```

### 2. `Cannot resolve 'mdi2p-pencil'` crash in the installed `.exe`

**Root cause:** `maven-assembly-plugin` overwrote `META-INF/services/org.kordamp.ikonli.IkonHandler`
files when merging JARs. The packaged application only retained one Ikonli icon pack and
lost all references to Material Design icons.

**Fix:** Replaced `maven-assembly-plugin` with `maven-shade-plugin` and added
`ServicesResourceTransformer` to properly merge all service registration files.

### 3. Native installer launch failure

**Root cause:** When `Main` (which extends `Application`) was set as the manifest main class,
the JVM failed to find the JavaFX runtime module in the unnamed module context, throwing:
`Error: JavaFX runtime components are missing`.

**Fix:** Added `AppLauncher.java` as a thin wrapper and configured it as the main class for
both the fat-jar manifest and the `jpackage` configuration.

---

## Version History

| Version | Date | Description |
|---|---|---|
| `1.0.0` | 2026 | Initial release — Java Swing UI |
| `1.7.3` | June 2026 | Phase 3 start — JavaFX migration, Ikonli icons, FXML layouts |
| `2.0.0` | June 2026 | Stable JavaFX release — all modules migrated, installer fixed, zombie process bug resolved |
| `2.4.1` | June 2026 | Tab navigation, system tray background mode, overhauled notifications, UI polish and responsive fixes — see [FEATURES_UI-UX.md](FEATURES_UI-UX.md) |

---

---

# Migración a JavaFX — Gestor de Finanzas *(Español)*

> Documento técnico que cubre todos los cambios realizados durante la Fase 3 del plan de mejoras:
> migración completa de la capa de presentación de Java Swing a JavaFX.
> Versión: 2.0.0 — Junio 2026

---

## Índice

1. [Motivación](#motivación)
2. [Alcance de la migración](#alcance-de-la-migración)
3. [Nuevas dependencias](#nuevas-dependencias)
4. [Cambios en la arquitectura](#cambios-en-la-arquitectura)
5. [Nuevos archivos y recursos](#nuevos-archivos-y-recursos)
6. [Cambios módulo por módulo](#cambios-módulo-por-módulo)
7. [Cambios en la compilación y empaquetado](#cambios-en-la-compilación-y-empaquetado)
8. [Bugs conocidos resueltos durante la migración](#bugs-conocidos-resueltos-durante-la-migración)
9. [Historial de versiones](#historial-de-versiones)

---

## Motivación

La aplicación original usaba **Java Swing** como framework de interfaz gráfica. Si bien era funcional, Swing es una tecnología legada con varias limitaciones:

- Sin renderizado por hardware — la UI se siente anticuada en monitores modernos.
- Sin soporte nativo de CSS — los temas personalizados requerían hacks complejos con `UIManager`.
- Sin soporte de íconos vectoriales — los íconos eran emojis o PNGs de baja resolución.
- Sin layouts declarativos FXML — toda la UI se construía programáticamente, mezclando layout con lógica.

**JavaFX** fue elegido como reemplazo porque es el framework de GUI moderno y oficialmente soportado por Java que resuelve todos estos problemas conservando la arquitectura MVC existente.

> Esta migración se clasifica como un cambio de **Versión Mayor** (`1.x → 2.0.0`) bajo Semantic Versioning porque el motor de renderizado, el sistema de estilos y el modelo de componentes FXML son fundamentalmente incompatibles con la implementación Swing anterior.

---

## Alcance de la migración

| Aspecto | Estado |
|---|---|
| Lógica de negocio (controladores, modelos) | ✅ Sin cambios |
| Persistencia de datos (JSON handlers) | ✅ Sin cambios |
| Paleta de colores | ✅ Preservada y portada a CSS |
| Vistas / Ventanas | ✅ Completamente reescritas en JavaFX + FXML |
| Íconos | ✅ Reemplazados por vectores Material Design de Ikonli |
| Archivos `.form` de Swing | ✅ Eliminados — reemplazados por archivos `.fxml` |

---

## Nuevas dependencias

Se agregaron las siguientes dependencias al `pom.xml`:

- **JavaFX 21.0.3:** `javafx-controls` y `javafx-fxml`
- **Ikonli 12.3.1:** `ikonli-javafx` e `ikonli-materialdesign2-pack`
- **Plugin:** `javafx-maven-plugin` para ejecutar con `mvn javafx:run`

> **Nota sobre Ikonli y el fat-jar:** Ikonli usa el mecanismo `ServiceLoader` de Java para descubrir los paquetes de íconos en tiempo de ejecución. El `maven-assembly-plugin` heredado sobreescribía estos archivos al fusionar JARs, causando un crash fatal. Por eso se migró a `maven-shade-plugin` con `ServicesResourceTransformer`.

---

## Cambios en la arquitectura

### Clase de arranque (`AppLauncher.java`)

Se agregó una clase envolvente que **no** extiende `Application`. Esto evita el error `JavaFX runtime components are missing` al ejecutar el fat-jar o el `.exe` instalado.

### Capa de Vista (Swing → JavaFX)

| Antes (Swing) | Después (JavaFX) |
|---|---|
| `JFrame` / `JPanel` | `Stage` / `Scene` |
| `JTable` | `TableView<T>` |
| `JButton`, `JLabel`, `JTextField` | `Button`, `Label`, `TextField` |
| `JOptionPane` | `Alert` / `Dialog` |
| UI construida en código Java | UI declarada en archivos `.fxml` |
| Estilos con `UIManager` | Estilos con `app.css` |

### Sistema de Diseño CSS

Se creó la hoja de estilos centralizada en `src/main/resources/styles/app.css` con todos los tokens de color, estilos de botones, tablas, formularios y componentes.

---

## Nuevos archivos y recursos

### Archivos FXML

Se crearon archivos `.fxml` en `src/main/resources/fxml/` para cada módulo (cuentas, movimientos, metas, movimientos recurrentes, recordatorios).

### Nuevas clases de Vista Java

Se crearon clases `*ViewFX.java` para cada módulo reemplazando las clases Swing originales.

### Hoja de estilos

`src/main/resources/styles/app.css` — Sistema de diseño CSS centralizado para todas las vistas.

---

## Cambios módulo por módulo

### Módulo de Cuentas
Nueva vista JavaFX con `ListView` para la lista de cuentas y `Label` para el resumen de saldo. `AccountsModule.java` actúa como fábrica estática que carga el FXML y muestra el Stage.

### Módulo de Movimientos
`MovementsViewFX.java` muestra movimientos en un `TableView` con columnas para fecha, descripción, categoría y monto.

### Módulo de Metas
`GoalsViewFX.java` muestra una barra de progreso por meta junto al monto objetivo y el progreso actual.

### Módulo de Movimientos Recurrentes
`RecurringsViewFX.java` lista los movimientos recurrentes con su frecuencia y próxima fecha de activación.

### Módulo de Recordatorios
`RemindersViewFX.java` usa `ComboBox<Integer>` para la selección de hora y minuto, lo que ofrece mejor integración visual con la paleta de colores. El programador de fondo usa **hilos demonio** para evitar procesos zombie al cerrar la app.

---

## Cambios en la compilación y empaquetado

### Reemplazo de `maven-assembly-plugin` por `maven-shade-plugin`

El `maven-shade-plugin` fusiona correctamente los archivos `META-INF/services/*` mediante `ServicesResourceTransformer`, preservando todos los paquetes de íconos de Ikonli. El nombre del artefacto resultante es `gestor-finanzas-x.x.x.jar` (sin sufijo `-jar-with-dependencies`).

### Configuración de `jpackage`

El plugin `jpackage-maven-plugin` genera un instalador `.exe` de Windows que empaqueta un JRE privado, por lo que el usuario final no necesita tener Java instalado.

### Workflow de GitHub Actions

No se requirieron cambios en `.github/workflows/release.yml`. El flujo es completamente compatible con los nuevos nombres de artefactos.

---

## Bugs conocidos resueltos durante la migración

### 1. Procesos zombie al cerrar la aplicación

**Causa:** El `ScheduledExecutorService` de `RemindersController` usaba hilos no-daemon. Java no termina el proceso JVM mientras haya hilos activos no-daemon, dejando el proceso en memoria indefinidamente tras cerrar la ventana.

**Solución:** Configurar el executor con un `ThreadFactory` que marque el hilo como demonio (`thread.setDaemon(true)`).

### 2. Crash `Cannot resolve 'mdi2p-pencil'` en el `.exe` instalado

**Causa:** `maven-assembly-plugin` sobreescribía los archivos `META-INF/services/org.kordamp.ikonli.IkonHandler` al fusionar JARs.

**Solución:** Migración a `maven-shade-plugin` con `ServicesResourceTransformer`.

### 3. Fallo al lanzar el instalador nativo

**Causa:** Tener `Main` (que extiende `Application`) como clase principal del manifiesto causaba que la JVM no encontrara el runtime de JavaFX.

**Solución:** Agregar `AppLauncher.java` como wrapper y configurarlo como clase principal.

---

## Historial de versiones

| Versión | Fecha | Descripción |
|---|---|---|
| `1.0.0` | 2026 | Versión inicial — UI Java Swing |
| `1.7.3` | Junio 2026 | Inicio Fase 3 — migración a JavaFX, íconos Ikonli, layouts FXML |
| `2.0.0` | Junio 2026 | Versión estable JavaFX — todos los módulos migrados, instalador corregido, bug de procesos zombie resuelto |
| `2.4.1` | Junio 2026 | Navegación por pestañas, modo segundo plano (bandeja del sistema), notificaciones renovadas, pulido de UI y correcciones de responsividad — ver [FEATURES_UI-UX.md](FEATURES_UI-UX.md) |

---

*Keep this document updated when new modules are migrated or build tooling changes.*
