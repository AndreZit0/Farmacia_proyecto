package Farmacia.V;

import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

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
    private PrintWriter out;
    private BufferedReader in;
    private Socket clienteSocket;

    public GUIServidor() {
        new Thread(this::servidor).start(); // Iniciar el servidor en un hilo separado

        // Enviar mensaje con el botón
        enviarMensajeAlClienteButton.addActionListener(e -> enviarMensaje());

        // Enviar mensaje con ENTER
        textField1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    enviarMensaje();
                }
            }
        });
        clientesButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                GUIClientes guiClientes = new GUIClientes();
                guiClientes.ejecutar();



            }
        });

        cajaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUICaja guiCaja = new GUICaja();
                guiCaja.ejecutar();

            }
        });
        pedidoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PedidoGUI pedidoGUIDAO = new PedidoGUI();
                pedidoGUIDAO.main();

            }
        });
        productosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ProductoGUI productoGUI = new ProductoGUI();
                productoGUI.main();


            }
        });
        REPORTESButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ReportesGUI reportesGUIDAO = new ReportesGUI();
                reportesGUIDAO.main();

            }
        });
        MOVIMIENTOSFINANCIEROSButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MovimientosGUI movimientosGUI = new MovimientosGUI();
                movimientosGUI.ejecutar();

            }
        });
    }

    /**
     * Método para iniciar el servidor y aceptar conexiones de clientes.
     */
    public void servidor() {
        try (ServerSocket serverSocket = new ServerSocket(123)) { // Asegurar que el puerto coincida con el cliente
            textArea1.append("Servidor iniciado. Esperando conexión...\n");

            clienteSocket = serverSocket.accept(); // Esperar conexión de un cliente
            textArea1.append("Cliente conectado.\n");

            // Configurar flujo de entrada y salida
            in = new BufferedReader(new InputStreamReader(clienteSocket.getInputStream()));
            out = new PrintWriter(clienteSocket.getOutputStream(), true);

            // Escuchar mensajes del cliente en un hilo separado
            new Thread(() -> {
                try {
                    String receivedMessage;
                    while ((receivedMessage = in.readLine()) != null) {
                        if (receivedMessage.equalsIgnoreCase("CLIENTE: EXIT")) {
                            textArea1.append("El cliente ha salido del chat.\n");
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
     * Envía un mensaje al cliente si el campo de texto no está vacío.
     */
    public void enviarMensaje() {
        String sendMessage = textField1.getText().trim();
        if (sendMessage.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No puedes enviar un mensaje vacío.");
            return;
        }

        if (out != null) {
            out.println("Farmacia: " + sendMessage);
            textArea1.append("Yo: " + sendMessage + "\n");
            textField1.setText("");

            if (sendMessage.equalsIgnoreCase("salir")) {
                out.println("SERVER: EXIT");
                cerrarConexion();
            }
        }

        // Volver a enfocar el campo de texto para seguir escribiendo
        textField1.requestFocus();
    }

    /**
     * Cierra la conexión con el cliente y libera los recursos.
     */
    public void cerrarConexion() {
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (clienteSocket != null) clienteSocket.close();
            textArea1.append("Conexión cerrada.\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ejecuta la interfaz gráfica del servidor.
     */
    public void ejecutar() {
        JFrame frame = new JFrame("Servidor de Chat");
        frame.setContentPane(this.main);
        frame.pack();
        frame.setSize(600, 400);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
