package Farmacia.V;

import Conexion.ConexionBD;
import Farmacia.C.ProductosDAO;
import Farmacia.M.Productos;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

/**
 * Esta clase representa la interfaz gráfica para administrar los productos de la farmacia.
 * Permite agregar, eliminar, actualizar y visualizar la información de los productos en una tabla.
 * También tiene botones para navegar a otras secciones de la aplicación como clientes, pedidos, caja, etc.
 */
public class ProductoGUI {
    private JPanel main;
    private JButton eliminarButton1;
    private JButton agregarButton;
    private JButton actualizarButton;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField5;
    private JTextField textField6;
    private JTextField textField7;
    private JTextField textField8;
    private JTable table1;
    private JComboBox comboBox1;
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


    // Esto nos ayuda a conectarnos a la base de datos
    private ConexionBD conexionBD = new ConexionBD();
    // Este objeto nos permite hacer las operaciones con los productos en la base de datos (guardar, borrar, etc.)
    ProductosDAO productoDAO = new ProductosDAO();

    // Esta variable guarda el número de la fila que seleccionamos en la tabla
    int filas;


    /**
     * Constructor de la clase ProductoGUI.
     * Aquí se inicializan los componentes de la interfaz, se cargan los datos de los productos
     * y se configuran los listeners para los botones y la tabla.
     */
    public ProductoGUI() {
        // Deshabilitamos el campo de texto del ID porque la base de datos lo genera automáticamente
        textField1.setEnabled(false);

        // Cargamos los datos de los productos desde la base de datos y los mostramos en la tabla
        obtenerDatos();

        // Cuando se hace clic en el botón "Agregar"
        agregarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Obtenemos los valores ingresados por el usuario en los campos de texto
                String nombre = textField2.getText();
                int precio = Integer.parseInt(textField3.getText());
                String categoria = comboBox1.getSelectedItem().toString();
                String descripcion = textField5.getText();
                int stock = Integer.parseInt(textField6.getText());
                int stock_minimo = Integer.parseInt(textField7.getText());
                Date fechaV = Date.valueOf(textField8.getText());

                // Creamos un objeto Producto con los datos
                Productos productos = new Productos(0, precio, stock, stock_minimo, nombre, descripcion, categoria, fechaV);
                // Usamos el objeto ProductosDAO para guardar el nuevo producto en la base de datos
                productoDAO.agregar(productos);
                // Volvemos a cargar los datos en la tabla para que se vea el nuevo producto
                obtenerDatos();
                // Limpiamos los campos de texto para que el usuario pueda ingresar un nuevo producto fácilmente
                clear();
            }
        });

        // Cuando se hace clic en el botón "Actualizar"
        actualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Obtenemos el ID del producto que queremos actualizar (debe estar seleccionado en la tabla)
                int id = Integer.parseInt(textField1.getText());
                // Obtenemos los nuevos valores de los campos de texto
                String nombre = textField2.getText();
                int precio = Integer.parseInt(textField3.getText());
                String categoria = comboBox1.getSelectedItem().toString();
                String descripcion = textField5.getText();
                int stock = Integer.parseInt(textField6.getText());
                int stock_minimo = Integer.parseInt(textField7.getText());
                Date fechaV = Date.valueOf(textField8.getText());

                // Creamos un objeto Producto con los datos actualizados, incluyendo el ID
                Productos productos = new Productos(id, precio, stock, stock_minimo, nombre, descripcion, categoria, fechaV);
                // Usamos el objeto ProductosDAO para actualizar el producto en la base de datos
                productoDAO.actualizar(productos);
                // Volvemos a cargar los datos en la tabla para ver los cambios
                obtenerDatos();
                // Limpiamos los campos de texto
                clear();
            }
        });

        // Cuando se hace clic en el botón "Eliminar"
        eliminarButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Obtenemos el ID del producto que queremos eliminar (debe estar seleccionado en la tabla)
                int id = Integer.parseInt(textField1.getText());
                // Usamos el objeto ProductosDAO para eliminar el producto de la base de datos
                productoDAO.eliminar(id);
                // Volvemos a cargar los datos en la tabla para ver que se eliminó
                obtenerDatos();
                // Limpiamos los campos de texto
                clear();
            }
        });

        // Cuando se hace clic con el mouse en una fila de la tabla
        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                // Obtenemos el número de la fila que se seleccionó
                int selectFilas = table1.getSelectedRow();

                // Si se seleccionó una fila (el número de fila es mayor o igual a 0)
                if (selectFilas >= 0) {
                    // Llenamos los campos de texto con los datos de la fila seleccionada
                    textField1.setText((String) table1.getValueAt(selectFilas, 0)); // ID
                    textField2.setText((String) table1.getValueAt(selectFilas, 1)); // Nombre
                    textField3.setText((String) table1.getValueAt(selectFilas, 2)); // Precio
                    comboBox1.setSelectedItem(table1.getValueAt(selectFilas, 3)); // Categoría
                    textField5.setText((String) table1.getValueAt(selectFilas, 4)); // Descripción
                    textField6.setText((String) table1.getValueAt(selectFilas, 5)); // Stock
                    textField7.setText((String) table1.getValueAt(selectFilas, 6)); // Stock Mínimo
                    // Para la fecha de vencimiento, verificamos que no sea nulo antes de mostrarla
                    Object fechaValor = table1.getValueAt(selectFilas, 7);
                    if (fechaValor != null) {
                        textField8.setText(fechaValor.toString()); // Vencimiento
                    }

                    // Guardamos el número de la fila seleccionada
                    filas = selectFilas;
                }
            }
        });

        // Cuando se hace clic en el botón "Clientes"
        clientesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Creamos una nueva ventana para la gestión de clientes
                GUIClientes guiClientes = new GUIClientes();
                // Mostramos la ventana de clientes
                guiClientes.ejecutar();
                // Cerramos la ventana actual de productos
                SwingUtilities.getWindowAncestor(clientesButton).dispose();
            }
        });

        // Cuando se hace clic en el botón "Sockets"
        socketsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Creamos e iniciamos el servidor de sockets
                GUIServidor guiServidor = new GUIServidor();
                guiServidor.ejecutar();

                // Creamos e iniciamos el cliente de sockets
                GUIClienteSocket guiClienteSocket = new GUIClienteSocket();
                guiClienteSocket.ejecutar();
                // Cerramos la ventana actual
                SwingUtilities.getWindowAncestor(socketsButton).dispose();
            }
        });

        // Cuando se hace clic en el botón "Caja"
        cajaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Abrimos la interfaz de la caja registradora
                GUICaja guiCaja = new GUICaja();
                guiCaja.ejecutar();
                SwingUtilities.getWindowAncestor(cajaButton).dispose();
            }
        });

        // Cuando se hace clic en el botón "Pedido"
        pedidoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Abrimos la interfaz para gestionar los pedidos
                PedidoGUI pedidoGUIDAO = new PedidoGUI();
                pedidoGUIDAO.main();
                SwingUtilities.getWindowAncestor(pedidoButton).dispose();
            }
        });

        // Cuando se hace clic en el botón "REPORTES"
        REPORTESButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Abrimos la interfaz para ver los reportes
                ReportesGUI reportesGUIDAO = new ReportesGUI();
                reportesGUIDAO.main();
                SwingUtilities.getWindowAncestor(REPORTESButton).dispose();
            }
        });

        // Cuando se hace clic en el botón "MOVIMIENTOSFINANCIEROS"
        MOVIMIENTOSFINANCIEROSButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Abrimos la interfaz para ver los movimientos financieros
                MovimientosGUI movimientosGUI = new MovimientosGUI();
                movimientosGUI.ejecutar();
                SwingUtilities.getWindowAncestor(MOVIMIENTOSFINANCIEROSButton).dispose();
            }
        });

        // Los siguientes bloques de código hacen que los botones del menú lateral cambien de color
        // cuando el mouse entra y sale de ellos, para dar una mejor sensación de interactividad.
        // (Esto se repite para cada botón del menú)
        clientesButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                clientesButton.setBackground(new Color(48, 192, 50)); // Color verde más claro al pasar el mouse
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                clientesButton.setBackground(Color.decode("#008000")); // Color verde original al quitar el mouse
            }
        });

        pedidoButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                pedidoButton.setBackground(new Color(48, 192, 50));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                pedidoButton.setBackground(Color.decode("#008000"));
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
        pedidoButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                pedidoButton.setBackground(new Color(48, 192, 50));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                pedidoButton.setBackground(Color.decode("#008000"));
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
     * Este método pone en blanco todos los campos de texto.
     * Se usa después de agregar, eliminar o actualizar un producto para que sea más fácil ingresar nuevos datos.
     */
    public void clear() {
        textField1.setText("");
        textField2.setText("");
        textField3.setText("");
        textField5.setText("");
        textField6.setText("");
        textField7.setText("");
        textField8.setText("");
    }

    /**
     * <p>Este método se encarga de ir a la base de datos, traer todos los productos que estén guardados
     * y mostrarlos en la tabla de la interfaz.</p>
     */
    public void obtenerDatos() {
        // Creamos un "modelo" para nuestra tabla. Piensa en él como la estructura de la tabla (las columnas).
        DefaultTableModel model = new DefaultTableModel();
        // Limpiamos cualquier dato que pueda haber en la tabla antes
        model.setRowCount(0);
        // Añadimos las columnas a nuestra tabla
        model.addColumn("id");
        model.addColumn("Nombre");
        model.addColumn("Precio");
        model.addColumn("Categoria");
        model.addColumn("Descripcion");
        model.addColumn("Stock");
        model.addColumn("Stock_minimo");
        model.addColumn("Fecha de vencimiento");

        table1.setModel(model);
        String[] dato = new String[8];

        Connection con = conexionBD.getConnection();
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT idproductos,nombre,precio,categoria,descripcion,stock,stock_minimo,fecha_vencimiento FROM productos");
            while (rs.next()) {
                dato[0] = rs.getString(1);
                dato[1] = rs.getString(2);
                dato[2] = rs.getString(3);
                dato[3] = rs.getString(4);
                dato[4] = rs.getString(5);
                dato[5] = rs.getString(6);
                dato[6] = rs.getString(7);
                dato[7] = rs.getString(8);

                model.addRow(dato);
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
        textField5.setBorder(bottom);
        textField6.setBorder(bottom);
        textField7.setBorder(bottom);
        textField8.setBorder(bottom);
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
        table1.getTableHeader().setForeground(Color.decode("#ffffff")); // Color del texto
        table1.getTableHeader().setBackground(Color.decode("#008000")); // Color de fondo
        scroll.getViewport().setBackground(Color.decode("#e8e6e8"));
    }



    /**
     * ejecuta la interfaz de producto
     */
    public void main() {
        JFrame frame = new JFrame("Producto");
        frame.setContentPane(this.main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setResizable(false);
        frame.setVisible(true);
        componentesPersonalizado();
        tablaPersonalizado();
    }
}
