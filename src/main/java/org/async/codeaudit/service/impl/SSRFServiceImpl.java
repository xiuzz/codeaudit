package org.async.codeaudit.service.impl;

import org.async.codeaudit.common.R;
import org.async.codeaudit.service.SSRFService;
import org.springframework.stereotype.Service;

@Service
public class SSRFServiceImpl implements SSRFService {
    @Override
    public R<String> code(String code) {
        String[] regs={".setInstanceFollowRedirects(false);",
                       ".setUseCaches(false);",
                        ".setConnectTimeout(connectTime);",
                        ".connect();",
                         "!=HttpURLConnection.HTTP_OK"};
        int count=0;
        for (String reg : regs) {
            if(code.contains(reg))
                count++;
        }
        if(count==0)return R.result("没有进行过滤","有很大SSRF攻击风险","red");
        else if(count<=3)return R.result("较小过滤","可能存在SSRF攻击","blue");
        else return  R.result("较全过滤","通过","green");
    }
}
