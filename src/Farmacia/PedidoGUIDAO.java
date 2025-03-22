package Farmacia;

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
    private JPanel Detalle_ped;

    private PedidoDAO pedidoDAO = new PedidoDAO();
    private ConexionBD conexionBD = new ConexionBD();
    private Detalle_pedidoDAO detalle_pedidoDAO = new Detalle_pedidoDAO();

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

            String query = "INSERT INTO pedidos (idclientes,fecha,estado,total) VALUES (?,?,?,0)";

            try {

                PreparedStatement pst = con.prepareStatement(query);

                pst.setInt(1, pedido.getIdclientes());
                pst.setTimestamp(2, pedido.getFecha());
                pst.setString(3, pedido.getEstado());

                int resultado = pst.executeUpdate();
                if (resultado > 0)
                    JOptionPane.showMessageDialog(null, "Agregado con Exito");

                else
                    JOptionPane.showMessageDialog(null, "No Agregado con Exito");

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

                        int cantidadReal = 0;
                        switch (medida.toLowerCase()) {
                            case "unidad":
                                cantidadReal = cantidad;
                                break;
                            case "blister":
                                cantidadReal = cantidad * 10;
                                break;
                            case "caja":
                                cantidadReal = cantidad * 100;
                                break;
                        }

                        PreparedStatement updateStockStmt = con.prepareStatement(updateStockQuery);
                        updateStockStmt.setInt(1, cantidadReal);
                        updateStockStmt.setInt(2, idProducto);
                        updateStockStmt.executeUpdate();
                        updateStockStmt.close();
                    }

                    rsDetalle.close();
                    detalleStmt.close();

                    JOptionPane.showMessageDialog(null, "Stock descontado");
                } else {
                    JOptionPane.showMessageDialog(null, "Error");
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

            String query= "SELECT MAX(idPedidos) FROM pedidos";

            Statement st;
            ConexionBD con = new ConexionBD();

            try {
                st = con.getConnection().createStatement();
                ResultSet rs = st.executeQuery(query);
                while(
                        rs.next()) {
                    comboBox5.addItem(rs.getString(1));
                    valID = rs.getInt(1);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
    }

//    public void inhabilitarPed(){
//        comboBox2.setEnabled(false);
//        comboBox3.setEnabled(false);
//        textField1.setEnabled(false);
//        textField4.setEnabled(false);
//        agregarButton.setEnabled(false);
//        actualizarButton.setEnabled(false);
//        eliminarButton.setEnabled(false);
//        verFacButton.setEnabled(false);
//    }
//    public void habilitarPed(){
//        comboBox2.setEnabled(true);
//        comboBox3.setEnabled(true);
//        textField1.setEnabled(true);
//        textField4.setEnabled(true);
//        agregarButton.setEnabled(true);
//        actualizarButton.setEnabled(true);
//        eliminarButton.setEnabled(true);
//        verFacButton.setEnabled(true);
//    }
//
//    public void inhabilitarDetPed(){
//        comboBox5.setEnabled(false);
//        comboBox4.setEnabled(false);
//        comboBox1.setEnabled(false);
//        textField7.setEnabled(false);
//        comboBox5.setEnabled(false);
//        agregarButtonP.setEnabled(false);
//        actualizarButtonP.setEnabled(false);
//        eliminarButtonP.setEnabled(false);
//        finalizarButton.setEnabled(false);
//    }
//    public void habilitarDetPed(){
//        comboBox5.setEnabled(true);
//        comboBox4.setEnabled(true);
//        comboBox1.setEnabled(true);
//        textField7.setEnabled(true);
//        comboBox5.setEnabled(true);
//        agregarButton.setEnabled(true);
//        actualizarButton.setEnabled(true);
//        eliminarButton.setEnabled(true);
//        finalizarButton.setEnabled(true);
//    }

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
            }
        });
        actualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                int id = Integer.parseInt(textField1.getText());
                int idCliente = pedidoDAO.obtenerIdSeleccionado(comboBox2,clienteMap);
                Timestamp fecha = new Timestamp(System.currentTimeMillis());
                String estado = comboBox3.getSelectedItem().toString();


                Pedido pedido = new Pedido(id,idCliente,0,estado,fecha);
                pedidoDAO.actualizar(pedido);
                pedidoDAO.descontarStock(id);




//                if(comboBox3.getSelectedItem().equals("Enviado")){
//                    for (int i = 0; i < tablePr.getRowCount(); i++) {
//                        int idProducto = productoMap.get(tablePr.getValueAt(i, 2).toString());
//                        String tipo = tablePr.getValueAt(i, 3).toString();
//                        int cantidad = Integer.parseInt(tablePr.getValueAt(i, 4).toString());
//
//                        detalle_pedidoDAO.actualizarStock(idProducto, tipo, cantidad);
//                    }
//                }


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

                Detalle_pedidoDAO.Detalle_pedido detped = new Detalle_pedidoDAO.Detalle_pedido(0, idpedidos,idproductos,cantidad,0,medidad);
                detalle_pedidoDAO.agregar(detped);
                obtenerDatosDetPed();


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
                super.mouseClicked(e);
                int selectFilas = Table1.getSelectedRow();

                if (selectFilas >= 0) {
                    textField1.setText((String) Table1.getValueAt(selectFilas,0));
                    comboBox2.setSelectedItem( Table1.getValueAt(selectFilas,1));
                    textField4.setText((String) Table1.getValueAt(selectFilas,2));
                    comboBox3.setSelectedItem( Table1.getValueAt(selectFilas,3));




                    filas = selectFilas;
                }
            }



        });

        tablePr.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
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
        finalizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                habilitarPed();
//                inhabilitarDetPed();

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

        Table1.setModel(model);
        model.setRowCount(0);

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = conexionBD.getConnection();
            String sql = "SELECT p.idPedidos, c.nombre, p.fecha, p.estado " +
                    "FROM pedidos AS p " +
                    "JOIN clientes AS c ON p.idclientes = c.idClientes " +
                    "ORDER BY p.idPedidos DESC LIMIT 1"; // Obtener solo el último pedido
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            if (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("idPedidos"),
                        rs.getString("nombre"),
                        rs.getString("fecha"),
                        rs.getString("estado")
                });
            } else {
                JOptionPane.showMessageDialog(null, "No hay pedidos registrados.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al obtener el último pedido.");
        } finally {

            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void obtenerDatosDetPed() {

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Numero de pedido");
        model.addColumn("Producto");
        model.addColumn("Tipo");
        model.addColumn("Cantidad");

        tablePr.setModel(model);
        String[] dato = new String[5];
        Connection con;
        try {
            con = conexionBD.getConnection();
            String query = "SELECT iddetalle_pedido, idpedidos ,p.nombre,medida,cantidad FROM detalle_pedido d JOIN productos p ON d.idproductos = p.idproductos WHERE idpedidos =" + valID;
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                dato[0] = rs.getString(1);
                dato[1] = rs.getString(2);
                dato[2] = rs.getString(3);
                dato[3] = rs.getString(4);
                dato[4] = rs.getString(5);

                model.addRow(dato);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }







    public void main() {
        JFrame frame = new JFrame("Pedidos");
        frame.setContentPane(this.main);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(700, 700);
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
