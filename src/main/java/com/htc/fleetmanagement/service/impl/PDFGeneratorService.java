package com.htc.fleetmanagement.service.impl;

import com.htc.fleetmanagement.entity.Driver;
import com.htc.fleetmanagement.entity.FleetClaim;


import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class PDFGeneratorService {



	// Fonts
	private static final Font TITLE_FONT =
	        new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);

	private static final Font HEADER_FONT =
	        new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);

	private static final Font LABEL_FONT =
	        new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);

	private static final Font VALUE_FONT =
	        new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);




    // Colors
    private static final BaseColor DARK_HEADER =
            new BaseColor(60, 70, 90);            

    private static final BaseColor ROW_LIGHT =
            new BaseColor(242, 242, 242);         

    private static final BaseColor ROW_WHITE =
            BaseColor.WHITE;

    private static final BaseColor PENDING_COLOR =
            new BaseColor(243, 156, 18);          


    public byte[] generateClaimPdf(FleetClaim claim, Driver driver) throws DocumentException {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 40, 40, 50, 50);
        PdfWriter.getInstance(document, outputStream);

        document.open();

        // title
        PdfPTable titleTable = new PdfPTable(1);
        titleTable.setWidthPercentage(100);

        PdfPCell titleCell = new PdfPCell(new Phrase("Fleet Claim Report", TITLE_FONT));
        titleCell.setBackgroundColor(DARK_HEADER);
        titleCell.setPadding(15);
        titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        titleCell.setBorder(Rectangle.NO_BORDER);
        titleTable.addCell(titleCell);
        document.add(titleTable);

        document.add(Chunk.NEWLINE);

        // Claim Information
        document.add(buildSectionHeader("Claim Information"));
        document.add(buildDetailTable(new String[][]{
                {"Claim ID",        "#" + claim.getClaimId()},
                {"Status",          claim.getStatus().toString()},
                {"Incident Date",   claim.getIncidentDate().toString()},
                {"Repair Cost",     "\u20B9 " + claim.getRepairCost().toString()},
        }));

        document.add(Chunk.NEWLINE);

        // driver information
        document.add(buildSectionHeader("Driver Information"));
        document.add(buildDetailTable(new String[][]{
                {"Driver Name",     driver.getName()},
                {"Driver Email",    driver.getEmail()},
                {"License Number",  driver.getLicenseNumber()},
                {"Risk Score",      driver.getRiskScore().toString()},
        }));

        document.add(Chunk.NEWLINE);


        //vehicle information
        document.add(buildSectionHeader("Vehicle Information"));
        document.add(buildDetailTable(new String[][]{
                {"Vehicle ID",      claim.getVehicle().getVehicleId().toString()},
                {"Vehicle VIN",     claim.getVehicle().getVin()},
                {"Make / Model",    claim.getVehicle().getMakeModel()},
        }));

        document.add(Chunk.NEWLINE);


        
        // status badge
        PdfPTable statusTable = new PdfPTable(1);
        statusTable.setWidthPercentage(30);
        statusTable.setHorizontalAlignment(Element.ALIGN_LEFT);

        Font statusFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE);
        PdfPCell statusCell = new PdfPCell(
                new Phrase("Status: " + claim.getStatus().toString(), statusFont));
        statusCell.setBackgroundColor(PENDING_COLOR);
        statusCell.setPadding(6);
        statusCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        statusCell.setBorder(Rectangle.NO_BORDER);
        statusTable.addCell(statusCell);
        document.add(statusTable);

        document.add(Chunk.NEWLINE);

        Paragraph footer = new Paragraph(
                "This is an automated report generated Fleet Management System — Confidential");
        footer.setAlignment(Element.ALIGN_CENTER);
        document.add(footer);

        document.close();
        return outputStream.toByteArray();
    }



    private PdfPTable buildSectionHeader(String title) throws DocumentException {
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);
        table.setSpacingBefore(5f);

        PdfPCell cell = new PdfPCell(new Phrase(title, HEADER_FONT));
        cell.setBackgroundColor(DARK_HEADER);
        cell.setPadding(8);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        return table;
    }

    private PdfPTable buildDetailTable(String[][] rows) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{3f, 7f});   // label col : value col

        boolean alternate = false;
        for (String[] row : rows) {
            BaseColor rowColor = alternate ? ROW_LIGHT : ROW_WHITE;

            PdfPCell labelCell = new PdfPCell(new Phrase(row[0], LABEL_FONT));
            labelCell.setBackgroundColor(rowColor);
            labelCell.setPadding(8);
            labelCell.setBorderColor(new BaseColor(221, 221, 221));

            PdfPCell valueCell = new PdfPCell(new Phrase(row[1], VALUE_FONT));
            valueCell.setBackgroundColor(rowColor);
            valueCell.setPadding(8);
            valueCell.setBorderColor(new BaseColor(221, 221, 221));

            table.addCell(labelCell);
            table.addCell(valueCell);

            alternate = !alternate;
        }

        return table;
    }
}