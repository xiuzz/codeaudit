package org.async.codeaudit.controller;

import lombok.extern.slf4j.Slf4j;
import org.async.codeaudit.common.CheckCodeUtil;
import org.async.codeaudit.common.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/common")
/**
 * 一些普适功能，验证码，文件上传下载
 */
public class CommonController {
    /**
     * 获取验证码请求
     * 后端向前端写入一张图片
     * @return
     */
   @GetMapping("/getCheck")
    public void  getCheckCode(HttpServletResponse response, HttpServletRequest request){
       //生成验证码
       ServletOutputStream os = null;
       HttpSession session = request.getSession();
       try {
           os = response.getOutputStream();
           String checkCode = CheckCodeUtil.outputVerifyImage(100, 50, os, 4);
           log.info("验证码{}",checkCode);
         session.setAttribute("checkCode",checkCode);
       } catch (IOException e) {
           e.printStackTrace();
       }
   }

    /**
     * 检查验证码
     * @param code
     * @param request
     * @return
     */
   @GetMapping("/sendCheck")
   public R<String>  sendCheck( String code,HttpServletRequest request){
       String checkCode = (String)request.getSession().getAttribute("checkCode");
       if(checkCode.equalsIgnoreCase(code)) return  R.success("验证通过");
       else  return  R.error("验证失败");
   }
}
