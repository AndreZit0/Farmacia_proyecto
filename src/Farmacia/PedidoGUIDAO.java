package Farmacia;

import Conexion.ConexionBD;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.HashMap;

public class PedidoGUIDAO {

    private JPanel main;
    private JTable Table1;
    private JTextField textField1;
    private JTextField textField4;
    private JButton actualizarButton;
    private JButton agregarButton;
    private JButton eliminarButton;
    private JButton verFacButton;
    private JComboBox comboBox2;
    private JComboBox comboBox3;
    private JTable tablePr;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField5;
    private JTextField textField6;
    private JTextField textField7;
    private JButton agregarButtonP;
    private JButton actualizarButtonP;
    private JButton eliminarButtonP;
    private JComboBox comboBox4;
    private JComboBox comboBox5;
    private JComboBox comboBox1;
    private JButton finalizarButton;
    private JButton clientesButton;
    private JButton productosButton;
    private JButton pedidoButton;
    private JButton cajaButton;
    private JButton socketsButton;
    private JButton REPORTESButton;
    private JButton generarFacturaButton;
    private JButton MOVIMIENTOSFINANCIEROSButton;
    private JPanel Detalle_ped;

    private PedidoDAO pedidoDAO = new PedidoDAO();
    private ConexionBD conexionBD = new ConexionBD();
    private Detalle_pedidoDAO detalle_pedidoDAO = new Detalle_pedidoDAO();
    private ProductoGUIDAO productoGUIDAO = new ProductoGUIDAO();

    private HashMap<String, Integer> clienteMap = new HashMap<>();
    private HashMap<String, Integer> productoMap = new HashMap<>();
    int filas = 0;
    int valID = 0;

    public class Pedido{

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
    class PedidoDAO {
        public void agregar(Pedido pedido) {
            Connection con = conexionBD.getConnection();

            String query = "INSERT INTO pedidos (idclientes,fecha,estado,total) VALUES (?,?,?,?)";

            try {

                PreparedStatement pst = con.prepareStatement(query);

                pst.setInt(1, pedido.getIdclientes());
                pst.setTimestamp(2, pedido.getFecha());
                pst.setString(3, pedido.getEstado());
                pst.setInt(4, pedido.getTotal());

                int resultado = pst.executeUpdate();
                if (resultado > 0)
                    JOptionPane.showMessageDialog(null, "Agregado con Exito");

                else {
                    JOptionPane.showMessageDialog(null, "No Agregado con Exito");
                }

            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "No Agregado con Exito");
            }
        }
        //actualizar
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
                if (resultado > 0)
                    JOptionPane.showMessageDialog(null, "Actualizado con Exito");
                else
                    JOptionPane.showMessageDialog(null, "No Actualizado con Exito");

            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "No Actualizado con Exito");
            }
        }



        //Obtener el id del pedido
        public int obtenerIdPedido(int idPedido) {
            int id = -1; // Valor por defecto si no encuentra el pedido
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



        //Eliminar
        public void eliminar(int id) {
            Connection con = conexionBD.getConnection();

            String query = "DELETE FROM pedidos WHERE idPedidos = ?";

            try {
                PreparedStatement pst = con.prepareStatement(query);
                pst.setInt(1, id);

                int resultado = pst.executeUpdate();

                if (resultado > 0)
                    JOptionPane.showMessageDialog(null, "Eliminado con Exito");
                else
                    JOptionPane.showMessageDialog(null, "No Eliminado con Exito");
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "No Eliminado con Exito");
            }
        }
        public int obtenerIdSeleccionado(JComboBox<String> comboBox, HashMap<String, Integer> map) {
            String seleccionado = (String) comboBox.getSelectedItem();
            return map.getOrDefault(seleccionado, -1); // Si no encuentra el nombre, devuelve -1
        }
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

                        // Verificar el stock actual antes de descontar
                        PreparedStatement stockStmt = con.prepareStatement(stockQuery);
                        stockStmt.setInt(1, idProducto);
                        ResultSet rsStock = stockStmt.executeQuery();

                        if (rsStock.next()) {
                            String nombreProducto = rsStock.getString("nombre");
                            int stockActual = rsStock.getInt("stock");
                            int stockMinimo = rsStock.getInt("stock_minimo");

                            // Si después de la reducción el stock está en nivel mínimo, avisar
                            if ((stockActual - cantidadReal) <= stockMinimo) {
                                JOptionPane.showMessageDialog(null, "Ya casi se te agota este producto: " + nombreProducto);
                            }

                            // Descontar stock
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

                    JOptionPane.showMessageDialog(null, "Stock descontado correctamente.");
                }

                rsEstado.close();
                estadoStmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error en la base de datos.");
            }
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

    public static int obtenerIdpedido = 0;

    public PedidoGUIDAO() {
        agregarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int idCliente = pedidoDAO.obtenerIdSeleccionado(comboBox2, clienteMap);
                    Timestamp fecha = new Timestamp(System.currentTimeMillis()); // Fecha y hora actual
                    String estado = comboBox3.getSelectedItem().toString(); // Obtener el estado del JComboBox

                    Pedido pedido = new Pedido(0, idCliente, 0, estado, fecha);
                    pedidoDAO.agregar(pedido);

                    obtenerDatosPed();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error al agregar el pedido.");
                }
