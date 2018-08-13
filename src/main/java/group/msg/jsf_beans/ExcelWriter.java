package group.msg.jsf_beans;


import group.msg.entities.Bug;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.activation.MimetypesFileTypeMap;
import java.io.*;
import java.util.List;

public class ExcelWriter implements Serializable{

    private static String[] columns = {"Id", "Title", "Description", "Version", "Fixed in version", "Target date", "Severity type",
            "Created by", "Assigned user", "Status Type", "Notification"};

    private FileOutputStream fileOut;
    private File excelPath;

    public void createExcel(List<Bug> bugs, String filename) throws IOException {
        // Create a Workbook
        Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file

        /* CreationHelper helps us create instances of various things like DataFormat,
           Hyperlink, RichTextString etc, in a format (HSSF, XSSF) independent way */
        CreationHelper createHelper = workbook.getCreationHelper();

        // Create a Sheet
        Sheet sheet = workbook.createSheet("Bug");

        // Create a Font for styling header cells
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.RED.getIndex());

        // Create a CellStyle with the font
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        // Create a Row
        Row headerRow = sheet.createRow(0);

        // Create cells
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }

        // Create Cell Style for formatting Date
        CellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));

        // Create Other rows and cells with employees data
        int rowNum = 1;
        for (Bug bug : bugs) {
            try {
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(bug.getId());
                row.createCell(1).setCellValue(bug.getTitle());
                row.createCell(2).setCellValue(bug.getDescription());
                row.createCell(3).setCellValue(bug.getVersion());
                row.createCell(4).setCellValue(bug.getFixedInVersion());

                Cell targetDate = row.createCell(5);
                targetDate.setCellValue(bug.getTargetDate());
                targetDate.setCellStyle(dateCellStyle);

                row.createCell(6).setCellValue(bug.getSeverityType().toString());
                row.createCell(7).setCellValue(bug.getCreatedBy().getUsername());
                row.createCell(8).setCellValue(bug.getAssignedTo().getUsername());
                row.createCell(9).setCellValue(bug.getStatusType().toString());
                row.createCell(10).setCellValue(bug.getNotification().getNotificationType().toString());
            }catch (NullPointerException e){
                e.printStackTrace();
            }

        }
        // Resize all columns to fit the content size
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write the output to a file
        excelPath = new File(filename +".xlsx");
        fileOut = new FileOutputStream(excelPath);
        workbook.write(fileOut);
        fileOut.close();

        // Closing the workbook
        workbook.close();

    }


    public StreamedContent downloadAttachment() throws IOException {

        InputStream stream = new FileInputStream(excelPath.getAbsolutePath());

        String contentType=new MimetypesFileTypeMap().getContentType(excelPath);

        String extension=FilenameUtils.getExtension(excelPath.getName());

        return new DefaultStreamedContent(stream, contentType, "Downloaded_excel_bug."+extension);
    }

}