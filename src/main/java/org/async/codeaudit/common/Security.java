package org.async.codeaudit.common;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
   网络协议类漏洞的综合工具类
 */
public class Security {

    /**
     * 判断是否为内网地址
     *
     * @return True or False
     */
    public static boolean isIntranet(String url) {
        Pattern reg = Pattern.compile("^(127\\.0\\.0\\.1)|(localhost)|(10\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})|(172\\.((1[6-9])|(2\\d)|(3[01]))\\.\\d{1,3}\\.\\d{1,3})|(192\\.168\\.\\d{1,3}\\.\\d{1,3})$");
        Matcher match = reg.matcher(url);
        Boolean a = match.find();
        return a;
    }

    /**
     * 判断是否是http类型
     *
     * @return True or False
     */
    public static boolean isHttp(String url) {
        return url.startsWith("http://") || url.startsWith("https://");
    }

    /**
     * 判断url是否在白名单内
     */
    public static boolean isWhite(String url) {
        List<String> url_list = new ArrayList<String>();
        url_list.add("baidu.com");
        url_list.add("www.baidu.com");
        url_list.add("oa.baidu.com");

        // 从url转换host
        URI uri = null;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            System.out.print(e);
        }
        String host = uri.getHost().toLowerCase();
        System.out.println(host);

        return url_list.contains(host);

    }

    public static String filterXss(String content) {
        content = StringUtils.replace(content, "&", "&amp;");
        content = StringUtils.replace(content, "<", "&lt;");
        content = StringUtils.replace(content, ">", "&gt;");
        content = StringUtils.replace(content, "\"", "&quot;");
        content = StringUtils.replace(content, "'", "&#x27;");
        content = StringUtils.replace(content, "/", "&#x2F;");
        return content;
    }


    public static boolean checkSql(String content) {
        String black = "'|;|--|+|,|%|=|>|<|*|(|)|and|or|exec|insert|select|delete|update|count|drop|chr|mid|master|truncate|char|declare";
        String[] black_list = black.split("|");
        for (int i = 0; i < black_list.length; i++) {
            if (content.contains(black_list[i])) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkTraversal(String content) {
        return content.contains("..") || content.contains("/");
    }

    public static boolean checkOs(String content) {
        String black = "|,&,&&,;,||";
        String[] black_list = black.split(",");
        for (String s : black_list) {
            if (content.contains(s)) {
                return true;
            }
        }
        return false;
    }

}