package Farmacia;

import java.sql.Timestamp;


public class Movimiento {

    int idMovimientos;
    String tipo;
    int idPedido;
    String categoria;
    Timestamp fecha;
    int monto;
    String descripcion;

    public Movimiento(int idMovimientos, String tipo, int idPedido, String categoria, Timestamp fecha, int monto, String descripcion) {
        this.idMovimientos = idMovimientos;
        this.tipo = tipo;
        this.idPedido = idPedido;
        this.categoria = categoria;
        this.fecha = fecha;
        this.monto = monto;
        this.descripcion = descripcion;
    }

    public int getIdMovimientos() { return idMovimientos; }
    public void setIdMovimientos(int idMovimientos) { this.idMovimientos = idMovimientos; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public int getIdPedido() { return idPedido; }
    public void setIdPedido(int idPedido) { this.idPedido = idPedido; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public Timestamp getFecha() { return fecha; }
    public void setFecha(Timestamp fecha) { this.fecha = fecha; }

    public int getMonto() { return monto; }
    public void setMonto(int monto) { this.monto = monto; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}
