package org.async.codeaudit.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.async.codeaudit.common.R;
import org.async.codeaudit.service.RCEService;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class RCEServiceImpl implements RCEService {
    /**
     * ProcessBuilder审计
     * 这个很简单
     * 我们一般运用ProcessBuilder进行ls操作
     * 只要把shell的拼接字符过滤了就行了
     * @return
     */
    @Override
    public R<String> pbCode(String code) {
        String[] regs = "/,&,&&,;,||".split(",");
        int count=0;
        for (String reg : regs) {
            if(code.contains(reg))count++;
        }
        if(count==regs.length)return  R.result("过滤完全","通过","green");
        if(count==0)return R.result("未对ProcessBuilder进行过滤","存在严重远程代码执行风险","red");
        else return  R.result("过滤程度较小","存在远程代码执行风险","yellow");
    }
    /**
     *对于Runtime.getRuntime().exec();这个函数
     * 第一步也是一样的看看有没有对输入的String cmd 命令
     * 进行过滤
     * 有 green
     * 没有
     * 进行第二轮
     * 看是否按找规范使用了exec()方法
     * 没有 red
     * 有 green
     * @return
     */
    @Override
    public R<String> runtimeCode(String code) {
        String[] regs = "/,&,&&,;,||".split(",");
        int count=0;
        for (String reg : regs) {
            if(code.contains(reg))count++;
        }
        if(count==regs.length)return  R.result("过滤完全","通过","green");
        if(count==0)return R.result("未对ProcessBuilder进行过滤","存在严重远程代码执行风险","red");
        else {
            Pattern pattern = Pattern.compile("Runtime\\.getRuntime\\(\\)\\.exec\\((.+)\\);");
            Matcher matcher = pattern.matcher(code);
            //找到了关键字
            if(matcher.find()){
                //将他切分
                String group = matcher.group();
                String substring = group.substring("Runtime.getRuntime().exec(".length(),group.lastIndexOf(")"));
                //继续正则，确定问题的出发位置
                Pattern compile = Pattern.compile("String " + substring);
                //如果直接传入的是字符串就没有漏洞风险
                if(compile.matcher(code).find()){
                    return  R.result("未寻找到漏洞风险","通过","green");
                }
               else{
                   return R.result("使用了exec(String[])方法, 且未对输入进行过滤,容易造成远程命令执行漏洞","有较大的安全风险","red");
                }
            }
            else{
                return  R.result("过滤程度较小","存在远程代码执行风险","yellow");
            }
        }
    }
}
