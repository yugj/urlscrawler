package indi.yugj.test.sitemap.utils.poi;

import com.google.common.io.Files;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by laixx on 2015/11/16.
 */
public class WorkBookVo {

    private Workbook workbook;

    private int excelLevel;

    private PoiExcelHelper excelHelper;

    public Workbook getWorkbook() {
        return workbook;
    }

    public void setWorkbook(Workbook workbook) {
        this.workbook = workbook;
    }

    public int getExcelLevel() {
        return excelLevel;
    }

    public void setExcelLevel(int excelLevel) {
        this.excelLevel = excelLevel;
    }

    public WorkBookVo(File excelFile) throws Exception {
        int excelLevel = PoiExcelHelper.getExcelLevel(excelFile.getName());
        PoiExcelHelper excelHelper = PoiExcelHelper.getHelper(Files.getFileExtension(excelFile.getName()));
        Workbook workbook = (excelLevel == PoiExcelHelper.HSSF)
            ?new HSSFWorkbook(new FileInputStream(excelFile))
            :new XSSFWorkbook(new FileInputStream(excelFile));

        this.workbook = workbook;
        this.excelHelper = excelHelper;
        this.excelLevel = excelLevel;
    }

    public WorkBookVo(Workbook workbook, PoiExcelHelper excelHelper, int excelLevel) {
        this.workbook = workbook;
        this.excelHelper = excelHelper;
        this.excelLevel = excelLevel;
    }

    public PoiExcelHelper getExcelHelper() {
        return excelHelper;
    }

    public void setExcelHelper(PoiExcelHelper excelHelper) {
        this.excelHelper = excelHelper;
    }
}
