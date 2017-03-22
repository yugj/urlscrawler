package indi.yugj.test.sitemap.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * Created by yugj on 17/3/21.
 */
public class HttpUtil {

    public static Integer getHttpStatus(String url) {

        try {
            URL u = new URL(url);
            HttpURLConnection uConnection = (HttpURLConnection) u.openConnection();
            uConnection.connect();
            int code = uConnection.getResponseCode();
            System.out.println(code);
            return code;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("connect failed");
        }

        return null;
    }

    public static String getPageInfo(String url) {

        URL urlConn = null;
        String temp;
        StringBuffer sb = new StringBuffer();
        try {
            urlConn = new URL(url);
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConn.openStream(), "utf-8"));// 读取网页全部内容
            while ((temp = in.readLine()) != null) {
                sb.append(temp);
            }
            in.close();
        } catch (final MalformedURLException me) {
            me.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static boolean isEndPage(String url) {
        String pageInfo = getPageInfo(url);
        String endFlag = "<div class='noData'>暂无数据</div>";
        return pageInfo.contains(endFlag);
    }

    public static void main(String[] args) {

        String url = "http://m.migudm.cn/comic/solo_p1112/";

        System.out.println(isEndPage(url));
    }
}
