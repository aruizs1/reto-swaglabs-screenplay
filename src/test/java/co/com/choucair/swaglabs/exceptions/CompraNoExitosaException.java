package co.com.choucair.swaglabs.exceptions;

public class CompraNoExitosaException extends AssertionError {

    public static final String MENSAJE_NO_VISIBLE =
            "La compra no finalizó correctamente: el mensaje de confirmación no es el esperado";

    public CompraNoExitosaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
