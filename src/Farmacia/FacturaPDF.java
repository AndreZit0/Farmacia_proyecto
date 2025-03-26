package Farmacia;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import javax.swing.*;
import com.itextpdf.text.Document;

import java.io.FileOutputStream;
import java.sql.*;

import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;


public class FacturaPDF {
    public void main() {

        ConexionBD conexionBD = new ConexionBD();

        int idpedido = PedidoGUIDAO.obtenerIdpedido;

        try (Connection conn = conexionBD.getConnection()) {

            String dest = "src/Facturas/factura_pedido"+idpedido+".pdf";
            String nom_pdf = "factura_pedido"+idpedido+".pdf";

            Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter.getInstance(document, new FileOutputStream(dest));
            document.open();

            // Encabezado con logo y título
            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidthPercentage(100);
            headerTable.setWidths(new float[]{3, 1}); // Mayor espacio para el título

            // Título alineado a la izquierda
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            PdfPCell titleCell = new PdfPCell(new Phrase("Factura de Compra \nFarmaTec", titleFont));
            titleCell.setBorder(Rectangle.NO_BORDER);
            titleCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            titleCell.setHorizontalAlignment(Element.ALIGN_LEFT);

            // Imagen alineada a la derecha
            Image img = Image.getInstance("src/imagenes/verde logo.png");
            img.scaleToFit(80, 80);
            PdfPCell imgCell = new PdfPCell(img);
            imgCell.setBorder(Rectangle.NO_BORDER);
            imgCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

            // Añadir las celdas en el orden correcto
            headerTable.addCell(titleCell);
            headerTable.addCell(imgCell);

            // Agregar la tabla del encabezado al documento
            document.add(headerTable);
            document.add(new Paragraph("\n"));

            // Datos del cliente

            String clienteQuery = "SELECT c.cedula, c.nombre, c.telefono, c.email, c.direccion " +
                    "FROM clientes c " +
                    "JOIN pedidos p ON c.idclientes = p.idclientes " +
                    "WHERE p.idpedidos = ?";

            try (PreparedStatement stmt = conn.prepareStatement(clienteQuery)) {
                stmt.setInt(1, idpedido);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        Font dataFont = new Font(Font.FontFamily.HELVETICA, 12);
                        document.add(new Paragraph("Cédula: " + rs.getString("cedula"), dataFont));
                        document.add(new Paragraph("Nombre: " + rs.getString("nombre"), dataFont));
                        document.add(new Paragraph("Teléfono: " + rs.getString("telefono"), dataFont));
                        document.add(new Paragraph("Email: " + rs.getString("email"), dataFont));
                        document.add(new Paragraph("Dirección: " + rs.getString("direccion"), dataFont));
                        document.add(new Paragraph("\n"));
                    }
                }
            }

            document.add(new Paragraph("N° de pedido: " + idpedido, new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD)));
            document.add(new Paragraph("\n"));

            // Crear tabla de productos
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1, 2, 2, 3, 2});

            // Encabezados
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

            // Agregar total
            PdfPCell totalCell = new PdfPCell(new Phrase("Total", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
            totalCell.setColspan(4);
            totalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(totalCell);
            table.addCell("$" + total);

            document.add(table);
            document.close();
            JOptionPane.showMessageDialog(null, "PDF creado con éxito: "+nom_pdf);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
