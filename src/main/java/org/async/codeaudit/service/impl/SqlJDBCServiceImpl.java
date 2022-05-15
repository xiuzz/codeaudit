package org.async.codeaudit.service.impl;

import org.async.codeaudit.common.R;
import org.async.codeaudit.service.SqlJDBCService;
import org.springframework.stereotype.Service;
import java.util.regex.Pattern;

@Service
public class SqlJDBCServiceImpl implements SqlJDBCService {
    @Override
    public R<String> code(String code) {
        if(code==null) return R.error("没有写入任何内容");
        R<String> r=null;
            //首先寻找有没有“prepareStatement”关键字
            if(code.contains("prepareStatement(")){
                //如果有，是否存在拼接
                Pattern p=Pattern.compile("\".*\".*\\+.*");
                if(p.matcher(code).find()){
                    //如果存在拼接，不规范
                    r=R.result("未按照预编译规范，使用了错误的sql拼接","存在严重sql注入风险","red");
                }
                else{
                    r=R.result(null,"通过","green");
                }
            }
            //没有
            else{
                //是否是OrderBy语句
                Pattern p=Pattern.compile("\border by\b");
                if(p.matcher(code).find()){
                    r=R.result("orderBy语句,可对传入数据进行Integer转型，","可能存在sql注入风险","yellow");
                }
                else{
                    r=R.result("未使用预编译且没有处理拼接风险","存在严重sql注入风险","red");
                }
            }
        return  r;
    }
}
