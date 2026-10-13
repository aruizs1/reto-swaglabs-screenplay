package co.com.choucair.swaglabs.interactions;

import co.com.choucair.swaglabs.exceptions.ElementoNoEncontradoException;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.targets.Target;
import net.serenitybdd.screenplay.waits.WaitUntil;
import org.openqa.selenium.WebDriverException;

import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

/**
 * Espera a que el elemento sea visible y lo toca.
 * Si no aparece, lanza una excepción de negocio con un mensaje claro en el reporte.
 */
public class Tocar implements Interaction {

    private static final int SEGUNDOS_ESPERA = 15;
    private final Target elemento;

    public Tocar(Target elemento) {
        this.elemento = elemento;
    }

    public static Tocar el(Target elemento) {
        return instrumented(Tocar.class, elemento);
    }

    @Override
    @Step("{0} toca #elemento")
    public <T extends Actor> void performAs(T actor) {
        try {
            actor.attemptsTo(
                    WaitUntil.the(elemento, isVisible()).forNoMoreThan(SEGUNDOS_ESPERA).seconds(),
                    Click.on(elemento)
            );
        } catch (WebDriverException error) {
            throw new ElementoNoEncontradoException(
                    String.format("No fue posible tocar %s después de %d segundos", elemento, SEGUNDOS_ESPERA),
                    error);
        }
    }
}
