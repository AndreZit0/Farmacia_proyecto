package Farmacia.V;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Esta clase representa la interfaz gráfica del servidor de chat.
 * Permite al servidor enviar y recibir mensajes de un cliente conectado.
 */
public class GUIServidor {
    private JTextField textField1;
    private JTextArea textArea1;
    private JButton enviarMensajeAlClienteButton;
    private JPanel main;
    private JButton clientesButton;
    private JButton productosButton;
    private JButton pedidoButton;
    private JButton cajaButton;
    private JButton socketsButton;
    private JButton REPORTESButton;
    private JButton MOVIMIENTOSFINANCIEROSButton;
    private JPanel sidebar;
    private PrintWriter out;
    private BufferedReader in;
    private Socket clienteSocket;

    /**
     * Constructor de la clase GUIServidor.
     * Inicializa la interfaz gráfica y arranca el servidor en un hilo separado
     * para no bloquear la interfaz de usuario. También configura los listeners
     * para el envío de mensajes.
     */
    public GUIServidor() {
        new Thread(this::servidor).start(); // Iniciar el servidor en un hilo separado

        // Enviar mensaje con el botón
        enviarMensajeAlClienteButton.addActionListener(e -> enviarMensaje());

        // Enviar mensaje con ENTER en el campo de texto
        textField1.addKeyListener(new KeyAdapter() {
            /**
             * Se llama cuando se presiona y se suelta una tecla en el campo de texto.
             * Si la tecla presionada es ENTER, se llama al método para enviar el mensaje.
             *
             * @param e El evento de la tecla.
             */
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    enviarMensaje();
                }
            }
        });
        clientesButton.addActionListener(new ActionListener() {
            /**
             * Acción a realizar cuando se hace clic en el botón de Clientes.
             * Abre la interfaz de gestión de clientes.
             *
             * @param e El evento de acción.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                GUIClientes guiClientes = new GUIClientes();
                guiClientes.ejecutar();
            }
        });

        cajaButton.addActionListener(new ActionListener() {
            /**
             * Acción a realizar cuando se hace clic en el botón de Caja.
             * Abre la interfaz de la caja.
             *
             * @param e El evento de acción.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                GUICaja guiCaja = new GUICaja();
                guiCaja.ejecutar();
            }
        });
        pedidoButton.addActionListener(new ActionListener() {
            /**
             * Acción a realizar cuando se hace clic en el botón de Pedidos.
             * Abre la interfaz de gestión de pedidos.
             *
             * @param e El evento de acción.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                PedidoGUI pedidoGUIDAO = new PedidoGUI();
                pedidoGUIDAO.main();
            }
        });
        productosButton.addActionListener(new ActionListener() {
            /**
             * Acción a realizar cuando se hace clic en el botón de Productos.
             * Abre la interfaz de gestión de productos.
             *
             * @param e El evento de acción.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                ProductoGUI productoGUI = new ProductoGUI();
                productoGUI.main();
            }
        });
        REPORTESButton.addActionListener(new ActionListener() {
            /**
             * Acción a realizar cuando se hace clic en el botón de Reportes.
             * Abre la interfaz de reportes.
             *
             * @param e El evento de acción.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                ReportesGUI reportesGUIDAO = new ReportesGUI();
                reportesGUIDAO.main();
            }
        });
        MOVIMIENTOSFINANCIEROSButton.addActionListener(new ActionListener() {
            /**
             * Acción a realizar cuando se hace clic en el botón de Movimientos Financieros.
             * Abre la interfaz de movimientos financieros.
             *
             * @param e El evento de acción.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                MovimientosGUI movimientosGUI = new MovimientosGUI();
                movimientosGUI.ejecutar();
            }
        });
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
     * Método para iniciar el servidor y aceptar conexiones de clientes.
     * Se ejecuta en un hilo separado.
     */
    public void servidor() {
        try (ServerSocket serverSocket = new ServerSocket(123)) { // Asegurar que el puerto coincida con el cliente
            SwingUtilities.invokeLater(() -> textArea1.append("Servidor iniciado. Esperando conexión...\n"));

            clienteSocket = serverSocket.accept(); // Esperar conexión de un cliente
            SwingUtilities.invokeLater(() -> textArea1.append("Cliente conectado.\n"));

            // Configurar flujo de entrada y salida
            in = new BufferedReader(new InputStreamReader(clienteSocket.getInputStream()));
            out = new PrintWriter(clienteSocket.getOutputStream(), true);

            // Escuchar mensajes del cliente en un hilo separado
            new Thread(() -> {
                try {
                    String receivedMessage;
                    while ((receivedMessage = in.readLine()) != null) {
                        if (receivedMessage.equalsIgnoreCase("CLIENTE: salir")) {
                            SwingUtilities.invokeLater(() -> textArea1.append("El cliente ha salido del chat.\n"));
                            cerrarConexion();
                            return;
                        }
                        String finalReceivedMessage = receivedMessage;
                        SwingUtilities.invokeLater(() -> textArea1.append(finalReceivedMessage + "\n"));
                    }
                } catch (IOException e) {
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Error en el servidor: " + e.getMessage()));
                }
            }).start();

        } catch (IOException e) {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Error al iniciar el servidor: " + e.getMessage()));
        }
    }

    /**
     * Método para enviar el mensaje escrito en el campo de texto al cliente conectado.
     * Limpia el campo de texto después de enviar el mensaje.
     */
    private void enviarMensaje() {
        String message = textField1.getText();
        if (out != null && !message.trim().isEmpty()) {
            out.println("Farmacia: " + message);
            textArea1.append("Farmacia: " + message + "\n");
            textField1.setText("");
        }
    }

    /**
     * Cierra la conexión con el cliente y los flujos de entrada/salida.
     */
    private void cerrarConexion() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (clienteSocket != null && !clienteSocket.isClosed()) clienteSocket.close();
        } catch (IOException e) {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Error al cerrar la conexión: " + e.getMessage()));
        }
    }

    /**
     * Método para ejecutar y mostrar la interfaz gráfica del servidor.
     */
    public void ejecutar() {
        JFrame frame = new JFrame("Servidor");
        frame.setContentPane(this.main);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cerrar solo esta ventana
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLocationRelativeTo(null); // Centrar la ventana
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            /**
             * Se llama cuando la ventana está en proceso de cierre.
             * Asegura que la conexión con el cliente se cierre correctamente.
             *
             * @param e El evento de la ventana.
             */
            @Override
            public void windowClosing(WindowEvent e) {
                cerrarConexion();
            }
        });
    }
}