package co.com.choucair.swaglabs.userinterfaces;

import net.serenitybdd.screenplay.targets.Target;

import static co.com.choucair.swaglabs.utils.LocalizadoresMoviles.idAccesibilidad;

public final class CheckoutInformacionPage {

    public static final Target CAMPO_NOMBRE =
            Target.the("campo First Name").located(idAccesibilidad("test-First Name"));
    public static final Target CAMPO_APELLIDO =
            Target.the("campo Last Name").located(idAccesibilidad("test-Last Name"));
    public static final Target CAMPO_CODIGO_POSTAL =
            Target.the("campo Zip/Postal Code").located(idAccesibilidad("test-Zip/Postal Code"));
    public static final Target BOTON_CONTINUAR =
            Target.the("botón CONTINUE").located(idAccesibilidad("test-CONTINUE"));

    private CheckoutInformacionPage() {
    }
}
