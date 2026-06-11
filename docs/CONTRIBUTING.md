# CONTRIBUTING — Gestor de Finanzas
> Guía de desarrollo y estándares del proyecto.  
> **Leer este archivo antes de escribir o modificar cualquier archivo del proyecto.**

---

## 1. Identidad del Proyecto

| Campo | Valor |
|---|---|
| Nombre | Gestor de Finanzas |
| Dominio | Aplicación de escritorio para finanzas personales |
| Arquitectura | Java Desktop · MVC + Observer · Persistencia JSON local |
| Etapa actual | Migración activa Swing → JavaFX |
| Distribución | JAR ejecutable · Instalador nativo (.exe) vía jpackage |
| Repositorio | [github.com/CapiMDR/Gestor_De_Finanzas](https://github.com/CapiMDR/Gestor_De_Finanzas) |

El Gestor de Finanzas es una aplicación de escritorio local que permite registrar cuentas,
movimientos, metas financieras, movimientos recurrentes y recordatorios. Todos los datos
se guardan localmente en el directorio `home` del usuario — sin servidores, sin nube,
sin autenticación.

---

## 2. Estructura del Proyecto

No crear archivos fuera de esta estructura sin acordarlo primero.

```
gestorFinanzas/
│
├── docs/
│   ├── CONTRIBUTING.md          ← Este archivo
│   └── APP_FLOW.md              ← Flujo técnico de la aplicación (arranque, datos, arquitectura)
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── accounts/        ← Módulo de cuentas (model / view / controller)
│   │   │   ├── movements/       ← Módulo de movimientos
│   │   │   ├── goals/           ← Módulo de metas financieras
│   │   │   ├── recurringMoves/  ← Módulo de movimientos recurrentes
│   │   │   ├── reminders/       ← Módulo de recordatorios
│   │   │   ├── reports/         ← Módulo de reportes y generación de datos
│   │   │   ├── filters/         ← Módulo de filtros por categoría y fecha
│   │   │   ├── config/          ← Configuración central (AppConfig.java)
│   │   │   └── com/mycompany/construccion/  ← Punto de entrada (Main.java / MainApp.java)
│   │   └── resources/
│   │       ├── fxml/            ← Layouts declarativos de JavaFX (*.fxml)
│   │       ├── styles/          ← Hojas de estilo JavaFX (app.css)
│   │       └── images/          ← Recursos de imagen y íconos
│   └── test/
│       └── java/                ← Tests unitarios (espejo de src/main/java)
│           ├── accounts/
│           ├── goals/
│           ├── movements/
│           └── reports/
│
├── lib/                         ← JARs de dependencias locales (legacy)
├── .github/workflows/           ← Pipelines de CI/CD (SonarCloud, Release)
├── pom.xml                      ← Definición del proyecto Maven
└── jreleaser.yml                ← Configuración de empaquetado y release
```

### 2.1 Estructura interna de cada módulo

Cada módulo de dominio sigue la misma convención de tres subcarpetas:

```
<módulo>/
├── <módulo>_model/      ← Entidades, lógica de negocio, acceso a datos (JSON)
├── <módulo>_view/       ← Presentación visual (JavaFX: *ViewFX.java + *.fxml)
└── <módulo>_controller/ ← Orquesta modelo y vista; responde a eventos del usuario
```

> **Regla:** La lógica de negocio **nunca** va en la Vista. La Vista **nunca** accede
> directamente al Modelo. Toda comunicación pasa por el Controlador.

---

## 3. Arquitectura — MVC + Observer

El proyecto implementa el patrón **Modelo-Vista-Controlador** combinado con el
**patrón Observer** para desacoplar la capa de datos de la interfaz de usuario.

### 3.1 Patrón Observer

`AccountManager` es el `Subject` central. Los Controladores que necesitan reaccionar
a cambios en el estado de las cuentas se registran como `Observer` y son notificados
automáticamente cuando el modelo cambia.

**Regla crítica al implementar una vista nueva:** Si un Controlador se registra como
Observer, **debe** desregistrarse cuando su ventana se cierra para evitar memory leaks:

```java
// ✅ Obligatorio en todo Controller que implemente Observer
addWindowListener(new WindowAdapter() {
    @Override
    public void windowClosed(WindowEvent e) {
        AccountManager.removeObserver(MyController.this);
    }
});
```

### 3.2 Principios SOLID aplicados

| Principio | Cómo se aplica en este proyecto |
|---|---|
| **S** — Single Responsibility | Cada clase tiene una responsabilidad. `JsonDataHandler` solo serializa/deserializa. `AppConfig` solo provee rutas. |
| **O** — Open/Closed | Nuevas vistas se agregan sin modificar el `AccountManager`. |
| **L** — Liskov Substitution | Los Observers son intercambiables: cualquier clase que implemente la interfaz `Observer` puede registrarse. |
| **I** — Interface Segregation | Los módulos exponen solo las operaciones que necesitan. |
| **D** — Dependency Inversion | Los Controladores dependen de interfaces, no de implementaciones concretas. Los constructores admiten inyección de dependencias para facilitar los tests. |

---

## 4. Convenciones de Código

### 4.1 Idioma

```
Código (clases, métodos, variables, constantes)  →  Inglés
Comentarios Javadoc y comentarios inline         →  Inglés
Mensajes al usuario final (UI)                   →  Español
Commits de Git                                   →  Español
```

> Este proyecto usa **Javadoc** como estándar de documentación. Toda clase y
> método público debe tener su bloque `/** ... */` en inglés.

### 4.2 Nomenclatura Java

| Elemento | Convención | Ejemplo |
|---|---|---|
| Clase | `PascalCase` | `AccountManager`, `GoalsController` |
| Interfaz | `PascalCase` | `Observer`, `AccountSubject` |
| Método | `camelCase` | `loadAccounts()`, `ensureDataDirExists()` |
| Variable | `camelCase` | `initialBalance`, `targetAmount` |
| Constante | `UPPER_SNAKE_CASE` | `DATA_DIR`, `FILE_PATH` |
| Paquete | `snake_case` (minúsculas) | `account_model`, `goals_controller` |

### 4.3 Comentarios y Javadoc

Todo clase y método público **debe** tener Javadoc. El estándar mínimo:

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

**Nunca usar `System.out.println()` en el código.** Usar SLF4J con Logback:

```java
// ✅ Correcto
private static final Logger log = LoggerFactory.getLogger(MyClass.class);
log.info("Accounts saved successfully — {} account(s).", count);
log.error("Error reading accounts file: {}", e.getMessage(), e);

// ❌ Prohibido
System.out.println("Cuentas guardadas: " + count);
```

Niveles de log a usar:
- `log.info()` → Operaciones exitosas relevantes (guardado, carga, inicio)
- `log.warn()` → Situaciones inesperadas pero recuperables
- `log.error()` → Errores que impiden una operación; incluir siempre la excepción

### 4.5 Encapsulamiento

- Los métodos que devuelven listas internas deben retornar
  `Collections.unmodifiableList(...)` para proteger el estado interno.
- Nunca exponer colecciones internas de forma mutable desde el modelo.

---

## 5. Stack Tecnológico

| Tecnología | Versión | Rol |
|---|---|---|
| Java | 21 (LTS) | Lenguaje principal |
| JavaFX | 21.0.3 | Framework de UI (migración desde Swing) |
| Ikonli + Material Design 2 | 12.3.1 | Íconos vectoriales escalables para JavaFX |
| Maven | 3.9+ | Gestión de dependencias y build |
| SLF4J + Logback | 2.0.13 / 1.5.6 | Sistema de logging |
| org.json | 20250517 | Serialización/deserialización JSON |
| JUnit Jupiter | 5.11.4 | Tests unitarios |
| Mockito | 5.11.0 | Mocks en tests |
| JaCoCo | 0.8.12 | Cobertura de código |
| SonarCloud | — | Análisis estático de calidad |

### 5.1 Gestión de versiones de dependencias

Las versiones clave de JavaFX están centralizadas en una propiedad de `pom.xml`:

```xml
<properties>
    <javafx.version>21.0.3</javafx.version>
</properties>
```

Si necesitas actualizar JavaFX, cambia **solo esta propiedad** — no las versiones individuales.

---

## 6. Persistencia de Datos

Los datos de usuario **nunca** se guardan en la carpeta del proyecto ni de instalación.
Se guardan en `~/.gestor-finanzas/` (ver `AppConfig.java` y `docs/APP_FLOW.md §2`).

**Por lo tanto:**
- Los archivos `*.json` están en `.gitignore` y **nunca** deben subirse al repositorio.
- Al agregar un nuevo tipo de dato persistente, agregar su ruta en `AppConfig.java` siguiendo el patrón existente.
- Los métodos de carga siempre deben manejar el caso de archivo inexistente devolviendo
  una colección vacía — nunca lanzar una excepción no controlada al usuario.

---

## 7. Flujo de Trabajo Git

No hacer commits directamente a `main`. Todo cambio entra por una rama propia
y se integra a `main` mediante Pull Request.

```
main                    ← Solo código estable aprobado
  └── <tipo>/<descripcion>   ← Rama de desarrollo individual
```

### 7.1 Formato de nombre de rama

```
feature/<descripcion-corta>      ← Nueva funcionalidad
fix/<descripcion-corta>          ← Corrección de bug
migration/<modulo>               ← Migración de Swing a JavaFX
refactor/<descripcion-corta>     ← Refactorización sin cambio funcional
docs/<descripcion-corta>         ← Solo documentación

Ejemplos:
  feature/export-csv
  fix/report-amount-total
  migration/accounts-javafx
  refactor/json-handler-atomic-write
```

### 7.2 Formato de commit

Mensajes en **español**, modo imperativo, con prefijo del módulo afectado:

```
[módulo] descripción corta del cambio

Ejemplos:
  [accounts] Corregir bug de balance cero en ReportGenerator
  [javafx] Agregar AccountViewFX con FXML y hoja de estilos
  [config] Centralizar rutas de datos en AppConfig
  [tests] Agregar tests de round-trip para JsonDataHandler
  [deps] Agregar dependencias JavaFX e Ikonli en pom.xml
  [docs] Crear CONTRIBUTING y APP_FLOW
```

### 7.3 Proceso de Pull Request

1. Crear la rama desde `main` actualizado: `git checkout -b feature/mi-cambio`
2. Hacer commits con el formato de la sección 7.2
3. Verificar el checklist de la Sección 9 antes de abrir el PR
4. Abrir el PR hacia `main` con una descripción clara de qué cambia y por qué
5. Esperar que el pipeline de SonarCloud pase sin nuevos code smells críticos
6. Merge a `main` solo tras revisión

---

## 8. Tests

### 8.1 Ubicación

Los tests deben estar en `src/test/java/` en el paquete exactamente equivalente
al del código que prueban:

```
src/main/java/accounts/account_model/JsonDataHandler.java
         ↕ mismo paquete
src/test/java/accounts/account_model/JsonDataHandlerTest.java
```

### 8.2 Estándares de tests

- Usar **JUnit Jupiter** (`@Test`, `@BeforeEach`, `@ExtendWith`)
- Usar **Mockito** (`@Mock`, `@InjectMocks`, `when(...).thenReturn(...)`) para aislar dependencias
- Usar `@TempDir` de JUnit para tests que involucren escritura de archivos
- Usar `@ExtendWith(MockitoExtension.class)` en tests con mocks

```java
// ✅ Ejemplo de test bien estructurado
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

### 8.3 Ejecutar los tests

```bash
mvn test                    # Ejecutar todos los tests
mvn test -pl accounts       # Solo el módulo de cuentas
mvn verify                  # Tests + reporte de cobertura JaCoCo
```

El reporte de cobertura se genera en `target/site/jacoco/index.html`.

---

## 9. Checklist antes de hacer un Pull Request

Verificar cada punto antes de abrir un PR:

- [ ] ¿Los nombres de clases, métodos y variables siguen las convenciones de la Sección 4.2?
- [ ] ¿El código está en inglés y los comentarios/Javadoc también en inglés?
- [ ] ¿Toda clase y método público tiene Javadoc con `@param` y `@return` donde aplica?
- [ ] ¿No hay ningún `System.out.println()` en el código producción?
- [ ] ¿Los errores se manejan con `try/catch` y se loguean con SLF4J?
- [ ] ¿Los nuevos datos persistentes usan `AppConfig` para sus rutas y se registran en él?
- [ ] ¿El archivo está en la carpeta correcta según la Sección 2?
- [ ] ¿Si el Controller implementa Observer, se desregistra correctamente al cerrar la ventana?
- [ ] ¿Los nuevos tests están en `src/test/java/` en el paquete correcto?
- [ ] ¿El pipeline de SonarCloud no reporta nuevos issues críticos o bloqueadores?
- [ ] ¿El nombre del commit sigue el formato `[módulo] descripción` de la Sección 7.2?

---

## 10. Prácticas Prohibidas

- ❌ Hacer commit directamente a `main`.
- ❌ Subir archivos `*.json` de datos al repositorio (están en `.gitignore`).
- ❌ Usar `System.out.println()` en código que se integre a `main`.
- ❌ Escribir lógica de negocio en la Vista.
- ❌ Acceder al Modelo directamente desde la Vista (sin pasar por el Controlador).
- ❌ Exponer listas internas del Modelo de forma mutable.
- ❌ Agregar rutas de archivos hardcodeadas fuera de `AppConfig.java`.
- ❌ Agregar dependencias al `pom.xml` sin acordarlo y documentarlo.
- ❌ Mezclar Swing y JavaFX en la misma vista — cada módulo usa una u otra, nunca ambas.

---

*Mantenido por RoastWare*  
*Actualizar este archivo cuando cambie el stack, la arquitectura, las convenciones o el flujo de trabajo del proyecto.*
