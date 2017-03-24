package indi.yugj.test.sitemap.htmlparser;

/**
 * 单线程递归抓取页面
 * Created by yugj on 17/3/20.
 */

import org.apache.commons.lang.StringUtils;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import java.util.LinkedList;
import java.util.Queue;

public class HtmlLinkCrawler {

    private static final String BASE_PREFIX = "http://www.migudm.cn";

    private Long totalUrl = 0L;
    private Long totalAttachFile = 0L;
    private Long totalNullUrl = 0L;

    // htmlParser解析器
    private Parser parser = new Parser();


    // 链接Filter过滤器
    private NodeClassFilter linkFilter = new NodeClassFilter(LinkTag.class);
    // 网站地图容器
    private Queue<String> queue = new LinkedList<String>();
    // 网址
    private String baseUrl;

    public HtmlLinkCrawler(String baseUrl){
        this.baseUrl = baseUrl;
        // 添加到容器
        this.queue.offer(baseUrl);
    }

    public void execute(String base){

        // 从首页地址开始抓取
        crawl(base);
    }

    /**
     * 根据URL抓取
     *
     * @param url
     */
    private void crawl(String url){
        try {
            parser.setURL(url);
            parser.getConnection().setConnectTimeout(500);
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

                countUrl(linkUrl,dataUrl);

                String fixUrl = BASE_PREFIX + dataUrl;
                System.out.println("total url size:" + getTotalUrl() + ",link url:" + linkUrl + ",data url:" + dataUrl);

                if(checkStationUrl(linkUrl) && noContains(linkUrl)){

                    queue.offer(linkUrl);
                    this.crawl(linkUrl);

                    int queenSize = getLinkUrlQueue().size();
                    System.out.println("--->> queue size :" + queenSize + ",link url :" + linkUrl);


                } else if (null != dataUrl && !"".equals(dataUrl) && noContains(fixUrl)) {

                    queue.offer(fixUrl);
                    this.crawl(fixUrl);

                    int queenSize = getLinkUrlQueue().size();
                    System.out.println("--->> queue size :" + queenSize + ",data url :" + fixUrl);
                }
            }

        } catch (ParserException e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否为站内地址
     *
     * @param link
     * @return
     */
    private boolean checkStationUrl(String link){
        return link!=null ? link.startsWith(this.baseUrl) : false;
    }

    /**
     * 是否包含该地址
     *
     * @param link
     * @return
     */
    private boolean noContains(String link){
        // 如果链接最后有 "#" 符号出现
        if(link.endsWith("#")){
            // 去掉#号
            link = link.substring(0, link.length()-1);
        }
        return !queue.contains(link);
    }

    /**
     * 获取到所有的站内容链接地址
     *
     * @return
     */
    public Queue<String> getLinkUrlQueue(){
        return this.queue;
    }


    /**
     * 统计不同类型地址
     * @param url
     * @param dataUrl
     */
    private void countUrl(String url,String dataUrl) {

        if (StringUtils.isBlank(url) && StringUtils.isBlank(dataUrl)) {
            this.totalNullUrl ++;
            return;
        }

        this.totalUrl ++ ;

        if (StringUtils.isNotBlank(dataUrl)) {
            return;
        }

        if (isAttachFile(url)) {
            this.totalAttachFile ++;
        }

    }

    /**
     * 判断是否为附件
     * @param url
     * @return
     */
    private boolean isAttachFile(String url) {
        if (StringUtils.isBlank(url)) {
            return false;
        }

        boolean isAttach = url.endsWith(".zip") || url.endsWith(".rar")
                || url.endsWith(".docx") || url.endsWith(".doc")
                || url.endsWith(".xlsx") || url.endsWith(".xls")
                || url.endsWith(".pptx") || url.endsWith(".ppt");

        return isAttach;
    }

    public Long getTotalUrl() {
        return this.totalUrl;
    }

    public Long getTotalAttachFile() {
        return this.totalAttachFile;
    }

    public Long getTotalNullUrl() {
        return this.totalNullUrl;
    }

}