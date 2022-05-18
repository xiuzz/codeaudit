package org.async.codeaudit.controller.audit.sql;

import lombok.extern.slf4j.Slf4j;
import org.async.codeaudit.common.R;
import org.async.codeaudit.common.Security;
import org.async.codeaudit.service.TraversalService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * 路径穿越（目录遍历）,
 * 应用系统在处理下载文件时未对文件进行过滤，
 * 系统后台程序程序中如果不能正确地过滤客户端提交的../和./之类的目录跳转符，攻击者可以通过输入../进行目录跳转，
 * 从而下载、删除任意文件。
 *漏洞的成因很简单
 * 就是建立一个让用户可以访问当前目录下的某个文件业务，
 * 但是未对这个业务的路径进行安全过滤
 * 让用户可以运用../穿梭到想去的位置，删除或者下载我们服务端的文件
 * 审计方式也特别简单
 * 让用户提交有用户访问服务器文件的功能页面，
 * 审计这个页面
 * 其实不管用户怎么样，如果没有使用ID索引（{id}）这种方式来下载文件，就直接化为蓝色等级及蓝色等级以下的等级
 * 如果有进行过滤的操作则为蓝色
 * 没有直接抛出红色
 * （这一个是逻辑最简单的一个了吧）
 */
@Slf4j
@RestController
@RequestMapping("/traversal")
public class TraversalController {
    @Resource
    TraversalService traversalService;
    @PostMapping("/code")
    public R<String> code(MultipartFile file){
        if(file.isEmpty())return  R.error("文件为空！");
        BufferedReader bufferedReader= null;
        log.info(file.toString());
        String code=null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            StringBuilder temp=new StringBuilder();
            while((code=bufferedReader.readLine())!=null){
                temp.append(code);
            }
            code=temp.toString();
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  traversalService.code(code);
    }
    /**
     * 这里可以模拟
     * 实际想发送/当前目录下的文件
     *进行 ，，/路径穿越演示，
     * 最后我们向前端发送两次不一样的文件
     * @poc http:///traversal/study/download?filename=../../../../../../../etc/passwd
     */
    @GetMapping("study/download")
    public String download(String filename, HttpServletRequest request, HttpServletResponse response) {
        // 下载的文件路径
        String filePath = System.getProperty("user.dir") + "/logs/" + filename;
        System.out.println("[*] 文件目录: " + filePath);

        // 使用流的形式下载文件
        try {
            // 加载文件
            File file = new File(filePath);
            InputStream fis = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();

            // 设置response的Header
            response.reset();
            response.addHeader("Content-Disposition", "attachment;filename=" + filename);
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
            return "下载文件成功：" + filePath;
        } catch (Exception e) {
            e.printStackTrace();
            return "未找到文件：" + filePath;
        }
    }

    /**
     * 采用过滤的方法
     * 到时候再让前端发送一样的请求
     * @poc http:///study/traversal/download/safe?filename=../
     */
    @GetMapping("study/download/safe")
    public String download_safe(String filename) {

        if (!Security.checkTraversal(filename)) {
            String filePath = System.getProperty("user.dir") + "/logs/" + filename;
            return "安全路径：" + filePath;
        } else {
            return "检测到非法遍历";
        }
    }
}
