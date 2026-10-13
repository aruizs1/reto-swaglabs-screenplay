package co.com.choucair.swaglabs.userinterfaces;

import net.serenitybdd.screenplay.targets.Target;

import static co.com.choucair.swaglabs.utils.LocalizadoresMoviles.conScrollHasta;

public final class CheckoutResumenPage {

    /** El botón FINISH queda bajo el pliegue: el localizador hace scroll hasta encontrarlo. */
    public static final Target BOTON_FINALIZAR =
            Target.the("botón FINISH").located(conScrollHasta("test-FINISH"));

    private CheckoutResumenPage() {
    }
}
