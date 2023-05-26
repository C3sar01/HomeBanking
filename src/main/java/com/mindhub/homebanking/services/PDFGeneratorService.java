package com.mindhub.homebanking.services;

import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.mindhub.homebanking.dtos.TransactionDTO;
import com.lowagie.text.Image;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
@Service
public class PDFGeneratorService {

    private MailService mailService;
    private List<TransactionDTO> listTransactionDTO;

    public PDFGeneratorService(List<TransactionDTO> listTransactionDTO) {
        this.listTransactionDTO = listTransactionDTO;
    }

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(5);

        com.lowagie.text.Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        cell.setPhrase(new Phrase("ID", font));

        table.addCell(cell);

        cell.setPhrase(new Phrase("Type of transaction", font));

        table.addCell(cell);

        cell.setPhrase(new Phrase("Date", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Description", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Amount", font));
        table.addCell(cell);

    }

    private void writeTableData(PdfPTable table) {
        for (TransactionDTO listTransactionDTO : listTransactionDTO) {
            table.addCell(String.valueOf(listTransactionDTO.getId()));
            table.addCell(String.valueOf(listTransactionDTO.getType()));
            table.addCell(String.valueOf(listTransactionDTO.getDate()));
            table.addCell(String.valueOf(listTransactionDTO.getDescription()));
            table.addCell(String.valueOf(listTransactionDTO.getAmount()));

        }
    }


    public void export(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fontTitle.setSize(18);
        fontTitle.setColor(Color.DARK_GRAY);

        Paragraph p = new Paragraph("Transactions", fontTitle);
        p.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(p);

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{1.5f, 3.5f, 3.0f, 3.0f, 1.5f});
        table.setSpacingBefore(10);

        writeTableHeader(table);
        writeTableData(table);

        document.add(table);
        document.close();
    }

}
