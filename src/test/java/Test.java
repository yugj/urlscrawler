import com.redfin.sitemapgenerator.ChangeFreq;
import com.redfin.sitemapgenerator.WebSitemapGenerator;
import com.redfin.sitemapgenerator.WebSitemapUrl;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

/**
 * site map
 * Created by yugj on 17/3/8.
 */
public class Test {

    @org.junit.Test
    public void gSiteMap() throws IOException {

        StringBuilder responseBuilder = null;
        BufferedReader reader = null;
        OutputStreamWriter wr = null;
        URL url;
        try {
            url = new URL("http://www.migudm.cn/comic/school_p2222/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setConnectTimeout(1000 * 5);
            conn.connect();

            int hell = conn.getResponseCode();

            System.out.println("hell " + hell);

            conn.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
