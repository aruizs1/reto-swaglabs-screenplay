# Reto SwagLabs – Automatización móvil con Screenplay

Automatización del **escenario feliz (happy path)** de compra de un producto en la app móvil
**Swag Labs** de Sauce Labs, usando el patrón **Screenplay** con Serenity BDD, Appium y Cucumber.

---

## 1. Stack tecnológico

| Herramienta | Uso |
|---|---|
| Java 17 | Lenguaje |
| Gradle 8 (wrapper incluido) | Gestión de dependencias y ejecución |
| Serenity BDD 4.2.1 | Screenplay + reportes vivos |
| Cucumber 7 (Gherkin en español) | Escenarios BDD |
| Appium 2 + UiAutomator2 | Driver móvil Android |
| JUnit Platform | Runner |

---

## 2. Escenario automatizado

```gherkin
# language: es
Antecedentes:
  Dado que "Andres" inicia sesión en Swag Labs con usuario "standard_user" y clave "secret_sauce"

@happyPath
Escenario: Compra exitosa de un producto
  Cuando agrega el producto "Sauce Labs Backpack" al carrito
  Y finaliza la compra con los datos de envío
    | nombre | apellido | codigoPostal |
    | Andres | Sabogal  | 110111       |
  Entonces debería ver el mensaje de confirmación "THANK YOU FOR YOU ORDER"
```

> El texto "THANK YOU FOR YOU ORDER" (sin la "R" en *YOUR*) es el que muestra la app tal cual.

---

## 3. Proceso realizado

1. **Análisis de la app**: se instaló el APK en un emulador y se inspeccionó con **Appium Inspector**.
   La app (React Native) expone sus elementos con *accessibility id* con prefijo `test-`
   (`test-Username`, `test-ADD TO CART`, `test-CHECKOUT`, `test-FINISH`, …), por lo que se priorizaron
   esos localizadores sobre XPath por ser más estables y rápidos.
2. **Diseño BDD**: se redactó el escenario en Gherkin en español, en lenguaje de negocio
   (qué hace el usuario, no cómo lo hace la UI).
3. **Implementación Screenplay**: cada paso de Gherkin se traduce en una *Task* de alto nivel que el
   *Actor* ejecuta; las tareas se componen de *Interactions*; la validación se hace con una *Question*.
4. **Scroll**: los botones CHECKOUT y FINISH quedan bajo el pliegue; en vez de gestos manuales
   se usa `UiScrollable…scrollIntoView(…)` como localizador, que hace scroll hasta encontrar el elemento.
5. **Esperas explícitas**: nada de `Thread.sleep`. Se usa `WaitUntil` en cada transición de pantalla.
6. **Avisos del sistema Android**: en Android 15+ el sistema muestra el aviso *"Android App Compatibility"*
   (la app no es compatible con páginas de memoria de 16 KB) la primera vez que se abre Swag Labs. Ese aviso tapa
   el login y no pertenece a la app, por lo que la Interaction `DescartarAvisosDelSistema` lo detecta y lo cierra
   automáticamente antes de iniciar sesión. Si el aviso no aparece, no hace nada.
7. **Manejo de excepciones**: excepciones propias con mensajes de negocio claros en el reporte.

### Arquitectura (Screenplay)

```
src/test/java/co/com/choucair/swaglabs
├── runners/            CompraProductoRunner        → Suite JUnit Platform + Cucumber
├── stepdefinitions/    CompraProductoStepDefinitions, Hooks (OnStage / OnlineCast)
├── tasks/              IniciarSesion, AgregarAlCarrito, FinalizarCompra   (QUÉ hace el actor)
├── interactions/       Tocar  (espera + click + excepción de negocio),
│                       DescartarAvisosDelSistema (cierra avisos de Android)  (CÓMO lo hace)
├── questions/          MensajeConfirmacion                                  (QUÉ ve el actor)
├── userinterfaces/     LoginPage, ProductosPage, CarritoPage,
│                       CheckoutInformacionPage, CheckoutResumenPage, CheckoutCompletoPage
├── models/             Usuario, DatosEnvio (builder)
├── exceptions/         CompraNoExitosaException, ElementoNoEncontradoException
└── utils/              LocalizadoresMoviles (fábrica de localizadores Appium)
src/test/resources
├── features/compra_producto.feature
├── serenity.conf       → capacidades Appium (Android por defecto, iOS opcional)
└── junit-platform.properties
```

