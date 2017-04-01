package indi.yugj.test.sitemap.htmlparser;

import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

/**
 * sg
 * Created by yugj on 17/3/21.
 */
public class SinglePageTest {

    public static void main(String args[]) {
        SinglePageTest test = new SinglePageTest();
        String url = "http://www.migudm.cn/cartoon/list_time/";

        test.crawl(url);



//        for (int i = 2; i <= 59; i++) {
//            String temp = url + i + "/";
//            test.crawl(temp);
//        }

    }

    private Parser parser = new Parser();
    private NodeClassFilter linkFilter = new NodeClassFilter(LinkTag.class);


    /**
     * 根据URL抓取
     *
     * @param url
     */
    private void crawl(String url){
        try {
            parser.setURL(url);
            // 解析器解析
            NodeList list = parser.parse(linkFilter);
            // 遍历器
            NodeIterator elements = list.elements();
            while(elements.hasMoreNodes()){

                // 获取到页面链接标签
                LinkTag linkTag = (LinkTag) elements.nextNode();
                // 页面链接地址
                String linkUrl =linkTag.getLink();
                String dataUrl = linkTag.getAttribute("data-url");

                System.out.println(",link Url :" + linkUrl);
                System.out.println(",data url :" + dataUrl);
            }

        } catch (ParserException e) {
            e.printStackTrace();
        }
    }



}
