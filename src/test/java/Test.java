import com.redfin.sitemapgenerator.ChangeFreq;
import com.redfin.sitemapgenerator.WebSitemapGenerator;
import com.redfin.sitemapgenerator.WebSitemapUrl;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * site map
 * Created by yugj on 17/3/8.
 */
public class Test {

    @org.junit.Test
    public void gSiteMap() throws IOException {

        File dest = new File("/Users/yugj/Documents/tmp/sitemap/");

        String homepage = "http://www.migudm.cn";

        WebSitemapGenerator wsg = new WebSitemapGenerator(homepage, dest);

        WebSitemapUrl url = new WebSitemapUrl.Options("http://www.migudm.cn/comic/")
                .lastMod(new Date()).priority(1.0).changeFreq(ChangeFreq.HOURLY).build();

        // this will configure the URL with lastmod=now, priority=1.0, changefreq=hourly

        wsg.addUrl(url);

        wsg.write();


    }
}
