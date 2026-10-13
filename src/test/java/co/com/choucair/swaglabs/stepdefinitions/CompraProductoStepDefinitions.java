package co.com.choucair.swaglabs.stepdefinitions;

import co.com.choucair.swaglabs.exceptions.CompraNoExitosaException;
import co.com.choucair.swaglabs.models.DatosEnvio;
import co.com.choucair.swaglabs.models.Usuario;
import co.com.choucair.swaglabs.questions.MensajeConfirmacion;
import co.com.choucair.swaglabs.tasks.AgregarAlCarrito;
import co.com.choucair.swaglabs.tasks.FinalizarCompra;
import co.com.choucair.swaglabs.tasks.IniciarSesion;
import io.cucumber.java.DataTableType;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Y;

import java.util.List;
import java.util.Map;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorCalled;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.Matchers.equalToIgnoringCase;

public class CompraProductoStepDefinitions {

    @DataTableType
    public DatosEnvio datosEnvio(Map<String, String> fila) {
        return DatosEnvio.conNombre(fila.get("nombre"))
                .apellido(fila.get("apellido"))
                .codigoPostal(fila.get("codigoPostal"));
    }

    @Dado("que {string} inicia sesión en Swag Labs con usuario {string} y clave {string}")
    public void iniciaSesion(String actor, String usuario, String clave) {
        theActorCalled(actor).wasAbleTo(
                IniciarSesion.con(new Usuario(usuario, clave))
        );
    }

    @Cuando("agrega el producto {string} al carrito")
    public void agregaElProducto(String producto) {
        theActorInTheSpotlight().attemptsTo(
                AgregarAlCarrito.elProducto(producto)
        );
    }

    @Y("finaliza la compra con los datos de envío")
    public void finalizaLaCompra(List<DatosEnvio> datos) {
        theActorInTheSpotlight().attemptsTo(
                FinalizarCompra.con(datos.get(0))
        );
    }

    @Entonces("debería ver el mensaje de confirmación {string}")
    public void deberiaVerElMensaje(String mensajeEsperado) {
        theActorInTheSpotlight().should(
                seeThat(MensajeConfirmacion.deCompra(), equalToIgnoringCase(mensajeEsperado))
                        .orComplainWith(CompraNoExitosaException.class,
                                CompraNoExitosaException.MENSAJE_NO_VISIBLE)
        );
    }
}
