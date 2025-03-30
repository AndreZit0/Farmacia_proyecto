package Farmacia.V;

import Conexion.ConexionBD;
import Farmacia.C.ClientesDAO;
import Farmacia.M.Clientes;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

/**
 * Esta clase representa la interfaz gráfica para la gestión de clientes en la farmacia.
 * Permite agregar, actualizar, eliminar y buscar clientes en la base de datos.
 */
public class GUIClientes {
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;
    private JTable table1;
    private JButton actualizarButton;
    private JButton agregarButton;
    private JButton eliminarButton;
    private JPanel main;
    private JTextField textField6;
    private JButton buscarClienteButton;
    private JButton clientesButton;
    private JButton productosButton;
    private JButton pedidoButton;
    private JButton cajaButton;
    private JButton socketsButton;
    private JButton REPORTESButton;
    private JButton MOVIMIENTOSFINANCIEROSButton;
    private JPanel sidebar;
    private JScrollPane scroll;
    private JLabel titulo;
    ClientesDAO clientesDAO = new ClientesDAO();
    int filas;

    /**
     * Constructor de la clase GUIClientes.
     * Aquí se inicializan todos los componentes de la ventana y se configuran
     * los "listeners" para que los botones hagan algo cuando se les hace clic.
     */
    public GUIClientes() {

        textField1.setEnabled(false); // El ID no se debería poder editar directamente

        obetenerClientes(); // Cargamos la lista de clientes al iniciar la ventana

        agregarButton.addActionListener(new ActionListener() {
            /**
             * Acción a realizar cuando se pulsa el botón "Agregar".
             * Llama al método para agregar un nuevo cliente y luego actualiza la tabla.
             *
             * @param e El evento del botón.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                agregar_cliente();
                obetenerClientes();
                clear(); // Limpiamos los campos después de agregar
            }
        });

        actualizarButton.addActionListener(new ActionListener() {
            /**
             * Acción a realizar cuando se pulsa el botón "Actualizar".
             * Llama al método para actualizar la información del cliente seleccionado.
             *
             * @param e El evento del botón.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizar_cliente();
                obetenerClientes();
                clear(); // Limpiamos los campos después de actualizar
            }
        });

        eliminarButton.addActionListener(new ActionListener() {
            /**
             * Acción a realizar cuando se pulsa el botón "Eliminar".
             * Llama al método para eliminar el cliente seleccionado.
             *
             * @param e El evento del botón.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminar();
                obetenerClientes();
                clear(); // Limpiamos los campos después de eliminar
            }
        });

        buscarClienteButton.addActionListener(new ActionListener() {
            /**
             * Acción a realizar cuando se pulsa el botón "Buscar Cliente".
             * Llama al método para buscar clientes por su cédula.
             *
             * @param e El evento del botón.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                buscar_cliente_cedula();
            }
        });
        table1.addMouseListener(new MouseAdapter() {
            /**
             * Acción a realizar cuando se hace clic en una fila de la tabla.
             * Carga los datos de la fila seleccionada en los campos de texto para editar.
             *
             * @param e El evento del ratón.
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int selectFilas = table1.getSelectedRow();

                if (selectFilas >= 0) {
                    textField1.setText((String) table1.getValueAt(selectFilas, 0));
                    textField2.setText((String) table1.getValueAt(selectFilas, 1));
                    textField3.setText((String) table1.getValueAt(selectFilas, 2));
                    textField4.setText((String) table1.getValueAt(selectFilas, 3));
                    textField5.setText((String) table1.getValueAt(selectFilas, 4));
                    textField6.setText((String) table1.getValueAt(selectFilas, 5));

                    filas = selectFilas; // Guardamos la fila seleccionada por si acaso
                }
            }
        });
        socketsButton.addActionListener(new ActionListener() {
            /**
             * Acción a realizar cuando se pulsa el botón de Sockets.
             * Abre las ventanas de servidor y cliente de sockets.
             *
             * @param e El evento del botón.
             */
            @Override
            public void actionPerformed(ActionEvent e) {

                GUIServidor guiServidor = new GUIServidor();
                guiServidor.ejecutar();

                GUIClienteSocket guiClienteSocket = new GUIClienteSocket();
                guiClienteSocket.ejecutar();
                SwingUtilities.getWindowAncestor(socketsButton).dispose();
            }
        });
        productosButton.addActionListener(new ActionListener() {
            /**
             * Acción a realizar cuando se pulsa el botón de Productos.
             * Abre la ventana de gestión de productos.
             *
             * @param e El evento del botón.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                ProductoGUI productoGUI = new ProductoGUI();
                productoGUI.main();
                SwingUtilities.getWindowAncestor(productosButton).dispose();
            }
        });

        cajaButton.addActionListener(new ActionListener() {
            /**
             * Acción a realizar cuando se pulsa el botón de Caja.
             * Abre la ventana de la caja.
             *
             * @param e El evento del botón.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                GUICaja guiCaja = new GUICaja();
                guiCaja.ejecutar();
                SwingUtilities.getWindowAncestor(cajaButton).dispose();
            }
        });
        pedidoButton.addActionListener(new ActionListener() {
            /**
             * Acción a realizar cuando se pulsa el botón de Pedidos.
             * Abre la ventana de gestión de pedidos.
             *
             * @param e El evento del botón.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                PedidoGUI pedidoGUIDAO = new PedidoGUI();
                pedidoGUIDAO.main();
                SwingUtilities.getWindowAncestor(pedidoButton).dispose();
            }
        });
        REPORTESButton.addActionListener(new ActionListener() {
            /**
             * Acción a realizar cuando se pulsa el botón de Reportes.
             * Abre la ventana de reportes.
             *
             * @param e El evento del botón.
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
             * Acción a realizar cuando se pulsa el botón de Movimientos Financieros.
             * Abre la ventana de movimientos financieros.
             *
             * @param e El evento del botón.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                MovimientosGUI movimientosGUI = new MovimientosGUI();
                movimientosGUI.ejecutar();
                SwingUtilities.getWindowAncestor(MOVIMIENTOSFINANCIEROSButton).dispose();
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
        cajaButton.addMouseListener(new MouseAdapter() {
            /**
             * Cambia el color de fondo del botón cuando el ratón entra.
             *
             * @param e El evento del ratón.
             */
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                cajaButton.setBackground(new Color(48, 192, 50));
            }

            /**
             * Restaura el color de fondo del botón cuando el ratón sale.
             *
             * @param e El evento del ratón.
             */
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                cajaButton.setBackground(Color.decode("#008000"));
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
    }

    /**
     * Limpia los campos de texto de la interfaz.
     */
    public void clear() {
        textField1.setText("");
        textField2.setText("");
        textField3.setText("");
        textField4.setText("");
        textField5.setText("");
        textField6.setText("");
    }

    /**
     * Obtiene todos los clientes de la base de datos y los muestra en la tabla.
     */
    public void obetenerClientes() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("id_Clientes");
        modelo.addColumn("Cedula");
        modelo.addColumn("Nombre");
        modelo.addColumn("Telefono");
        modelo.addColumn("Email");
        modelo.addColumn("Dirección");
        table1.setModel(modelo);

        String[] dato = new String[6];
        Connection con = ConexionBD.getConnection();

        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("Select * FROM clientes");

            while (rs.next()) {
                dato[0] = rs.getString(1);
                dato[1] = rs.getString(2);
                dato[2] = rs.getString(3);
                dato[3] = rs.getString(4);
                dato[4] = rs.getString(5);
                dato[5] = rs.getString(6);
                modelo.addRow(dato);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Agrega un nuevo cliente a la base de datos utilizando la información de los campos de texto.
     */
    public void agregar_cliente() {
        String cedula = textField2.getText();
        String nombre = textField3.getText();
        String telefono = textField4.getText();
        String email = textField5.getText();
        String direccion = textField6.getText();

        Clientes clientes = new Clientes(0, cedula, nombre, telefono, email, direccion);

        if (clientesDAO.agregar_cliente(clientes)) {
            JOptionPane.showMessageDialog(null, "¡Cliente agregado con éxito!");
        } else {
            JOptionPane.showMessageDialog(null, "¡Ups! No se pudo crear el cliente.");
        }
    }

    /**
     * Actualiza la información del cliente seleccionado en la base de datos.
     * Utiliza los datos de los campos de texto.
     */
    public void actualizar_cliente() {
        int id_cliente = Integer.parseInt(textField1.getText());
        String cedula = textField2.getText();
        String nombre = textField3.getText();
        String telefono = textField4.getText();
        String email = textField5.getText();
        String direccion = textField6.getText();
        clientesDAO.actualizar_cliente(id_cliente, cedula, nombre, telefono, email, direccion);
    }

    /**
     * Elimina el cliente seleccionado de la base de datos, utilizando el ID mostrado en el campo de texto.
     */
    public void eliminar() {
        int id = Integer.parseInt(textField1.getText());
        clientesDAO.eliminarClientes(id);
    }

    /**
     * Busca clientes por su número de cédula en la base de datos y muestra los resultados en la tabla.
     * Si el campo de cédula está vacío, muestra un mensaje.
     */
    public void buscar_cliente_cedula() {
        String cedula = textField2.getText().trim();

        if (cedula.isEmpty()) {
            JOptionPane.showMessageDialog(null, "El campo de cédula está vacío.");
            return;
        }

        String query = "SELECT * FROM clientes WHERE cedula = ?";
        Connection con = ConexionBD.getConnection();

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, cedula);
            try (ResultSet rs = ps.executeQuery()) {
                DefaultTableModel modelo = new DefaultTableModel();
                modelo.addColumn("id_Clientes");
                modelo.addColumn("Cedula");
                modelo.addColumn("Nombre");
                modelo.addColumn("Telefono");
                modelo.addColumn("Email");
                modelo.addColumn("Dirección");

                if (rs.next()) {
                    Object[] fila = {
                            rs.getInt("idclientes"),
                            rs.getString("cedula"),
                            rs.getString("nombre"),
                            rs.getString("telefono"),
                            rs.getString("email"),
                            rs.getString("direccion")
                    };
                    modelo.addRow(fila);
                    table1.setModel(modelo);
                } else {
                    JOptionPane.showMessageDialog(null, "Cliente con cédula " + cedula + " no encontrado.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();

            JOptionPane.showMessageDialog(null,"Error al buscar cliente: " + e.getMessage());

        }
    }

    /**
     * Personaliza los componentes de entrada de la interfaz gráfica.
     * <p>
     * Aplica un borde en la parte inferior de los JTextField y JComboBox con un color verde.
     * Además, ajusta la fuente del título.
     * </p>
     */
    public void componentesPersonalizado() {
        Border bottom = BorderFactory.createMatteBorder(0, 0, 1, 0, Color.decode("#008000"));
        textField1.setBorder(bottom);
        textField2.setBorder(bottom);
        textField3.setBorder(bottom);
        textField4.setBorder(bottom);
        textField5.setBorder(bottom);
        textField6.setBorder(bottom);

        titulo.setFont(new Font("", Font.BOLD, 32));

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
     * Método principal para ejecutar y mostrar la interfaz de clientes.
     */

    public void ejecutar() {
        JFrame frame = new JFrame("Clientes");
        frame.setContentPane(this.main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setResizable(false);
        frame.setVisible(true);
        tablaPersonalizado();
        componentesPersonalizado();

    }

}