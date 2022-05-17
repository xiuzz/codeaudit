package org.async.codeaudit.service.impl;

import org.async.codeaudit.common.R;
import org.async.codeaudit.service.TraversalService;
import org.springframework.stereotype.Service;

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
      if(code.contains("System.Property"))return  null;
      return null;
    }
}
