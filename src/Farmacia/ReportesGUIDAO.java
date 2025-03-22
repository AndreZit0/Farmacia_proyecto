package Farmacia;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase ReportesGUIDAO que gestiona la interfaz gráfica y la obtención de reportes desde la base de datos.
 */
public class ReportesGUIDAO {
    private JPanel main;
    private JTable table1;
    private JButton diariasButton;
    private JButton semanalesButton;
    private JButton mensualesButton;
    private JLabel labelTitulo;

    private ReportesDAO reportesDAO = new ReportesDAO();
    ConexionBD conexionBD = new ConexionBD();

    /**
     * Esta clase almacena el reporte con los datos de una ventas.
     */
    class Reporte {
        int idPedido, cantidad;
        String cliente, producto, fecha;

        /**
         * Constructor de la clase Reporte.
         * @param idPedido ID del pedido.
         * @param cliente Nombre del cliente.
         * @param producto Nombre del producto.
         * @param cantidad Cantidad del producto pedido.
         * @param fecha Fecha del pedido.
         */
        public Reporte(int idPedido, String cliente, String producto, int cantidad, String fecha) {
            this.idPedido = idPedido;
            this.cliente = cliente;
            this.producto = producto;
            this.cantidad = cantidad;
            this.fecha = fecha;
        }
    }

    /**
     * Esta clase permite gestionar y acceder a la base de datos para obtener los reportes de las ventas.
     */
    class ReportesDAO {
        /**
         * Obtiene los reportes de pedidos según el intervalo de tiempo especificado.
         * @param intervalo Intervalo de tiempo (por ejemplo, "1 DAY", "1 WEEK", "1 MONTH").
         * @return Lista de reportes obtenidos desde la base de datos.
         */
        public List<Reporte> obtenerOrdenesPorPeriodo(String intervalo) {
            List<Reporte> reportes = new ArrayList<>();
            String query = "SELECT p.idPedidos, c.nombre AS Cliente, pr.nombre AS Producto, dp.cantidad, p.fecha " +
                    "FROM pedidos p " +
                    "JOIN clientes c ON p.idclientes = c.idclientes " +
                    "JOIN detalle_pedido dp ON p.idPedidos = dp.idpedidos " +
                    "JOIN productos pr ON dp.idproductos = pr.idproductos " +
                    "WHERE p.fecha >= NOW() - INTERVAL " + intervalo;

            try (Connection con = conexionBD.getConnection();
                 PreparedStatement stmt = con.prepareStatement(query)) {
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    reportes.add(new Reporte(
                            rs.getInt("idPedidos"),
                            rs.getString("Cliente"),
                            rs.getString("Producto"),
                            rs.getInt("cantidad"),
                            rs.getString("fecha")
                    ));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return reportes;
        }
    }

    /**
     * El constructor ReportesGUIDAO permite asignar los eventos a cada boton.
     */
    public ReportesGUIDAO() {
        diariasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarReportes("1 DAY");
            }
        });
        semanalesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarReportes("1 WEEK");
            }
        });
        mensualesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarReportes("1 MONTH");
            }
        });
    }

    /**
     * Muestra los reportes en la tabla según el intervalo de tiempo seleccionado.
     * @param intervalo Intervalo de tiempo a consultar (por ejemplo, "1 DAY").
     */
    public void mostrarReportes(String intervalo) {
        List<Reporte> reportes = reportesDAO.obtenerOrdenesPorPeriodo(intervalo);
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID Pedido");
        model.addColumn("Cliente");
        model.addColumn("Producto");
        model.addColumn("Cantidad");
        model.addColumn("Fecha");

        for (Reporte reporte : reportes) {
            model.addRow(new Object[]{
                    reporte.idPedido,
                    reporte.cliente,
                    reporte.producto,
                    reporte.cantidad,
                    reporte.fecha
            });
        }

        table1.setModel(model);
    }

    /**
     * Inicia y muestra la interfaz gráfica de reportes.
     */
    public void main() {
        JFrame frame = new JFrame("Reportes");
        frame.setContentPane(this.main);
        frame.setSize(600, 400);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}

