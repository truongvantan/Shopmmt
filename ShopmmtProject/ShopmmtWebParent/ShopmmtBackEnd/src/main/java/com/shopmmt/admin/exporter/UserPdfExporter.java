package com.shopmmt.admin.exporter;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.shopmmt.common.entity.User;

import jakarta.servlet.http.HttpServletResponse;

public class UserPdfExporter extends AbstractExporter {
	public void export(List<User> listUsers, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, ".pdf", "application/pdf");

		Document document = new Document(PageSize.A4);
		PdfWriter.getInstance(document, response.getOutputStream());

		document.open();

		// Configuration encoding UTF-8
		Font font = new Font(BaseFont.createFont("static/fonts/ARIAL.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
		font.setSize(14);

		Paragraph paragraph = new Paragraph("Danh sách người dùng", font);
		paragraph.setAlignment(Paragraph.ALIGN_CENTER);

		document.add(paragraph);

		PdfPTable table = new PdfPTable(6);
		table.setWidthPercentage(100f);
		table.setSpacingBefore(10);
		table.setWidths(new float[] { 0.8f, 3.8f, 2.3f, 3.2f, 3.0f, 2.0f });

		writeTableHeader(table);
		writeTableData(table, listUsers);

		document.add(table);

		document.close();
	}

	private void writeTableData(PdfPTable table, List<User> listUsers) throws DocumentException, IOException {
		Font font = new Font(BaseFont.createFont("static/fonts/ARIAL.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
		font.setColor(Color.BLACK);
		for (User user : listUsers) {
			table.addCell(new Phrase(String.valueOf(user.getId()), font));
			table.addCell(new Phrase(user.getEmail(), font));
			table.addCell(new Phrase(user.getFirstName(), font));
			table.addCell(new Phrase(user.getFirstName(), font));
			table.addCell(new Phrase(user.getRoles().toString(), font));
			table.addCell(new Phrase(String.valueOf(user.isEnabled()), font));
		}
	}

	private void writeTableHeader(PdfPTable table) throws DocumentException, IOException {
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(Color.BLACK);
		cell.setPadding(2);
		
		Font font = new Font(BaseFont.createFont("static/fonts/ARIAL.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED));

		font.setSize(13);
		font.setColor(Color.WHITE);

		cell.setPhrase(new Phrase("ID", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("E-mail", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Tên", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Họ đệm", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Vai trò", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Khả dụng", font));
		table.addCell(cell);
	}
}
