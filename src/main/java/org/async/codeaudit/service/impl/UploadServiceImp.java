package org.async.codeaudit.service.impl;


import org.async.codeaudit.common.R;
import org.async.codeaudit.service.UploadService;
import org.springframework.stereotype.Service;

@Service
public class UploadServiceImp implements UploadService{
    /**
     * 因此审计方式也很简单，
     * 只需要检查这个代码是否有文件传输，
     * 但是后端接收文件的方式有多种
     * 主流的三种
     * 第一种通过IO方式如commonsMultipartFile 我们直接扫描IOException(这样做，误报率很大，但是用户一般上传的都是有问题的界面）
     * 第二种,第三种通过ServletFileUpload或者MultipartFile上传，这个直接搜索关键字，误报率低
     * 然后审计用户是否有文件write这个操作，
     * 如果有查看是否有过滤操作
     * 如果有过滤操作
     * .indexOf() 提示blue未过滤干净
     */
    @Override
    public R<String> code(String code) {
        String[] regs={"IOException","ServletFileUpload","MultipartFile","java.IO.File","RequestMethod"};
        boolean loop=false;
        for (String reg : regs) {
            if (code.contains(reg)) {
                loop = true;
                break;
            }
        }
        //如果有这些关键字
      if(loop){
          if(code.contains(".write")){
              if(code.contains(".lastIndexOf(\".\")")){
                   return  R.result("使用了正确的过滤方式","通过","green");
              }
            else if(code.contains(".indexOf(\".\")")||code.contains("contentType")){
                  return  R.result("较简单的过滤方式","未过滤完全","yellow");
              }
            else{
                return  R.result("存在严重的文件上传风险","未过滤","red");
              }
          }
         else {
            return R.result("上传后没有进行写入到前端的危险操作","没有上传风险","green");
          }
      }
      else return  R.result("没有找到上传代码","无法判断","grey");
    }
}
