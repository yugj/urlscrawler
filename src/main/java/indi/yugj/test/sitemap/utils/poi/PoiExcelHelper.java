package indi.yugj.test.sitemap.utils.poi;

import com.google.common.collect.Lists;
import com.google.common.io.Files;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Excel统一POI处理类（针对2003以前和2007以后两种格式的兼容处理）
 * this helper refers to @link http://cgs1999.iteye.com/blog/1525665
 *
 * Created by hyxing on 2015/3/21.
 */

public abstract class PoiExcelHelper {

    protected Logger logger = LoggerFactory.getLogger(getClass());
    //不连续值的分割符
    public static final String SEPARATOR = ",";
    //两个连续值的连接符
    public static final String CONNECTOR = "-";

    public static final int HSSF = 0;
    public static final int XSSF = 1;
    public static final String HSSF_EXTENSION = "xls";
    public static final String XSSF_EXTENSION = "xlsx";

    public abstract List<List<String>> readExcel(InputStream is, int sheetIndex, String rows, String columns) throws IOException;

    public abstract List<List<String>> readExcel(InputStream is, int sheetIndex, String rows, int[] cols) throws IOException;

    public abstract Workbook getWorkbook();



    public static PoiExcelHelper getHelper(int type){
        PoiExcelHelper helper = null;
        if(HSSF == type){
            helper = PoiExcelHSSFHelper.getInstance();

        }else if(XSSF == type){
            helper = PoiExcelXSSFHelper.getInstance();
        }
        return helper;
    }

    public static PoiExcelHelper getHelper(String extension){
        PoiExcelHelper helper = null;
        if(StringUtils.equals(extension, HSSF_EXTENSION)){
            helper = PoiExcelHSSFHelper.getInstance();

        }else if(StringUtils.equals(extension, XSSF_EXTENSION)){
            helper = PoiExcelXSSFHelper.getInstance();
        }
        return helper;
    }

    public static int getExcelLevel(String fileName) throws Exception {
        if (StringUtils.isBlank(fileName)) {
            throw new IllegalArgumentException("文件名不能为空");
        }

        String fileExtension = Files.getFileExtension(fileName);
        if(StringUtils.equals(fileExtension, PoiExcelHelper.HSSF_EXTENSION)){
            return PoiExcelHelper.HSSF;

        }else if (StringUtils.equals(fileExtension, PoiExcelHelper.XSSF_EXTENSION)){
            return PoiExcelHelper.XSSF;

        }else{
            throw new Exception("文件扩展名应为xls或xlsx");
        }
    }

    public static void checkExcelLevel(String fileName) throws Exception {
        getExcelLevel(fileName);
    }

    public List<List<String>> readExcel(File file, int sheetIndex) throws IOException {
        return readExcel(new FileInputStream(file), sheetIndex, "1-", "1-");
    }

    public List<List<String>> readExcel(InputStream is, int sheetIndex) throws IOException {
        return readExcel(is, sheetIndex, "1-", "1-");
    }

    public List<List<String>> readExcel(File file, int sheetIndex, String rows) throws IOException {
        return readExcel(new FileInputStream(file), sheetIndex, rows, "1-");
    }

    public List<List<String>> readExcel(InputStream is, int sheetIndex, String rows) throws IOException {
        return readExcel(is, sheetIndex, rows, "1-");
    }

    public List<List<String>> readExcel(File file, int sheetIndex, String[] columns) throws IOException {
        return readExcel(file, sheetIndex, "1-", columns);
    }

    public List<List<String>> readExcel(InputStream is, int sheetIndex, String[] columns) throws IOException {
        return readExcel(is, sheetIndex, "1-", columns);
    }

    public List<List<String>> readExcel(File file, int sheetIndex, String rows, String[] columns) throws IOException {
        int[] cols = getColumnNumber(columns);

        return readExcel(new FileInputStream(file), sheetIndex, rows, cols);
    }

    public List<List<String>> readExcel(InputStream is, int sheetIndex, String rows, String[] columns) throws IOException {
        int[] cols = getColumnNumber(columns);

        return readExcel(is, sheetIndex, rows, cols);
    }

