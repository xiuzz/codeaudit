package org.async.codeaudit.service.impl;

import org.async.codeaudit.common.R;
import org.async.codeaudit.service.TraversalService;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class TraversalServiceImpl implements TraversalService {
    /** 让用户提交有用户访问服务器文件的功能页面，
 * 审计这个页面
 * 其实不管用户怎么样，如果没有使用ID索引（{id}）这种方式来下载文件，就直接化为蓝色等级及蓝色等级以下的等级
 * 如果有进行过滤的操作则为蓝色
 * 没有直接抛出红色
 * （这一个是逻辑最简单的一个了吧）
     */
    @Override
    public R<String> code(String code) {
        //如果是路径拼接样式
        Pattern reg1 = Pattern.compile("System.getProperty.+\\+.+;");
        if(reg1.matcher(code).find()){
            //如果是路径拼接，寻找有没有进行拼接过滤
            if(code.contains("..")||code.contains(".")){
                //表示用户可能进行了预处理（这里误报率太高了）
                //算了以后优化吧
                return R.result("用了路径拼接，但是对拼接做了处理哦","建议采用ID索引方式","blue");
            }
           else{
                //这里肯定是未处理了
               return  R.result("路径拼接且未处理","存在严重的路径穿越漏洞","red");
            }
        }
        else return R.result("没有出现路径拼接问题","通过","green");
    }
}
