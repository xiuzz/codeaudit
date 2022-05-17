package org.async.codeaudit.controller.audit.sql;

import lombok.extern.slf4j.Slf4j;
import org.async.codeaudit.common.R;
import org.async.codeaudit.service.TraversalService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 路径穿越（目录遍历）,
 * 应用系统在处理下载文件时未对文件进行过滤，
 * 系统后台程序程序中如果不能正确地过滤客户端提交的../和./之类的目录跳转符，攻击者可以通过输入../进行目录跳转，
 * 从而下载、删除任意文件。
 *漏洞的成因很简单
 * 就是建立一个让用户可以访问当前目录下的某个文件业务，
 * 但是未对这个业务的路径进行安全过滤
 * 让用户可以运用../穿梭到想去的位置，删除或者下载我们服务端的文件
 * 审计方式也特别简单
 * 让用户提交有用户访问服务器文件的功能页面，
 * 审计这个页面
 * 其实不管用户怎么样，如果没有使用ID索引（{id}）这种方式来下载文件，就直接化为蓝色等级及蓝色等级以下的等级
 * 如果有进行过滤的操作则为蓝色
 * 没有直接抛出红色
 * （这一个是逻辑最简单的一个了吧）
 */
@Slf4j
@RestController
@RequestMapping("/traversal")
public class TraversalController {
    @Resource
    TraversalService traversalService;
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
        return  traversalService.code(code);
    }
}
