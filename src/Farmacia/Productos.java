package Farmacia;

import java.sql.Date;

public class Productos {

    int idproductos,precio,	stock,stock_minimo;
    String nombre,descripcion,categoria;
    Date fechaV;

    /**
     * Constructor para crear un nuevo producto con todos sus atributos.
     *
     * @param idproductos Identificador único del producto.
     * @param precio Precio del producto.
     * @param stock Cantidad disponible en el inventario.
     * @param stock_minimo Cantidad mínima requerida en el inventario.
     * @param nombre Nombre del producto.
     * @param descripcion Breve descripción del producto.
     * @param categoria Categoría a la que pertenece el producto.
     * @param fechaV Fecha de vencimiento del producto.
     */

    public Productos(int idproductos, int precio, int stock, int stock_minimo,String nombre, String descripcion, String categoria, Date fechaV) {
        this.idproductos = idproductos;
        this.precio = precio;
        this.stock = stock;
        this.stock_minimo = stock_minimo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.fechaV = fechaV;
    }

    /**
     * Obtiene el id del producto
     * @return Id del producto
     */
    public int getIdproductos() {
        return idproductos;
    }

    /**
     * establece el nuevo id del producto
     * @param idproductos nuevo id del producto
     */

    public void setIdproductos(int idproductos) {
        this.idproductos = idproductos;
    }

    /**
     * obtiene el precio del producto
     * @return Precio del producto
     */
    public int getPrecio() {
        return precio;
    }

    /**
     * establece el nuevo precio del producto
     * @param precio Nuevo precio del producto
     */
    public void setPrecio(int precio) {
        this.precio = precio;
    }

    /**
     * obtiene el stock del producto
     * @return Stock de producto
     */
    public int getStock() {
        return stock;
    }

    /***
     * establece el nuevo stock
     * @param stock Stock nuevo
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * obtiene el stock minimo
     * @return Stock minimo
     */
    public int getStock_minimo() {
        return stock_minimo;
    }

    /**
     * establece el nuevo stock minimo
     * @param stock_minimo StocK            minimo
     */
    public void setStock_minimo(int stock_minimo) {
        this.stock_minimo = stock_minimo;
    }

    /**
     * obtiene el nombre
     * @return Nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * establece el nuevo nommbre
     * @param nombre Nuevo nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * obtiene la descripcion del producto
     * @return Descripcion del producto
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * establece una nueva descripcion del producto
     * @param descripcion Nueva descripcion del producto
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * obtiene la categoria del producto
     * @return categoria del producto
     */
    public String getCategoria() {
        return categoria;
    }

    /**
     * establece la categoria nueva del producto
     * @param categoria nueva categoria del producto
     */
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    /**
     * obtiene la fecha de vencimiento del producto
     * @return fecha de vencimiento del producto
     */
    public Date getFechaV() {
        return fechaV;
    }

    /**
     * establece nueva fecha de venciminto del producto
     * @param fechaV Nueva fecha de vencimiento del producto
     */
    public void setFechaV(Date fechaV) {
        this.fechaV = fechaV;
    }
}