### Cómo se cubren los temas de valor adicional

| Tema | Dónde se aplica |
|---|---|
| **Patrones de diseño** | Screenplay (Actor-Task-Interaction-Question), **Builder** en `DatosEnvio`, **Factory Method** en los métodos estáticos (`IniciarSesion.con(...)`, `LocalizadoresMoviles`), Target parametrizado (`{0}`) |
| **Clean Code** | Nombres expresivos en español de negocio, métodos cortos de una responsabilidad, constantes en lugar de números mágicos, sin código muerto ni comentarios redundantes |
| **SOLID** | **S**: cada clase hace una sola cosa (UI ≠ Task ≠ Question). **O**: nuevas acciones se agregan como nuevas Tasks sin modificar las existentes. **L**: todas las Tasks son intercambiables como `Performable`. **I**: `Task`, `Interaction`, `Question` son interfaces pequeñas. **D**: los steps dependen de abstracciones (Tasks/Questions), no del driver |
| **FIRST** | **Fast**: esperas explícitas, sin sleeps. **Independent**: cada escenario inicia la app limpia (`noReset=false`) y su propio login. **Repeatable**: datos fijos de la app de pruebas. **Self-validating**: termina en una aserción automática. **Timely**: escrito junto al análisis del flujo |
| **Patrón AAA** | **Arrange** = `Dado` (login), **Act** = `Cuando/Y` (agregar y comprar), **Assert** = `Entonces` (Question + `seeThat`) |
| **Manejo de excepciones** | `Tocar` envuelve fallos de WebDriver en `ElementoNoEncontradoException`; la verificación usa `.orComplainWith(CompraNoExitosaException.class, …)` para un mensaje de negocio claro |

---

## 4. Cómo ejecutar la prueba

### 4.1 Prerrequisitos

1. **JDK 17+** y variable `JAVA_HOME` configurada.
2. **Android Studio** con un emulador (AVD) creado, o un dispositivo físico con depuración USB.
   Variables `ANDROID_HOME` y `platform-tools` en el `PATH` (`adb devices` debe listar el equipo).
3. **Node.js 18+** y **Appium 2**:
   ```bash
   npm install -g appium
   appium driver install uiautomator2
   ```
4. **APK de Swag Labs**: descargar `Android.SauceLabs.Mobile.Sample.app.2.7.1.apk` desde
   <https://github.com/saucelabs/sample-app-mobile/releases> y copiarlo en la carpeta `apps/`.

### 4.2 Configuración

Revisar `src/test/resources/serenity.conf` y ajustar si es necesario:

- `deviceName`: nombre del dispositivo según `adb devices` (por defecto `emulator-5554`).
- `app`: ruta del APK (por defecto `apps/Android.SauceLabs.Mobile.Sample.app.2.7.1.apk`).

### 4.3 Ejecución

1. Iniciar el emulador.
2. Iniciar Appium en otra terminal:
   ```bash
   appium
   ```
3. Ejecutar las pruebas desde la raíz del proyecto:
   ```bash
   # Linux / macOS
   chmod +x gradlew ejecutar-prueba.command   # solo la primera vez
   ./gradlew clean test

   # Windows
   gradlew.bat clean test

   # Solo el happy path por tag
   ./gradlew clean test -Dcucumber.filter.tags="@happyPath"

   # Ejecutar en iOS (simulador, requiere macOS + Xcode)
   ./gradlew clean test -Denvironment=ios
   ```
   También puede ejecutarse desde el IDE (IntelliJ) con clic derecho sobre `CompraProductoRunner`.

