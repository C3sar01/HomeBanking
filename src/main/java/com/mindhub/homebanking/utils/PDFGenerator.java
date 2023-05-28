package com.mindhub.homebanking.utils;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.mindhub.homebanking.dtos.TransactionDTO;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class PDFGenerator {

    private List<TransactionDTO> listTransactionDTO;

    public PDFGenerator(List<TransactionDTO> listTransactionDTO) {
        this.listTransactionDTO = listTransactionDTO;
    }

    public byte[] export() throws DocumentException, IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, byteArrayOutputStream);

        document.open();

        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fontTitle.setSize(18);
        fontTitle.setColor(Color.BLUE);

        Paragraph title = new Paragraph("Transactions", fontTitle);
        title.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(title);

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{1.5f, 3.5f, 3.0f, 3.0f, 1.5f});
        table.setSpacingBefore(10);

        writeTableHeader(table);
        writeTableData(table);

        document.add(table);

        document.close();

        return byteArrayOutputStream.toByteArray();
    }

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        cell.setPhrase(new Phrase("ID", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Type", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Date", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Description", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Amount", font));
        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table) {
        for (TransactionDTO transactionDTO : listTransactionDTO) {
            table.addCell(String.valueOf(transactionDTO.getId()));
            table.addCell(String.valueOf(transactionDTO.getType()));
            table.addCell(String.valueOf(transactionDTO.getDate()));
            table.addCell(String.valueOf(transactionDTO.getDescription()));
            table.addCell(String.valueOf(transactionDTO.getAmount()));
        }
    }
}
