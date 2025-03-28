package Farmacia.C;

import Conexion.ConexionBD;
import Farmacia.M.Productos;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Esta clase se encarga de hablar con la base de datos para todo lo relacionado con los productos.
 * Aquí podemos agregar, eliminar y actualizar la información de los productos.
 */
public class ProductosDAO {
    ConexionBD conexionBD = new ConexionBD();

    /**
     * Guarda un nuevo producto en la base de datos.
     *
     * @param productos El objeto `Productos` que contiene la información del producto que queremos guardar.
     * Debe tener el nombre, precio, stock, stock mínimo, fecha de vencimiento, descripción y categoría del producto.
     */
    public void agregar(Productos productos) {

        Connection con = conexionBD.getConnection();

        String query = "INSERT INTO productos (nombre,precio,stock,stock_minimo,fecha_vencimiento,descripcion,categoria) VALUES (?,?,?,?,?,?,?)";

        try {
            PreparedStatement pst = con.prepareStatement(query);

            pst.setString(1, productos.getNombre());
            pst.setInt(2, productos.getPrecio());
            pst.setInt(3, productos.getStock());
            pst.setInt(4, productos.getStock_minimo());
            pst.setDate(5, productos.getFechaV());
            pst.setString(6, productos.getDescripcion());
            pst.setString(7, productos.getCategoria());

            int resultado = pst.executeUpdate();

            if (resultado > 0) {
                JOptionPane.showMessageDialog(null, "Producto agregado");
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo agregar el producto :(");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "un error al agregar el producto.");
        }
    }

    /**
     * Elimina un producto de la base de datos usando su ID.
     *
     * @param id El ID del producto que queremos borrar. ¡Ten cuidado con esto!
     */
    public void eliminar(int id) {
        Connection con = conexionBD.getConnection();

        String query = "DELETE FROM productos WHERE idproductos = ?";

        try {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, id);

            int resultado = pst.executeUpdate();

            if (resultado > 0) {
                JOptionPane.showMessageDialog(null, "Producto eliminado");
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo eliminar el producto :(");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al eliminar el producto.");
        }
    }

    /**
     * Actualiza la información de un producto que ya existe en la base de datos.
     *
     * @param productos El objeto `Productos` con la información actualizada.
     * Es importante que el ID del producto en este objeto sea el correcto para saber cuál actualizar.
     */
    public void actualizar(Productos productos) {
        Connection con = conexionBD.getConnection();
        String query = "UPDATE productos SET nombre = ?,precio = ?, stock = ?, stock_minimo = ?, fecha_vencimiento = ?, descripcion = ?, categoria = ? WHERE idproductos = ?";

        try {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, productos.getNombre());
            pst.setInt(2, productos.getPrecio());
            pst.setInt(3, productos.getStock());
            pst.setInt(4, productos.getStock_minimo());
            pst.setDate(5, productos.getFechaV());
            pst.setString(6, productos.getDescripcion());
            pst.setString(7, productos.getCategoria());
            pst.setInt(8, productos.getIdproductos());

            int resultado = pst.executeUpdate();
            if (resultado > 0) {
                JOptionPane.showMessageDialog(null, "Producto actualizado");
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo actualizar el producto :(");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, " Error al actualizar el producto.");
        }
    }
}