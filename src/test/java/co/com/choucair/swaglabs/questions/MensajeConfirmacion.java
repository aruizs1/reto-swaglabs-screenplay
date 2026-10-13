package co.com.choucair.swaglabs.questions;

import net.serenitybdd.screenplay.annotations.Subject;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.questions.Text;
import net.serenitybdd.screenplay.waits.WaitUntil;

import static co.com.choucair.swaglabs.userinterfaces.CheckoutCompletoPage.MENSAJE_CONFIRMACION;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

@Subject("el mensaje de confirmación de la compra")
public class MensajeConfirmacion implements Question<String> {

    public static MensajeConfirmacion deCompra() {
        return new MensajeConfirmacion();
    }

    @Override
    public String answeredBy(Actor actor) {
        actor.attemptsTo(WaitUntil.the(MENSAJE_CONFIRMACION, isVisible()).forNoMoreThan(15).seconds());
        return Text.of(MENSAJE_CONFIRMACION).answeredBy(actor).trim();
    }
}
