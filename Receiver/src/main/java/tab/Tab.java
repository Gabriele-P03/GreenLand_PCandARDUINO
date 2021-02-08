package tab;


import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.util.Calendar;
import java.util.Scanner;
import java.util.TimeZone;

/**
 * This class contains static method to fill the xlsx file which contains
 * every daily average of the surveys months-bt-months.
 * Each file contains 12 months of surveys, one-for-sheet as said below
 *
 * @author GABRIELE
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

    private static void checkExcelFile() throws IOException {
        int lenght = workbook.getNumberOfSheets();
        for(int i = 0; i < lenght; i++){
            if(!workbook.getSheetAt(i).getSheetName().equals(months[i])){
                allowDelete();
                for (i = 0; i < lenght; i++){
                    //Every time it remove a sheet, workbook's sheets' size decrease.
                    //So it has to delete always the first sheet
                    workbook.removeSheetAt(0);
                }
                lenght = 0;
                break;
            }
        }
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

    private static void setSheet(int sheetIndex) {
        Row row = workbook.getSheetAt(sheetIndex).createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("temperature");
        cell = row.createCell(1);
        cell.setCellValue("humidity");
        cell = row.createCell(2);
        cell.setCellValue("light");
    }

    private static void allowDelete(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Seems like a sheet name is wrong!\n" +
                "Edit by your hand or let me do it, but I will delete all them...\n" +
                "I suggest you, above all if the file is big, to edit the wrong name and to re-run this program\n");
        String allow;
        do{
            System.out.println("Insert y/Y to allow deleting else close this program");
            allow = scanner.next();
        }while (!allow.equals("y") && !allow.equals("Y"));
    }

    public static File getFile() {
        return file;
    }

    private static String getMonth(){ return String.valueOf(Calendar.getInstance().get(Calendar.MONTH));}

    private static String getYear(){ return String.valueOf(Calendar.getInstance().get(Calendar.YEAR));}

    public static boolean isFlagReady() {
        return flagReady; }
}