### 4.4 Ejecución automática (macOS)

El script `ejecutar-prueba.command` hace todo el proceso: configura Java 17, enciende el primer
emulador disponible si no hay dispositivo conectado, descarga e instala el APK de Swag Labs,
levanta Appium si no está corriendo y ejecuta `./gradlew clean test`. Se ejecuta con doble clic
desde Finder o con `./ejecutar-prueba.command`. La salida queda en `ejecucion.log`, y la de
Appium en `appium.log` cuando el script es quien lo levanta.

> La app se instala con `adb install` antes de la prueba y Appium solo la abre con
> `appPackage`/`appActivity`. Como Appium 2/3 usa el protocolo W3C, las capacidades propias de
> Appium llevan el prefijo `appium:`, que Serenity agrega con `appium.process.desired.capabilities`
> y `appium.additional.capabilities` (ver `serenity.conf`).

### 4.5 Reporte

Al terminar, Serenity genera el reporte con capturas de cada paso en:

```
target/site/serenity/index.html
```

### 4.6 Evidencia de ejecución

Ejecución con `./gradlew clean test` (12-10-2026), en emulador Android `emulator-5554` con Appium 3.8.0,
partiendo de la app recién instalada y **sin ninguna intervención manual** (el aviso de compatibilidad
de Android apareció y la prueba lo cerró sola):

```
Escenario: Compra exitosa de un producto
  Dado que "Andres" inicia sesión en Swag Labs ...            ✔
  Cuando agrega el producto "Sauce Labs Backpack" al carrito   ✔
  Y finaliza la compra con los datos de envío                  ✔
  Entonces debería ver el mensaje de confirmación ...          ✔
BUILD SUCCESSFUL in 1m 33s   (26 de 26 pasos exitosos)
```

---

## 5. Otros escenarios que probaría

Para verificar el correcto funcionamiento del flujo de compra, además del happy path:

**Inicio de sesión**
1. Login con usuario bloqueado (`locked_out_user`) → mensaje "Sorry, this user has been locked out."
2. Login con credenciales inválidas → mensaje de error de usuario/clave.
3. Login con campos vacíos → "Username is required" / "Password is required".

**Catálogo y carrito**
4. Agregar varios productos y validar que el contador del carrito coincide.
5. Eliminar un producto desde el catálogo (REMOVE) y desde el carrito.
6. Validar que nombre, descripción y precio del producto en el carrito coinciden con el catálogo.
7. Agregar un producto desde la pantalla de detalle del producto.
8. Ordenar productos (A-Z, Z-A, precio ascendente/descendente) y validar el orden.
9. Persistencia del carrito al navegar entre pantallas / volver con "CONTINUE SHOPPING".
10. Intentar hacer checkout con el carrito vacío (comportamiento esperado vs. real).

**Checkout**
11. Formulario con cada campo obligatorio vacío → "First Name / Last Name / Postal Code is required".
12. Datos con caracteres especiales, espacios o longitudes extremas.
13. Validar en el resumen (Overview): subtotal = suma de precios, impuesto y total correctos.
14. Cancelar la compra en "Your Information" y en "Overview", y verificar que el carrito se conserva.
15. Tras finalizar la compra, el carrito queda vacío y "BACK HOME" regresa al catálogo.

**Usuarios especiales y no funcionales**
16. Repetir el flujo con `problem_user` (imágenes/datos erróneos) y `performance_glitch_user` (tiempos de respuesta).
17. Rotación de pantalla, envío de la app a segundo plano e interrupciones (llamada/notificación) en medio del checkout.
18. Pruebas en distintas versiones de Android/iOS y tamaños de pantalla.
19. Pérdida de conexión durante la compra (si la app dependiera de servicios).

Estos escenarios se implementarían reutilizando las mismas Tasks y Questions
(por ejemplo `IniciarSesion.con(usuarioBloqueado)` + una Question `MensajeDeError`),
lo que demuestra el beneficio del patrón Screenplay.
