package indi.yugj.test.sitemap.htmlparser;

/**
 * entry
 * Created by yugj on 17/3/20.
 */

import indi.yugj.test.sitemap.utils.FileUtil;
import indi.yugj.test.sitemap.utils.HttpUtil;

import java.io.IOException;
import java.util.Set;

public class CrawlerWapMain {

    /**
     * entry
     */
    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();

        String baseWap = "http://m.migudm.cn/";
        HtmlLinkCrawler crawler = new HtmlLinkCrawler(baseWap);

        crawler.execute(baseWap);

        crawSpecialPage4Wap(crawler);

        System.out.println("抓取结束,准备写入文件...");

        Set<String> linkUrlQueue = crawler.getLinkUrlQueue();

        // 打印站内链接地址，并输出到sitemap.txt文件
        String fileWap = "/Users/yugj/Documents/tmp/sitemap/htmlparser/sitemap-wap-" + startTime + ".txt";

        for(String linkUrl : linkUrlQueue){
            System.out.println("写入文件:" + linkUrl);
            try {
                FileUtil.writeln(fileWap, linkUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        long endTime = System.currentTimeMillis();

        System.out.println("文件写入成功,文件位置:" + fileWap);

        System.out.println("抓取页面总时间:" + (endTime - startTime));
    }


    private static void crawSpecialPage4Wap(HtmlLinkCrawler crawler) {

        //漫画特殊页面抓取
        specialBaseCraw(crawler,"http://m.migudm.cn/comic/solo_p");
        specialBaseCraw(crawler,"http://m.migudm.cn/comic/tiaoman_p");
        specialBaseCraw(crawler,"http://m.migudm.cn/comic/end_p");
        specialBaseCraw(crawler,"http://m.migudm.cn/comic/japan_p");
        specialBaseCraw(crawler,"http://m.migudm.cn/comic/school_p");
        specialBaseCraw(crawler,"http://m.migudm.cn/comic/terror_p");
        specialBaseCraw(crawler,"http://m.migudm.cn/comic/funny_p");
        specialBaseCraw(crawler,"http://m.migudm.cn/comic/classic_p");
        specialBaseCraw(crawler,"http://m.migudm.cn/comic/sports_p");
        specialBaseCraw(crawler,"http://m.migudm.cn/comic/emotion_p");
        specialBaseCraw(crawler,"http://m.migudm.cn/comic/kehuan_p");
        specialBaseCraw(crawler,"http://m.migudm.cn/comic/suspense_p");
        specialBaseCraw(crawler,"http://m.migudm.cn/comic/cour_p");
        specialBaseCraw(crawler,"http://m.migudm.cn/comic/wuxia_p");
        specialBaseCraw(crawler,"http://m.migudm.cn/comic/fantasy_p");
        specialBaseCraw(crawler,"http://m.migudm.cn/comic/tanbi_p");
        specialBaseCraw(crawler,"http://m.migudm.cn/comic/urban_p");

        //动画特殊页面抓取
        specialBaseCraw(crawler,"http://m.migudm.cn/cartoon/end_p");
        specialBaseCraw(crawler,"http://m.migudm.cn/cartoon/news_p");
        specialBaseCraw(crawler,"http://m.migudm.cn/cartoon/child_p");
        specialBaseCraw(crawler,"http://m.migudm.cn/cartoon/education_p");
        specialBaseCraw(crawler,"http://m.migudm.cn/cartoon/mengxi_p");
        specialBaseCraw(crawler,"http://m.migudm.cn/cartoon/sports_p");
        specialBaseCraw(crawler,"http://m.migudm.cn/cartoon/adventure_p");
        specialBaseCraw(crawler,"http://m.migudm.cn/cartoon/war_p");
        specialBaseCraw(crawler,"http://m.migudm.cn/cartoon/school_p");
        specialBaseCraw(crawler,"http://m.migudm.cn/cartoon/classic_p");
        specialBaseCraw(crawler,"http://m.migudm.cn/cartoon/qinzi_p");
        specialBaseCraw(crawler,"http://m.migudm.cn/cartoon/urban_p");
        specialBaseCraw(crawler,"http://m.migudm.cn/cartoon/guoxue_p");
        specialBaseCraw(crawler,"http://m.migudm.cn/cartoon/kehuan_p");
        specialBaseCraw(crawler,"http://m.migudm.cn/cartoon/emotion_p");
        specialBaseCraw(crawler,"http://m.migudm.cn/cartoon/rexue_p");
        specialBaseCraw(crawler,"http://m.migudm.cn/cartoon/wuxia_p");
        specialBaseCraw(crawler,"http://m.migudm.cn/cartoon/suspense_p");
        specialBaseCraw(crawler,"http://m.migudm.cn/cartoon/magic_p");
        specialBaseCraw(crawler,"http://m.migudm.cn/cartoon/minsu_p");

    }



    private static void specialBaseCraw(HtmlLinkCrawler crawler,String subUrl) {

        for (int i = 2; i <= 500; i++) {
            String tempPage = subUrl + i + "/";

            boolean isEnd = HttpUtil.isEndPage(tempPage);

            if (isEnd) {
                System.out.println("-_______--->>>>>_____>>> end !!!" + tempPage);
                break;
            }
            crawler.execute(tempPage);
        }
    }

}