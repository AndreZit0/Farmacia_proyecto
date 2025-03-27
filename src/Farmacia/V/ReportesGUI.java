package Farmacia.V;

import Conexion.ConexionBD;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase ReportesGUIDAO que gestiona la interfaz gráfica y la obtención de reportes desde la base de datos.
 */
public class ReportesGUI {
    private JPanel main;
    private JTable table1;
    private JButton diariasButton;
    private JButton semanalesButton;
    private JButton mensualesButton;

    private JButton MOVIMIENTOSFINANCIEROSButton;
    private JPanel sidebar;
    private JButton CHATButton;
    private JButton reportesButton;
    private JButton CAJAButton;
    private JButton PEDIDOSButton;
    private JButton PRODUCTOSButton;
    private JButton CLIENTESButton;


    private ReportesDAO reportesDAO = new ReportesDAO();
    ConexionBD conexionBD = new ConexionBD();

    /**
     * Esta clase almacena el reporte con los datos de una ventas.
     */
    class Reporte {
        int idPedido, cantidad;
        String cliente, producto, fecha, medida;

        /**
         * Constructor de la clase Reporte.
         * @param idPedido ID del pedido.
         * @param cliente Nombre del cliente.
         * @param producto Nombre del producto.
         * @param cantidad Cantidad del producto pedido.
         * @param fecha Fecha del pedido.
         */
        public Reporte(int idPedido, String cliente, String producto, int cantidad, String fecha, String medida) {
            this.idPedido = idPedido;
            this.cliente = cliente;
            this.producto = producto;
            this.cantidad = cantidad;
            this.fecha = fecha;
            this.medida = medida;
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
            String query = "SELECT p.idPedidos, c.nombre AS Cliente, pr.nombre AS Producto, dp.medida, dp.cantidad, p.fecha " +
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
                            rs.getString("fecha"),
                            rs.getString("medida")
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
    public ReportesGUI() {
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
        CLIENTESButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                GUIClientes guiClientes = new GUIClientes();
                guiClientes.ejecutar();
                SwingUtilities.getWindowAncestor(CLIENTESButton).dispose();


            }
        });
        CHATButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                GUIServidor guiServidor = new GUIServidor();
                guiServidor.ejecutar();

                GUIClienteSocket guiClienteSocket = new GUIClienteSocket();
                guiClienteSocket.ejecutar();
                SwingUtilities.getWindowAncestor(CHATButton).dispose();



            }
        });

        CAJAButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUICaja guiCaja = new GUICaja();
                guiCaja.ejecutar();
                SwingUtilities.getWindowAncestor(CAJAButton).dispose();
            }
        });
        PEDIDOSButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PedidoGUI pedidoGUIDAO = new PedidoGUI();
                pedidoGUIDAO.main();
                SwingUtilities.getWindowAncestor(PEDIDOSButton).dispose();
            }
        });
        PRODUCTOSButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ProductoGUI productoGUI = new ProductoGUI();
                productoGUI.main();
                SwingUtilities.getWindowAncestor(PRODUCTOSButton).dispose();

            }
        });
        MOVIMIENTOSFINANCIEROSButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MovimientosGUI movimientosGUI = new MovimientosGUI();
                movimientosGUI.ejecutar();
                SwingUtilities.getWindowAncestor(MOVIMIENTOSFINANCIEROSButton).dispose();
            }
        });
        CLIENTESButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                CLIENTESButton.setBackground(new Color(48,192,50));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                CLIENTESButton.setBackground(Color.decode("#008000"));
            }
        });
        PRODUCTOSButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                PRODUCTOSButton.setBackground(new Color(48,192,50));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                PRODUCTOSButton.setBackground(Color.decode("#008000"));
            }
        });

        PEDIDOSButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                PEDIDOSButton.setBackground(new Color(48,192,50));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                PEDIDOSButton.setBackground(Color.decode("#008000"));
            }
        });
        CAJAButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                CAJAButton.setBackground(new Color(48,192,50));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                CAJAButton.setBackground(Color.decode("#008000"));
            }
        });
        CHATButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                CHATButton.setBackground(new Color(48,192,50));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                CHATButton.setBackground(Color.decode("#008000"));
            }
        });
        MOVIMIENTOSFINANCIEROSButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                MOVIMIENTOSFINANCIEROSButton.setBackground(new Color(48,192,50));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                MOVIMIENTOSFINANCIEROSButton.setBackground(Color.decode("#008000"));
            }
        });
        diariasButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                diariasButton.setBackground(new Color(48,192,50));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                diariasButton.setBackground(Color.decode("#008000"));
            }
        });
        semanalesButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                semanalesButton.setBackground(new Color(48,192,50));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                semanalesButton.setBackground(Color.decode("#008000"));
            }
        });
        mensualesButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                mensualesButton.setBackground(new Color(48,192,50));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                mensualesButton.setBackground(Color.decode("#008000"));
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
                    reporte.cantidad + "   " + reporte.medida,
                    reporte.fecha,
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
        frame.setSize(1200, 700);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}