package tab;

import org.apache.poi.ss.usermodel.*;

import java.io.*;

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
                    setWriterPointer(year, month, day);

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

    private static void setWriterPointer(String year, String month, String day) throws IOException {
        selectWorkbook();
        selectSheet(month);
        selectRow(Integer.valueOf(day));
    }


    private static void selectWorkbook() throws IOException { workbook = WorkbookFactory.create(fisExcel); }

    private static void selectSheet(String month) { sheet = workbook.getSheet(Tab.months[Integer.valueOf(month)-1]); }

    //Give -1 to create a new row
    private static void selectRow(int rowIndex){
        row = sheet.createRow(rowIndex);}


    private static void allowEditing() {
        if(!Tab.isFlagReady()){
            throw new IllegalStateException("Trying to use a file unavailable...\nMake sure to have called Tab#instance() before");
        }
    }
}
