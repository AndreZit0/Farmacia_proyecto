package Farmacia;

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
    private PrintWriter out;
    private BufferedReader in;
    private Socket socket;

    /**
     * Constructor de la clase GUIClienteSocket.
     * Inicializa la conexión con el servidor y configura la interfaz gráfica.
     */
    public GUIClienteSocket() {
        conectarServidor();

        enviarMensajeAServidorButton.addActionListener(e -> enviarMensaje());


        textField1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) { // Cambié keyPressed a keyReleased
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    enviarMensaje(); // Llama directamente al método sin simular un clic
                }
            }
        });

    }

    /**
     * Establece la conexión con el servidor mediante sockets.
     * Si la conexión falla, muestra un mensaje de error y cierra la aplicación.
     */
    public void conectarServidor() {
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
                        if (receivedMessage.equalsIgnoreCase("SERVER: EXIT")) {
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

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al conectar con el servidor: " + e.getMessage());
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
            e.printStackTrace();
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
}