//                inhabilitarPed();
//                habilitarDetPed();
                tablePr.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        super.mouseClicked(e);
                        int selectFilas = tablePr.getSelectedRow();

                        if (selectFilas >= 0) {
                            textField2.setText((String) Table1.getValueAt(selectFilas,0));
                            comboBox5.setSelectedItem( Table1.getValueAt(selectFilas,1));
                            comboBox4.setSelectedItem( Table1.getValueAt(selectFilas,2));
                            comboBox1.setSelectedItem( Table1.getValueAt(selectFilas,3));
                            textField7.setText((String) Table1.getValueAt(selectFilas,4));

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
                pedidoDAO.obtener_ordenes();
                obtenerDatosPed();
                pedidoDAO.obtener_ordenes();
            }
        });
        actualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id = Integer.parseInt(textField1.getText());
                int idCliente = pedidoDAO.obtenerIdSeleccionado(comboBox2, clienteMap);
                Timestamp fecha = new Timestamp(System.currentTimeMillis());
                String estado = comboBox3.getSelectedItem().toString();

                Pedido pedido = new Pedido(id, idCliente, 0, estado, fecha);
                pedidoDAO.actualizar(pedido);
                pedidoDAO.descontarStock(id);

                if (estado.equalsIgnoreCase("entregado")) {
                    int idPedido = pedidoDAO.obtenerIdPedido(id);

                    if (idPedido != -1) { // Esto es Si el pedido existe, insertamos en movimientos financieros
                        String query = "INSERT INTO movimientos_financieros (idPedidos, tipo, categoria, monto, fecha, descripcion) VALUES (?, ?, ?, ?, ?, ?)";

                        try (Connection conn = conexionBD.getConnection();
                             PreparedStatement stmt = conn.prepareStatement(query)) {

                            stmt.setInt(1, idPedido);
                            stmt.setString(2, "efectivo");
                            stmt.setString(3, "ingreso");
                            stmt.setDouble(4, obtenerTotalPedido(idPedido));
                            stmt.setTimestamp(5, fecha);
                            stmt.setString(6, "Se acaba de realizar una venta en FarmaciaTech");

                            stmt.executeUpdate();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                }

                obtenerDatosPed();
            }
        });
        eliminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id = Integer.parseInt(textField1.getText());
                pedidoDAO.eliminar(id);

                obtenerDatosPed();
            }
        });
        //Botones para agregar el detalle de pedido
        agregarButtonP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int idpedidos = Integer.parseInt(comboBox5.getSelectedItem().toString());
                int idproductos = pedidoDAO.obtenerIdSeleccionado(comboBox4, productoMap);
                int cantidad = Integer.parseInt(textField7.getText());
                String medidad = comboBox1.getSelectedItem().toString();
                int precioUnitario = obtenerPrecioUnitario(idproductos,medidad) ;
                int subtotal = precioUnitario * cantidad; // Calcula el subtotal

                Detalle_pedidoDAO.Detalle_pedido detped = new Detalle_pedidoDAO.Detalle_pedido(0, idpedidos, idproductos, cantidad, subtotal, medidad);
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
                int idproductos = pedidoDAO.obtenerIdSeleccionado(comboBox4, productoMap);
                int cantidad = Integer.parseInt(textField7.getText());
                String medidad = comboBox1.getSelectedItem().toString();

                Detalle_pedidoDAO.Detalle_pedido detped = new Detalle_pedidoDAO.Detalle_pedido(id, idpedidos,idproductos,cantidad,0,medidad);
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
                    textField1.setText((String) Table1.getValueAt(selectFilas,0));
                    comboBox2.setSelectedItem( Table1.getValueAt(selectFilas,1));
                    textField4.setText((String) Table1.getValueAt(selectFilas,2));
                    comboBox3.setSelectedItem( Table1.getValueAt(selectFilas,3));

                    obtenerIdpedido = Integer.parseInt(Table1.getValueAt(selectFilas,0).toString());



                    filas = selectFilas;
                }
            }



        });
        tablePr.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectFilas = tablePr.getSelectedRow();

                if (selectFilas >= 0) {
                    textField2.setText((String) tablePr.getValueAt(selectFilas,0));
                    comboBox5.setSelectedItem( tablePr.getValueAt(selectFilas,1));
                    comboBox4.setSelectedItem( tablePr.getValueAt(selectFilas,2));
                    comboBox1.setSelectedItem( tablePr.getValueAt(selectFilas,3));
                    textField7.setText((String) tablePr.getValueAt(selectFilas,4));

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
                SwingUtilities.getWindowAncestor(socketsButton).dispose();



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
                ProductoGUIDAO productoGUIDAO = new ProductoGUIDAO();
                productoGUIDAO.main();
                SwingUtilities.getWindowAncestor(productosButton).dispose();

            }
        });
        REPORTESButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ReportesGUIDAO reportesGUIDAO = new ReportesGUIDAO();
                reportesGUIDAO.main();
                SwingUtilities.getWindowAncestor(REPORTESButton).dispose();
            }
        });
        MOVIMIENTOSFINANCIEROSButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MovimientosGUIDAO movimientosGUIDAO = new MovimientosGUIDAO();
                movimientosGUIDAO.ejecutar();
                SwingUtilities.getWindowAncestor(MOVIMIENTOSFINANCIEROSButton).dispose();
            }
        });
    }
    public void limpiar(){
        textField1.setText("");
        textField4.setText("");
    }
    public void obtenerDatosPed() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Nombre de Cliente");
        model.addColumn("Fecha");
        model.addColumn("Estado");
        model.addColumn("Total");

        Table1.setModel(model);
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
                dato[1] = cedula + " / " + nombre;;
                dato[2] = rs.getString(4);
                dato[3] = rs.getString(5);
                dato[4] = rs.getString(6);


                model.addRow(dato);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

    }

    public void obtenerDatosDetPed() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Numero de pedido");
        model.addColumn("Producto");
        model.addColumn("Tipo");
        model.addColumn("Cantidad");
        model.addColumn("Precio Total");
        tablePr.setModel(model);
        String[] dato = new String[6];
        Connection con;
        try {
            con = conexionBD.getConnection();
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

    public void actualizarTotalOrden(int id_pedido) {
        int total = calcularTotalOrden(id_pedido); // Calcula el total de la orden
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

    public void main() {
        JFrame frame = new JFrame("Pedidos");
        frame.setContentPane(this.main);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(1200, 700);
        frame.setResizable(false);
        frame.setVisible(true);
        pedidoDAO.obtener_productos();
        obtenerDatosPed();
        pedidoDAO.obtener_ordenes();
        obtenerDatosDetPed();
        pedidoDAO.obtener_clientes();
//        inhabilitarDetPed();
        //inhabilitarDetPed();
    }
}
