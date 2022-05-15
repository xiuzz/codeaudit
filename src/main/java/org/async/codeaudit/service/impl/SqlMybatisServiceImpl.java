package org.async.codeaudit.service.impl;

import org.async.codeaudit.common.R;
import org.async.codeaudit.service.SqlMybatisService;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class SqlMybatisServiceImpl implements SqlMybatisService {
    @Override
    public R<String> code(String code) {
        R<String> r=null;
        Pattern p=Pattern.compile("\\$.*}");
        //mybatis是否使用了${}
        if(p.matcher(code).find()){
            //是否是order by语句
           p=Pattern.compile("\\border by\\b.\\$.*}");
           if(p.matcher(code).find()){
               r=R.result("orderBy语句,可对传入数据进行Integer转型，或者进行排序映射","可能存在sql注入风险","yellow");
           }
          else{
               r=R.result("未使用预编译的#{}方式","存在严重sql注入风险","red");
           }
        }
        else{
            r=R.result(null,"通过","green");
        }
        return r;
    }
}
