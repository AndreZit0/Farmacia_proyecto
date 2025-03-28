package Farmacia.C;

import Conexion.ConexionBD;
import Farmacia.M.Pedido;
import Farmacia.V.PedidoGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

/**
 * Esta clase se encarga de manejar la información de los pedidos en la base de datos.
 * Aquí podemos guardar, buscar, actualizar y borrar pedidos.
 */
public class PedidoDAO {
    PedidoGUI pedidoGUI;
    ConexionBD conexionBD = new ConexionBD();


    /**
     * Guarda un nuevo pedido en la base de datos.
     *
     * @param pedido El objeto con la información del pedido que queremos guardar.
     * Debe tener los datos del cliente, la fecha, el estado y el total.
     */
    public static void agregar(Pedido pedido) {
        Connection con = ConexionBD.getConnection();

        String query = "INSERT INTO pedidos (idclientes,fecha,estado,total) VALUES (?,?,?,?)";

        try {

            PreparedStatement pst = con.prepareStatement(query);

            pst.setInt(1, pedido.getIdclientes());
            pst.setTimestamp(2, pedido.getFecha());
            pst.setString(3, pedido.getEstado());
            pst.setInt(4, pedido.getTotal());

            int resultado = pst.executeUpdate();
            if (resultado > 0) {
                JOptionPane.showMessageDialog(null, "Pedido guardado exitosamente.");
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo guardar el pedido.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Hubo un error al guardar el pedido.");
        }
    }

    /**
     * Actualiza la información de un pedido que ya existe.
     *
     * @param pedido El objeto con la información actualizada del pedido.
     * Es importante que el ID del pedido en este objeto sea correcto.
     */
    public void actualizar(Pedido pedido) {
        Connection con = conexionBD.getConnection();
        String query = "UPDATE pedidos SET idclientes = ?,fecha = ?,estado = ? WHERE idPedidos = ?";

        try {
            PreparedStatement pst = con.prepareStatement(query);

            pst.setInt(1, pedido.getIdclientes());
            pst.setTimestamp(2, pedido.getFecha());
            pst.setString(3, pedido.getEstado());
            pst.setInt(4, pedido.getIdPedidos());

            int resultado = pst.executeUpdate();
            if (resultado > 0) {
                JOptionPane.showMessageDialog(null, "Pedido actualizado.");
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo actualizar el pedido.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al actualizar el pedido.");
        }
    }


    /**
     * Busca un pedido por su ID y devuelve el ID encontrado.
     *
     * @param idPedido El ID del pedido que estamos buscando.
     * @return El ID del pedido si se encuentra, o -1 si no existe.
     */
    public int obtenerIdPedido(int idPedido) {
        int id = -1; // Valor por defecto si no lo encontramos
        String sql = "SELECT idPedidos FROM pedidos WHERE idPedidos = ?";

        try (Connection conn = conexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idPedido);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                id = rs.getInt("idPedidos");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }


    /**
     * Elimina un pedido de la base de datos usando su ID.
     *
     * @param id El ID del pedido que queremos eliminar. ¡Cuidado con esto!
     */
    public void eliminar(int id) {
        Connection con = conexionBD.getConnection();

        String query = "DELETE FROM pedidos WHERE idPedidos = ?";

        try {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, id);

            int resultado = pst.executeUpdate();

            if (resultado > 0) {
                JOptionPane.showMessageDialog(null, "Pedido eliminado.");
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo eliminar el pedido.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al eliminar el pedido.");
        }
    }

    /**
     * Cuando un pedido se marca como enviado, esta función resta la cantidad pedida del stock de cada producto.
     * También revisa si el stock de algún producto está llegando al mínimo y muestra una alerta.
     *
     * @param idPedido El ID del pedido que se acaba de enviar.
     */
    public void descontarStock(int idPedido) {
        Connection con = conexionBD.getConnection();

        String estadoQuery = "SELECT estado FROM pedidos WHERE idpedidos = ?";
        String detalleQuery = "SELECT idproductos, cantidad, medida FROM detalle_pedido WHERE idpedidos = ?";
        String updateStockQuery = "UPDATE productos SET stock = stock - ? WHERE idproductos = ?";
        String stockQuery = "SELECT nombre, stock, stock_minimo FROM productos WHERE idproductos = ?";

        try {
            PreparedStatement estadoStmt = con.prepareStatement(estadoQuery);
            estadoStmt.setInt(1, idPedido);
            ResultSet rsEstado = estadoStmt.executeQuery();

            if (rsEstado.next() && "Enviado".equalsIgnoreCase(rsEstado.getString("estado"))) {
                PreparedStatement detalleStmt = con.prepareStatement(detalleQuery);
                detalleStmt.setInt(1, idPedido);
                ResultSet rsDetalle = detalleStmt.executeQuery();

                while (rsDetalle.next()) {
                    int idProducto = rsDetalle.getInt("idproductos");
                    int cantidad = rsDetalle.getInt("cantidad");
                    String medida = rsDetalle.getString("medida");

                    int cantidadReal = switch (medida.toLowerCase()) {
                        case "unidad" -> cantidad;
                        case "blister" -> cantidad * 10;
                        case "caja" -> cantidad * 100;
                        default -> 0;
                    };

                    // Verificamos el stock antes de quitar
                    PreparedStatement stockStmt = con.prepareStatement(stockQuery);
                    stockStmt.setInt(1, idProducto);
                    ResultSet rsStock = stockStmt.executeQuery();

                    if (rsStock.next()) {
                        String nombreProducto = rsStock.getString("nombre");
                        int stockActual = rsStock.getInt("stock");
                        int stockMinimo = rsStock.getInt("stock_minimo");

                        // Si después de descontar, el stock está en el mínimo o menos, avisamos
                        if ((stockActual - cantidadReal) <= stockMinimo) {
                            JOptionPane.showMessageDialog(null, "¡Ojo! El producto " + nombreProducto + " está casi agotado.");
                        }

                        // Ahora sí, actualizamos el stock
                        PreparedStatement updateStockStmt = con.prepareStatement(updateStockQuery);
                        updateStockStmt.setInt(1, cantidadReal);
                        updateStockStmt.setInt(2, idProducto);
                        updateStockStmt.executeUpdate();
                        updateStockStmt.close();
                    }

                    rsStock.close();
                    stockStmt.close();
                }

                rsDetalle.close();
                detalleStmt.close();

                JOptionPane.showMessageDialog(null, "Stock descontado.");
            }

            rsEstado.close();
            estadoStmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error con la base de datos al descontar stock.");
        }
    }

    /**
     * Obtiene todos los pedidos de la base de datos y los muestra en la tabla de la interfaz.
     * Muestra el ID del pedido, el nombre del cliente, la fecha, el estado y el total.
     */
    public void obtenerDatosPed() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Nombre de Cliente");
        model.addColumn("Fecha");
        model.addColumn("Estado");
        model.addColumn("Total");

        pedidoGUI.Table1.setModel(model);
        String[] dato = new String[5];
        model.setRowCount(0);

        Connection con;

        try {
            con = conexionBD.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT p.idPedidos, c.nombre, c.cedula, p.fecha, p.estado, p.total " +
                    "FROM pedidos AS p " +
                    "JOIN clientes AS c ON p.idclientes = c.idClientes");
            while (rs.next()) {
                int cedula = rs.getInt("c.cedula");
                String nombre = rs.getString("c.nombre");
                dato[0] = rs.getString(1);
                dato[1] = cedula + " / " + nombre;
                ;
                dato[2] = rs.getString(4);
                dato[3] = rs.getString(5);
                dato[4] = rs.getString(6);


                model.addRow(dato);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }


    /**
     * Obtiene los detalles de un pedido específico y los muestra en la tabla de detalles.
     * Muestra el ID del detalle, el número del pedido, el nombre del producto, el tipo de medida, la cantidad y el precio total de ese detalle.
     */
    public void obtenerDatosDetPed() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Numero de pedido");
        model.addColumn("Producto");
        model.addColumn("Tipo");
        model.addColumn("Cantidad");
        model.addColumn("Precio Total");
        pedidoGUI.tablePr.setModel(model);
        String[] dato = new String[6];
        Connection con;
        try {
            con = conexionBD.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT iddetalle_pedido, idpedidos, p.nombre,p.precio, medida, cantidad, subtotal " +
                    "FROM detalle_pedido d JOIN productos p ON d.idproductos = p.idproductos WHERE idpedidos = " + pedidoGUI.valID);
            while (rs.next()) {
                int precio = rs.getInt("p.precio");
                String nombre = rs.getString("p.nombre");
                dato[0] = rs.getString(1);
                dato[1] = rs.getString(2);
                dato[2] = nombre + " / " + precio;
                dato[3] = rs.getString(5);
                dato[4] = rs.getString(6);
                dato[5] = rs.getString(7);

                model.addRow(dato);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtiene el precio de un producto, teniendo en cuenta si se vende por unidad, blister o caja.
     *
     * @param idProducto El ID del producto.
     * @param medida     La forma en que se vende ("unidad", "blister", "caja").
     * @return El precio del producto según la medida.
     */
    public int obtenerPrecioUnitario(int idProducto, String medida) {
        int precioUnitario = 0;
        Connection con = conexionBD.getConnection();
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT precio FROM productos WHERE idproductos = " + idProducto);
            if (rs.next()) {
                precioUnitario = rs.getInt(1);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Ajustamos el precio si la medida no es unidad
        switch (medida.toLowerCase()) {
            case "blister":
                precioUnitario *= 10;
                break;
            case "caja":
                precioUnitario *= 100;
                break;
        }
        return precioUnitario;
    }

    /**
     * Calcula el total de un pedido y lo guarda en la base de datos.
     *
     * @param id_pedido El ID del pedido al que le vamos a actualizar el total.
     */
    public void actualizarTotalOrden(int id_pedido) {
        int total = calcularTotalOrden(id_pedido); // Primero calculamos el total
        Connection con = conexionBD.getConnection();
        try {
            Statement stmt = con.createStatement();
            String query = "UPDATE pedidos SET total = " + total + " WHERE idPedidos = " + id_pedido;
            stmt.executeUpdate(query);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Calcula el total de un pedido sumando el precio de todos los productos que incluye,
     * considerando la cantidad y la forma de venta (unidad, blister, caja).
     *
     * @param idOrden El ID del pedido.
     * @return El precio total del pedido.
     */
    public int calcularTotalOrden(int idOrden) {
        int total = 0;
        Connection con = conexionBD.getConnection();
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT dp.idproductos, dp.medida, dp.cantidad, p.precio " +
                    "FROM detalle_pedido dp " +
                    "JOIN productos p ON dp.idproductos = p.idproductos " +
                    "WHERE dp.idPedidos = " + idOrden);

            while (rs.next()) {
                int precioBase = rs.getInt("precio");
                String medida = rs.getString("medida");
                int cantidad = rs.getInt("cantidad");

                // Ajustamos el precio según la medida
                switch (medida.toLowerCase()) {
                    case "blister":
                        precioBase *= 10;
                        break;
                    case "caja":
                        precioBase *= 100;
                        break;
                }

                total += precioBase * cantidad;
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    /**
     * Obtiene el total de un pedido sumando los subtotales de cada producto en el detalle del pedido.
     *
     * @param idPedido El ID del pedido.
     * @return El total del pedido (la suma de los subtotales).
     */
    public double obtenerTotalPedido(int idPedido) {
        double total = 0.0;
        String sql = "SELECT SUM(subtotal) AS total FROM detalle_pedido WHERE idPedidos = ?";

        try (Connection conn = conexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idPedido);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                total = rs.getDouble("total"); // Aquí obtenemos la suma de los subtotales
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }
}