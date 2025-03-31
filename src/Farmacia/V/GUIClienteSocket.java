package Farmacia.V;

import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;

/**
 * Clase que representa un cliente de chat con interfaz gráfica usando sockets.
 */
public class GUIClienteSocket {
    private JTextField textField1;
    private JTextArea textArea1;
    private JButton enviarMensajeAServidorButton;
    private JPanel main;
    private JButton SALIRButton;
    private PrintWriter out;
    private BufferedReader in;
    private Socket socket;

    /**
     * Constructor de la clase GUIClienteSocket.
     * Inicializa la conexión con el servidor y configura la interfaz gráfica.
     */
    public GUIClienteSocket() {
        if (!conectarServidor()) {
            JOptionPane.showMessageDialog(null, "No se pudo conectar al servidor.");

        }

        enviarMensajeAServidorButton.addActionListener(e -> enviarMensaje());

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
                if (out != null) {
                    out.println("CLIENTE: EXIT");
                }
                cerrarConexion();
                SwingUtilities.getWindowAncestor(SALIRButton).dispose();

            }
        });
    }

    /**
     * Establece la conexión con el servidor mediante sockets.
     * Si la conexión falla, retorna false.
     */
    public boolean conectarServidor() {
        try {
            String serverAddress = JOptionPane.showInputDialog("Ingrese la IP del servidor (localhost si es local)");
            if (serverAddress == null || serverAddress.isEmpty()) serverAddress = "localhost";

            int puerto = 123; // Puerto de conexión
            socket = new Socket(serverAddress, puerto);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // Hilo para recibir mensajes del servidor
            new Thread(() -> {
                try {
                    String receivedMessage;
                    while ((receivedMessage = in.readLine()) != null) {
                        if (receivedMessage.equalsIgnoreCase("Farmacia: salir")) {
                            SwingUtilities.invokeLater(() -> textArea1.append("El Servidor ha salido del chat.\n"));
                            cerrarConexion();
                            return;
                        }
                        String finalReceivedMessage = receivedMessage;
                        SwingUtilities.invokeLater(() -> textArea1.append(finalReceivedMessage + "\n"));
                    }
                } catch (IOException e) {
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Error en el cliente: " + e.getMessage()));
                }
            }).start();

            return true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al conectar con el servidor: " + e.getMessage());
            return false;
        }
    }

    /**
     * Envía un mensaje al servidor si el campo de texto no está vacío.
     * Si el mensaje es "salir", se cierra la conexión.
     */
    public void enviarMensaje() {
        String sendMessage = textField1.getText().trim();
        if (sendMessage.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No puedes enviar un mensaje vacío.");
            return;
        }

        if (out != null) {
            out.println("Cliente: " + sendMessage);
            textArea1.append("Yo: " + sendMessage + "\n");
            textField1.setText("");

            if (sendMessage.equalsIgnoreCase("salir")) {
                out.println("CLIENTE: EXIT");
                cerrarConexion();
                System.exit(0);
            }
            textField1.requestFocus();
        }
    }

    /**
     * Cierra la conexión con el servidor y los recursos de entrada/salida.
     */
    public void cerrarConexion() {
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al cerrar la conexión: " + e.getMessage());
        }
    }

    /**
     * Ejecuta la interfaz gráfica del cliente de chat.
     */
    public void ejecutar() {
        JFrame frame = new JFrame("Cliente de Chat");
        frame.setContentPane(this.main);
        frame.pack();
        frame.setSize(600, 400);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    /**
     * Método principal para ejecutar la aplicación cliente.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GUIClienteSocket().ejecutar());
    }
}
