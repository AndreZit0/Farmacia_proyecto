package Farmacia;

import Conexion.ConexionBD;
import Farmacia.V.PedidoGUI;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Stock_minimoPDF {

    public void pdfStock() {
        ConexionBD conexionBD = new ConexionBD();

        // Definir la ruta antes del try para que sea accesible en toda la función
        String dest = "src/Facturas/stock_minimoPDF.pdf";

        try (Connection conn = conexionBD.getConnection()) {
            Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter.getInstance(document, new FileOutputStream(dest));
            document.open();

            // Agregar cabecera
            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidthPercentage(100);
            headerTable.setWidths(new float[]{3, 1});

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            PdfPCell titleCell = new PdfPCell(new Phrase("Reporte de stock minimo", titleFont));
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

            // Creación de la tabla de productos
            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{3, 3, 3});

            Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
            BaseColor headerColor = new BaseColor(0, 128, 0);
            String[] headersText = {"N°", "Nombre", "Stock"};
            for (String header : headersText) {
                PdfPCell headerCell = new PdfPCell(new Phrase(header, headerFont));
                headerCell.setBackgroundColor(headerColor);
                headerCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(headerCell);
            }
            String Query = "SELECT idproductos, nombre, stock FROM productos WHERE stock <= stock_minimo"
                    ;

            try (PreparedStatement stmt = conn.prepareStatement(Query)) {
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        int id = rs.getInt("idproductos");
                        String nombre = rs.getString("nombre");
                        int stock = rs.getInt("stock");

                        table.addCell(""+id);
                        table.addCell(nombre);
                        table.addCell(""+stock);
                    }
                }
            }

            document.add(table);

            document.close();

            JOptionPane.showMessageDialog(null, "PDF creado con éxito: ");

            File file = new File(dest);
            if (file.exists()) {
                Desktop.getDesktop().open(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
