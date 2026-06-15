# Changelog — Gestor de Finanzas

All notable changes to this project are documented in this file.
Format is based on [Keep a Changelog](https://keepachangelog.com/) and [Conventional Commits](https://www.conventionalcommits.org/).

---

## [2.4.1] — June 2026

### Summary
This release delivers a significant architectural and UX upgrade over `v2.0.0`. The main shell was restructured around a tab-based navigation model, background execution via the system tray was introduced, the notifications system was overhauled, and a widespread UI polish pass was applied across all views.

---

### Added

- **Tab-Based Navigation (`MainShell`)**: The main application window now uses a multi-tab navigation system. Each account's sub-modules (Movements, Goals, Recurring Moves, Reminders, Reports) are displayed as navigable tabs within the account shell, replacing the previous single-view model.
- **Background Execution — System Tray (`SingleInstanceGuard`)**: The application can now run silently in the system tray after its main window is closed. A `SingleInstanceGuard` prevents duplicate processes from running simultaneously, using a localhost socket lock on port `49152`.
- **Notification Read States**: Notifications now carry an `isRead` flag. Unread items are visually distinguished in the notification list with a bold style and an indicator badge in the top bar.
- **Individual Notification Deletion**: Each notification item in the list now displays a delete button (trash icon) that appears on hover, allowing fine-grained management.
- **Bulk Notification Actions**: Two new icon buttons were added to the notification panel header:
  - **Sweep icon** — Delete all notifications at once.
  - **Double-check icon** — Mark all notifications as read.
  Both buttons display explanatory tooltips on hover.
- **Notification Type Icons**: Each notification in the list now shows a leading icon reflecting its source (reminder, recurring payment, etc.).
- **Settings Help Tooltips with Hover Animation**: The question-mark icons (`?`) next to each setting option now scale up and change color on hover, providing clear visual feedback that the icon is interactive and has a tooltip.
- **Info Panel — Usage Guide**: The *About* dialog was converted from a release notes panel into an interactive step-by-step usage guide that explains the main features of the application to new users.
- **App CSS loaded in Dialogs**: The `Settings` and `Info` dialogs now explicitly load `app.css`, ensuring that all design tokens and component styles are applied consistently.

---

### Changed

- **Movements View — Full Responsiveness**: The `movements.fxml` layout was refactored using `HBox` with `HGrow.ALWAYS` constraints to correctly fill the available width at any window size, both small and maximized.
- **Dashboard Back Button → Integrated into Header**: The button to return to the main dashboard was moved out of its own standalone `VBox` and integrated directly into the blue header bar of each account sub-view, aligned to the right of the section title. This recovers vertical space and improves visual hierarchy.
- **Account Context Label**: The account name is now displayed alongside the section title in all sub-views (e.g., Movements, Goals), mirroring the existing behavior in the Movements tab.
- **Filter Panel**: Fixed the Movements filter panel to open as a proper sidebar rather than a separate window.
- **Date Picker Calendar Headers**: Fixed corrupted/blank month and year labels in the date picker calendars within the Movements view.
- **"Últimos 7 días" Label**: Fixed a character encoding issue where the accented text displayed as corrupted characters in the account dashboard.
- **Calculator Button Removed**: The non-functional calculator button was removed from the dashboard to avoid misleading the user. Per-account calculators remain available in the main account panel.
- **README — Team Credits**: Updated the `Authors` section in `README.md` to include the team name **RoastWare** and repository owner **Capi** alongside all individual contributors. Jose Pablo Martinez's contribution was expanded to reflect general refactoring and UI improvements.

---

### Fixed

- **CSS Warning `Could not resolve '-fx-danger-dark'`**: The undefined CSS variable `-fx-danger-dark` was replaced with the explicit hex value `#9B2C2C` in `app.css`, eliminating the `CssStyleHelper` runtime warning.
- **`NullPointerException` in `URLImageSource`** *(known, non-blocking)*: A JVM-level warning triggered at startup in development environments. Does not affect end users running the packaged installer.

---

### Removed

- **`FxmlTester.java`**: Temporary FXML inspection utilities used during UI development were deleted from both `src/main/java/config/` and `src/test/java/accounts/`.
- **Legacy Dashboard Files**: Obsolete Swing-era dashboard components were removed from the source tree.

---

---

# Registro de Cambios — Gestor de Finanzas *(Español)*

Todos los cambios notables del proyecto se documentan en este archivo.
El formato está basado en [Keep a Changelog](https://keepachangelog.com/) y [Conventional Commits](https://www.conventionalcommits.org/).

---

## [2.4.1] — Junio 2026

### Resumen
Esta versión entrega una mejora arquitectónica y de UX significativa respecto a `v2.0.0`. El shell principal fue reestructurado con un modelo de navegación por pestañas, se introdujo la ejecución en segundo plano mediante la bandeja del sistema, el sistema de notificaciones fue renovado completamente y se aplicó un pulido de UI generalizado en todas las vistas.

---

### Añadido

- **Navegación por Pestañas (`MainShell`)**: La ventana principal ahora usa un sistema de pestañas para navegar. Los sub-módulos de cada cuenta (Movimientos, Metas, Recurrentes, Recordatorios, Reportes) se muestran como pestañas navegables dentro del shell de cuenta, reemplazando el modelo de vista única anterior.
- **Ejecución en Segundo Plano — Bandeja del Sistema (`SingleInstanceGuard`)**: La aplicación puede ejecutarse silenciosamente en la bandeja del sistema tras cerrar la ventana principal. Un `SingleInstanceGuard` evita que se ejecuten procesos duplicados, usando un socket local en el puerto `49152`.
- **Estado de Lectura en Notificaciones**: Las notificaciones ahora tienen un indicador `isRead`. Los elementos no leídos se distinguen visualmente con estilo negrita y un badge en la barra superior.
- **Eliminación Individual de Notificaciones**: Cada notificación en la lista muestra un botón de eliminación (ícono de basurera) al hacer hover, permitiendo una gestión precisa.
- **Acciones Masivas de Notificaciones**: Se añadieron dos botones de ícono al encabezado del panel:
  - **Ícono de escoba** — Eliminar todas las notificaciones.
  - **Ícono de doble check** — Marcar todas como leídas.
  Ambos botones muestran tooltips explicativos al hacer hover.
- **Íconos de Tipo en Notificaciones**: Cada notificación en la lista muestra un ícono indicando su origen (recordatorio, pago recurrente, etc.).
- **Tooltips de Ayuda en Configuración con Animación**: Los íconos de signo de interrogación (`?`) junto a cada opción de configuración ahora escalan y cambian de color al hacer hover, indicando claramente que son interactivos y tienen un tooltip.
- **Panel de Info — Guía de Uso**: El diálogo *Acerca de* fue convertido de un panel de novedades de la versión a una guía interactiva paso a paso que explica las funcionalidades principales de la aplicación a nuevos usuarios.
- **CSS cargado en Diálogos**: Los diálogos de *Configuración* e *Info* ahora cargan explícitamente `app.css`, garantizando que todos los tokens de diseño y estilos de componentes se apliquen de forma consistente.

---

### Modificado

- **Vista de Movimientos — Responsividad Completa**: El layout `movements.fxml` fue refactorizado con `HBox` y restricciones `HGrow.ALWAYS` para ocupar correctamente el ancho disponible en cualquier tamaño de ventana.
- **Botón Volver al Dashboard → Integrado en el Encabezado**: El botón para regresar al dashboard fue movido de su propio `VBox` independiente al encabezado azul de cada sub-vista de cuenta, alineado a la derecha del título de la sección. Esto recupera espacio vertical y mejora la jerarquía visual.
- **Etiqueta de Contexto de Cuenta**: El nombre de la cuenta ahora aparece junto al título de sección en todas las sub-vistas, replicando el comportamiento existente en la pestaña de Movimientos.
- **Panel de Filtros**: Se corrigió el panel de filtros de Movimientos para que se abra como una barra lateral en lugar de una ventana aparte.
- **Encabezados del Selector de Fecha**: Se corrigieron las etiquetas de mes y año en blanco o corruptas en los calendarios de selección de fecha dentro de la vista de Movimientos.
- **Etiqueta "Últimos 7 días"**: Se corrigió un problema de codificación de caracteres donde el texto con tilde aparecía corrupto en el dashboard de cuenta.
- **Botón de Calculadora Eliminado**: El botón de calculadora no funcional fue eliminado del dashboard para evitar confusión. Las calculadoras por cuenta siguen disponibles en el panel principal de cada cuenta.
- **README — Créditos del Equipo**: Se actualizó la sección `Autores` del `README.md` para incluir al equipo **RoastWare** y al dueño del repositorio **Capi**, junto a todos los colaboradores individuales. La contribución de Jose Pablo Martinez fue expandida para reflejar las refactorizaciones generales y mejoras de UI.

---

### Corregido

- **Warning de CSS `Could not resolve '-fx-danger-dark'`**: La variable CSS no definida `-fx-danger-dark` fue reemplazada por el valor hexadecimal explícito `#9B2C2C` en `app.css`, eliminando la advertencia de `CssStyleHelper` en tiempo de ejecución.
- **`NullPointerException` en `URLImageSource`** *(conocido, no bloqueante)*: Una advertencia de la JVM disparada al inicio en entornos de desarrollo. No afecta a los usuarios finales que ejecutan el instalador empaquetado.

*Para el historial completo de versiones y la migración de Swing a JavaFX, consulta [MIGRATION_JAVAFX.md](MIGRATION_JAVAFX.md).*
