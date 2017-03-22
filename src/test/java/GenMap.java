/**
 * site map gen test
 * Created by yugj on 17/3/20.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.redfin.sitemapgenerator.ChangeFreq;
import com.redfin.sitemapgenerator.SitemapGeneratorBuilder;
import com.redfin.sitemapgenerator.WebSitemapGenerator;
import com.redfin.sitemapgenerator.WebSitemapUrl;

public class GenMap {

    public final static String INDEX_HTML = "index.html";

    public static WebSitemapGenerator wsg = null;

    // public static LinkedBlockingQueue<String> lbQueue = new LinkedBlockingQueue<String>();

    public static void main(String[] args) throws IOException, InterruptedException {

        String baseUrl = "http://www.migudm.cn/";
        File oDir = new File("/Users/yugj/Documents/tmp/sitemap/output");
        File iFile = new File("/Users/yugj/Documents/tmp/sitemap/detail.china.alibaba.com");

        init(baseUrl, oDir);
        // read(iDir);
        // while(!lbQueue.isEmpty()) {
        //   String url = (String) lbQueue.poll();
        //   addUrl(url);
        // }

        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(iFile)));

        while (br.ready()) {
            String url = br.readLine();
            if (url == null || "".equals(url)) continue;
            WebSitemapUrl.Options op = new WebSitemapUrl.Options(url);
            op.priority(1.0d);
            op.changeFreq(ChangeFreq.DAILY);
            op.lastMod(new Date());
            addUrl(op.build());
        }

        finish();
    }

    public static void init(String baseUrl, File oDir) throws MalformedURLException {
        SitemapGeneratorBuilder<WebSitemapGenerator> sitemapGenBuilder = WebSitemapGenerator.builder(baseUrl, oDir);

        sitemapGenBuilder.gzip(true);

        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd_HH");
        String dataStr = dateformat.format(new Date());
        sitemapGenBuilder.fileNamePrefix("sitemap_" + dataStr);

        wsg = sitemapGenBuilder.build();
    }

    public static void addUrl(String url) throws MalformedURLException {
        wsg.addUrl(url);
    }

    public static void addUrl(WebSitemapUrl url) throws MalformedURLException {
        wsg.addUrl(url);
    }

    public static void finish() {
        wsg.write();
        wsg.writeSitemapsWithIndex();
    }

// public static void read(File iFile) throws IOException, InterruptedException {
//   BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(iFile)));
//   while(br.ready()) {
//     lbQueue.put(br.readLine());
//   }
// }
}