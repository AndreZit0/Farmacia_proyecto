package Farmacia;

import Conexion.ConexionBD;
import Farmacia.V.PedidoGUI;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.*;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

public class FacturaPDF {

    public void factura() {
        ConexionBD conexionBD = new ConexionBD();
        int idpedido = PedidoGUI.obtenerIdpedido;

        // Definir la ruta antes del try para que sea accesible en toda la función
        String dest = "src/Facturas/factura_pedido" + idpedido + ".pdf";
        String nom_pdf = "factura_pedido" + idpedido + ".pdf";

        try (Connection conn = conexionBD.getConnection()) {
            Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter.getInstance(document, new FileOutputStream(dest));
            document.open();

            // Agregar cabecera
            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidthPercentage(100);
            headerTable.setWidths(new float[]{3, 1});

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            PdfPCell titleCell = new PdfPCell(new Phrase("Factura de Compra \nFarmaTech", titleFont));
            titleCell.setBorder(Rectangle.NO_BORDER);
            titleCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            titleCell.setHorizontalAlignment(Element.ALIGN_LEFT);

            Image img = Image.getInstance("src/imagenes/verde logo.png");
            img.scaleToFit(80, 80);
            PdfPCell imgCell = new PdfPCell(img);
            imgCell.setBorder(Rectangle.NO_BORDER);
            imgCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

            headerTable.addCell(titleCell);
            headerTable.addCell(imgCell);

            document.add(headerTable);
            document.add(new Paragraph("\n"));

            // Obtener datos del cliente
            String clienteQuery = "SELECT c.cedula, c.nombre, c.telefono, c.email, c.direccion, p.fecha " +
                    "FROM clientes c " +
                    "JOIN pedidos p ON c.idclientes = p.idclientes " +
                    "WHERE p.idpedidos = ?";

            try (PreparedStatement stmt = conn.prepareStatement(clienteQuery)) {
                stmt.setInt(1, idpedido);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        Font dataFont = new Font(Font.FontFamily.HELVETICA, 12);
                        document.add(new Paragraph("Fecha del pedido: " + rs.getString("fecha"), dataFont));
                        document.add(new Paragraph("Cédula: " + rs.getString("cedula"), dataFont));
                        document.add(new Paragraph("Nombre: " + rs.getString("nombre"), dataFont));
                        document.add(new Paragraph("Teléfono: " + rs.getString("telefono"), dataFont));
                        document.add(new Paragraph("Email: " + rs.getString("email"), dataFont));
                        document.add(new Paragraph("Dirección: " + rs.getString("direccion"), dataFont));
                        document.add(new Paragraph("\n"));
                    }
                }
            }

            document.add(new Paragraph("N° de Factura: " + idpedido, new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD)));
            document.add(new Paragraph("\n"));

            // Creación de la tabla de productos
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1, 2, 2, 3, 2});

            Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
            BaseColor headerColor = new BaseColor(0, 128, 0);
            String[] headersText = {"N°", "Producto", "Cantidad", "Precio Unitario", "Subtotal"};
            for (String header : headersText) {
                PdfPCell headerCell = new PdfPCell(new Phrase(header, headerFont));
                headerCell.setBackgroundColor(headerColor);
                headerCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(headerCell);
            }

            int total = 0;
            String productosQuery = "SELECT pr.nombre AS Producto, dp.medida, dp.cantidad, pr.precio, dp.subtotal " +
                    "FROM pedidos p " +
                    "JOIN detalle_pedido dp ON p.idPedidos = dp.idpedidos " +
                    "JOIN productos pr ON dp.idproductos = pr.idproductos " +
                    "WHERE p.idPedidos = ?";

            int cont = 0;

            try (PreparedStatement stmt = conn.prepareStatement(productosQuery)) {
                stmt.setInt(1, idpedido);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String nombre = rs.getString("Producto");
                        String medida = rs.getString("medida");
                        int cantidad = rs.getInt("cantidad");
                        int precio = rs.getInt("precio");
                        int subtotal = rs.getInt("subtotal");
                        total += subtotal;

                        cont++;
                        table.addCell(String.valueOf(cont));
                        table.addCell(nombre);
                        table.addCell(cantidad + " " + medida);
                        table.addCell("$" + precio);
                        table.addCell("$" + subtotal);
                    }
                }
            }

            // Calcular IVA
            int iva = (int) (total * 0.19);

            // Agregar fila con el IVA
            PdfPCell ivaCell = new PdfPCell(new Phrase("IVA (19%)", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
            ivaCell.setColspan(4);
            ivaCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(ivaCell);
            table.addCell("$" + iva);

            // Calcular total con IVA
            int totalConIVA = total + iva;

            // Agregar fila con el total final
            PdfPCell totalConIvaCell = new PdfPCell(new Phrase("Total a Pagar", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
            totalConIvaCell.setColspan(4);
            totalConIvaCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(totalConIvaCell);
            table.addCell("$" + totalConIVA);

            document.add(table);

            Font thanksFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
            Paragraph thanks = new Paragraph("Gracias por su compra", thanksFont);
            thanks.setAlignment(Element.ALIGN_CENTER);
            document.add(new Paragraph("\n")); // Espacio antes del mensaje
            document.add(thanks);
            document.close();

            JOptionPane.showMessageDialog(null, "PDF creado con éxito: " + nom_pdf);

            File file = new File(dest);
            if (file.exists()) {
                Desktop.getDesktop().open(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Intentar abrir el PDF automaticamente
        try {
            File pdfFile = new File(dest);
            if (pdfFile.exists()) {
                Desktop.getDesktop().open(pdfFile);
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró el archivo PDF.");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "No se pudo abrir el archivo PDF.");
        }
    }
}
