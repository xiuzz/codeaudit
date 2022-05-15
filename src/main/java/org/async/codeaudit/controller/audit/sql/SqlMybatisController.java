package org.async.codeaudit.controller.audit.sql;

import lombok.extern.slf4j.Slf4j;
import org.async.codeaudit.common.R;

import org.async.codeaudit.service.SqlMybatisService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * sql注入的问题
 * mybatis 框架
 * mybatis框架${}对应statement
 *#{}对应preparedStatement
 */
@Slf4j
@RestController
@RequestMapping("/mybatis")
public class SqlMybatisController {
  @Resource
  private SqlMybatisService mybatisService;
 @PostMapping("/code")
  public R<String> code(MultipartFile file){
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
     return mybatisService.code(code);
 }

    /**
     * mybatis这种框架其实没有什么好sql漏洞审计教学的地方
     * 就是除了order by使用${}以外，用#{}
     * 遇到order by情况代码要过滤
     * @return
     */
 @GetMapping("/study")
  public R<String> study(){
     return null;
  }
}
