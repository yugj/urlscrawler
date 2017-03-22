package indi.yugj.test.sitemap.utils.poi;

import com.google.common.collect.Lists;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Excel 读取（97-2003格式）
 * Created by hyxing on 2015/3/21.
 */

public class PoiExcelHSSFHelper extends PoiExcelHelper {
    private static PoiExcelHSSFHelper instance = new PoiExcelHSSFHelper();

    private PoiExcelHSSFHelper(){}

    static PoiExcelHSSFHelper getInstance(){
        return instance;
    }


    @Override
    public List<List<String>> readExcel(InputStream is, int sheetIndex, String rows, String columns) throws IOException {
        List<List<String>> dataList = Lists.newArrayList();
        try {
            HSSFWorkbook workbook = new HSSFWorkbook(is);
            HSSFSheet sheet = workbook.getSheetAt(sheetIndex);

            dataList = readExcel(sheet, rows, getColumnNumber(sheet, columns));
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return dataList;
    }

    @Override
    public List<List<String>> readExcel(InputStream is, int sheetIndex, String rows, int[] cols) throws IOException {
        List<List<String>> dataList = Lists.newArrayList();
        try {
            HSSFWorkbook workbook = new HSSFWorkbook(is);
            HSSFSheet sheet = workbook.getSheetAt(sheetIndex);

            dataList = readExcel(sheet, rows, cols);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return dataList;
    }

    @Override
    public Workbook getWorkbook(){
        return new HSSFWorkbook();
    }
}
