
package Farmacia.V;

import Conexion.ConexionBD;
import Farmacia.*;
import Farmacia.C.CajaDAO;
import Farmacia.C.Detalle_pedidoDAO;
import Farmacia.C.PedidoDAO;
import Farmacia.M.Detalles_pedido;
import Farmacia.M.Pedido;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.HashMap;

/**
 * Clase que representa la interfaz gráfica para la gestión de pedidos.
 * Permite crear, actualizar, eliminar y visualizar pedidos, así como sus detalles.
 */
public class PedidoGUI {

    private JPanel main;
    /**
     * Tabla para mostrar la información de los pedidos.
     */
    public JTable Table1;
    private JTextField textField1;
    private JTextField textField4;
    private JButton actualizarButton;
    private JButton agregarButton;
    private JButton eliminarButton;
    JComboBox comboBox2;
    private JComboBox comboBox3;
    /**
     * Tabla para mostrar los detalles de un pedido específico.
     */
    public JTable tablePr;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField5;
    private JTextField textField6;
    private JTextField textField7;
    private JButton agregarButtonP;
    private JButton actualizarButtonP;
    private JButton eliminarButtonP;
    JComboBox comboBox4;
    JComboBox comboBox5;
    private JComboBox comboBox1;
    private JButton clientesButton;
    private JButton productosButton;
    private JButton pedidoButton;
    private JButton cajaButton;
    private JButton socketsButton;
    private JButton REPORTESButton;
    private JButton generarFacturaButton;
    private JButton MOVIMIENTOSFINANCIEROSButton;
    private JPanel sidebar;
    private JScrollPane scroll;
    private JScrollPane scroll1;
    private JLabel titulo;

    private PedidoDAO pedidoDAO;
    private ConexionBD conexionBD = new ConexionBD();
    private Detalle_pedidoDAO detalle_pedidoDAO = new Detalle_pedidoDAO();
    private CajaDAO cajaDAO = new CajaDAO();
    private HashMap<String, Integer> clienteMap = new HashMap<>();
    private HashMap<String, Integer> productoMap = new HashMap<>();

    private int filas = 0;
    /**
     * Almacena el ID del pedido seleccionado.
     */
    public int valID = 0;

    /**
     * Almacena el ID del pedido para generar la factura.
     */
    public static int obtenerIdpedido = 0;

