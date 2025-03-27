package Farmacia.M;

import java.sql.Timestamp;

public class Pedido {

        int idPedidos, idclientes, total;
        String estado;
        Timestamp fecha;

        public Pedido(int idPedidos, int idclientes, int total, String estado, Timestamp fecha) {
            this.idPedidos = idPedidos;
            this.idclientes = idclientes;
            this.total = total;
            this.estado = estado;
            this.fecha = fecha;
        }

        public int getIdPedidos() {
            return idPedidos;
        }

        public void setIdPedidos(int idPedidos) {
            this.idPedidos = idPedidos;
        }

        public int getIdclientes() {
            return idclientes;
        }

        public void setIdclientes(int idclientes) {
            this.idclientes = idclientes;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public String getEstado() {
            return estado;
        }

        public void setEstado(String estado) {
            this.estado = estado;
        }

        public Timestamp getFecha() {
            return fecha;
        }

        public void setFecha(Timestamp fecha) {
            this.fecha = fecha;
        }

}

