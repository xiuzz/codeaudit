package org.async.codeaudit.controller.audit;

import lombok.extern.slf4j.Slf4j;
import org.async.codeaudit.common.R;
import org.async.codeaudit.service.XXEService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * XXE漏洞，
 * 既xml外部注入漏洞
 * 感觉web审计原理很多都差不多，
 * 这个的原理是在有xml注入的方法上
 * 没有对该方法进行过滤
 * 常见的几个解析xml语言的接口
 * XMLReadr(方法为parse())
 * SAXBuilder(方法为build())
 * SAXReader(方法为read())//dom4j.org出品
 * Digester(方法为parse)//Apace Commons库中的一个jar包
 * DocumentBuilderFactory(创建DocumentBuilder，parse())
 */
@Slf4j
@RestController
@RequestMapping("/xxe")
public class XXEController {
    @Resource
    private XXEService xxeService;
    @PostMapping("/code")
    public R<String> code(MultipartFile file){
        if(file.isEmpty())return  R.error("文件为空！");
        BufferedReader bufferedReader= null;
        log.info(file.toString());
        String code=null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            StringBuilder temp=new StringBuilder();
            while((code=bufferedReader.readLine())!=null){
                temp.append(code);
            }
            code=temp.toString();
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  xxeService.code(code);
    }
}
