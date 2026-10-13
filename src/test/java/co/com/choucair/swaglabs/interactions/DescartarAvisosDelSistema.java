package co.com.choucair.swaglabs.interactions;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.thucydides.core.webdriver.WebDriverFacade;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import static io.appium.java_client.AppiumBy.androidUIAutomator;
import static net.serenitybdd.screenplay.Tasks.instrumented;

/**
 * En Android 15+ el sistema muestra el aviso "Android App Compatibility" (la app no es compatible
 * con páginas de memoria de 16 KB) la primera vez que se abre Swag Labs. El aviso tapa la pantalla
 * de login y no pertenece a la app, así que la prueba lo cierra si aparece.
 * Espera hasta que la app quede visible o se agote el tiempo; si no hay avisos, no hace nada.
 */
public class DescartarAvisosDelSistema implements Interaction {

    private static final By BOTON_ACEPTAR_SISTEMA = androidUIAutomator(
            "new UiSelector().packageNameMatches(\"^(android|com\\.(google\\.)?android\\..*)$\")"
                    + ".textMatches(\"(?i)^(ok|aceptar)$\")");
    private static final By APP_VISIBLE = androidUIAutomator(
            "new UiSelector().packageName(\"com.swaglabsmobileapp\").descriptionStartsWith(\"test-\")");

    private static final Duration TIEMPO_MAXIMO = Duration.ofSeconds(20);
    private static final Duration INTERVALO = Duration.ofMillis(500);
    private static final Duration ESPERA_IMPLICITA_POR_DEFECTO = Duration.ofSeconds(5);

    public static DescartarAvisosDelSistema siAparecen() {
        return instrumented(DescartarAvisosDelSistema.class);
    }

    @Override
    @Step("{0} cierra los avisos del sistema Android si aparecen")
    public <T extends Actor> void performAs(T actor) {
        WebDriver driver = driverSinEsperasDeSerenity(BrowseTheWeb.as(actor).getDriver());
        driver.manage().timeouts().implicitlyWait(Duration.ZERO);
        try {
            Instant limite = Instant.now().plus(TIEMPO_MAXIMO);
            while (Instant.now().isBefore(limite)) {
                List<WebElement> botones = driver.findElements(BOTON_ACEPTAR_SISTEMA);
                if (!botones.isEmpty()) {
                    aceptar(botones.get(0));
                } else if (!driver.findElements(APP_VISIBLE).isEmpty()) {
                    return;
                }
                esperar();
            }
        } finally {
            driver.manage().timeouts().implicitlyWait(ESPERA_IMPLICITA_POR_DEFECTO);
        }
    }

    /**
     * Serenity vuelve a aplicar su espera implícita (5 s) antes de cada búsqueda; para sondear
     * rápido se usa el driver de Appium que Serenity envuelve.
     */
    private WebDriver driverSinEsperasDeSerenity(WebDriver driver) {
        return driver instanceof WebDriverFacade facade ? facade.getProxiedDriver() : driver;
    }

    private void aceptar(WebElement boton) {
        try {
            boton.click();
        } catch (StaleElementReferenceException avisoYaCerrado) {
            // El aviso se cerró entre la búsqueda y el clic: no es un error.
        }
    }

    private void esperar() {
        try {
            Thread.sleep(INTERVALO.toMillis());
        } catch (InterruptedException interrupcion) {
            Thread.currentThread().interrupt();
        }
    }
}
