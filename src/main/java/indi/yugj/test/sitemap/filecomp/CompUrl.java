package indi.yugj.test.sitemap.filecomp;

import indi.yugj.test.sitemap.utils.poi.PoiExcelHelper;
import indi.yugj.test.sitemap.utils.poi.WorkBookVo;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by yugj on 17/3/21.
 */
public class CompUrl {

    public static void main(String[] args) throws Exception {

        String wapUrlPath = "/Users/yugj/Documents/tmp/compurl/link-www.txt";
        String opusPath = "/Users/yugj/Documents/tmp/compurl/allopus.xlsx";

        String errorPath = "/Users/yugj/Documents/tmp/compurl/error-www.txt";

        File urlFile = new File(wapUrlPath);

        String urlAll = getUrlAllStr(urlFile);
        System.out.println("url 装载完成");

        File opusFile = new File(opusPath);
        List<String> opusAll = getOpusIdList(opusFile);
        System.out.println("opus load done");

        for (String opus : opusAll) {
            if (urlAll.indexOf(opus) < 0) {
                System.out.println("错误:" + opus);
                writeln(errorPath, opus);
            }
        }

    }


    private static String getUrlAllStr(File file) throws IOException {

        InputStreamReader read = new InputStreamReader(new FileInputStream(file));
        BufferedReader bufferedReader = new BufferedReader(read);

        String lineTxt = null;

        StringBuffer rs = new StringBuffer();

        while ((lineTxt = bufferedReader.readLine()) != null) {
            rs.append(lineTxt);
        }

        read.close();

        return rs.toString();

    }

    public static List<String> getOpusIdList(File file) throws Exception {

        WorkBookVo importWorkBookVo = new WorkBookVo(file);

        PoiExcelHelper excelHelper = importWorkBookVo.getExcelHelper();

        Sheet sheet = importWorkBookVo.getWorkbook().getSheetAt(0);

        List<String> rs = new ArrayList<String>();

        for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {

            Row row = sheet.getRow(rowNum);

            if (row == null) {
                throw new Exception("" + (rowNum + 1) + "行为空");
            }

            String displayId = excelHelper.getCellValue(row, 2);
            String type = excelHelper.getCellValue(row, 6);

            if (StringUtils.isNotBlank(displayId) && ("漫画".equals(type) || "动画".equals(type))) {
                rs.add(displayId);
            }

        }

        return rs;
    }

    public static void write(String path, String text) throws IOException {
        File file = new File(path);
        FileWriter fw = new FileWriter(file, true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.append(text);
        bw.flush();
        bw.close();
        fw.close();
    }

    /**
     * @param path
     * @param text
     * @throws IOException
     */
    public static void writeln(String path, String text) throws IOException {
        write(path, text + "\r\n");
    }
}
