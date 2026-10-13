package co.com.choucair.swaglabs.tasks;

import co.com.choucair.swaglabs.interactions.Tocar;
import co.com.choucair.swaglabs.models.DatosEnvio;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Enter;

import static co.com.choucair.swaglabs.userinterfaces.CarritoPage.BOTON_CHECKOUT;
import static co.com.choucair.swaglabs.userinterfaces.CheckoutInformacionPage.BOTON_CONTINUAR;
import static co.com.choucair.swaglabs.userinterfaces.CheckoutInformacionPage.CAMPO_APELLIDO;
import static co.com.choucair.swaglabs.userinterfaces.CheckoutInformacionPage.CAMPO_CODIGO_POSTAL;
import static co.com.choucair.swaglabs.userinterfaces.CheckoutInformacionPage.CAMPO_NOMBRE;
import static co.com.choucair.swaglabs.userinterfaces.CheckoutResumenPage.BOTON_FINALIZAR;
import static net.serenitybdd.screenplay.Tasks.instrumented;

public class FinalizarCompra implements Task {

    private final DatosEnvio datos;

    public FinalizarCompra(DatosEnvio datos) {
        this.datos = datos;
    }

    public static FinalizarCompra con(DatosEnvio datos) {
        return instrumented(FinalizarCompra.class, datos);
    }

    @Override
    @Step("{0} diligencia los datos de envío y finaliza la compra")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Tocar.el(BOTON_CHECKOUT),
                Enter.theValue(datos.getNombre()).into(CAMPO_NOMBRE),
                Enter.theValue(datos.getApellido()).into(CAMPO_APELLIDO),
                Enter.theValue(datos.getCodigoPostal()).into(CAMPO_CODIGO_POSTAL),
                Tocar.el(BOTON_CONTINUAR),
                Tocar.el(BOTON_FINALIZAR)
        );
    }
}
