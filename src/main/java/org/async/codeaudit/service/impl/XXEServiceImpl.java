package org.async.codeaudit.service.impl;

import org.async.codeaudit.common.R;
import org.async.codeaudit.service.XXEService;
import org.springframework.stereotype.Service;
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
 * 审计方式为查看用户是否禁用DTD
 * 所以其实也没什么好讲的
 * 直接搜索就行了
 */
@Service
public class XXEServiceImpl implements XXEService {
    @Override
    public R<String> code(String code) {
        String[] regs={"XMLReadr","SAXBuilder","SAXReader","Digester","DocumentBuilderFactory"};
        String[] reg2s={"http://apache.org/xml/features/disallow-doctype-decl",
                        "http://xml.org/sax/features/external-general-entities",
                        "http://xml/ogr/sax/features/external-parameter-entites"};
        for (String reg : regs) {
            if(code.contains(reg)){
                for (String reg2 : reg2s) {
                    if(code.contains(reg2))
                        return R.result("禁用了DTD解析","通过","green");
                }
                return R.result("没有禁用DTD解析","存在xxe风险","red");
            }
        }
        return R.result("没有审计到关键信息","无法判断","grey");
    }
}
