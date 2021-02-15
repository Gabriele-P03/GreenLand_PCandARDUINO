package tab;

import org.apache.poi.ss.usermodel.*;
import java.io.*;


/**
 * This class contains some static methods useful
 * to edit the spreadsheet
 *
 * @author GABRIELE-P03
 * @gitHub https://github.com/Gabriele-P03/GreenLand_PCandARDUINO/tree/master/Receive
 */
public class TabUtils {

    private static File file = new File("resources", "surveys.txt");
    private static FileReader fr = null;
    private static BufferedReader br = null;

    private static File excelFile = null;
    private static FileInputStream fisExcel = null;
    private static FileOutputStream fosExcel = null;

    private static Workbook workbook = null;
    private static Sheet sheet = null;
    private static Row row = null;
    private static Cell cell = null;

    private static String year, month, day;

    private static boolean setReaders() throws FileNotFoundException {
        if (file.exists()) {
            fr = new FileReader(file);
            br = new BufferedReader(fr);
            return true;
        }
        return false;
    }

    /**
     * Called when a new inputStream reading has been done.
     * It will store data from text file onto spreadsheet
     */
    public static void append(){
        allowEditing();
        try{
            if(!setReaders()){
                return;
            }
            String buffer, temperature, humidity, light;

            while((buffer = br.readLine()) != null) {
                if(!buffer.equals("")) {
                    divideDate(buffer);

                    if ((temperature = br.readLine()) != null) {
                    }
                    if ((humidity = br.readLine()) != null) {
                    }
                    if ((light = br.readLine()) != null) {
                    }

                    excelFile = new File("resources", year + ".xlsx");
                    fisExcel = new FileInputStream(excelFile);
                    setWriterPointer(month, day);

                    cell = row.createCell(0);
                    cell.setCellValue(temperature);
                    cell = row.createCell(1);
                    cell.setCellValue(humidity);
                    cell = row.createCell(2);
                    cell.setCellValue(light);

                    fosExcel = new FileOutputStream(excelFile);
                    fosExcel.flush();
                    workbook.write(fosExcel);
                }
            }
            br.close();
            fr.close();
            //file.delete();

        }catch (IOException e){
            e.printStackTrace();
        }

    }

    /**
     * Given a data, such as 01-01-2021, it will divide it
     * DAY= 01
     * MONTH = 01
     * YEAR = 2021
     *
     * Get the two index whose represents the dashes inside the
     * string and then divide it
     *
     * @param buffer
     */
    private static void divideDate(String buffer) {
        int x = 0, y = 0, index = 0;
        for(int i = 0; i < 2; i++){
            while(buffer.toCharArray()[index++] != '-'){}
            x = y;
            y = index;
        }
        day = buffer.substring(0, x-1);
        month = buffer.substring(x, y-1);
        year = buffer.substring(y);
    }

    /**
     * Select a workbook, the right sheet(as the month) and the right row(as the day)
     *
     * @param month
     * @param day
     * @throws IOException
     */
    private static void setWriterPointer(String month, String day) throws IOException {
        selectWorkbook();
        selectSheet(month);
        selectRow(Integer.valueOf(day));
    }


    private static void selectWorkbook() throws IOException { workbook = WorkbookFactory.create(fisExcel); }

    private static void selectSheet(String month) { sheet = workbook.getSheet(Tab.months[Integer.valueOf(month)-1]); }

    private static void selectRow(int rowIndex){ row = sheet.createRow(rowIndex);}


    /**
     * Check if the file is ready to be edited, if not throw a new IllegalState
     *
     * @see Tab
     */
    private static void allowEditing() {
        if(!Tab.isFlagReady()){
            throw new IllegalStateException("Trying to use a file unavailable...\nMake sure to have called Tab#instance() before");
        }
    }
}