    /**
     * Constructor de la clase PedidoGUI. Inicializa los componentes de la interfaz y los listeners de los eventos.
     */
    public PedidoGUI() {
        this.pedidoDAO = new PedidoDAO();

        //Inhabilitar Campos
        textField1.setEnabled(false);
        textField4.setEnabled(false);
        textField2.setEnabled(false);
        comboBox5.setEnabled(false);

        agregarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int idCliente = obtenerIdSeleccionado(comboBox2, clienteMap);
                Timestamp fecha = new Timestamp(System.currentTimeMillis()); // Fecha y hora actual
                String estado = comboBox3.getSelectedItem().toString(); // Obtener el estado del JComboBox

                Pedido pedido = new Pedido(0, idCliente, 0, estado, fecha);
                pedidoDAO.agregar(pedido);

                obtenerDatosPed();

//
                tablePr.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        super.mouseClicked(e);
                        int selectFilas = tablePr.getSelectedRow();

                        if (selectFilas >= 0) {
                            textField2.setText((String) Table1.getValueAt(selectFilas, 0));
                            comboBox5.setSelectedItem(Table1.getValueAt(selectFilas, 1));
                            comboBox4.setSelectedItem(Table1.getValueAt(selectFilas, 2));
                            comboBox1.setSelectedItem(Table1.getValueAt(selectFilas, 3));
                            textField7.setText((String) Table1.getValueAt(selectFilas, 4));

                            filas = selectFilas;
                        }
                    }
                });
                Table1.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent evt) {
                        evt.consume();
                    }
                });
                obtener_ordenes();
                obtenerDatosPed();
                obtener_ordenes();
            }
        });
        actualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id = Integer.parseInt(textField1.getText());
                int idCliente = obtenerIdSeleccionado(comboBox2, clienteMap);
                Timestamp fecha = new Timestamp(System.currentTimeMillis());
                String estado = comboBox3.getSelectedItem().toString();

                Pedido pedido = new Pedido(id, idCliente, 0, estado, fecha);
                pedidoDAO.actualizar(pedido);
                pedidoDAO.descontarStock(id);

                try (Connection con = conexionBD.getConnection()) {
                    String estadoActualQuery = "SELECT estado FROM pedidos WHERE idPedidos = ?";
                    try (PreparedStatement pstObtener = con.prepareStatement(estadoActualQuery)) {
                        pstObtener.setInt(1, pedido.getIdPedidos());
                        try (ResultSet rs = pstObtener.executeQuery()) {
                            if (rs.next()) {
                                String estado2 = rs.getString("estado");
                                if (estado2.equalsIgnoreCase("Entregado")) {
                                    String[] opcionesPago = {"Efectivo"};
                                    String metodoPago = (String) JOptionPane.showInputDialog(
                                            null,
                                            "Seleccione el método de pago:",
                                            "Método de Pago",
                                            JOptionPane.QUESTION_MESSAGE,
                                            null,
                                            opcionesPago,
                                            opcionesPago[0]
                                    );

                                    if (metodoPago != null) { // Si el usuario selecciona un método de pago
                                        Double iva = 0.19;
                                        int idPedido = pedidoDAO.obtenerIdPedido(id);
                                        if (idPedido != -1) {
                                            double totalPedido = pedidoDAO.obtenerTotalPedido(idPedido) * (1+iva);
                                            String query = "INSERT INTO movimientos_financieros (id_pedido, tipo, categoria, monto, fecha, descripcion) VALUES (?, ?, ?, ?, ?, ?)";

                                            try (PreparedStatement stmt = con.prepareStatement(query)) {
                                                stmt.setInt(1, idPedido);
                                                stmt.setString(2, "Ingreso");
                                                stmt.setString(3, metodoPago.toLowerCase());
                                                stmt.setDouble(4, totalPedido);
                                                stmt.setTimestamp(5, fecha);
                                                stmt.setString(6, "Venta realizada con " + metodoPago);

                                                stmt.executeUpdate();

                                                int valorActual = cajaDAO.obtenerValorCaja();
                                                int nuevoValor = (int) (valorActual + Math.round(totalPedido));
                                                boolean actualizado = cajaDAO.actualizarValorCaja(nuevoValor);

                                                if (actualizado) {
                                                    JOptionPane.showMessageDialog(null, "El estado cambió a entregado.");
                                                    JOptionPane.showMessageDialog(null, "Se actualizó correctamente la caja.");
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "No se encontró el pedido en la base de datos.");
                            }
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

                obtenerDatosPed();
            }
        });


        eliminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id = Integer.parseInt(textField1.getText());
                pedidoDAO.eliminar(id);
                limpiar();
                pedidoDAO.obtenerDatosPed();
            }
        });
        //Botones para agregar el detalle de pedido
        agregarButtonP.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Double iva = 0.19;
                int idpedidos = Integer.parseInt(comboBox5.getSelectedItem().toString());
                int idproductos = obtenerIdSeleccionado(comboBox4, productoMap);
                int cantidad = Integer.parseInt(textField7.getText());
                String medidad = comboBox1.getSelectedItem().toString();
                int precioUnitario = obtenerPrecioUnitario(idproductos, medidad);
                int subtotal = precioUnitario * cantidad; // Calcula el subtotal

                Detalles_pedido detped = new Detalles_pedido(0, idpedidos, idproductos, cantidad, subtotal, medidad);
                detalle_pedidoDAO.agregar(detped);

                actualizarTotalOrden(idpedidos); // Actualiza el total del pedido
                obtenerDatosDetPed();
                obtenerDatosPed();
            }
        });
        actualizarButtonP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id = Integer.parseInt(textField2.getText());
                int idpedidos = Integer.parseInt(comboBox5.getSelectedItem().toString());
                int idproductos = obtenerIdSeleccionado(comboBox4, productoMap);
                int cantidad = Integer.parseInt(textField7.getText());
                String medidad = comboBox1.getSelectedItem().toString();

                Detalles_pedido detped = new Detalles_pedido(id, idpedidos, idproductos, cantidad, 0, medidad);
                detalle_pedidoDAO.actualizar(detped);

                obtenerDatosDetPed();
            }
        });
        eliminarButtonP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id = Integer.parseInt(textField2.getText());
                detalle_pedidoDAO.eliminar(id);

                obtenerDatosDetPed();
            }
        });
        Table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int selectFilas = Table1.getSelectedRow();

                if (selectFilas >= 0) {
                    textField1.setText((String) Table1.getValueAt(selectFilas, 0));
                    comboBox2.setSelectedItem(Table1.getValueAt(selectFilas, 1));
                    textField4.setText((String) Table1.getValueAt(selectFilas, 2));
                    comboBox3.setSelectedItem(Table1.getValueAt(selectFilas, 3));

                    obtenerIdpedido = Integer.parseInt(Table1.getValueAt(selectFilas, 0).toString());

                    filas = selectFilas;
                }
            }
        });
        tablePr.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectFilas = tablePr.getSelectedRow();

                if (selectFilas >= 0) {
                    textField2.setText((String) tablePr.getValueAt(selectFilas, 0));
                    comboBox5.setSelectedItem(tablePr.getValueAt(selectFilas, 1));
                    comboBox4.setSelectedItem(tablePr.getValueAt(selectFilas, 2));
                    comboBox1.setSelectedItem(tablePr.getValueAt(selectFilas, 3));
                    textField7.setText((String) tablePr.getValueAt(selectFilas, 4));

                    filas = selectFilas;
                }
            }
        });

        generarFacturaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FacturaPDF facturaPDF = new FacturaPDF();
                facturaPDF.factura();
            }
        });
        clientesButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                GUIClientes guiClientes = new GUIClientes();
                guiClientes.ejecutar();
                SwingUtilities.getWindowAncestor(clientesButton).dispose();
            }
        });
        socketsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                GUIServidor guiServidor = new GUIServidor();
                guiServidor.ejecutar();

                GUIClienteSocket guiClienteSocket = new GUIClienteSocket();
                guiClienteSocket.ejecutar();
            }
        });

        cajaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUICaja guiCaja = new GUICaja();
                guiCaja.ejecutar();
                SwingUtilities.getWindowAncestor(cajaButton).dispose();
            }
        });

        productosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ProductoGUI productoGUIDAO = new ProductoGUI();
                productoGUIDAO.main();
                SwingUtilities.getWindowAncestor(productosButton).dispose();
            }
        });
        REPORTESButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ReportesGUI reportesGUIDAO = new ReportesGUI();
                reportesGUIDAO.main();
                SwingUtilities.getWindowAncestor(REPORTESButton).dispose();
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
        clientesButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                clientesButton.setBackground(new Color(48, 192, 50));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                clientesButton.setBackground(Color.decode("#008000"));
            }
        });
        productosButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                productosButton.setBackground(new Color(48, 192, 50));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                productosButton.setBackground(Color.decode("#008000"));
            }
        });
        cajaButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                cajaButton.setBackground(new Color(48, 192, 50));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                cajaButton.setBackground(Color.decode("#008000"));
            }
        });
        REPORTESButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                REPORTESButton.setBackground(new Color(48, 192, 50));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                REPORTESButton.setBackground(Color.decode("#008000"));
            }
        });
        socketsButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                socketsButton.setBackground(new Color(48, 192, 50));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                socketsButton.setBackground(Color.decode("#008000"));
            }
        });
        MOVIMIENTOSFINANCIEROSButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                MOVIMIENTOSFINANCIEROSButton.setBackground(new Color(48, 192, 50));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                MOVIMIENTOSFINANCIEROSButton.setBackground(Color.decode("#008000"));
            }
        });
    }

    /**
     * Limpia los campos de texto para la información del pedido.
     */
    public void limpiar() {
        textField1.setText("");
        textField4.setText("");
    }

    /**
     * Obtiene y muestra los datos del último pedido en la tabla de pedidos.
     */
    public void obtenerDatosPed() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Nombre de Cliente");
        model.addColumn("Fecha");
        model.addColumn("Estado");
        model.addColumn("Total + IVA ");

        Table1.setModel(model);
        String[] dato = new String[5];
        model.setRowCount(0);

        Connection con;

        try {
            con = conexionBD.getConnection();
            Statement stmt = con.createStatement();
            Table1.removeAll();
            ResultSet rs = stmt.executeQuery("SELECT p.idPedidos, c.nombre, c.cedula, p.fecha, p.estado, p.total " +
                    "FROM pedidos AS p " +
                    "JOIN clientes AS c ON p.idclientes = c.idClientes " +
                    "ORDER BY p.idPedidos DESC " +
                    "LIMIT 1");

            if (rs.next()) {
                int cedula = rs.getInt("cedula");
                String nombre = rs.getString("nombre");
                dato[0] = rs.getString(1);
                dato[1] = cedula + " / " + nombre;
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
     * Obtiene y muestra los datos del los productos de un pedido.
     */
    public void obtenerDatosDetPed() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Numero de pedido");
        model.addColumn("Producto");
        model.addColumn("Tipo");
        model.addColumn("Cantidad");
        model.addColumn("Sub Total");
        tablePr.setModel(model);
        String[] dato = new String[6];
        Connection con;
        try {
            con = conexionBD.getConnection();
            tablePr.removeAll();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT iddetalle_pedido, idpedidos, p.nombre,p.precio, medida, cantidad, subtotal " +
                    "FROM detalle_pedido d JOIN productos p ON d.idproductos = p.idproductos WHERE idpedidos = " + valID);
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
     * Obtiene el precio unitario del producto.
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

        // Ajustar el precio según la medida
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
     * actualiza el total de ordenes.
     */
    public void actualizarTotalOrden(int id_pedido) {
        Double iva = 0.19;
        int total = (int) (calcularTotalOrden(id_pedido) * (1 + iva)); // Calcula el total de la orden con IVA
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
     * calcula el total de las ordenes.
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

                // Ajustar el precio según la medida
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
     * Obtiene el ID asociado al elemento seleccionado en un JComboBox.
     * El mapeo entre el nombre del elemento y su ID se proporciona a través de un HashMap.
     *
     * @param comboBox El {@link JComboBox} del cual se obtendrá el elemento seleccionado.
     * @param map      El {@link HashMap} que contiene la correspondencia entre los nombres de los elementos (claves de tipo String)
     * y sus respectivos IDs (valores de tipo Integer).
     * @return El ID (valor Integer) asociado al elemento seleccionado en el JComboBox.
     * Devuelve -1 si el elemento seleccionado no se encuentra como clave en el HashMap.
     */
    public int obtenerIdSeleccionado(JComboBox<String> comboBox, HashMap<String, Integer> map) {
        String seleccionado = (String) comboBox.getSelectedItem();
        return map.getOrDefault(seleccionado, -1); // Si no encuentra el nombre, devuelve -1
    }


    public void obtener_clientes(){

        String query= "Select idClientes,cedula, nombre from clientes";

        Statement st;
        ConexionBD con = new ConexionBD();

        try {
            st = con.getConnection().createStatement();
            ResultSet rs = st.executeQuery(query);
            comboBox2.removeAllItems(); // Limpiar antes de agregar nuevos elementos
            clienteMap.clear(); // También limpiar el mapa

            while(
                    rs.next()) {
                int id = rs.getInt("idClientes");
                String nombre = rs.getString("nombre");
                String cedula = rs.getString("cedula");

                String identificacion = cedula+" / "+nombre;

                clienteMap.put(identificacion, id);
                comboBox2.addItem(identificacion);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void obtener_productos(){

        String query= "SELECT idproductos,nombre, precio FROM productos";

        Statement st;
        ConexionBD con = new ConexionBD();
        try {
            st = con.getConnection().createStatement();
            ResultSet rs = st.executeQuery(query);
           comboBox4.removeAllItems(); // Limpiar antes de agregar nuevos elementos
            productoMap.clear(); // También limpiar el mapa

            while(
                    rs.next()) {
                int id = rs.getInt("idproductos");
                String nombre = rs.getString("nombre");
                int precio = rs.getInt("precio");

                String producto = nombre+" / "+precio;

                productoMap.put(producto, id);
                comboBox4.addItem(producto);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void obtener_ordenes(){

        String query = "SELECT MAX(idPedidos) FROM pedidos";

        ConexionBD con = new ConexionBD();
        try (Connection connection = con.getConnection();
             Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            if (rs.next()) {
                int ultimoID = rs.getInt(1); // Obtener el último ID

                comboBox5.removeAllItems(); // Limpiar ComboBox
                comboBox5.addItem(String.valueOf(ultimoID)); // Agregar solo el último ID
                valID = ultimoID; // Guardar el ID más alto en la variable
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al actualizar el ComboBox: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }
    public double obtenerTotalPedido(int idPedido) {
        double total = 0.0;
        String sql = "SELECT SUM(subtotal) AS total FROM detalle_pedido WHERE idPedidos = ?";

        try (Connection conn = conexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idPedido);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                total = rs.getDouble("total"); // acá está la suma de todos los subtotales del pedido
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
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
        textField4.setBorder(bottom);
        textField7.setBorder(bottom);

        comboBox1.setBorder(bottom);
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
        Table1.getTableHeader().setForeground(Color.decode("#ffffff")); // Color del texto
        Table1.getTableHeader().setBackground(Color.decode("#008000"));
        tablePr.getTableHeader().setForeground(Color.decode("#ffffff")); // Color del texto
        tablePr.getTableHeader().setBackground(Color.decode("#008000")); // Color de fondo
        scroll.getViewport().setBackground(Color.decode("#e8e6e8"));
        scroll.getViewport().setBackground(Color.decode("#e8e6e8"));
    }


    /**
     * ejecuta la interfaz de pedido
     */

    public void main() {
        JFrame frame = new JFrame("Pedidos");
        frame.setContentPane(this.main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setResizable(false);
        frame.setVisible(true);
        obtener_productos();
        obtenerDatosPed();
        obtener_ordenes();
        obtenerDatosDetPed();
        obtener_clientes();
        componentesPersonalizado();
        tablaPersonalizado();

    }
}
