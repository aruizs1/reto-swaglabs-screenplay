package co.com.choucair.swaglabs.models;

public class Usuario {

    private final String nombreUsuario;
    private final String clave;

    public Usuario(String nombreUsuario, String clave) {
        this.nombreUsuario = nombreUsuario;
        this.clave = clave;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getClave() {
        return clave;
    }
}
