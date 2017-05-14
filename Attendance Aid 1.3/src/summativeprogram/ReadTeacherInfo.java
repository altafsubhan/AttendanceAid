package summativeprogram;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadTeacherInfo {

    private static int lunchCounter = 0;
    private static String folderPath = SplashScreen.getFolderPath();

    /*
     * Input: array containing names of the teachers that are away
     * Post-condition: will read and store the daily schedule of the teachers
     */
    public static String[][] extractTeacherInfo(String[] x) throws FileNotFoundException, IOException {
        // Opens a file with daily schedules of all teachers //
        FileInputStream file = new FileInputStream(new File(folderPath +
                "/Spreadsheets/TeacherSchedules.xlsx"));
        XSSFWorkbook wb = new XSSFWorkbook(file);
        XSSFSheet sheet = wb.getSheetAt(0);

        String[][] awaySchedule = new String[x.length][8];
        int rowCounter = 0, columnCounter;
        Iterator<Row> rowIterator = sheet.iterator();

        while (rowIterator.hasNext()) {
            columnCounter = 0;
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();

            // reads and stores on array the schedules for teachers provided by the user //
            for (int i = 0; i < x.length; i++) {
                if (row.getCell(0).toString().equals(x[i])) {
                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        // checks the cell type and formats accordingly
                        switch (cell.getCellType()) {
                            case Cell.CELL_TYPE_STRING:
                                if (columnCounter == 4 || columnCounter == 5) {
                                    lunchCounter++;
                                    awaySchedule[rowCounter][columnCounter] = cell.getStringCellValue();
                                    break;
                                } else {
                                    awaySchedule[rowCounter][columnCounter] = cell.getStringCellValue();
                                    break;
                                }
                            case Cell.CELL_TYPE_BLANK:
                                awaySchedule[rowCounter][columnCounter] = "spare";
                                break;
                        }
                        columnCounter++;
                    }
                    rowCounter++;
                }
            }
        }
        file.close();

        return awaySchedule;
    }

    /*
     * Post-condition: will return the number of lunch supervisions required
     */
    public static int getLunchCounter() {
        return lunchCounter;
    }
}
