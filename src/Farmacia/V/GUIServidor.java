package Farmacia.V;

import javax.swing.*;
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
    private JButton SALIRButton;
    private JButton clientesButton;
    private JButton productosButton;
    private JButton pedidoButton;
    private JButton cajaButton;
    private JButton REPORTESButton;
    private JButton MOVIMIENTOSFINANCIEROSButton;
    private PrintWriter out;
    private BufferedReader in;
    private Socket clienteSocket;
    private ServerSocket serverSocket;

    /**
     * Constructor de la clase GUIServidor.
     * Inicializa la interfaz gráfica y arranca el servidor en un hilo separado.
     */
    public GUIServidor() {
        new Thread(this::servidor).start(); // Iniciar el servidor en un hilo separado

        enviarMensajeAlClienteButton.addActionListener(e -> enviarMensaje());

        textField1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    enviarMensaje();
                }
            }
        });

        SALIRButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(null, "¿Seguro que deseas salir?", "Confirmar salida", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                enviarMensajeSalida();
                cerrarConexion();
                SwingUtilities.getWindowAncestor(SALIRButton).dispose();
            }
        });
    }

    /**
     * Método para iniciar el servidor y aceptar conexiones de clientes.
     */
    public void servidor() {
        try {
            serverSocket = new ServerSocket(123); // Asegurar que el puerto coincida con el cliente
            SwingUtilities.invokeLater(() -> textArea1.append("Servidor iniciado. Esperando conexión...\n"));

            clienteSocket = serverSocket.accept(); // Esperar conexión de un cliente
            SwingUtilities.invokeLater(() -> textArea1.append("Cliente conectado.\n"));

            // Configurar flujo de entrada y salida
            in = new BufferedReader(new InputStreamReader(clienteSocket.getInputStream()));
            out = new PrintWriter(clienteSocket.getOutputStream(), true);

            // Hilo para recibir mensajes del cliente
            new Thread(() -> {
                try {
                    String receivedMessage;
                    while ((receivedMessage = in.readLine()) != null) {
                        if (receivedMessage.equalsIgnoreCase("CLIENTE: EXIT")) {
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
     * Método para enviar un mensaje al cliente.
     */
    private void enviarMensaje() {
        String message = textField1.getText().trim();
        if (out != null && !message.isEmpty()) {
            out.println("Farmacia: " + message);
            textArea1.append("Farmacia: " + message + "\n");
            textField1.setText("");
        }
    }

    /**
     * Envía un mensaje de salida al cliente antes de cerrar la conexión.
     */
    private void enviarMensajeSalida() {
        if (out != null) {
            out.println("Farmacia: salir");
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
            if (serverSocket != null && !serverSocket.isClosed()) serverSocket.close();
        } catch (IOException e) {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Error al cerrar la conexión: " + e.getMessage()));
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
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                enviarMensajeSalida();
                cerrarConexion();
            }
        });
    }

    /**
     * Método principal para ejecutar la aplicación servidor.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GUIServidor().ejecutar());
    }
}