    /** 读取Excel文件内容 */
    protected List<List<String>> readExcel(Sheet sheet, String rows, int[] cols) {
        List<List<String>> dataList = Lists.newArrayList();
        // 处理行信息，并逐行列块读取数据
        String[] rowList = rows.split(SEPARATOR);
        for (String rowStr : rowList) {
            if (rowStr.contains(CONNECTOR)) {
                String[] rowArr = rowStr.trim().split(CONNECTOR);
                int start = Integer.parseInt(rowArr[0]) - 1;
                int end;
                if (rowArr.length == 1) {
                    end = sheet.getLastRowNum();
                } else {
                    end = Integer.parseInt(rowArr[1].trim()) - 1;
                }
                dataList.addAll(getRowsValue(sheet, start, end, cols));
            } else {
                List<String> row = getRowValue(sheet, Integer.parseInt(rowStr) - 1, cols);
                if(row != null) {
                    dataList.add(row);
                }
            }
        }
        return dataList;
    }

    /** 获取连续行、列数据 */
    protected List<List<String>> getRowsValue(Sheet sheet, int startRow, int endRow,int startCol, int endCol) {
        if (endRow < startRow || endCol < startCol) {
            return null;
        }

        List<List<String>> data = Lists.newArrayList();
        for (int i = startRow; i <= endRow; i++) {
            List<String> row = getRowValue(sheet, i, startCol, endCol);
            if(row != null) {
                data.add(row);
            }
        }
        return data;
    }

    /** 获取连续行、不连续列数据 */
    private List<List<String>> getRowsValue(Sheet sheet, int startRow, int endRow, int[] cols) {
        if (endRow < startRow) {
            return null;
        }

        List<List<String>> data = Lists.newArrayList();
        for (int i = startRow; i <= endRow; i++) {
            List<String> rowData = getRowValue(sheet, i, cols);
            if(rowData != null) {
                data.add(rowData);
            }
        }
        return data;
    }

    /** 获取行连续列数据 */
    private List<String> getRowValue(Sheet sheet, int rowIndex, int startCol, int endCol) {
        if(endCol < startCol) {
            return null;
        }
        Row row = sheet.getRow(rowIndex);
        return getRowValue(row,startCol,endCol);
    }

    /** 获取行不连续列数据 */
    private List<String> getRowValue(Sheet sheet, int rowIndex, int[] cols) {
        Row row = sheet.getRow(rowIndex);
        return getRowValue(row,0,cols.length-1);
    }

    private List<String> getRowValue(Row row,int startCol, int endCol){
        boolean isRowBlank = true;
        List<String> rowData = Lists.newArrayList();

        for (int i=startCol; i <= endCol; i++) {
            String val = getCellValue(row, i);
            if(StringUtils.isNotBlank(val)){
                isRowBlank = false;
            }
            rowData.add(val);
        }
        if(isRowBlank){
            return null;
        }
        return rowData;
    }

    /**
     * 获取单元格内容
     *
     * @param row
     * @param column
     *            a excel column string like 'A', 'C' or "AA".
     * @return
     */
    public String getCellValue(Row row, String column) {
        return getCellValue(row,getColumnNumber(column));
    }

    /**
     * 获取单元格内容
     *
     * @param row
     * @param col
     *            a excel column index from 0 to 65535
     * @return
     */
    public String getCellValue(Row row, int col) {
        if (row == null) {
            return "";
        }
        Cell cell = row.getCell(col);
        return getCellValue(cell);
    }

