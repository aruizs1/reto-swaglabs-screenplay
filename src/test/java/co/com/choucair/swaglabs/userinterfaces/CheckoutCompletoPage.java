package co.com.choucair.swaglabs.userinterfaces;

import net.serenitybdd.screenplay.targets.Target;

public final class CheckoutCompletoPage {

    public static final Target MENSAJE_CONFIRMACION = Target.the("mensaje de confirmación de la orden")
            .locatedBy("//*[@content-desc='test-CHECKOUT: COMPLETE!']"
                    + "//android.widget.TextView[contains(@text,'THANK YOU')]");

    private CheckoutCompletoPage() {
    }
}
