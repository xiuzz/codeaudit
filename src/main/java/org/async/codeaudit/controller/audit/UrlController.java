package org.async.codeaudit.controller.audit;

import lombok.extern.slf4j.Slf4j;
import org.async.codeaudit.common.R;
import org.async.codeaudit.common.Security;
import org.async.codeaudit.service.UrlService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * url重定向漏洞,漏洞原因很简单
 * 用户输入url
 * 未对这个url进行过滤，
 * 一般url跳转
 * 为固定的第三方url
 * 或者自允许跳转到内部
 *
 */
@Slf4j
@RestController
@RequestMapping("/url")
public class UrlController {
    @Resource
    private UrlService urlService;
    @PostMapping("/code")
    public R<String> code(MultipartFile file){
         return  null;
    }
    /**
     * @poc http:///study/url/vul?url=http://www.google.com
     * 这里跳转会不会造成侵权啊
     */
    @GetMapping("study/vul")
    public String vul(String url) {
        return "redirect:" + url;
    }
    /**
     * @safe 白名单模式
     */
    @GetMapping("/safe")
    @ResponseBody
    public String safe(String url) {
        if (Security.isWhite(url)) {
            return "安全域名：" + url;
        } else {
            return "非法重定向域名！！";
        }
    }
}
