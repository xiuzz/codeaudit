package org.async.codeaudit.service;

import org.async.codeaudit.common.R;

public interface UploadService {
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
     * .indexOf() 提示blue 为过滤干净
     */
    public R<String> code(String code);
}
