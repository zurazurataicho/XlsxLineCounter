package org.zura.XlsxLineCounter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class XlsxLineCounter {
    private final String xlsxDirectory;
    private DirectoryStream<Path> xlsxDirectoryStream;
    XlsxLineCounter(String xlsxDirectory) {
        this.xlsxDirectory = xlsxDirectory;
    }
    private Integer countRowNumbers(XSSFSheet sheet) {
        Integer currentRow = 1; // 先頭行(0)はヘッダであるという前提
        Integer beforeNo = 0;
        try {
            while (true) {
                if (sheet.getRow(currentRow) == null) {
                    break;
                }
                Cell noCell = sheet.getRow(currentRow).getCell(0);    // 行最初のカラムはNo.であると仮定
                Double no = noCell.getNumericCellValue();   // No.カラムは数値型であると仮定
                if (no == 0.0) {
                    break;
                }
                beforeNo = no.intValue();
                currentRow += 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return beforeNo;
    }
    private Integer getTotalRows(InputStream inXlsx) throws IOException {
        XSSFWorkbook xlsxBook = new XSSFWorkbook(inXlsx);
        XSSFSheet xlsxSheet = xlsxBook.getSheetAt(0);   // 最初のシート固定
        return countRowNumbers(xlsxSheet);
    }
    private void outputLineRows(Integer lineRows, Optional<Path> inXlsx) {
        inXlsx.ifPresent(path -> System.out.println(path + "," + lineRows));
    }
    private void outputTotalRows(Integer totalRows, String text) {
        System.out.println(text + "," + totalRows);
    }
    public void run() throws IOException {
        xlsxDirectoryStream = Files.newDirectoryStream(Paths.get(xlsxDirectory), "*.xlsx");
        Integer totalRows = 0;
        for (Path entry : xlsxDirectoryStream) {
            Integer lastRow = getTotalRows(Files.newInputStream(entry));
            outputLineRows(lastRow, Optional.ofNullable(entry));
            totalRows += lastRow;
        }
        outputTotalRows(totalRows, "合計");
    }
}
