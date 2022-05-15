package org.async.codeaudit.controller.audit;

import lombok.extern.slf4j.Slf4j;
import org.async.codeaudit.common.Http;
import org.async.codeaudit.common.R;
import org.async.codeaudit.common.Security;
import org.async.codeaudit.service.SSRFService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/SSRF")
public class SSRFController {
    @Resource
    private SSRFService ssrfService;

    /**
     *SSRF(Server-Side Request Forgery) 服务器端请求伪造，是一种由攻击者构造形成由服务端发起请求的一个安全漏洞
     * 避免直接访问不可信地址
     * 服务器访问不可信地址时，禁止访问私有地址段及内网域名。
     * 建议通过URL解析函数进行解析，获取host或者domain后通过DNS获取其IP，然后和内网地址进行比较。
     * 对已校验通过地址进行访问时，应关闭跟进跳转功能。
     *
     *因此扫描手段位扫描url是否有漏洞
     * @param file
     * @return
     */
    @PostMapping("/code")
   public R<String> code(MultipartFile file){
       return  null;
   }
    /**
     * @poc http:///SSRF/study/urlConnection?url=http://www.baidu.com
     * @poc http:///SSRF/study/URLConnection?url=file:///etc/passwd
     */
    @GetMapping("study/urlConnection")
    public String URLConnection(String url) {
        System.out.println("[Vul] 执行SSRF：" + url);
        return Http.URLConnection(url);
    }


    /**
     * @poc bypass 短链接：http://127.0.0.1:8888/SSRF/study/httpURLConnection/safe?url=http://surl-8.cn/0
     */
    @GetMapping("study/urlConnection/safe")
    public String URLConnectionSafe(String url) {
        if (!Security.isHttp(url)){
            return "不允许非http/https协议!!!";
        }else if (Security.isIntranet(url)) {
            return "不允许访问内网!!!";
        }else{
            return Http.URLConnection(url);
        }
    }

    /**
     * @safe 不允许重定向
     */
    @GetMapping("study/httpURLConnection/safe")
    public String HTTPURLConnection(String url) {
        if (!Security.isHttp(url)){
            return "不允许非http/https协议!!!";
        }else if (Security.isIntranet(url)) {
            return "不允许访问内网!!!";
        }else{
            return Http.HTTPURLConnection(url);
        }
    }
}
