package indi.yugj.test.sitemap.htmlparser;

/**
 * entry
 * Created by yugj on 17/3/20.
 */

import indi.yugj.test.sitemap.utils.FileUtil;

import java.io.IOException;
import java.util.Set;

public class CrawlerWwwMain {

    /**
     * entry
     */
    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();

        String baseWww = "http://www.migudm.cn/";
        HtmlLinkCrawler crawler = new HtmlLinkCrawler(baseWww);

        crawler.execute(baseWww);

        catchSpecialPage4Www(crawler);

        System.out.println("抓取结束,准备写入文件...");

        Set<String> linkUrlQueue = crawler.getLinkUrlQueue();

        // 打印站内链接地址，并输出到sitemap.txt文件
        String fileWww = "/Users/yugj/Documents/hell/sitemap/htmlparser/sitemap-www-" + startTime + ".txt";

        for(String linkUrl : linkUrlQueue){
            System.out.println("写入文件:" + linkUrl);
            try {
                FileUtil.writeln(fileWww, linkUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        long endTime = System.currentTimeMillis();

        System.out.println("文件写入成功,文件位置:" + fileWww);
        System.out.println("抓取页面总时间:" + (endTime - startTime));
        System.out.println("网站总链接数:" + crawler.getTotalUrl());
        System.out.println("网站去重数:" + linkUrlQueue.size());
        System.out.println("网站总附件数:" + crawler.getTotalAttachFile());
        System.out.println("网站空地址数:" + crawler.getTotalNullUrl());
    }


    private static void catchSpecialPage4Www(HtmlLinkCrawler crawler) {

        specialBaseCraw(crawler,"http://www.migudm.cn/ugc_p",15);

    }

    private static void specialBaseCraw(HtmlLinkCrawler crawler,String subUrl,int endPage) {

        for (int i = 2; i <= endPage; i++) {
            String tempPage = subUrl + i + "/";
            crawler.execute(tempPage);
        }
    }

}