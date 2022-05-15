package org.async.codeaudit.controller.audit;

import lombok.extern.slf4j.Slf4j;
import org.async.codeaudit.common.R;
import org.async.codeaudit.common.Security;
import org.async.codeaudit.service.XssService;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;

@RestController
@RequestMapping("/xss")
@Slf4j
public class XssController {
    @Resource
    XssService xssService;
    @PostMapping("/code")
    /**
     * xss
     * 分为反射型，存储型
     * 但其实不管哪种
     * 他的主要成型原因都是
     * 输入可控并且没有经过过滤便直接输出，
     * 因此防御XSS漏洞一般有以下几种方式
     * 1.编写全局过滤器
     * 2.采用开源安全控制库
     * 3.对所有字符采用HTML实体编码
     * 审计手段即为查看前后端接口，有没有存在xss注入风险，
     */
    public R<String> code(MultipartFile file){
        /*
        * 在实现servletRequest地方查看应用上下文*/
        return null;
    }
    /**
     * 反射型XSS学习区内容
     *
     * @poc http:///xss/study/reflect?content=<img/src=1 onerror=alert(1)>
     */
    @GetMapping("study/reflect")
    public static String reflect(String content) {
        System.out.println("[vul] 执行xss");
        return content;
    }
    /**@Poc http:///xss/study/escape
     * @safe 方案一、采用自带函数HtmlUtils.htmlEscape()来过滤
     */
    @GetMapping("study/escape")
    public static String safe1(String content) {
        return HtmlUtils.htmlEscape(content);
    }


    /**@Poc http:///xss/study/filter
     * @safe 方案二、自己做一个XssFilter或者采用第三方, 基于转义的方式
     */
    @GetMapping("study/filter")
    public static String safe2(String content) {
        return Security.filterXss(content);
    }

    /**@Poc http:///xss/study/whitelist
     * @safe 富文本情况下，做黑白名单
     */
    @GetMapping("study/whitelist")
    public static String safe3(String content) {
        Safelist Safelist = (new Safelist())
                .addTags("p", "hr", "div", "img", "span", "textarea")  // 设置允许的标签
                .addAttributes("a", "href", "title") // 设置标签允许的属性, 避免如nmouseover属性
                .addProtocols("img", "src", "http", "https")  // img的src属性只允许http和https开头
                .addProtocols("a", "href", "http", "https");
        return Jsoup.clean(content, Safelist);
    }
}
