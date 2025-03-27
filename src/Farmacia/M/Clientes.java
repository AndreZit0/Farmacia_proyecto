/**
 * Clase que representa un cliente en el sistema de farmacia.
 */
package Farmacia.M;

public class Clientes {

    // Atributos de la clase
    private int id_cliente;
    private String cedula;
    private String nombre;
    private String telefoono;
    private String email;
    private String direccion;

    /**
     * Constructor de la clase Clientes.
     *
     * @param id_cliente Identificador único del cliente.
     * @param cedula Número de cédula del cliente.
     * @param nombre Nombre completo del cliente.
     * @param telefoono Número de teléfono del cliente.
     * @param email Dirección de correo electrónico del cliente.
     * @param direccion Dirección física del cliente.
     */
    public Clientes(int id_cliente, String cedula, String nombre, String telefoono, String email, String direccion) {
        this.id_cliente = id_cliente;
        this.cedula = cedula;
        this.nombre = nombre;
        this.telefoono = telefoono;
        this.email = email;
        this.direccion = direccion;
    }

    /**
     * Obtiene el ID del cliente.
     *
     * @return El identificador único del cliente.
     */
    public int getId_cliente() {
        return id_cliente;
    }

    /**
     * Establece el ID del cliente.
     *
     * @param id_cliente Identificador único del cliente.
     */
    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    /**
     * Obtiene la cédula del cliente.
     *
     * @return La cédula del cliente.
     */
    public String getCedula() {
        return cedula;
    }

    /**
     * Establece la cédula del cliente.
     *
     * @param cedula Número de cédula del cliente.
     */
    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    /**
     * Obtiene el nombre del cliente.
     *
     * @return El nombre del cliente.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del cliente.
     *
     * @param nombre Nombre completo del cliente.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el teléfono del cliente.
     *
     * @return El número de teléfono del cliente.
     */
    public String getTelefoono() {
        return telefoono;
    }

    /**
     * Establece el teléfono del cliente.
     *
     * @param telefoono Número de teléfono del cliente.
     */
    public void setTelefoono(String telefoono) {
        this.telefoono = telefoono;
    }

    /**
     * Obtiene el correo electrónico del cliente.
     *
     * @return El correo electrónico del cliente.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el correo electrónico del cliente.
     *
     * @param email Dirección de correo electrónico del cliente.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtiene la dirección del cliente.
     *
     * @return La dirección del cliente.
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * Establece la dirección del cliente.
     *
     * @param direccion Dirección física del cliente.
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}

