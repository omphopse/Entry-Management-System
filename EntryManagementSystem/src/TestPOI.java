import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;

public class TestPOI {
    public static void main(String[] args) throws Exception {

        FileInputStream fis = new FileInputStream(AppConfig.DATA_FILE);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(1);

        for (Row row : sheet) {
            for (Cell cell : row) {
                System.out.print(cell + "\t");
            }
            System.out.println();
        }

        workbook.close();
        fis.close();
    }
}
