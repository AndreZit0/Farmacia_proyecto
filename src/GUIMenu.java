import Farmacia.V.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Esta clase representa el menú principal de la aplicación de la farmacia.
 * Tiene botones para ir a diferentes partes del programa, como clientes, caja, productos, etc.
 */
public class GUIMenu {
    private JButton clientesButton;
    private JButton cajaButton;
    private JButton socketsButton;
    private JPanel main;
    private JButton pedidoButton;
    private JButton productosButton;
    private JButton REPORTESButton;
    private JButton MOVIMIENTOSFINANCIEROSButton;
    private JPanel sidebar;
    private JLabel titulo;

    /**
     * Constructor de la clase GUIMenu.
     * Aquí se configuran los botones para que hagan algo cuando el usuario los presiona.
     * Por ejemplo, cuando se presiona el botón de "Clientes", se abre la ventana para ver los clientes.
     */
    public GUIMenu() {

        // Cuando se hace clic en el botón de "Clientes"
        clientesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Creamos una ventana para mostrar la información de los clientes
                GUIClientes guiClientes = new GUIClientes();
                // Hacemos que esa ventana se muestre
                guiClientes.ejecutar();
                // Cerramos la ventana del menú principal
                SwingUtilities.getWindowAncestor(clientesButton).dispose();


            }
        });

        // Cuando se hace clic en el botón de "Sockets"
        socketsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Creamos y mostramos la ventana del servidor de sockets
                GUIServidor guiServidor = new GUIServidor();
                guiServidor.ejecutar();

                // Creamos y mostramos la ventana del cliente de sockets
                GUIClienteSocket guiClienteSocket = new GUIClienteSocket();
                guiClienteSocket.ejecutar();

            }
        });

        // Cuando se hace clic en el botón de "Caja"
        cajaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Abrimos la ventana de la caja registradora
                GUICaja guiCaja = new GUICaja();
                guiCaja.ejecutar();
                // Cerramos el menú
                SwingUtilities.getWindowAncestor(cajaButton).dispose();
            }
        });

        // Cuando se hace clic en el botón de "Pedido"
        pedidoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Abrimos la ventana para gestionar los pedidos
                PedidoGUI pedidoGUIDAO = new PedidoGUI();
                pedidoGUIDAO.main();
                // Cerramos el menú
                SwingUtilities.getWindowAncestor(pedidoButton).dispose();
            }
        });

        // Cuando se hace clic en el botón de "Productos"
        productosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Abrimos la ventana para ver y administrar los productos
                ProductoGUI productoGUI = new ProductoGUI();
                productoGUI.main();
                // Cerramos el menú
                SwingUtilities.getWindowAncestor(productosButton).dispose();

            }
        });

        // Cuando se hace clic en el botón de "REPORTES"
        REPORTESButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Abrimos la ventana para ver los reportes
                ReportesGUI reportesGUIDAO = new ReportesGUI();
                reportesGUIDAO.main();
                // Cerramos el menú
                SwingUtilities.getWindowAncestor(REPORTESButton).dispose();
            }
        });

        // Cuando se hace clic en el botón de "MOVIMIENTOSFINANCIEROS"
        MOVIMIENTOSFINANCIEROSButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Abrimos la ventana para ver los movimientos de dinero
                MovimientosGUI movimientosGUI = new MovimientosGUI();
                movimientosGUI.ejecutar();
                // Cerramos el menú
                SwingUtilities.getWindowAncestor(MOVIMIENTOSFINANCIEROSButton).dispose();
            }
        });

        // Los siguientes bloques de código hacen que los botones cambien de color
        // cuando el mouse pasa por encima. Esto le da un toque visual a la interfaz.
        clientesButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                clientesButton.setBackground(new Color(48, 192, 50)); // Verde más claro al entrar el mouse
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                clientesButton.setBackground(Color.decode("#008000")); // Verde original al salir el mouse
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
     * Este es el método principal para iniciar la ventana del menú.
     * Se crea una nueva ventana (JFrame) y se le pone dentro el contenido del menú (this.main).
     */
    public static void main(String[] args) {
        GUIMenu guiMenu = new GUIMenu();
        JFrame frame = new JFrame("FARMAC TECH");
        frame.setContentPane(new GUIMenu().main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setResizable(false);
        frame.setVisible(true);
        guiMenu.titulo.setFont(new Font("", Font.BOLD, 32));
    }

    /**
     * Este método también se llama 'main' pero no es el principal de la aplicación.
     * Parece que se usa para mostrar la ventana del menú en otro contexto.
     */
    public void main() {
        JFrame frame = new JFrame("Reportes");
        frame.setContentPane(this.main);
        frame.setSize(600, 500);
        frame.setResizable(false);
        frame.setVisible(true);

    }
}
