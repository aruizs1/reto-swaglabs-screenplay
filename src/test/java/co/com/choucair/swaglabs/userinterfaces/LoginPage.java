package co.com.choucair.swaglabs.userinterfaces;

import net.serenitybdd.screenplay.targets.Target;

import static co.com.choucair.swaglabs.utils.LocalizadoresMoviles.idAccesibilidad;

public final class LoginPage {

    public static final Target CAMPO_USUARIO =
            Target.the("campo usuario").located(idAccesibilidad("test-Username"));
    public static final Target CAMPO_CLAVE =
            Target.the("campo contraseña").located(idAccesibilidad("test-Password"));
    public static final Target BOTON_LOGIN =
            Target.the("botón LOGIN").located(idAccesibilidad("test-LOGIN"));

    private LoginPage() {
    }
}
