package tab;

import org.apache.poi.ss.usermodel.*;
import java.io.*;
import java.util.Calendar;
import java.util.Scanner;


/**
 * This class contains static method to fill the xlsx file which contains
 * every daily average of the surveys.
 * Each file contains 12 months of surveys (one-for-sheet).
 *
 * Before to make available the spreadsheet, it will check every sheet name,
 * if at least one is wrong, every sheet will be deleted and recreate...
 * O course this program will wait that the user be agreed.
 *
 * I suggest, as the program say, to edit by your hand the wrong name and
 * rerun this program.
 * If you cannot do it, please make a backup of the spreadsheet with the
 * wrong sheet name and let this program delete the file
 *
 * @author GABRIELE-P03
 * @gitHub https://github.com/Gabriele-P03/GreenLand_PCandARDUINO/tree/master/Receive
 */
public class Tab {

    public static final String months[] = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};

    private static File file = null;

    private static boolean flagReady = false;
    private static FileInputStream fis = null ;
    private static FileOutputStream fos = null;
    private static Workbook workbook = null;
    private static Sheet[] sheets = new Sheet[12];


    public static void instance(){
        file = new File("resources", getYear() + ".xlsx");
        if(!file.exists()){
            setNewExcelFile();
        }else{
            try {
                fis = new FileInputStream(file);
                workbook = WorkbookFactory.create(fis);
                checkExcelFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        flagReady = true;
    }

    /**
     * Check a file already created.
     * If there are some wrong sheet's name, it will delete all one
     * and create other 12 new sheets
     *
     * @throws IOException
     */
    private static void checkExcelFile() throws IOException {
        int lenght = workbook.getNumberOfSheets();

        /*
            Check every sheet name and remove if it is wrong
         */
        for(int i = 0; i < lenght; i++){
            if(!workbook.getSheetAt(i).getSheetName().equals(months[i])){
                allowDelete();
                for (i = 0; i < lenght; i++){
                    /*
                        Every time it remove a sheet, workbook's sheets' size decrease.
                        So it has to delete always the first sheet, 'till there's at least one
                     */
                    workbook.removeSheetAt(0);
                }
                lenght = 0;
                break;
            }
        }

        //Create the remaining sheets
        if(lenght < 12){
            for(int i = lenght; i < 12; i++){
                workbook.createSheet(months[i]);
                setSheet(i);
            }
            fos = new FileOutputStream(file);
            workbook.write(fos);
            fos.flush();
            fos.close();
        }
    }

    /**
     * If the excel file called YEAR.xlsx not exists, it will create it.
     */
    private static void setNewExcelFile(){
        try {
            file.createNewFile();
            workbook = WorkbookFactory.create(true);
            for(int i = 0; i < 12; i++){
                workbook.createSheet(months[i]);
                setSheet(i);
            }
            fos = new FileOutputStream(file);
            workbook.write(fos);
            fos.flush();
            workbook.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * It will write, on the first row, what the numbers below represents.
     * As below...
     *  ----------------------------
     * 0|temperature|humidity|light|
     *  ---------------------------
     */
    private static void setSheet(int sheetIndex) {
        Row row = workbook.getSheetAt(sheetIndex).createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("temperature");
        cell = row.createCell(1);
        cell.setCellValue("humidity");
        cell = row.createCell(2);
        cell.setCellValue("light");
    }

    /**
     * 'Cause when a sheet name is wrong it has to delete all sheets and
     * create them, it will before wait that user be agreed
     */
    private static void allowDelete(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Seems like a sheet name is wrong!\n" +
                "Edit by your hand or let me do it, but I will delete all them...\n" +
                "I suggest you, above all if the file is big, to edit the wrong name and to rerun this program\n" +
                "If you cannot edit it, MAKE A BACKUP and rerun me\n");
        String allow;
        do{
            System.out.println("Insert y/Y to allow deleting else close this program");
            allow = scanner.next();
        }while (!allow.equals("y") && !allow.equals("Y"));
    }



    private static String getYear(){ return String.valueOf(Calendar.getInstance().get(Calendar.YEAR));}

    public static boolean isFlagReady() { return flagReady; }
}
