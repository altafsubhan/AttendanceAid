package summativeprogram;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.CellRangeAddressList;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class PrintTimetable {

    private static String sheetName;
    private static String fileName;
    private static XSSFWorkbook onCallsWb;
    private static XSSFWorkbook workbook;
    private static XSSFSheet onCallsSheet;
    private static XSSFSheet sheet;
    private static int noOfAwayTeachers;
    private static FileInputStream onCallsFile;
    private static String period;
    private static String onCallNames;
    private static ArrayList<String> onCallsA = new ArrayList<String>();
    private static ArrayList<String> onCallsB = new ArrayList<String>();
    private static ArrayList<String> onCallsC = new ArrayList<String>();
    private static ArrayList<String> onCallsD = new ArrayList<String>();
    private static ArrayList<String> onCallsE = new ArrayList<String>();
    private static String[] lunchOnCalls = new String[20];
    private static String folderPath = SplashScreen.getFolderPath();

    /*
     * Post-condition: will create an excel file containing the timetable of
     * on-call teachers for the day
     */
    public static void createTimetable() throws IOException {
        // creates a blank workbook //
        workbook = new XSSFWorkbook();

        // creates a blank sheet //
        sheetName = StartScreen.getDate();
        sheet = workbook.createSheet(sheetName);

        createTitleRow();       //writes the titles to the spreadsheet
        openOnCallsFile();      //opens the file with on-calls information

        noOfAwayTeachers = StartScreen.getNoOfAwayTeachers();   //obtains no of away teachers
        String[] awayTeacherNames = StartScreen.getAwayTeacherNames();
        String[][] awaySchedule = new String[noOfAwayTeachers][8];

        String temp;
        Row[] teacherRows = new Row[noOfAwayTeachers * 2];      //creates rows for the spreadsheet
        awaySchedule = ReadTeacherInfo.extractTeacherInfo(awayTeacherNames);    //obtains schedules of teachers that are away
        String day = StartScreen.getDay();      //obtains the current day

        // for creating borders of the cells //
        CellStyle borderStyle = workbook.createCellStyle();
        borderStyle.setBorderBottom(XSSFCellStyle.BORDER_MEDIUM);
        borderStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());

        int lunchCounter = 0;
        int[] counter = new int[5];
        int rowCounter = 0;

        for (int x = 0; x < 5; x++) {
            counter[x] = 0;
        }

        getOnCalls();       //gets the available on-calls
        getLunchOnCalls(ReadTeacherInfo.getLunchCounter());      //obtains the on-calls available for lunch

        // writes on the file schedule of every teachers that is away //
        for (int i = 0; i < noOfAwayTeachers * 2; i += 2) {
            teacherRows[i] = sheet.createRow(i + 1);
            teacherRows[i + 1] = sheet.createRow(i + 2);

            // goes through every period //
            for (int j = 0; j < 8; j++) {
                // for writing the names of the teachers that are away //
                if (j == 0) {
                    Cell cell = teacherRows[i].createCell(j);
                    Cell cell2 = teacherRows[i + 1].createCell(j);

                    // for aligning the name //
                    CellStyle nameStyle = workbook.createCellStyle();
                    nameStyle.setVerticalAlignment(nameStyle.VERTICAL_CENTER);
                    cell.setCellStyle(nameStyle);
                    cell2.setCellStyle(borderStyle);

                    // for merging cells //
                    cell.setCellValue(awaySchedule[rowCounter][j]);
                    sheet.addMergedRegion(new CellRangeAddress(i + 1, i + 2, 0, 0));

                    sheet.autoSizeColumn(j);    //for adjusting the size of the column
                } // if the away teacher has a class in period A //
                else if (j == 1) {
                    Cell cell = teacherRows[i].createCell(j);
                    Cell onCallCell = teacherRows[i + 1].createCell(j);

                    // leaves the cell blank if it is a spare period //
                    if (awaySchedule[rowCounter][j].equals("spare")) {
                        cell.setCellValue("");
                        onCallCell.setCellStyle(borderStyle);
                        sheet.autoSizeColumn(j);
                    } // writes down the course code if there is a class //
                    else {
                        cell.setCellValue(awaySchedule[rowCounter][j]);
                        temp = onCallsA.get(counter[0]);    //obtains the available on-call teacher name
                        onCallCell.setCellValue(temp);      //writes the name on the file
                        onCallCell.setCellStyle(borderStyle);
                        onCallsA.remove(counter[0]);    //removes the name from the array storage
                        counter[0]++;
                    }
                } // if the away teacher has a class in period B - repeats procedure //
                else if (j == 2) {
                    Cell cell = teacherRows[i].createCell(j);
                    Cell onCallCell = teacherRows[i + 1].createCell(j);

                    if (awaySchedule[rowCounter][j].equals("spare")) {
                        cell.setCellValue("");
                        onCallCell.setCellStyle(borderStyle);
                        sheet.autoSizeColumn(j);
                    } else {
                        cell.setCellValue(awaySchedule[rowCounter][j]);
                        temp = onCallsB.get(counter[1]);
                        onCallCell.setCellValue(temp);
                        onCallCell.setCellStyle(borderStyle);
                        onCallsB.remove(counter[1]);
                        counter[1]++;
                    }
                } // if the away teacher has a class in period C - repeats procedure //
                else if (j == 3) {
                    Cell cell = teacherRows[i].createCell(j);
                    Cell onCallCell = teacherRows[i + 1].createCell(j);

                    if (awaySchedule[rowCounter][j].equals("spare")) {
                        cell.setCellValue("");
                        onCallCell.setCellStyle(borderStyle);
                        sheet.autoSizeColumn(j);
                    } else {
                        cell.setCellValue(awaySchedule[rowCounter][j]);
                        temp = onCallsC.get(counter[2]);
                        onCallCell.setCellValue(temp);
                        onCallCell.setCellStyle(borderStyle);
                        onCallsC.remove(counter[2]);
                        counter[2]++;
                    }
                } // for lunch supervision //
                else if ((j == 4) || (j == 5)) {
                    Cell cell = teacherRows[i].createCell(j);
                    Cell onCallCell = teacherRows[i + 1].createCell(j);

                    // checks if the away teacher has lunch supervision on the current day //
                    if (awaySchedule[rowCounter][j].endsWith("(M)") && (day.equals("Mon"))) {

                        // assigns an on-call if true //
                        cell.setCellValue(awaySchedule[rowCounter][j]);
                        onCallCell.setCellValue(lunchOnCalls[lunchCounter]);
                        lunchCounter++;

                        // proceudre repeated for other days //
                    } else if (awaySchedule[rowCounter][j].endsWith("(T)") && (day.equals("Tue"))) {

                        cell.setCellValue(awaySchedule[rowCounter][j]);
                        onCallCell.setCellValue(lunchOnCalls[lunchCounter]);
                        lunchCounter++;

                    } else if (awaySchedule[rowCounter][j].endsWith("(W)") && (day.equals("Wed"))) {

                        cell.setCellValue(awaySchedule[rowCounter][j]);
                        onCallCell.setCellValue(lunchOnCalls[lunchCounter]);
                        lunchCounter++;

                    } else if (awaySchedule[rowCounter][j].endsWith("(Th)") && (day.equals("Thu"))) {

                        cell.setCellValue(awaySchedule[rowCounter][j]);
                        onCallCell.setCellValue(lunchOnCalls[lunchCounter]);
                        lunchCounter++;

                    } else if (awaySchedule[rowCounter][j].endsWith("(F)") && (day.equals("Fri"))) {

                        cell.setCellValue(awaySchedule[rowCounter][j]);
                        onCallCell.setCellValue(lunchOnCalls[lunchCounter]);
                        lunchCounter++;

                    }
                    onCallCell.setCellStyle(borderStyle);
                    sheet.autoSizeColumn(j);
                } // if the away teacher has a class in period D - repeats procedure //
                else if (j == 6) {
                    Cell cell = teacherRows[i].createCell(j);
                    Cell onCallCell = teacherRows[i + 1].createCell(j);

                    if (awaySchedule[rowCounter][j].equals("spare")) {
                        cell.setCellValue("");
                        onCallCell.setCellStyle(borderStyle);
                        sheet.autoSizeColumn(j);
                    } else {
                        cell.setCellValue(awaySchedule[rowCounter][j]);
                        temp = onCallsD.get(counter[3]);
                        onCallCell.setCellValue(temp);
                        onCallCell.setCellStyle(borderStyle);
                        onCallsD.remove(counter[3]);
                        counter[3]++;
                    }
                } // if the away teacher has a class in period E - repeats procedure //
                else if (j == 7) {
                    Cell cell = teacherRows[i].createCell(j);
                    Cell onCallCell = teacherRows[i + 1].createCell(j);

                    if (awaySchedule[rowCounter][j].equals("spare")) {
                        cell.setCellValue("");
                        onCallCell.setCellStyle(borderStyle);
                        sheet.autoSizeColumn(j);
                    } else {
                        cell.setCellValue(awaySchedule[rowCounter][j]);
                        temp = onCallsE.get(counter[4]);
                        onCallCell.setCellValue(temp);
                        onCallCell.setCellStyle(borderStyle);
                        onCallsE.remove(counter[4]);
                        counter[4]++;
                    }
                }
                sheet.autoSizeColumn(j);    // adjusts the column size
            }
            rowCounter++;
        }

        fileName = folderPath + "/Timetables/" + StartScreen.getDate() + ".xlsx";  // sets the file name

        // writes the workbook to a file //
        FileOutputStream out = new FileOutputStream(new File(fileName));
        workbook.write(out);
        out.close();
        onCallsFile.close();
    }

    /*
     * Post-condition: will return the name of the file created
     */
    public static String getFileName() throws IOException {
        fileName = StartScreen.getDate() + ".xlsx";
        return fileName;
    }

    /*
     * Post-condition: will add on-calls available for each period to the
     * respective array
     */
    public static void getOnCalls() {
        int i = 1;

        // obtains on-calls available for every period and saves on arrays //
        while (i < onCallsSheet.getLastRowNum()) {
            period = onCallsSheet.getRow(i).getCell(9).toString();
            onCallNames = onCallsSheet.getRow(i).getCell(0).toString();
            if (period.equals("A")) {
                onCallsA.add(onCallNames);
            } else if (period.equals("B")) {
                onCallsB.add(onCallNames);
            } else if (period.equals("C")) {
                onCallsC.add(onCallNames);
            } else if (period.equals("D")) {
                onCallsD.add(onCallNames);
            } else if (period.equals("E")) {
                onCallsE.add(onCallNames);
            }
            i++;
        }
    }

    /*
     * Input: number of lunch supervisions required
     * Post-condition: will add required number of on-call teachers to the array
     * for lunch supervisions
     */
    public static void getLunchOnCalls(int x) {
        int counter = 0;

        for (int i = onCallsSheet.getLastRowNum(); i > onCallsSheet.getLastRowNum() - x; i--) {
            lunchOnCalls[counter] = onCallsSheet.getRow(i).getCell(0).toString();
            counter++;
        }
    }

    /*
     * Post-condition: will set CellStyle for the title cells
     */
    public static void setTitleText(Cell x) {
        XSSFCellStyle style = workbook.createCellStyle();
        XSSFFont titleFont = workbook.createFont();
        titleFont.setFontHeight(14);
        titleFont.setItalic(true);
        style.setFont(titleFont);
        style.setFillForegroundColor(IndexedColors.BLUE.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        x.setCellStyle(style);
    }

    /*
     * Post-condition: will open the file containing information for on-call
     * teachers
     */
    public static void openOnCallsFile() throws FileNotFoundException, IOException {
        onCallsFile = new FileInputStream(new File(folderPath +
                "/Spreadsheets/OnCalls_Winter_2015_16.xlsx"));
        onCallsWb = new XSSFWorkbook(onCallsFile);
        onCallsSheet = onCallsWb.getSheetAt(0);
    }

    /*
     * Post-condition: will create the title row on the spreadsheet
     */
    public static void createTitleRow() {
        String[] titles = {"TEACHERS:", "PERIOD A:", "PERIOD B:",
            "PERIOD C:", "LUNCH (1ST):", "LUNCH (2ND):", "PERIOD D:", "PERIOD E:"};
        Row row1 = sheet.createRow(0);
        for (int i = 0; i
                < 8; i++) {
            Cell cell = row1.createCell(i);
            cell.setCellValue(titles[i]);
            setTitleText(
                    cell);
        }
    }
}
