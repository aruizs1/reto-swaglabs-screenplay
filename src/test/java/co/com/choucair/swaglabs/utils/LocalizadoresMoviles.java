package co.com.choucair.swaglabs.utils;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

/**
 * Fábrica de localizadores reutilizables para la app Swag Labs (React Native).
 * La app expone sus elementos con content-desc / accessibility id con prefijo "test-".
 */
public final class LocalizadoresMoviles {

    private static final String SCROLL_HASTA_DESCRIPCION =
            "new UiScrollable(new UiSelector().scrollable(true).instance(0))"
                    + ".scrollIntoView(new UiSelector().description(\"%s\"))";

    private LocalizadoresMoviles() {
    }

    public static By idAccesibilidad(String id) {
        return AppiumBy.accessibilityId(id);
    }

    /** Localiza el elemento haciendo scroll automático hasta él (Android / UiAutomator2). */
    public static By conScrollHasta(String descripcion) {
        return AppiumBy.androidUIAutomator(String.format(SCROLL_HASTA_DESCRIPCION, descripcion));
    }
}
