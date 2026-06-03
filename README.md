# Personal Finance Manager — Gestor de Finanzas

> A Java Swing desktop application for personal finance management, featuring account tracking, transaction logging, savings goals, recurring payments, reminders, and financial reports.

---

## Table of Contents

1. [About](#about)
2. [Features](#features)
3. [Tech Stack & Dependencies](#tech-stack--dependencies)
4. [Project Structure](#project-structure)
5. [Prerequisites](#prerequisites)
6. [Setup & Running](#setup--running)
7. [Running Tests](#running-tests)
8. [Architecture](#architecture)
9. [Data Persistence](#data-persistence)
10. [Contributing](#contributing)
11. [Authors](#authors)
12. [Versión en Español](#versión-en-español)

---

## About

**Personal Finance Manager** is a desktop application built with Java Swing and Maven. It allows users to manage multiple financial accounts, register income and expense movements, track savings goals, schedule recurring payments, set date-based reminders, and generate financial reports with charts powered by JFreeChart.

---

## Features

| Module | Functionality |
|---|---|
| 💳 **Account Management** | Create, edit and delete Cash or Digital accounts in MXN or USD. View current balance. Calculate interest. |
| 📊 **Movements** | Register income and expense transactions per account, with categories and timestamps. |
| 🎯 **Goals** | Define savings goals per account with a target amount and description. Track progress. |
| 🔁 **Recurring Moves** | Set up recurring payments/income (daily, weekly, biweekly, monthly, yearly) that auto-trigger when due. |
| 🔔 **Reminders** | Schedule date-based reminders with a name and message. Alerts fire automatically when the scheduled time passes. |
| 📈 **Reports** | Generate movement reports filtered by today or the last 7 days, with totals and charts. |
| 🗂️ **Filters** | Filter movements by categories (income/expense) within an account view. |

---

## Tech Stack & Dependencies

| Technology | Version | Purpose |
|---|---|---|
| Java | 21+ | Core language |
| Maven | 3.x | Build & dependency management |
| Java Swing | (JDK built-in) | GUI framework |
| `org.json` | 20250517 | JSON data persistence |
| JFreeChart | 1.5.4 | Financial charts in reports |
| JCalendar | 1.4 | Date picker widget (local JAR) |
| JUnit 4 / JUnit Jupiter | 4.11 / 5.13 | Unit testing |
| Mockito | 5.11.0 | Mocking in unit tests |

> **Note:** `jcalendar-1.4.jar` is included locally in the `lib/` directory and must be installed into your local Maven repository before building (see [Setup](#setup--running)).

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
│   │   │   │   └── account_view/
│   │   │   ├── movements/           # Movements module (MVC)
│   │   │   │   ├── movement_controller/
│   │   │   │   ├── movement_model/
│   │   │   │   └── movement_view/
│   │   │   ├── goals/               # Savings goals module (MVC)
│   │   │   │   ├── goals_controller/
│   │   │   │   ├── goals_model/
│   │   │   │   └── goals_view/
│   │   │   ├── recurringMoves/      # Recurring payments module (MVC)
│   │   │   │   ├── recurring_controller/
│   │   │   │   ├── recurring_model/
│   │   │   │   └── recurring_view/
│   │   │   ├── reminders/           # Reminders/notifications module (MVC)
│   │   │   │   ├── reminder_controller/
│   │   │   │   ├── reminder_model/
│   │   │   │   └── reminder_view/
│   │   │   ├── reports/             # Financial reports module
│   │   │   │   ├── controllerReport/
│   │   │   │   └── modelReport/
│   │   │   ├── filters/             # Category filters module
│   │   │   │   ├── controllerFilter/
│   │   │   │   ├── modelFilter/
│   │   │   │   └── viewFilter/
│   │   │   └── com/mycompany/construccion/
│   │   │       ├── Main.java        # Application entry point
│   │   │       └── FrmMain.java     # Main application frame
│   │   └── resources/
│   │       └── images/              # UI icon assets
│   └── test/
│       └── java/                    # Unit tests (JUnit + Mockito)
│           ├── accounts/
│           ├── goals/
│           └── movements/
├── accounts_data.json               # Persisted account data
├── categories_data.json             # Persisted categories
├── recurrings.json                  # Persisted recurring moves
├── reminders.json                   # Persisted reminders
├── pom.xml                          # Maven build file
└── .gitignore
```

---

## Prerequisites

- **Java JDK 21 or higher** — [Download](https://www.oracle.com/java/technologies/downloads/)
- **Apache Maven 3.x** — [Download](https://maven.apache.org/download.cgi)
- **VSCode** with the [Extension Pack for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack) (recommended IDE)
  - Or **NetBeans 12+** (original IDE; `.form` files are NetBeans GUI designer descriptors)

---

## Setup & Running

### 1. Clone the repository

```bash
git clone https://github.com/CapiMDR/Gestor_De_Finanzas.git
cd Gestor_De_Finanzas
```

### 2. Install the local JCalendar dependency

The `jcalendar-1.4.jar` is not available on Maven Central, so it must be installed manually into your local Maven repository once:

```bash
mvn install:install-file \
  -Dfile=lib/jcalendar-1.4.jar \
  -DgroupId=com.toedter \
  -DartifactId=jcalendar \
  -Dversion=1.4 \
  -Dpackaging=jar
```

> On Windows (PowerShell), replace line breaks with a single line or use backticks `` ` `` for continuation.

### 3. Compile the project

```bash
mvn compile
```

### 4. Run the application

**From terminal:**
```bash
mvn exec:java -Dexec.mainClass="com.mycompany.construccion.Main"
```

**From VSCode** (with Extension Pack for Java installed):
1. Open `Main.java`
2. Click the ▶️ **Run** button that appears above the `main` method

**From VSCode with F5** (after setting up `launch.json`):
```json
// .vscode/launch.json
{
  "version": "0.2.0",
  "configurations": [
    {
      "type": "java",
      "name": "Launch Main",
      "request": "launch",
      "mainClass": "com.mycompany.construccion.Main",
      "projectName": "reminders_notifications"
    }
  ]
}
```

> **Note:** The JSON data files (`accounts_data.json`, `reminders.json`, etc.) are read from the **working directory** where the application is launched. Running from the project root ensures data is found correctly.

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

---

## Architecture

The project follows the **MVC (Model-View-Controller)** pattern across all modules, and uses the **Observer pattern** for cross-module communication (e.g., when accounts update, the view is automatically notified).

```
┌─────────────┐       ┌──────────────┐       ┌──────────────┐
│    View     │◄──────│  Controller  │──────►│    Model     │
│  (Swing UI) │       │  (Logic)     │       │  (Data/JSON) │
└─────────────┘       └──────────────┘       └──────────────┘
       ▲                                             │
       └──────────── Observer notifications ─────────┘
```

### Key design patterns used:
- **MVC** — Every module (accounts, movements, goals, etc.) is split into model, view, and controller packages.
- **Observer / Subject** — `AccountManagerSubject`, `ReportSubject`, `MovementManagerSubject` notify registered observers on data changes.
- **Singleton-like static manager** — `AccountManager` is a static utility class providing centralized account operations.

---

## Data Persistence

All data is persisted as **JSON files** in the project root using `org.json`:

| File | Contents |
|---|---|
| `accounts_data.json` | All accounts with their movements and goals |
| `categories_data.json` | Custom movement categories |
| `recurrings.json` | Recurring payment definitions |
| `reminders.json` | Scheduled reminders |

Data is loaded on startup and saved automatically after every modification.

---

## Contributing

1. Fork the repository
2. Create a new branch: `git checkout -b feature/your-feature-name`
3. Commit your changes: `git commit -m "feat: add your feature"`
4. Push to your branch: `git push origin feature/your-feature-name`
5. Open a Pull Request

---

## Authors

| Name | Module(s) |
|---|---|
| Martín Jesús Pool Chuc | Accounts, Movements, Movement Categories |
| Jose Pablo Martinez | Goals |
| villa | Reports |

> For a full list of contributors, see the [GitHub repository](https://github.com/CapiMDR/Gestor_De_Finanzas).

---

---

# Versión en Español

> Una aplicación de escritorio Java Swing para la gestión de finanzas personales, con seguimiento de cuentas, registro de movimientos, metas de ahorro, pagos recurrentes, recordatorios e informes financieros.

---

## Índice

1. [Acerca del proyecto](#acerca-del-proyecto)
2. [Funcionalidades](#funcionalidades)
3. [Tecnologías y dependencias](#tecnologías-y-dependencias)
4. [Estructura del proyecto](#estructura-del-proyecto)
5. [Prerrequisitos](#prerrequisitos)
6. [Configuración y ejecución](#configuración-y-ejecución)
7. [Ejecutar pruebas](#ejecutar-pruebas)
8. [Arquitectura](#arquitectura)
9. [Persistencia de datos](#persistencia-de-datos)
10. [Contribuir](#contribuir)
11. [Autores](#autores)

---

## Acerca del proyecto

**Gestor de Finanzas** es una aplicación de escritorio desarrollada con Java Swing y Maven. Permite al usuario gestionar múltiples cuentas financieras, registrar movimientos de ingreso y gasto, hacer seguimiento de metas de ahorro, programar pagos recurrentes, establecer recordatorios por fecha y generar reportes financieros con gráficas mediante JFreeChart.

---

## Funcionalidades

| Módulo | Funcionalidad |
|---|---|
| 💳 **Gestión de Cuentas** | Crear, editar y eliminar cuentas de tipo Efectivo o Digital en MXN o USD. Ver saldo actual. Calcular intereses. |
| 📊 **Movimientos** | Registrar ingresos y gastos por cuenta, con categorías y fecha/hora automática. |
| 🎯 **Metas** | Definir metas de ahorro por cuenta con monto objetivo y descripción. Hacer seguimiento del progreso. |
| 🔁 **Movimientos Recurrentes** | Configurar pagos/ingresos recurrentes (diario, semanal, quincenal, mensual, anual) que se activan automáticamente al vencer. |
| 🔔 **Recordatorios** | Programar recordatorios con nombre, mensaje y fecha. Las alertas se disparan automáticamente cuando llega la hora programada. |
| 📈 **Reportes** | Generar reportes de movimientos filtrados por hoy o los últimos 7 días, con totales y gráficas. |
| 🗂️ **Filtros** | Filtrar movimientos por categorías (ingresos/gastos) dentro de la vista de una cuenta. |

---

## Tecnologías y dependencias

| Tecnología | Versión | Propósito |
|---|---|---|
| Java | 21+ | Lenguaje principal |
| Maven | 3.x | Construcción y gestión de dependencias |
| Java Swing | (incluido en JDK) | Framework de interfaz gráfica |
| `org.json` | 20250517 | Persistencia de datos en JSON |
| JFreeChart | 1.5.4 | Gráficas en los reportes financieros |
| JCalendar | 1.4 | Selector de fechas (JAR local) |
| JUnit 4 / JUnit Jupiter | 4.11 / 5.13 | Pruebas unitarias |
| Mockito | 5.11.0 | Mocking en pruebas unitarias |

> **Nota:** `jcalendar-1.4.jar` está incluido localmente en la carpeta `lib/` y debe instalarse en el repositorio local de Maven antes de compilar (ver [Configuración](#configuración-y-ejecución)).

---

## Estructura del proyecto

Consulta la sección [Project Structure](#project-structure) en inglés — la estructura de carpetas es idéntica.

---

## Prerrequisitos

- **Java JDK 21 o superior** — [Descargar](https://www.oracle.com/java/technologies/downloads/)
- **Apache Maven 3.x** — [Descargar](https://maven.apache.org/download.cgi)
- **VSCode** con el [Extension Pack for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack) (IDE recomendado)
  - O **NetBeans 12+** (IDE original; los archivos `.form` son descriptores del diseñador gráfico de NetBeans)

---

## Configuración y ejecución

### 1. Clonar el repositorio

```bash
git clone https://github.com/CapiMDR/Gestor_De_Finanzas.git
cd Gestor_De_Finanzas
```

### 2. Instalar la dependencia local JCalendar

`jcalendar-1.4.jar` no está disponible en Maven Central, por lo que debe instalarse manualmente en el repositorio local de Maven una sola vez:

```bash
mvn install:install-file \
  -Dfile=lib/jcalendar-1.4.jar \
  -DgroupId=com.toedter \
  -DartifactId=jcalendar \
  -Dversion=1.4 \
  -Dpackaging=jar
```

> En Windows (PowerShell), escribe el comando en una sola línea o usa backtick `` ` `` para continuar en la siguiente línea.

### 3. Compilar el proyecto

```bash
mvn compile
```

### 4. Ejecutar la aplicación

**Desde la terminal:**
```bash
mvn exec:java -Dexec.mainClass="com.mycompany.construccion.Main"
```

**Desde VSCode** (con Extension Pack for Java instalado):
1. Abre `Main.java`
2. Haz clic en el botón ▶️ **Run** que aparece sobre el método `main`

> **Nota:** Los archivos JSON de datos se leen desde el **directorio de trabajo** donde se lanza la aplicación. Ejecutar desde la raíz del proyecto garantiza que los datos se encuentren correctamente.

---

## Ejecutar pruebas

```bash
mvn test
```

Las pruebas están en `src/test/java/` y cubren cuentas, movimientos, categorías y metas de ahorro.

---

## Arquitectura

El proyecto sigue el patrón **MVC (Modelo-Vista-Controlador)** en todos los módulos, y usa el **patrón Observer** para comunicación entre módulos (por ejemplo, cuando se actualiza una cuenta, la vista se notifica automáticamente).

### Patrones de diseño utilizados:
- **MVC** — Cada módulo (cuentas, movimientos, metas, etc.) está dividido en paquetes `model`, `view` y `controller`.
- **Observer / Subject** — `AccountManagerSubject`, `ReportSubject` y `MovementManagerSubject` notifican a los observadores registrados cuando los datos cambian.
- **Gestor estático** — `AccountManager` es una clase utilitaria estática que centraliza las operaciones sobre cuentas.

---

## Persistencia de datos

Todos los datos se persisten como **archivos JSON** en la raíz del proyecto usando `org.json`:

| Archivo | Contenido |
|---|---|
| `accounts_data.json` | Todas las cuentas con sus movimientos y metas |
| `categories_data.json` | Categorías de movimientos personalizadas |
| `recurrings.json` | Definiciones de movimientos recurrentes |
| `reminders.json` | Recordatorios programados |

Los datos se cargan al iniciar la aplicación y se guardan automáticamente después de cada modificación.

---

## Contribuir

1. Haz un fork del repositorio
2. Crea una rama nueva: `git checkout -b feature/nombre-de-tu-feature`
3. Haz commit de tus cambios: `git commit -m "feat: descripción del cambio"`
4. Sube tu rama: `git push origin feature/nombre-de-tu-feature`
5. Abre un Pull Request

---

## Autores

| Nombre | Módulo(s) |
|---|---|
| Martín Jesús Pool Chuc | Cuentas, Movimientos, Categorías de Movimientos |
| Jose Pablo Martinez | Metas de Ahorro |
| villa | Reportes |

> Para la lista completa de contribuidores, visita el [repositorio en GitHub](https://github.com/CapiMDR/Gestor_De_Finanzas).
