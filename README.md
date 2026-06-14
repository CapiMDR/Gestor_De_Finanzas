# Personal Finance Manager — Gestor de Finanzas

> A JavaFX desktop application for personal finance management, featuring account tracking, transaction logging, savings goals, recurring payments, reminders, and financial reports.

---

## Table of Contents

1. [About](#about)
2. [Built With](#built-with)
3. [First Delivery Documentation](#first-delivery-documentation)
4. [Features](#features)
5. [Tech Stack & Dependencies](#tech-stack--dependencies)
6. [Project Structure](#project-structure)
7. [Prerequisites](#prerequisites)
8. [Setup & Running](#setup--running)
9. [Running Tests](#running-tests)
10. [Architecture](#architecture)
11. [Data Persistence](#data-persistence)
12. [Contributing](#contributing)
13. [Authors](#authors)
14. [Features & UI/UX Changelog](docs/FEATURES_UI-UX.md)
15. [Versión en Español](#versión-en-español)

---

## About

**Personal Finance Manager** is a desktop application built with **JavaFX** and Maven. It allows users to manage multiple financial accounts, register income and expense transactions, track savings goals, schedule recurring payments, set date-based reminders, and generate financial reports with charts powered by JFreeChart.

> **v2.4.1** — The application was fully migrated from Java Swing to JavaFX in Phase 3 of the improvement plan, and this release introduces tab navigation, system tray background execution, and enhanced notifications. See [docs/MIGRATION_JAVAFX.md](docs/MIGRATION_JAVAFX.md) for the full migration log.

---

## Built With

| Technology | Version | Purpose |
|---|---|---|
| ![Java](https://img.shields.io/badge/Java_21_LTS-ED8B00?style=flat-square&logo=openjdk&logoColor=white) | 21 LTS | Core language — all application logic is written in Java |
| ![Maven](https://img.shields.io/badge/Apache_Maven-C71A36?style=flat-square&logo=apachemaven&logoColor=white) | 3.x | Build tool and dependency manager |
| ![JavaFX](https://img.shields.io/badge/JavaFX_21-007396?style=flat-square&logo=openjdk&logoColor=white) | 21.0.3 | Modern desktop GUI framework — replaced Java Swing in v2.0.0 (Now v2.4.1) |
| ![Ikonli](https://img.shields.io/badge/Ikonli-grey?style=flat-square&logoColor=white) | 12.3.1 | Vector icon library — Material Design 2 icon pack |
| ![JFreeChart](https://img.shields.io/badge/JFreeChart-4285F4?style=flat-square&logoColor=white) | 1.5.4 | Chart rendering library used in the reports module |
| ![JCalendar](https://img.shields.io/badge/JCalendar-grey?style=flat-square&logoColor=white) | 1.4 | Date picker widget used in forms (local JAR in `lib/`) |
| ![JUnit 5](https://img.shields.io/badge/JUnit_5-25A162?style=flat-square&logo=junit5&logoColor=white) | 5.11.4 | Unit testing framework |
| ![Mockito](https://img.shields.io/badge/Mockito-78A641?style=flat-square&logoColor=white) | 5.11.0 | Mocking library used in controller tests |
| ![org.json](https://img.shields.io/badge/org.json-grey?style=flat-square&logoColor=white) | 20250517 | JSON parsing and serialization for data persistence |
| ![SLF4J](https://img.shields.io/badge/SLF4J_Logback-grey?style=flat-square&logoColor=white) | 2.0.13 / 1.5.6 | Structured application logging |

---

## First Delivery Documentation

The following documents were produced during the first project delivery and serve as the design and planning baseline for the application:

| Document | Description |
|---|---|
| [Product Description](https://github.com/CapiMDR/Gestor_De_Finanzas/blob/First_Delivery/Delivery/Product_Description.md) | General description of the product, its purpose and scope |
| [Work Plan](https://github.com/CapiMDR/Gestor_De_Finanzas/blob/First_Delivery/Delivery/Work_Plan.md) | Task breakdown and timeline for the first delivery |
| [MockUps](https://github.com/CapiMDR/Gestor_De_Finanzas/blob/First_Delivery/Delivery/Design/MockUps.md) | UI mockups and visual design of the application |
| [Class Diagrams](https://github.com/CapiMDR/Gestor_De_Finanzas/blob/First_Delivery/Delivery/Design/Class_Diagrams) | UML class diagrams of the initial architecture |

> All documents are located in the [`First_Delivery`](https://github.com/CapiMDR/Gestor_De_Finanzas/blob/First_Delivery/Delivery) branch of this repository.

---

## Features

| Module | Functionality |
|---|---|
| **Account Management** | Create, edit and delete Cash or Digital accounts in MXN or USD. View current balance. Calculate interest. |
| **Movements** | Register income and expense transactions per account, with categories and timestamps. Fully responsive view. |
| **Goals** | Define savings goals per account with a target amount and description. Track progress. |
| **Recurring Moves** | Set up recurring payments/income (daily, weekly, biweekly, monthly, yearly) that auto-trigger when due. |
| **Reminders** | Schedule date-based reminders with a name and message. Alerts fire automatically when the scheduled time passes. |
| **Reports** | Generate movement reports filtered by today or the last 7 days, with totals and charts. |
| **Filters** | Filter movements by categories (income/expense) within an account view. |
| **Notifications** | In-app notification panel with read states, individual/bulk deletion, and type icons. |
| **Background Mode** | Run silently in the system tray after closing the main window — keeps reminders firing. |
| **Tab Navigation** | Navigate between account sub-modules (Movements, Goals, Recurring, Reminders, Reports) via tabs. |

---

## Tech Stack & Dependencies

| Technology | Version | Purpose |
|---|---|---|
| Java | 21+ | Core language |
| Maven | 3.x | Build & dependency management |
| JavaFX | 21.0.3 | GUI framework (replaces Swing since v2.0.0) |
| Ikonli + Material Design 2 | 12.3.1 | Vector icons |
| `org.json` | 20250517 | JSON data persistence |
| JFreeChart | 1.5.4 | Financial charts in reports |
| JCalendar | 1.4 | Date picker widget (local JAR) |
| SLF4J + Logback | 2.0.13 / 1.5.6 | Structured logging |
| JUnit Jupiter | 5.11.4 | Unit testing |
| Mockito | 5.11.0 | Mocking in unit tests |

> **Note:** `jcalendar-1.4.jar` is included locally in the `lib/` directory and must be installed into your local Maven repository using the `mvn install:install-file` command before building (see [Setup & Running](#setup--running)).

---

## Project Structure

```
gestorFinanzas/
├── lib/
│   └── jcalendar-1.4.jar            # Local dependency (date picker)
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── accounts/            # Account module (MVC)
│   │   │   │   ├── account_controller/
│   │   │   │   ├── account_model/
│   │   │   │   └── account_view/    # AccountViewFX.java (JavaFX)
│   │   │   ├── movements/           # Movements module (MVC)
│   │   │   │   ├── movement_controller/
│   │   │   │   ├── movement_model/
│   │   │   │   └── movement_view/   # MovementsViewFX.java (JavaFX)
│   │   │   ├── goals/               # Savings goals module (MVC)
│   │   │   │   ├── goals_controller/
│   │   │   │   ├── goals_model/
│   │   │   │   └── goals_view/      # GoalsViewFX.java (JavaFX)
│   │   │   ├── recurringMoves/      # Recurring payments module (MVC)
│   │   │   │   ├── recurring_controller/
│   │   │   │   ├── recurring_model/
│   │   │   │   └── recurring_view/  # RecurringsViewFX.java (JavaFX)
│   │   │   ├── reminders/           # Reminders/notifications module (MVC)
│   │   │   │   ├── reminder_controller/
│   │   │   │   ├── reminder_model/
│   │   │   │   └── reminder_view/   # RemindersViewFX.java (JavaFX)
│   │   │   ├── reports/             # Financial reports module (Model only, UI in accounts)
│   │   │   ├── filters/             # Category filters module
│   │   │   ├── utils/               # Shared utilities (UIUtils, etc.)
│   │   │   ├── config/
│   │   │   │   └── AppConfig.java   # Centralized data paths
│   │   │   └── com/mycompany/construccion/
│   │   │       ├── Main.java        # JavaFX Application entry point
│   │   │       └── AppLauncher.java # Fat-jar / installer wrapper
│   │   └── resources/
│   │       ├── fxml/                # FXML layout files for all views
│   │       │   ├── accounts/
│   │       │   ├── movements/
│   │       │   ├── goals/
│   │       │   ├── recurringMoves/
│   │       │   └── reminders/
│   │       ├── styles/
│   │       │   └── app.css          # Centralized CSS design system
│   │       └── images/              # Icon assets
│   └── test/
│       └── java/                    # Unit tests (JUnit + Mockito)
│           ├── accounts/
│           ├── goals/
│           └── movements/
├── docs/
│   ├── MIGRATION_JAVAFX.md         # JavaFX migration log
│   ├── FEATURES_UI-UX.md           # UI/UX feature changelog (v2.4.1)
│   └── APP_FLOW.md                # Technical architecture document
├── .github/
│   └── workflows/
│       ├── sonarcloud.yml           # SonarCloud static analysis
│       └── release.yml              # Native installer release
├── jreleaser.yml                    # JReleaser artifact distribution config
├── pom.xml                          # Maven build file
└── .gitignore
```

---

## Prerequisites

- **Java JDK 21 or higher** — [Download](https://www.oracle.com/java/technologies/downloads/)
- **Apache Maven 3.x** — [Download](https://maven.apache.org/download.cgi)
- **VSCode** with the [Extension Pack for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack) (recommended IDE)

---

## Setup & Running

### 1. Clone the repository

```bash
git clone https://github.com/CapiMDR/Gestor_De_Finanzas.git
cd Gestor_De_Finanzas
```

### 2. Install local dependencies (Run once per machine)

Before compiling, you must install the local `jcalendar` dependency into your local Maven repository:

```bash
mvn install:install-file "-Dfile=lib/jcalendar-1.4.jar" "-DgroupId=com.toedter" "-DartifactId=jcalendar" "-Dversion=1.4" "-Dpackaging=jar"
```

> **Important Note:** This command only needs to be executed once per machine.

### 3. Run the application

**From terminal (JavaFX plugin):**
```bash
mvn javafx:run
```

**Or via the fat-jar (after packaging):**
```bash
mvn package -DskipTests
java -jar target/gestor-finanzas-2.4.1.jar
```

> **Data storage:** All JSON data files are saved to `~/.gestor-finanzas/` in the user's home directory, completely independent of the installation or execution path.

---

## Running Tests

```bash
mvn test
```

Tests are located in `src/test/java/` and cover:

- `AccountTest` — Account model unit tests
- `AccountManagerTest` — Account manager CRUD operations
- `MovementTest` — Movement creation and category logic
- `CategoryManagerTest` — Category management tests
- `GoalsControllerTest` — Savings goals controller tests
- `GoalDetailControllerTest` — Goal detail logic tests

> **Note:** The goals module tests currently have compilation issues unrelated to the JavaFX migration. Build with `-DskipTests` to skip them when packaging.

---

## Architecture

The project follows the **MVC (Model-View-Controller)** pattern across all modules, and uses the **Observer pattern** for cross-module communication (e.g., when accounts update, the view is automatically notified).

```
┌─────────────────┐       ┌──────────────┐       ┌──────────────┐
│    View          │◄──────│  Controller  │──────►│    Model     │
│  (JavaFX/FXML)  │       │  (Logic)     │       │  (Data/JSON) │
└─────────────────┘       └──────────────┘       └──────────────┘
       ▲                                                 │
       └──────────────── Observer notifications ─────────┘
```

### Key design patterns used:
- **MVC** — Every module (accounts, movements, goals, etc.) is split into model, view, and controller packages.
- **Observer / Subject** — `AccountManagerSubject`, `ReportSubject`, `MovementManagerSubject` notify registered observers on data changes.
- **Singleton-like static manager** — `AccountManager` is a static utility class providing centralized account operations.

For full architectural details see [docs/APP_FLOW.md](docs/APP_FLOW.md).
For the complete migration log from Swing to JavaFX see [docs/MIGRATION_JAVAFX.md](docs/MIGRATION_JAVAFX.md).

---

## Data Persistence

All data is persisted as **JSON files** in the user's home directory at `~/.gestor-finanzas/` using `org.json`:

| File | Contents |
|---|---|
| `accounts_data.json` | All accounts with their movements and goals |
| `categories_data.json` | Custom movement categories |
| `recurrings.json` | Recurring payment definitions |
| `reminders.json` | Scheduled reminders |

Data is loaded on startup and saved automatically after every modification using **atomic writes** to prevent JSON corruption.

---

## Contributing

1. Fork the repository
2. Create a new branch: `git checkout -b feature/your-feature-name`
3. Commit your changes following [Conventional Commits](https://www.conventionalcommits.org/): `git commit -m "feat: add your feature"`
4. Push to your branch: `git push origin feature/your-feature-name`
5. Open a Pull Request

See [docs/CONTRIBUTING.md](docs/CONTRIBUTING.md) for full contribution guidelines.

---

## Authors

| Name | Module(s) |
|---|---|
| RoastWare (Team) | Overall Application Architecture & Design |
| Capi | Lead Developer & Repository Owner |
| Martín Jesús Pool Chuc | Accounts, Movements, Movement Categories |
| Jose Pablo Martinez | Goals, General Refactoring & UI Improvements |
| villa | Reports |

> For a full list of contributors, see the [GitHub repository](https://github.com/CapiMDR/Gestor_De_Finanzas).

---

---

# Versión en Español

> Una aplicación de escritorio **JavaFX** para la gestión de finanzas personales, con seguimiento de cuentas, registro de movimientos, metas de ahorro, pagos recurrentes, recordatorios e informes financieros.

---

## Índice

1. [Acerca del proyecto](#acerca-del-proyecto)
2. [Construido con](#construido-con)
3. [Documentación de la Primera Entrega](#documentación-de-la-primera-entrega)
4. [Funcionalidades](#funcionalidades)
5. [Tecnologías y dependencias](#tecnologías-y-dependencias)
6. [Estructura del proyecto](#estructura-del-proyecto)
7. [Prerrequisitos](#prerrequisitos)
8. [Configuración y ejecución](#configuración-y-ejecución)
9. [Ejecutar pruebas](#ejecutar-pruebas)
10. [Arquitectura](#arquitectura)
11. [Persistencia de datos](#persistencia-de-datos)
12. [Contribuir](#contribuir)
13. [Autores](#autores)

---

## Acerca del proyecto

**Gestor de Finanzas** es una aplicación de escritorio desarrollada con **JavaFX** y Maven. Permite al usuario gestionar múltiples cuentas financieras, registrar movimientos de ingreso y gasto, hacer seguimiento de metas de ahorro, programar pagos recurrentes, establecer recordatorios por fecha y generar reportes financieros con gráficas mediante JFreeChart.

> **v2.4.1** — La aplicación fue migrada completamente de Java Swing a JavaFX en la Fase 3 del plan de mejoras, y en esta versión se introduce el sistema de pestañas, el modo en segundo plano y mejoras masivas en notificaciones. Consulta [docs/MIGRATION_JAVAFX.md](docs/MIGRATION_JAVAFX.md) para el registro completo de la migración.

---

## Construido con

| Tecnología | Versión | Propósito |
|---|---|---|
| ![Java](https://img.shields.io/badge/Java_21_LTS-ED8B00?style=flat-square&logo=openjdk&logoColor=white) | 21 LTS | Lenguaje principal |
| ![Maven](https://img.shields.io/badge/Apache_Maven-C71A36?style=flat-square&logo=apachemaven&logoColor=white) | 3.x | Herramienta de construcción y gestión de dependencias |
| ![JavaFX](https://img.shields.io/badge/JavaFX_21-007396?style=flat-square&logo=openjdk&logoColor=white) | 21.0.3 | Framework moderno de GUI — reemplazó Java Swing en v2.0.0 (Actual v2.4.1) |
| ![Ikonli](https://img.shields.io/badge/Ikonli-grey?style=flat-square&logoColor=white) | 12.3.1 | Librería de íconos vectoriales — paquete Material Design 2 |
| ![JFreeChart](https://img.shields.io/badge/JFreeChart-4285F4?style=flat-square&logoColor=white) | 1.5.4 | Biblioteca de gráficas en el módulo de reportes |
| ![JCalendar](https://img.shields.io/badge/JCalendar-grey?style=flat-square&logoColor=white) | 1.4 | Selector de fechas (JAR local en `lib/`) |
| ![JUnit 5](https://img.shields.io/badge/JUnit_5-25A162?style=flat-square&logo=junit5&logoColor=white) | 5.11.4 | Framework de pruebas unitarias |
| ![Mockito](https://img.shields.io/badge/Mockito-78A641?style=flat-square&logoColor=white) | 5.11.0 | Biblioteca de mocking en pruebas de controladores |
| ![org.json](https://img.shields.io/badge/org.json-grey?style=flat-square&logoColor=white) | 20250517 | Parseo y serialización JSON para la persistencia |
| ![SLF4J](https://img.shields.io/badge/SLF4J_Logback-grey?style=flat-square&logoColor=white) | 2.0.13 / 1.5.6 | Logging estructurado de la aplicación |

---

## Documentación de la Primera Entrega

Los siguientes documentos fueron producidos durante la primera entrega del proyecto y sirven como base de diseño y planificación:

| Documento | Descripción |
|---|---|
| [Descripción del Producto](https://github.com/CapiMDR/Gestor_De_Finanzas/blob/First_Delivery/Delivery/Product_Description.md) | Descripción general del producto, su propósito y alcance |
| [Plan de Trabajo](https://github.com/CapiMDR/Gestor_De_Finanzas/blob/First_Delivery/Delivery/Work_Plan.md) | Desglose de tareas y cronograma de la primera entrega |
| [MockUps](https://github.com/CapiMDR/Gestor_De_Finanzas/blob/First_Delivery/Delivery/Design/MockUps.md) | Mockups de la interfaz y diseño visual de la aplicación |
| [Diagramas de Clases](https://github.com/CapiMDR/Gestor_De_Finanzas/blob/First_Delivery/Delivery/Design/Class_Diagrams) | Diagramas UML de la arquitectura inicial |

> Todos los documentos se encuentran en la rama [`First_Delivery`](https://github.com/CapiMDR/Gestor_De_Finanzas/blob/First_Delivery/Delivery) de este repositorio.

---

## Funcionalidades

| Módulo | Funcionalidad |
|---|---|
| **Gestión de Cuentas** | Crear, editar y eliminar cuentas de tipo Efectivo o Digital en MXN o USD. Ver saldo actual. Calcular intereses. |
| **Movimientos** | Registrar ingresos y gastos por cuenta, con categorías y fecha/hora automática. |
| **Metas** | Definir metas de ahorro por cuenta con monto objetivo y descripción. Hacer seguimiento del progreso. |
| **Movimientos Recurrentes** | Configurar pagos/ingresos recurrentes (diario, semanal, quincenal, mensual, anual) que se activan automáticamente al vencer. |
| **Recordatorios** | Programar recordatorios con nombre, mensaje y fecha. Las alertas se disparan automáticamente cuando llega la hora programada. |
| **Reportes** | Generar reportes de movimientos filtrados por hoy o los últimos 7 días, con totales y gráficas. |
| **Filtros** | Filtrar movimientos por categorías (ingresos/gastos) dentro de la vista de una cuenta. |

---

## Tecnologías y dependencias

| Tecnología | Versión | Propósito |
|---|---|---|
| Java | 21+ | Lenguaje principal |
| Maven | 3.x | Construcción y gestión de dependencias |
| JavaFX | 21.0.3 | Framework de interfaz gráfica (reemplaza Swing desde v2.0.0) |
| Ikonli + Material Design 2 | 12.3.1 | Íconos vectoriales |
| `org.json` | 20250517 | Persistencia de datos en JSON |
| JFreeChart | 1.5.4 | Gráficas en los reportes financieros |
| JCalendar | 1.4 | Selector de fechas (JAR local) |
| SLF4J + Logback | 2.0.13 / 1.5.6 | Logging estructurado |
| JUnit Jupiter | 5.11.4 | Pruebas unitarias |
| Mockito | 5.11.0 | Mocking en pruebas unitarias |

> **Nota:** `jcalendar-1.4.jar` está incluido localmente en la carpeta `lib/` y debe instalarse en el repositorio local de Maven usando el comando `mvn install:install-file` antes de compilar.

---

## Estructura del proyecto

Consulta la sección [Project Structure](#project-structure) en inglés — la estructura de carpetas es idéntica.

---

## Prerrequisitos

- **Java JDK 21 o superior** — [Descargar](https://www.oracle.com/java/technologies/downloads/)
- **Apache Maven 3.x** — [Descargar](https://maven.apache.org/download.cgi)
- **VSCode** con el [Extension Pack for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack) (IDE recomendado)

---

## Configuración y ejecución

### 1. Clonar el repositorio

```bash
git clone https://github.com/CapiMDR/Gestor_De_Finanzas.git
cd Gestor_De_Finanzas
```

### 2. Instalar dependencias locales (Ejecutar una vez por máquina)

```bash
mvn install:install-file "-Dfile=lib/jcalendar-1.4.jar" "-DgroupId=com.toedter" "-DartifactId=jcalendar" "-Dversion=1.4" "-Dpackaging=jar"
```

### 3. Ejecutar la aplicación

**Desde la terminal (plugin JavaFX):**
```bash
mvn javafx:run
```

**O mediante el fat-jar (después de empaquetar):**
```bash
mvn package -DskipTests
java -jar target/gestor-finanzas-2.4.1.jar
```

> **Almacenamiento de datos:** Todos los archivos JSON se guardan en `~/.gestor-finanzas/` dentro del directorio `home` del usuario, completamente independiente de la ruta de instalación.

---

## Ejecutar pruebas

```bash
mvn test
```

Las pruebas están en `src/test/java/` y cubren cuentas, movimientos, categorías y metas de ahorro.

> **Nota:** El módulo de metas tiene errores de compilación en sus pruebas no relacionados con la migración a JavaFX. Usa `-DskipTests` al empaquetar para omitirlas.

---

## Arquitectura

El proyecto sigue el patrón **MVC (Modelo-Vista-Controlador)** en todos los módulos, y usa el **patrón Observer** para comunicación entre módulos.

### Patrones de diseño utilizados:
- **MVC** — Cada módulo está dividido en paquetes `model`, `view` y `controller`.
- **Observer / Subject** — `AccountManagerSubject`, `ReportSubject` y `MovementManagerSubject` notifican a los observadores cuando los datos cambian.
- **Gestor estático** — `AccountManager` centraliza las operaciones sobre cuentas.

Consulta [docs/APP_FLOW.md](docs/APP_FLOW.md) para detalles completos de la arquitectura.
Consulta [docs/MIGRATION_JAVAFX.md](docs/MIGRATION_JAVAFX.md) para el registro completo de la migración de Swing a JavaFX.

---

## Persistencia de datos

Todos los datos se persisten como **archivos JSON** en `~/.gestor-finanzas/` usando `org.json`:

| Archivo | Contenido |
|---|---|
| `accounts_data.json` | Todas las cuentas con sus movimientos y metas |
| `categories_data.json` | Categorías de movimientos personalizadas |
| `recurrings.json` | Definiciones de movimientos recurrentes |
| `reminders.json` | Recordatorios programados |

Los datos se cargan al iniciar y se guardan automáticamente tras cada modificación mediante **escrituras atómicas**.

---

## Contribuir

1. Haz un fork del repositorio
2. Crea una rama nueva: `git checkout -b feature/nombre-de-tu-feature`
3. Haz commit siguiendo [Conventional Commits](https://www.conventionalcommits.org/): `git commit -m "feat: descripción del cambio"`
4. Sube tu rama: `git push origin feature/nombre-de-tu-feature`
5. Abre un Pull Request

Consulta [docs/CONTRIBUTING.md](docs/CONTRIBUTING.md) para las guías completas de contribución.

---

## Autores

| Nombre | Módulo(s) |
|---|---|
| RoastWare (Equipo) | Arquitectura y Diseño General de la Aplicación |
| Capi | Desarrollador Principal y Dueño del Repositorio |
| Martín Jesús Pool Chuc | Cuentas, Movimientos, Categorías de Movimientos |
| Jose Pablo Martinez | Metas de Ahorro, Refactorización General y Mejoras de UI |
| villa | Reportes |

> Para la lista completa de contribuidores, visita el [repositorio en GitHub](https://github.com/CapiMDR/Gestor_De_Finanzas).
