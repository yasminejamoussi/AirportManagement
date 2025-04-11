package com.example.Airport_Management.livraisonbagages;

import com.example.Airport_Management.passager.Passager;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

@Component
public class PDFGenerator {

    public byte[] generateDeliveryReceipt(LivraisonBagagesDTO livraisonDTO) throws DocumentException {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Add title
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("Reçu de Livraison de Bagages", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("\n"));

            // Add delivery information
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 12);
            Font boldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            document.add(new Paragraph("Numéro de livraison: " + livraisonDTO.getLivraisonBagages().getId(), normalFont));
            document.add(new Paragraph("Date prévue: " + dateFormat.format(livraisonDTO.getLivraisonBagages().getDate_prevue()), normalFont));
            document.add(new Paragraph("Adresse de livraison: " + livraisonDTO.getLivraisonBagages().getAdresse(), normalFont));
            document.add(new Paragraph("Mode de livraison: " + livraisonDTO.getLivraisonBagages().getModeLivraison(), normalFont));
            document.add(new Paragraph("Statut: " + livraisonDTO.getLivraisonBagages().getStatut(), normalFont));
            document.add(new Paragraph("\n"));

            // Add passenger information
            document.add(new Paragraph("Informations des passagers:", boldFont));
            for (Passager passager : livraisonDTO.getPassagers()) {
                document.add(new Paragraph("- " + passager.getNom() + " " + passager.getPrenom() +
                        " (Tel: " + passager.getNumero() + ")", normalFont));
            }
            document.add(new Paragraph("\n"));

            // Add lost objects information
            document.add(new Paragraph("Objets à livrer:", boldFont));
            for (ObjetPerduDTO objet : livraisonDTO.getObjetPerdus()) {
                document.add(new Paragraph("- Type: " + objet.getObjetPerdu().getType_objet() +
                        "\n  Description: " + objet.getObjetPerdu().getDescription(), normalFont));
            }

            // Add footer
            document.add(new Paragraph("\n\n"));
            Paragraph footer = new Paragraph("Ce document sert de preuve de livraison des bagages.",
                    new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC));
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

        } finally {
            document.close();
        }

        return out.toByteArray();
    }
}