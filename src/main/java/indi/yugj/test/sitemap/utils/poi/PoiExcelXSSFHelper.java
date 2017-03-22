package indi.yugj.test.sitemap.utils.poi;


import com.google.common.collect.Lists;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
* Excel 读取2007格式
* Created by hyxing on 2015/3/21.
*/

public class PoiExcelXSSFHelper extends PoiExcelHelper {
    private static PoiExcelXSSFHelper instance = new PoiExcelXSSFHelper();

    private PoiExcelXSSFHelper(){}

    static PoiExcelXSSFHelper getInstance(){
        return instance;
    }

    @Override
    public List<List<String>> readExcel(InputStream is, int sheetIndex, String rows, String columns) throws IOException {
        List<List<String>> dataList = Lists.newArrayList();
        XSSFWorkbook workbook = new XSSFWorkbook(is);
        XSSFSheet sheet = workbook.getSheetAt(sheetIndex);

        dataList = readExcel(sheet, rows, getColumnNumber(sheet, columns));
        return dataList;
    }

    @Override
    public List<List<String>> readExcel(InputStream is, int sheetIndex, String rows, int[] cols) throws IOException {
        List<List<String>> dataList = Lists.newArrayList();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(is);
            XSSFSheet sheet = workbook.getSheetAt(sheetIndex);

            dataList = readExcel(sheet, rows, cols);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return dataList;
    }

    @Override
    public Workbook getWorkbook() {
        return new XSSFWorkbook();
    }
}