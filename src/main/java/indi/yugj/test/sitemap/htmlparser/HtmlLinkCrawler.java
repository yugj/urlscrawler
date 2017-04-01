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

import java.util.HashSet;
import java.util.Set;

public class HtmlLinkCrawler {

    private static final String BASE_PREFIX = "http://www.migudm.cn";

    private Long totalUrl = 0L;
    private Long totalAttachFile = 0L;
    private Long totalNullUrl = 0L;

    private Parser parser = new Parser();
    private NodeClassFilter linkFilter = new NodeClassFilter(LinkTag.class);

    private Set<String> urlContainer = new HashSet<String>();

    private String baseUrl;

    public HtmlLinkCrawler(String baseUrl) {
        this.baseUrl = baseUrl;
        this.urlContainer.add(baseUrl);
    }

    public void execute(String base) {
        this.crawl(base);
    }

    /**
     * 根据URL抓取
     *
     * @param url
     */
    private void crawl(String url) {

        try {

            parser.setURL(url);
            parser.getConnection().setConnectTimeout(500);

            NodeList list = parser.parse(linkFilter);

            NodeIterator elements = list.elements();

            while (elements.hasMoreNodes()) {

                LinkTag linkTag = (LinkTag) elements.nextNode();

                String linkUrl = linkTag.getLink();
                String dataUrl = linkTag.getAttribute("data-url");

                //count link by type
                if (isAttachFile(linkUrl)) {
                    this.totalAttachFile++;
                    continue;
                }
                countUrl(linkUrl, dataUrl);

                String fixUrl = BASE_PREFIX + dataUrl;
                System.out.println("total url count:" + getTotalUrl() + ",link url:" + linkUrl + ",data url:" + dataUrl);

                String targetUrl = null;
                String targetFrom = null;
                if (checkStationUrl(linkUrl) && noContains(linkUrl)) {
                    targetUrl = linkUrl;
                    targetFrom = "src";
                } else if (null != dataUrl && !"".equals(dataUrl) && noContains(fixUrl)) {
                    targetUrl = fixUrl;
                    targetFrom = "data-url";
                } else {
                    continue;
                }

                urlContainer.add(targetUrl);

                int queenSize = getLinkUrlQueue().size();
                System.out.println(targetFrom + url + "--->> urlContainer size :" + queenSize + ",target url :" + targetUrl);

                this.crawl(targetUrl);

            }

        } catch (ParserException e) {
            System.out.println("error url : " + url);
            e.printStackTrace();
        }
    }

    /**
     * 是否为站内地址
     *
     * @param link
     * @return
     */
    private boolean checkStationUrl(String link) {
        return link != null && link.startsWith(this.baseUrl);
    }

    /**
     * 是否包含该地址
     *
     * @param link
     * @return
     */
    private boolean noContains(String link) {
        // 如果链接最后有 "#" 符号出现
        if (link.endsWith("#")) {
            // 去掉#号
            link = link.substring(0, link.length() - 1);
        }
        return !urlContainer.contains(link);
    }

    /**
     * 获取到所有的站内容链接地址
     *
     * @return
     */
    public Set<String> getLinkUrlQueue() {
        return this.urlContainer;
    }


    /**
     * 统计不同类型地址
     *
     * @param url
     * @param dataUrl
     */
    private void countUrl(String url, String dataUrl) {

        if (StringUtils.isBlank(url) && StringUtils.isBlank(dataUrl)) {
            this.totalNullUrl++;
            return;
        }

        this.totalUrl++;

//        if (StringUtils.isNotBlank(dataUrl)) {
//            return;
//        }

//        if (isAttachFile(url)) {
//            this.totalAttachFile ++;
//        }

    }

    /**
     *
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