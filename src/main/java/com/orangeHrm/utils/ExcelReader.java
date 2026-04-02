package com.orangeHrm.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReader {

    private static String stubFileName = "src/test/resources/TestStub.xlsx";
    private static Workbook stubWorkbook;
    private static Sheet sheet;
    private static String sheetName;
    private boolean isSheetOpen = false;
    private boolean isSheetClosable= true;
    //WebDriver driver;


    public ExcelReader() {
        intiateWorkbook();
        //this.driver = Driver.getInstance();
    }

    public void intiateWorkbook() {
        try {
            if(!isSheetOpen){
                stubWorkbook = new XSSFWorkbook(new FileInputStream(new File(stubFileName)));
                isSheetOpen = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeWorkbook() {
        try {
            if(isSheetOpen && isSheetClosable){
                stubWorkbook.close();
                isSheetOpen = false;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeWorkbook() {
        try {
            if(isSheetOpen && isSheetClosable){
                Sheet tempSheet = getSheet();
                stubWorkbook.removeSheetAt(stubWorkbook.getSheetIndex(sheetName));
                Sheet newSheet = stubWorkbook.createSheet(sheetName);
                newSheet = copySheet(tempSheet, newSheet);
                sheet = newSheet;
                stubWorkbook.write(new FileOutputStream(new File(stubFileName)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Sheet copySheet(Sheet source, Sheet target){
        for(int i=0;i<source.getPhysicalNumberOfRows();i++){
            Row currRow = source.getRow(i);
            Row newRow = target.createRow(i);
            for(int j=0;j<currRow.getLastCellNum();j++){
                Cell currCell = currRow.getCell(j);
                Cell newCell = newRow.createCell(j);
                if(null != currCell && currCell.getCellType() == CellType.STRING){
                    //System.out.print(currCell.getStringCellValue()+"\t");
                    newCell.setCellValue(currCell.getStringCellValue());
                }else{
                    //System.out.print("Blank\t");
                }

            }
            //System.out.println();
        }
        return target;
    }

    private void printSheet(Sheet sheet){
        for(int i=0;i<sheet.getPhysicalNumberOfRows();i++){
            Row currRow = sheet.getRow(i);
            for(int j=0;j<currRow.getLastCellNum();j++){
                Cell currCell = currRow.getCell(j);
                if(null != currCell && currCell.getCellType() == CellType.STRING){
                    System.out.print(currCell.getStringCellValue()+"\t");
                }else{
                    System.out.print("Blank\t");
                }

            }
            System.out.println();
        }
    }

    private void printSheet(){
        for(int i=0;i<sheet.getPhysicalNumberOfRows();i++){
            Row currRow = sheet.getRow(i);
            for(int j=0;j<currRow.getLastCellNum();j++){
                Cell currCell = currRow.getCell(j);
                if(null != currCell && currCell.getCellType() == CellType.STRING){
                    System.out.print(currCell.getStringCellValue()+"\t");
                }else{
                    System.out.print("Blank\t");
                }

            }
            System.out.println();
        }
    }
    private void printWorkbook(){
        for(int k = 0;k<stubWorkbook.getNumberOfSheets();k++){
            Sheet sheet = stubWorkbook.getSheetAt(k);
            System.out.println("Sheet name is "+sheet.getSheetName());
            for(int i=0;i<sheet.getPhysicalNumberOfRows();i++){
                Row currRow = sheet.getRow(i);
                for(int j=0;j<currRow.getLastCellNum();j++){
                    Cell currCell = currRow.getCell(j);
                    if(null != currCell && currCell.getCellType() == CellType.STRING){
                        System.out.print(currCell.getStringCellValue()+"\t");
                    }else{
                        System.out.print("Blank\t");
                    }

                }
                System.out.println();
            }
        }

    }

    public Sheet getSheet(){
        if(sheet == null){
            if(null == sheetName){
                System.out.println("Sheet name has not been set or not existing");
            }else{
                sheet = stubWorkbook.getSheet(sheetName);
            }
        }
        return sheet;
    }

    /**
     * Reusable method to safely get string value from a cell
     * @param cell The cell to extract value from
     * @return String value of the cell, or empty string if null
     */
    private String getCellStringValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        try {
            cell.setCellType(CellType.STRING);
            return cell.getStringCellValue();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Reusable method to find a row by its header value (first column)
     * @param headerValue The value to search for in the first column
     * @return Row object if found, null otherwise
     */
    private Row getRowByHeader(String headerValue) {
        if (sheet == null) {
            return null;
        }
        for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
            Row currRow = sheet.getRow(i);
            if (currRow == null) continue;
            Cell firstCell = currRow.getCell(0);
            String cellValue = getCellStringValue(firstCell);
            if (cellValue != null && cellValue.equals(headerValue)) {
                return currRow;
            }
        }
        return null;
    }

    /**
     * Reusable method to find a column index by its header name (in first row)
     * @param headerName The column header name to search for
     * @return Column index if found, -1 otherwise
     */
    private int getColumnIndexByHeader(String headerName) {
        if (sheet == null || sheet.getRow(0) == null) {
            return -1;
        }
        Row headerRow = sheet.getRow(0);
        for (int j = 0; j < headerRow.getLastCellNum(); j++) {
            Cell cell = headerRow.getCell(j);
            String cellValue = getCellStringValue(cell);
            if (cellValue != null && cellValue.equals(headerName)) {
                return j;
            }
        }
        return -1;
    }

    /**
     * Reusable method to find a value in a specific row and return its column index
     * @param row The row to search in (starting from column 1)
     * @param value The value to search for
     * @return Column index if found, -1 otherwise
     */
    private int findValueIndexInRow(Row row, String value) {
        if (row == null) {
            return -1;
        }
        for (int j = 1; j < row.getLastCellNum(); j++) {
            Cell cell = row.getCell(j);
            String cellValue = getCellStringValue(cell);
            if (cellValue != null && cellValue.equals(value)) {
                return j;
            }
        }
        return -1;
    }

    public void addSheetIfNotExisting(String sheetName) {
        ExcelReader.sheetName = sheetName;
        intiateWorkbook();
        
        // Check if sheet already exists
        for (int i = 0; i < stubWorkbook.getNumberOfSheets(); i++) {
            Sheet s = stubWorkbook.getSheetAt(i);
            if (s.getSheetName().equalsIgnoreCase(sheetName)) {
                return; // Sheet already exists
            }
        }
        
        // Create new sheet
        stubWorkbook.createSheet(sheetName);
        ExcelReader.sheetName = sheetName;
        ExcelReader.sheet = stubWorkbook.getSheet(sheetName);
        writeWorkbook();
        closeWorkbook();
        clearSheet(sheet);
    }

    /**
     * Get all column headers from the first row of the current sheet.
     * Useful for debugging and understanding available columns.
     * @return List of column header names
     */
    public java.util.List<String> getAllColumnHeaders() {
        java.util.List<String> headers = new java.util.ArrayList<>();
        if (sheet == null || sheet.getRow(0) == null) {
            return headers;
        }
        Row headerRow = sheet.getRow(0);
        for (int j = 0; j < headerRow.getLastCellNum(); j++) {
            Cell cell = headerRow.getCell(j);
            String cellValue = getCellStringValue(cell);
            if (cellValue != null) {
                headers.add(cellValue);
            }
        }
        return headers;
    }

    public void clearSheet() {
        intiateWorkbook();
        sheet = getSheet();
        for (int rowIndex = sheet.getPhysicalNumberOfRows() - 1; rowIndex >= 0; rowIndex--) {
            sheet.removeRow(sheet.getRow(rowIndex));
        }
        writeWorkbook();
        closeWorkbook();
    }

    public void clearSheet(Sheet sheet) {
        intiateWorkbook();
        sheet = getSheet();
        for (int rowIndex = sheet.getPhysicalNumberOfRows() - 1; rowIndex >= 0; rowIndex--) {
            sheet.removeRow(sheet.getRow(rowIndex));
        }
        writeWorkbook();
        closeWorkbook();
    }

    public void addData(String header, String value) {
        addData(header, value, true);
    }

    public void addData(String header, String value, boolean append) {
        intiateWorkbook();
        sheet = getSheet();
        Row row = getRowByHeader(header);
        
        if (row != null) {
            // Header exists, find next empty cell or append
            int cellIndex = 1;
            while (row.getCell(cellIndex) != null) {
                cellIndex++;
            }
            if (append) {
                row.createCell(cellIndex).setCellValue(value);
            } else {
                if (cellIndex != 1) {
                    cellIndex--;
                }
                row.createCell(cellIndex).setCellValue(value);
            }
        } else {
            // Header doesn't exist, create new row
            Row lastRow = sheet.createRow(sheet.getPhysicalNumberOfRows());
            lastRow.createCell(0).setCellValue(header);
            lastRow.createCell(1).setCellValue(value);
        }
        
        writeWorkbook();
        closeWorkbook();
    }

    public void addData(String header, String value, int pos) {
        intiateWorkbook();
        sheet = getSheet();
        Row row = getRowByHeader(header);
        
        if (row != null) {
            // Header exists, add data at specified position
            Cell cell = row.getCell(pos);
            if (cell != null) {
                cell.setCellValue(value);
            } else {
                // Create empty cells up to position
                int cellIndex = 1;
                while (row.getCell(cellIndex) != null) {
                    cellIndex++;
                }
                // Fill gaps
                while (cellIndex < pos) {
                    row.createCell(cellIndex++);
                }
                row.createCell(pos).setCellValue(value);
            }
        } else {
            // Header doesn't exist, create new row
            Row lastRow = sheet.createRow(sheet.getPhysicalNumberOfRows());
            lastRow.createCell(0).setCellValue(header);
            lastRow.createCell(1).setCellValue(value);
        }
        
        writeWorkbook();
        closeWorkbook();
    }

    public boolean setReadingSheet(String sheetName){
        intiateWorkbook();
        boolean isSheetExisting = false;
        for(int i=0;i<stubWorkbook.getNumberOfSheets();i++){
            Sheet s = stubWorkbook.getSheetAt(i);
            if(s.getSheetName().equalsIgnoreCase(sheetName)){
                isSheetExisting = true;
                ExcelReader.sheetName = sheetName;
                sheet = null;
                break;
            }
        }
        closeWorkbook();
        if(!isSheetExisting){
            ExcelReader.sheetName = null;

        }
        return isSheetExisting;
    }

    public String getData(String header, int testDataNum) {
        intiateWorkbook();
        sheet = getSheet();
        Row row = getRowByHeader(header);
        if (row != null) {
            String data = extractCellData(row.getCell(testDataNum));
            closeWorkbook();
            return data;
        }
        closeWorkbook();
        return null;
    }


    private String extractCellData(Cell cell){
        String cellContent=null;
        cell.setCellType(CellType.STRING);
        return cell.getStringCellValue();
		/*switch(cell.getCellTypeEnum()){
		case STRING:
			cellContent = cell.getStringCellValue();
			break;
		case BOOLEAN:
			cellContent = String.valueOf(cell.getBooleanCellValue());
			break;
		case NUMERIC:
			double d = cell.getNumericCellValue();
			long ll = (long)d;
			if(d == ll){
				cellContent = String.valueOf(d);
			}else{
				cellContent = String.valueOf(ll);
			}
			break;
		default:
			System.out.println("Invalid data found in "+cell.getAddress());
		}
		return cellContent;*/
    }

    public List<String> getAllValuesForHeader(String header) {
        intiateWorkbook();
        List<String> allData = new ArrayList<String>();
        sheet = getSheet();
        Row headerRow = getRowByHeader(header);
        if (headerRow != null) {
            allData.add(header);
            for (int j = 1; j < headerRow.getLastCellNum(); j++) {
                Cell cell = headerRow.getCell(j);
                String cellValue = getCellStringValue(cell);
                if (cellValue != null) {
                    allData.add(cellValue);
                }
            }
        }
        closeWorkbook();
        return allData;
    }

    public boolean checkForPresenceOfValueForHeader(String header, String value) {
        intiateWorkbook();
        sheet = getSheet();
        try {
            Row row = getRowByHeader(header);
            if (row != null) {
                int valueIndex = findValueIndexInRow(row, value);
                closeWorkbook();
                return valueIndex != -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeWorkbook();
        return false;
    }
    public String getValueAgainstAnotherValue(String primeHeader, String primeValue, String targetHeader){
        intiateWorkbook();
        sheet = getSheet();
        try {
            // Find the column index where primeValue is located
            Row primeRow = getRowByHeader(primeHeader);
            if (primeRow == null) {
                closeWorkbook();
                return null;
            }
            int targetValueIndex = findValueIndexInRow(primeRow, primeValue);
            if (targetValueIndex == -1) {
                closeWorkbook();
                return null;
            }
            
            // Find the target row and get value at the same column index
            Row targetRow = getRowByHeader(targetHeader);
            if (targetRow != null) {
                Cell targetValueCell = targetRow.getCell(targetValueIndex);
                if (targetValueCell != null) {
                    String targetValue = getCellStringValue(targetValueCell);
                    closeWorkbook();
                    return targetValue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeWorkbook();
        return null;
    }



    private int getValueIndexForHeader(String header, String value){
        Row row = getRowByHeader(header);
        if (row == null) {
            return -1;
        }
        return findValueIndexInRow(row, value);
    }

    public String setValueAgainstAnotherValue(String primeHeader, String primeValue, String targetHeader, String targetValue){
        intiateWorkbook();
        sheet = getSheet();
        int targetValueIndex = 0;
        boolean isPrimeValueFound = false;
        boolean isTargetHeaderFound = false;
        
        try {
            // Find where primeValue is located in primeHeader row
            Row primeRow = getRowByHeader(primeHeader);
            if (primeRow != null) {
                targetValueIndex = findValueIndexInRow(primeRow, primeValue);
                isPrimeValueFound = (targetValueIndex != -1);
            }
            
            // If prime value not found, add it
            if (!isPrimeValueFound) {
                isSheetClosable = false;
                addData(primeHeader, primeValue, true);
                isSheetClosable = true;
                primeRow = getRowByHeader(primeHeader);
                if (primeRow != null) {
                    targetValueIndex = findValueIndexInRow(primeRow, primeValue);
                }
            }
            
            // Find target row and set value
            Row targetRow = getRowByHeader(targetHeader);
            if (targetRow != null) {
                isTargetHeaderFound = true;
                Cell targetValueCell = targetRow.getCell(targetValueIndex);
                if (targetValueCell != null) {
                    targetValue = getCellStringValue(targetValueCell);
                } else {
                    // Create cells if needed
                    for (int j = 1; j <= targetValueIndex; j++) {
                        Cell cell = targetRow.getCell(j);
                        if (cell == null) {
                            targetRow.createCell(j);
                        }
                    }
                    targetRow.getCell(targetValueIndex).setCellValue(targetValue);
                }
            } else {
                // Create new row if target header doesn't exist
                Row lastRow = sheet.createRow(sheet.getPhysicalNumberOfRows());
                lastRow.createCell(0).setCellValue(targetHeader);
                for (int j = 1; j <= targetValueIndex; j++) {
                    lastRow.createCell(j);
                }
                lastRow.getCell(targetValueIndex).setCellValue(targetValue);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        writeWorkbook();
        closeWorkbook();
        return targetValue;
    }

    public String setSheetAndGetData(String sheetName, String header, int colIndex){

        setReadingSheet(sheetName);
        return getData(header,colIndex);
    }


    /**
     * Read data using row header and column header name
     * @param rowHeader The row identifier (first column value)
     * @param colHeader The column header name
     * @return Data value at intersection of row and column
     */
    public String getData(String rowHeader, String colHeader) {
        intiateWorkbook();
        sheet = getSheet();
        int colIndex = getColumnIndexByHeader(colHeader);
        if (colIndex == -1) {
            closeWorkbook();
            return null;
        }
        Row row = getRowByHeader(rowHeader);
        if (row != null) {
            String data = extractCellData(row.getCell(colIndex));
            closeWorkbook();
            return data;
        }
        closeWorkbook();
        return null;
    }


    public int getValueIndexForHeaderCell(String headerCellValue){
        return getColumnIndexByHeader(headerCellValue);
    }



    public String getFirstCellData() {
        intiateWorkbook();
        String data = null;
        sheet = getSheet();
        Row currRow = sheet.getRow(0);
        String rowHeader = currRow.getCell(0).getStringCellValue();

        closeWorkbook();
        return rowHeader;
    }

    //Sushant

    public int getLastColNum(){

        intiateWorkbook();
        sheet = getSheet();
        Row currRow = sheet.getRow(0);
        return currRow.getPhysicalNumberOfCells();
        //	return currRow.getLastCellNum() ;

    }

    //sushant

    public String getcellData(int rownum,int celNum){

        intiateWorkbook();
        String data = null;
        sheet = getSheet();
        Row currRow = sheet.getRow(rownum);
        data = currRow.getCell(celNum).getStringCellValue();

        closeWorkbook();
        return data;

    }

}
