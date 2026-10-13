package co.com.choucair.swaglabs.exceptions;

public class ElementoNoEncontradoException extends RuntimeException {

    public ElementoNoEncontradoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
