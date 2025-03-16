package Farmacia;

public class Clientes {

    //Atributos

    int id_cliente;
    String cedula, nombre, telefoono, email, direccion;


    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefoono() {
        return telefoono;
    }

    public void setTelefoono(String telefoono) {
        this.telefoono = telefoono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Clientes(int id_cliente, String cedula, String nombre, String telefoono, String email, String direccion) {
        this.id_cliente = id_cliente;
        this.cedula = cedula;
        this.nombre = nombre;
        this.telefoono = telefoono;
        this.email = email;
        this.direccion = direccion;


    }
}
