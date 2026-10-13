package co.com.choucair.swaglabs.userinterfaces;

import net.serenitybdd.screenplay.targets.Target;

import static co.com.choucair.swaglabs.utils.LocalizadoresMoviles.idAccesibilidad;

public final class ProductosPage {

    public static final Target LISTA_PRODUCTOS =
            Target.the("listado de productos").located(idAccesibilidad("test-PRODUCTS"));

    /** Botón ADD TO CART dentro de la tarjeta del producto cuyo nombre se recibe como parámetro. */
    public static final Target BOTON_AGREGAR_PRODUCTO = Target.the("botón ADD TO CART de {0}")
            .locatedBy("//*[@content-desc='test-Item' and .//*[@text='{0}']]"
                    + "//*[@content-desc='test-ADD TO CART']");

    public static final Target ICONO_CARRITO =
            Target.the("ícono del carrito").located(idAccesibilidad("test-Cart"));

    private ProductosPage() {
    }
}
