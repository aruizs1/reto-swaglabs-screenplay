package co.com.choucair.swaglabs.userinterfaces;

import net.serenitybdd.screenplay.targets.Target;

import static co.com.choucair.swaglabs.utils.LocalizadoresMoviles.conScrollHasta;

public final class CarritoPage {

    public static final Target PRODUCTO_EN_CARRITO = Target.the("producto {0} en el carrito")
            .locatedBy("//*[@content-desc='test-Item']//*[@text='{0}']");

    public static final Target BOTON_CHECKOUT =
            Target.the("botón CHECKOUT").located(conScrollHasta("test-CHECKOUT"));

    private CarritoPage() {
    }
}
