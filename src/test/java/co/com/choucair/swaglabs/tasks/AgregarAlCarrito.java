package co.com.choucair.swaglabs.tasks;

import co.com.choucair.swaglabs.interactions.Tocar;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.waits.WaitUntil;

import static co.com.choucair.swaglabs.userinterfaces.CarritoPage.PRODUCTO_EN_CARRITO;
import static co.com.choucair.swaglabs.userinterfaces.ProductosPage.BOTON_AGREGAR_PRODUCTO;
import static co.com.choucair.swaglabs.userinterfaces.ProductosPage.ICONO_CARRITO;
import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

public class AgregarAlCarrito implements Task {

    private final String producto;

    public AgregarAlCarrito(String producto) {
        this.producto = producto;
    }

    public static AgregarAlCarrito elProducto(String producto) {
        return instrumented(AgregarAlCarrito.class, producto);
    }

    @Override
    @Step("{0} agrega '#producto' al carrito")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Tocar.el(BOTON_AGREGAR_PRODUCTO.of(producto)),
                Tocar.el(ICONO_CARRITO),
                WaitUntil.the(PRODUCTO_EN_CARRITO.of(producto), isVisible()).forNoMoreThan(10).seconds()
        );
    }
}
