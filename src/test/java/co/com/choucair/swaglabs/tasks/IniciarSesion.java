package co.com.choucair.swaglabs.tasks;

import co.com.choucair.swaglabs.interactions.DescartarAvisosDelSistema;
import co.com.choucair.swaglabs.interactions.Tocar;
import co.com.choucair.swaglabs.models.Usuario;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.waits.WaitUntil;

import static co.com.choucair.swaglabs.userinterfaces.LoginPage.BOTON_LOGIN;
import static co.com.choucair.swaglabs.userinterfaces.LoginPage.CAMPO_CLAVE;
import static co.com.choucair.swaglabs.userinterfaces.LoginPage.CAMPO_USUARIO;
import static co.com.choucair.swaglabs.userinterfaces.ProductosPage.LISTA_PRODUCTOS;
import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

public class IniciarSesion implements Task {

    private final Usuario usuario;

    public IniciarSesion(Usuario usuario) {
        this.usuario = usuario;
    }

    public static IniciarSesion con(Usuario usuario) {
        return instrumented(IniciarSesion.class, usuario);
    }

    @Override
    @Step("{0} inicia sesión en Swag Labs")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                DescartarAvisosDelSistema.siAparecen(),
                WaitUntil.the(CAMPO_USUARIO, isVisible()).forNoMoreThan(30).seconds(),
                Enter.theValue(usuario.getNombreUsuario()).into(CAMPO_USUARIO),
                Enter.theValue(usuario.getClave()).into(CAMPO_CLAVE),
                Tocar.el(BOTON_LOGIN),
                WaitUntil.the(LISTA_PRODUCTOS, isVisible()).forNoMoreThan(15).seconds()
        );
    }
}
