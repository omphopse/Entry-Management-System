import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;

public class UpdateExcelData {
     public static void updateCellInExcel(int sheetIndex, long recordId, int idColumnIndex, int targetCellIndex,
            String cellValue) {
        String filePath = AppConfig.DATA_FILE;
        try {
            File f = new File(filePath);
            if (!f.exists()) {
                System.out.println("Excel file does not exist.");
                return;
            }

            FileInputStream fis = new FileInputStream(f);
            Workbook workbook = new XSSFWorkbook(fis);
            fis.close();

            if (workbook.getNumberOfSheets() <= sheetIndex) {
                System.out.println("Sheet index " + sheetIndex + " does not exist.");
                workbook.close();
                return;
            }

            Sheet sheet = workbook.getSheetAt(sheetIndex);
            DataFormatter formatter = new DataFormatter();

            boolean found = false;
            for (Row row : sheet) {
                if (row.getRowNum() == 0)
                    continue; // skip header

                String idStr = formatter.formatCellValue(row.getCell(idColumnIndex));
                if (idStr == null || idStr.trim().isEmpty())
                    continue;

                try {
                    long id = Long.parseLong(idStr.trim());
                    if (id == recordId) {
                        found = true;
                        Cell cell = row.getCell(targetCellIndex);
                        if (cell == null)
                            cell = row.createCell(targetCellIndex);
                        cell.setCellValue(cellValue != null ? cellValue : "");

                        FileOutputStream fos = new FileOutputStream(filePath);
                        workbook.write(fos);
                        fos.close();
                        break;
                    }
                } catch (NumberFormatException nfe) {

                }
            }

            if (!found) {
                System.out.println("Record with ID " + recordId + " not found in sheet index " + sheetIndex + ".");
            }

            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateVisaApplicationCellInExcel(long applicationId, int cellIndex, String cellValue) {
        updateCellInExcel(3, applicationId, 0, cellIndex, cellValue);
    }
}