    /**
     * 获取单元格内容
     *
     * @param cell
     * @return
     */
    private String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }

        String value = "";
        try {
            // This step is used to prevent Integer string being output with
            // '.0'.
            if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {

                //读取时间数据
                if(HSSFDateUtil.isCellDateFormatted(cell)){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    return sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue())).toString();
                }

                cell.setCellType(Cell.CELL_TYPE_STRING);
                value = cell.getStringCellValue();
                Float.parseFloat(value);
                value = value.replaceAll("\\.0$", "");
                value = value.replaceAll("\\.0+$", "");
            }else if(cell.getCellType() == Cell.CELL_TYPE_BOOLEAN){
                value = cell.getBooleanCellValue()+"";
            }else if(cell.getCellType() == Cell.CELL_TYPE_STRING){
                value = cell.getStringCellValue();
            }else {
                value = StringUtils.trim(cell.toString());
            }
            return value;
        } catch (NumberFormatException ex) {
            logger.error(ex.getMessage(),ex);
            throw ex;
        }
    }

    /**
     * Change excel column letter to integer number
     *
     * @param columns
     *            column letter of excel file, like A,B,AA,AB
     * @return
     */
    private int[] getColumnNumber(String[] columns) {
        int[] cols = new int[columns.length];
        for(int i=0; i<columns.length; i++) {
            cols[i] = getColumnNumber(columns[i]);
        }
        return cols;
    }

    /**
     * Change excel column letter to integer number
     *
     * @param column
     *            column letter of excel file, like A,B,AA,AB
     * @return
     */
    private int getColumnNumber(String column) {
        int length = column.length();
        short result = 0;
        for (int i = 0; i < length; i++) {
            char letter = column.toUpperCase().charAt(i);
            int value = letter - 'A' + 1;
            result += value * Math.pow(26, length - i - 1);
        }
        return result - 1;
    }

    /**
     * Change excel column string to integer number array
     *
     * @param sheet
     *            excel sheet
     * @param columns
     *            column letter of excel file, like A,B,AA,AB
     * @return
     */
    protected int[] getColumnNumber(Sheet sheet, String columns) {
        // 拆分后的列为动态，采用List暂存
        List<Integer> result = Lists.newArrayList();
        String[] colList = columns.split(SEPARATOR);
        for(String colStr : colList){
            if(colStr.contains(CONNECTOR)){
                String[] colArr = colStr.trim().split(CONNECTOR);
                int start = Integer.parseInt(colArr[0]) - 1;
                int end;
                if(colArr.length == 1){
                    end = sheet.getRow(sheet.getFirstRowNum()).getLastCellNum() - 1;
                }else{
                    end = Integer.parseInt(colArr[1].trim()) - 1;
                }
                for(int i=start; i<=end; i++) {
                    result.add(i);
                }
            }else{
                result.add(Integer.parseInt(colStr) - 1);
            }
        }

        // 将List转换为数组
        int len = result.size();
        int[] cols = new int[len];
        for(int i = 0; i<len; i++) {
            cols[i] = result.get(i).intValue();
        }

        return cols;
    }

    protected void setSheetColumnsWidth(Sheet sheet, int[]  columnsWidth){
        if(sheet == null){
            return;
        }

        //列宽设置
        if(!ArrayUtils.isEmpty(columnsWidth)) {
            for (int i = 0; i < columnsWidth.length; i++) {
                if (columnsWidth[i] != 0) {
                    sheet.setColumnWidth(i, columnsWidth[i]);
                }
            }
        }
    }

    /**
     * 获取servlet环境下的导出文件名称
     * @param fileName
     * @param userAgent servlet环境下的客户端代理
     * @return
     * @throws java.io.UnsupportedEncodingException
     */
    public String getExportExcelFileNameForServlet(String fileName,String userAgent)
        throws UnsupportedEncodingException {

        if(StringUtils.isBlank(fileName)){
            return null;
        }
        if (StringUtils.contains(userAgent, "MSIE") || StringUtils.contains(userAgent, "Trident")) {
            //IE浏览器
            fileName = URLEncoder.encode(fileName, "UTF8");

        } else if (StringUtils.contains(userAgent, "Mozilla")) {
            //google,火狐浏览器
            fileName = new String(fileName.getBytes(), "ISO8859-1");

        } else {
            fileName = URLEncoder.encode(fileName, "UTF8");
        }

        if(this.getClass().equals(PoiExcelHSSFHelper.class)){
            fileName = fileName + "." + HSSF_EXTENSION;
        }else{
            fileName = fileName + "." + XSSF_EXTENSION;
        }

        return fileName;
    }

    /**
     * 生成excel文件导出，可设置所有列宽
     * @param out
     * @param datas excel文件数据，带有表头
     * @param columnsWidth  所有列宽。数组元素为0（不设置），即采用默认的列宽
     */
    public void export(OutputStream out,List<List<String>> datas,int[] columnsWidth){
        try {
            Workbook workbook = getWorkbook();
            createSheetWithHeader(workbook,datas,columnsWidth);
            workbook.write(out);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }finally {
            try {
                out.close();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    /**
     * 生成excel文件导出
     * @param out
     * @param datas  excel文件数据，带有表头
     */
    public void export(OutputStream out,List<List<String>> datas){
        export(out,datas,null);
    }


    /**
     * 生成excel文件导出
     * @param out
     */
    public void export(Workbook workbook,OutputStream out){
        try {
            if(workbook == null){
                return;
            }
            workbook.write(out);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }finally {
            try {
                out.close();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }


    public Sheet createSheetWithHeader(Workbook workbook,List<List<String>> datas,int[] columnsWidth){
        if(CollectionUtils.isEmpty(datas) || workbook == null ){
            return null;
        }

        Sheet sheet = workbook.createSheet();
        setSheetColumnsWidth(sheet,columnsWidth);

        //设置表头字体
        Font headerFont = workbook.createFont();
        headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        CellStyle cellStyle= workbook.createCellStyle();
        cellStyle.setFont(headerFont);

        //表头数据
        Row headerRow = sheet.createRow((short)0);
        List<String> header = datas.get(0);
        if(header!=null){
            for(int i=0;i<header.size();i++){
                Cell cell = headerRow.createCell(i);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(header.get(i));
            }
        }

        for (int i=1; i<datas.size(); i++){
            Row row = sheet.createRow(i);
            List<String> rowData = datas.get(i);
            for (int j=0;j<rowData.size();j++){
                Cell cell = row.createCell(j);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                Object o = rowData.get(j);
                //写入到表中的数值型对象恢复为数值型，
                if(o instanceof BigDecimal){
                    BigDecimal b=(BigDecimal)o;
                    cell.setCellValue(b.doubleValue());

                }else if(o instanceof Integer){
                    Integer it =(Integer)o;
                    cell.setCellValue(it.intValue());

                }else if(o instanceof Long){
                    Long l =(Long)o;
                    cell.setCellValue(l.intValue());

                }else if(o instanceof Double){
                    Double d =(Double)o;
                    cell.setCellValue(d.doubleValue());

                }else if(o instanceof Float){
                    Float f = (Float)o;
                    cell.setCellValue(f.floatValue());

                }else{
                    cell.setCellValue(o+"");
                }
            }
        }

        return sheet;
    }

    /**
     * 默认方式添加下拉框数据
     * @param sheet
     * @param explicitDatas 下拉框数据数组
     * @param column excel列的位置
     */
    public void dafaultAddExplicitConstraintData(Sheet sheet,String[] explicitDatas,int column){

        if(ArrayUtils.isEmpty(explicitDatas)){
            return;
        }

        DataValidationHelper helper = sheet.getDataValidationHelper();

        //CellRangeAddressList(firstRow, lastRow, firstCol, lastCol)设置行列范围
        CellRangeAddressList addressList = new CellRangeAddressList(0, Integer.MAX_VALUE, column,column);

        //设置下拉框数据
        DataValidationConstraint constraint = helper.createExplicitListConstraint(explicitDatas);
        DataValidation dataValidation = helper.createValidation(constraint, addressList);

        //处理Excel兼容性问题
        if(dataValidation instanceof XSSFDataValidation) {
            dataValidation.setSuppressDropDownArrow(true);
            dataValidation.setShowErrorBox(true);
        }else {
            dataValidation.setSuppressDropDownArrow(false);
        }
        sheet.addValidationData(dataValidation);
    }


    public static void main(String[] args){
        String filePath1 = "D:/test/test.xls";
        File file  = new File(filePath1);
        PoiExcelHelper helper1 = PoiExcelHelper.getHelper(Files.getFileExtension(file.getName()));
        List<List<String>> content = null;
        try {
            content = helper1.readExcel(file,0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

