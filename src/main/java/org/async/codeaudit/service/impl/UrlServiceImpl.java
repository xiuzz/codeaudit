package org.async.codeaudit.service.impl;

import org.async.codeaudit.service.UrlService;
import org.springframework.stereotype.Service;

@Service
public class UrlServiceImpl implements UrlService {
    /**    
     *重定向错误
     *常用跳转方式
     * 通过ModelAndView方式
     * 通过返回String方式
     * 使用SendRedirect方式
     * 使用RedirectAttributes方式
     * 通过设置Header来进行跳转
     * 其实这个不好审计的
     */
    @Override
    public void code(String code) {

    }
}
