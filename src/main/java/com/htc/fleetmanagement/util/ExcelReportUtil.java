package com.htc.fleetmanagement.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.htc.fleetmanagement.entity.PolicyTable;

import java.io.*;
import java.util.List;

public class ExcelReportUtil {

    public static byte[] generatePoliciesExcelReport(List<PolicyTable> policies) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        Workbook workbook = new XSSFWorkbook();
        
        try {
            Sheet sheet = workbook.createSheet("Policies");

            // Create header row with formatting
            Row headerRow = sheet.createRow(0);
            CellStyle headerStyle = createHeaderCellStyle(workbook);

            String[] headers = {"Policy Number", "Policy Name", "Premium", "Benefits"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Set column widths
            sheet.setColumnWidth(0, 25 * 256); // Policy Number
            sheet.setColumnWidth(1, 30 * 256); // Policy Name
            sheet.setColumnWidth(2, 15 * 256); // Premium
            sheet.setColumnWidth(3, 40 * 256); // Benefits

            // Add data rows
            CellStyle dataStyle = createDataCellStyle(workbook);
            CellStyle currencyStyle = createCurrencyCellStyle(workbook);

            int rowNum = 1;
            for (PolicyTable policy : policies) {
                Row row = sheet.createRow(rowNum++);

                // Policy Number
                Cell cellPolicyNumber = row.createCell(0);
                cellPolicyNumber.setCellValue(policy.getMasterPolicyNumber());
                cellPolicyNumber.setCellStyle(dataStyle);

                // Policy Name
                Cell cellPolicyName = row.createCell(1);
                cellPolicyName.setCellValue(policy.getPolicyName());
                cellPolicyName.setCellStyle(dataStyle);

                // Premium
                Cell cellPremium = row.createCell(2);
                cellPremium.setCellValue(policy.getPremium().doubleValue());
                cellPremium.setCellStyle(currencyStyle);

                // Benefits
                Cell cellBenefits = row.createCell(3);
                cellBenefits.setCellValue(policy.getBenefits());
                cellBenefits.setCellStyle(dataStyle);
                
                // Wrap text for benefits column
                dataStyle.setWrapText(true);
            }

            // Write to byte stream using buffered output
            try (BufferedOutputStream bufferedOut = new BufferedOutputStream(byteStream)) {
                workbook.write(bufferedOut);
                bufferedOut.flush();
            }
        } finally {
            workbook.close();
        }

        return byteStream.toByteArray();
    }

    private static CellStyle createHeaderCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        
        // Set background color (light blue)
        style.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Set font
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);

        // Set borders
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);

        // Set alignment
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        return style;
    }

    private static CellStyle createDataCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        
        // Set borders
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);

        // Set alignment
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.TOP);

        return style;
    }

    private static CellStyle createCurrencyCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        
        // Set currency format
        style.setDataFormat(workbook.createDataFormat().getFormat("Rs#,##0.00"));

        // Set borders
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);

        // Set alignment
        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        return style;
    }
}
