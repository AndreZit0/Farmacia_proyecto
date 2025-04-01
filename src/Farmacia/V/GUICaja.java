package Farmacia.V;

import Conexion.ConexionBD;
import Farmacia.GUIMenu;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Esta clase representa la interfaz gráfica para la gestión de la caja de la farmacia.
 * Permite visualizar los valores actuales en caja y navegar a otras secciones del sistema.
 */
public class GUICaja {
    private JTable table1;
    private JPanel main;
    private JButton clientesButton;
    private JButton productosButton;
    private JButton pedidoButton;
    private JButton cajaButton;
    private JButton socketsButton;
    private JButton REPORTESButton;
    private JButton MOVIMIENTOSFINANCIEROSButton;
    private JPanel sidebar;
    private JLabel titulo;
    private JScrollPane scroll;
    private JButton farmacTechButton;

    /**
     * Constructor de la clase GUICaja.
     * Inicializa la interfaz, carga los valores de la caja en la tabla
     * y configura los listeners para los botones de navegación.
     */
    public GUICaja() {
        obtener_valores_de_caja();

        clientesButton.addActionListener(new ActionListener() {
            /**
             * Acción a realizar cuando se hace clic en el botón de Clientes.
             * Abre la interfaz de gestión de clientes y cierra la interfaz actual.
             *
             * @param e El evento de acción.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                GUIClientes guiClientes = new GUIClientes();
                guiClientes.ejecutar();
                SwingUtilities.getWindowAncestor(clientesButton).dispose();
            }
        });
        socketsButton.addActionListener(new ActionListener() {
            /**
             * Acción a realizar cuando se hace clic en el botón de Sockets.
             * Abre las interfaces del servidor y cliente de sockets, y cierra la interfaz actual.
             *
             * @param e El evento de acción.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                GUIServidor guiServidor = new GUIServidor();
                guiServidor.ejecutar();

                GUIClienteSocket guiClienteSocket = new GUIClienteSocket();
                guiClienteSocket.ejecutar();
                //SwingUtilities.getWindowAncestor(socketsButton).dispose();
            }
        });

        pedidoButton.addActionListener(new ActionListener() {
            /**
             * Acción a realizar cuando se hace clic en el botón de Pedidos.
             * Abre la interfaz de gestión de pedidos y cierra la interfaz actual.
             *
             * @param e El evento de acción.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                PedidoGUI pedidoGUIDAO = new PedidoGUI();
                pedidoGUIDAO.main();
                SwingUtilities.getWindowAncestor(pedidoButton).dispose();
            }
        });
        productosButton.addActionListener(new ActionListener() {
            /**
             * Acción a realizar cuando se hace clic en el botón de Productos.
             * Abre la interfaz de gestión de productos y cierra la interfaz actual.
             *
             * @param e El evento de acción.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                ProductoGUI productoGUI = new ProductoGUI();
                productoGUI.main();
                SwingUtilities.getWindowAncestor(productosButton).dispose();
            }
        });
        REPORTESButton.addActionListener(new ActionListener() {
            /**
             * Acción a realizar cuando se hace clic en el botón de Reportes.
             * Abre la interfaz de generación de reportes y cierra la interfaz actual.
             *
             * @param e El evento de acción.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                ReportesGUI reportesGUIDAO = new ReportesGUI();
                reportesGUIDAO.main();
                SwingUtilities.getWindowAncestor(REPORTESButton).dispose();
            }
        });
        MOVIMIENTOSFINANCIEROSButton.addActionListener(new ActionListener() {
            /**
             * Acción a realizar cuando se hace clic en el botón de Movimientos Financieros.
             * Abre la interfaz de gestión de movimientos financieros y cierra la interfaz actual.
             *
             * @param e El evento de acción.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                MovimientosGUI movimientosGUI = new MovimientosGUI();
                movimientosGUI.ejecutar();
                SwingUtilities.getWindowAncestor(MOVIMIENTOSFINANCIEROSButton).dispose();
            }
        });

        // hover de los botones

        clientesButton.addMouseListener(new MouseAdapter() {
            /**
             * Cambia el color de fondo del botón cuando el ratón entra.
             *
             * @param e El evento del ratón.
             */
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                clientesButton.setBackground(new Color(48, 192, 50));
            }

            /**
             * Restaura el color de fondo del botón cuando el ratón sale.
             *
             * @param e El evento del ratón.
             */
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                clientesButton.setBackground(Color.decode("#008000"));
            }
        });
        productosButton.addMouseListener(new MouseAdapter() {
            /**
             * Cambia el color de fondo del botón cuando el ratón entra.
             *
             * @param e El evento del ratón.
             */
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                productosButton.setBackground(new Color(48, 192, 50));
            }

            /**
             * Restaura el color de fondo del botón cuando el ratón sale.
             *
             * @param e El evento del ratón.
             */
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                productosButton.setBackground(Color.decode("#008000"));
            }
        });

        pedidoButton.addMouseListener(new MouseAdapter() {
            /**
             * Cambia el color de fondo del botón cuando el ratón entra.
             *
             * @param e El evento del ratón.
             */
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                pedidoButton.setBackground(new Color(48, 192, 50));
            }

            /**
             * Restaura el color de fondo del botón cuando el ratón sale.
             *
             * @param e El evento del ratón.
             */
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                pedidoButton.setBackground(Color.decode("#008000"));
            }
        });
        REPORTESButton.addMouseListener(new MouseAdapter() {
            /**
             * Cambia el color de fondo del botón cuando el ratón entra.
             *
             * @param e El evento del ratón.
             */
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                REPORTESButton.setBackground(new Color(48, 192, 50));
            }

            /**
             * Restaura el color de fondo del botón cuando el ratón sale.
             *
             * @param e El evento del ratón.
             */
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                REPORTESButton.setBackground(Color.decode("#008000"));
            }
        });
        socketsButton.addMouseListener(new MouseAdapter() {
            /**
             * Cambia el color de fondo del botón cuando el ratón entra.
             *
             * @param e El evento del ratón.
             */
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                socketsButton.setBackground(new Color(48, 192, 50));
            }

            /**
             * Restaura el color de fondo del botón cuando el ratón sale.
             *
             * @param e El evento del ratón.
             */
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                socketsButton.setBackground(Color.decode("#008000"));
            }
        });
        MOVIMIENTOSFINANCIEROSButton.addMouseListener(new MouseAdapter() {
            /**
             * Cambia el color de fondo del botón cuando el ratón entra.
             *
             * @param e El evento del ratón.
             */
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                MOVIMIENTOSFINANCIEROSButton.setBackground(new Color(48, 192, 50));
            }

            /**
             * Restaura el color de fondo del botón cuando el ratón sale.
             *
             * @param e El evento del ratón.
             */
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                MOVIMIENTOSFINANCIEROSButton.setBackground(Color.decode("#008000"));
            }
        });
        farmacTechButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUIMenu guiMenu = new GUIMenu();
                guiMenu.main();
                SwingUtilities.getWindowAncestor(farmacTechButton).dispose();
            }
        });
    }

    /**
     * Obtiene los valores actuales de la caja desde la base de datos
     * y los muestra en la tabla de la interfaz.
     */
    public void obtener_valores_de_caja() {
        DefaultTableModel modelo = new DefaultTableModel();

        modelo.addColumn("id_Caja");
        modelo.addColumn("formato");
        modelo.addColumn("valor");

        table1.setModel(modelo);

        String[] dato = new String[3];
        Connection con = ConexionBD.getConnection();

        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("Select * FROM caja");

            while (rs.next()) {
                dato[0] = rs.getString(1);
                dato[1] = rs.getString(2);
                dato[2] = rs.getString(3);

                modelo.addRow(dato);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Personaliza la apariencia de la tabla en la interfaz gráfica.
     * <p>
     * Modifica el color de fondo y el color del texto del encabezado de la tabla.
     * Además, ajusta el fondo del área de visualización del JScrollPane.
     * </p>
     */
    public void tablaPersonalizado() {
        // Cambiar el color del encabezado de la tabla
        table1.getTableHeader().setForeground(Color.decode("#ffffff")); // Color del texto
        table1.getTableHeader().setBackground(Color.decode("#008000")); // Color de fondo
        scroll.getViewport().setBackground(Color.decode("#e8e6e8"));
    }

    /**
     * Método principal para ejecutar y mostrar la interfaz de la caja.
     */
    public void ejecutar() {
        JFrame frame = new JFrame("caja");
        frame.setContentPane(this.main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setResizable(false);
        frame.setVisible(true);
        tablaPersonalizado();
        titulo.setFont(new Font("", Font.BOLD, 32)); //da el tamaño y fuente al titulo
    }
}