package co.com.choucair.swaglabs.models;

/**
 * Datos del formulario "Checkout: Your Information".
 * Se construye con un builder fluido: DatosEnvio.conNombre("Ana").apellido("Ruiz").codigoPostal("110111")
 */
public class DatosEnvio {

    private final String nombre;
    private String apellido;
    private String codigoPostal;

    private DatosEnvio(String nombre) {
        this.nombre = nombre;
    }

    public static DatosEnvio conNombre(String nombre) {
        return new DatosEnvio(nombre);
    }

    public DatosEnvio apellido(String apellido) {
        this.apellido = apellido;
        return this;
    }

    public DatosEnvio codigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
        return this;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }
}
