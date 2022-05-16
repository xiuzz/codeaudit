package org.async.codeaudit.controller.audit;


import lombok.extern.slf4j.Slf4j;
import org.async.codeaudit.common.R;
import org.async.codeaudit.service.UploadService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
/**
 * java类的文件上传漏洞
 * 文件上传漏洞，是指用户上传了一个可执行的脚本文件(如jsp\php\asp)，并通过此脚本文件获得了执行服务器端命令的能力。常见场景是web服务器允许用户上传图片或者普通文本文件保存，这种漏洞属于低成本高杀伤力
 * 文件类型限制
 * 必须在服务器端采用白名单方式对上传或下载的文件类型、大小进行严格的限制。仅允许业务所需文件类型上传，避免上传.jsp、.jspx、.html、.exe等文件
 */
@RestController
@Slf4j
@RequestMapping("/upload")
public class UploadController {
    @Resource
   private UploadService uploadService;
    // 设置保存文件的路径，不安全的web路径下
    private static final String UPLOADED_FOLDER = System.getProperty("user.dir") + "/src/main/resources/static/file/";

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
     * @param file
     * @return
     */
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
        return  uploadService.code(code);
    }
    /**
     * @vul 上传文件到前端页面，未做任何处理(这里其实不是很容易向用户展示这样的风险)
     * 如果让用户上传他们的文件，可能把我们的网页搞崩了
     * 上传我们的文件效果又不是很好，全部做成静态界面算了
     * @poc http:///study/uploadVul
     */
    @PostMapping("study/uploadVul")
    public String singleFileUpload( MultipartFile file,
                                   RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "请选择要上传的文件");
            return "redirect:upload";

        }

        try {
            byte[] bytes = file.getBytes();
            Path dir = Paths.get(UPLOADED_FOLDER);
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());

            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
            }
            Files.write(path, bytes);
            System.out.println("[+] 上传文件成功：" + path);
            redirectAttributes.addFlashAttribute("message",
                    "上传文件成功：" + path + "");

        } catch (Exception e) {
            return e.toString();
        }
        return "redirect:upload";
    }


    /**
     * @safe 白名单后缀名处理
     * spring提供的白名单
     * @poc http:///study/uploadSafe
     */
    @PostMapping("/uploadSafe")
    public String singleFileUploadSafe(@RequestParam("file") MultipartFile file,
                                       RedirectAttributes redirectAttributes) {

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "请选择要上传的文件");
            return "redirect:upload";
        }

        try {
            byte[] bytes = file.getBytes();
            String fileName = file.getOriginalFilename();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());

            // 获取文件后缀名，并且索引到最后一个，避免使用.jpg.jsp来绕过
            String Suffix = fileName.substring(fileName.lastIndexOf("."));

            String[] SuffixSafe = {".jpg", ".png", ".jpeg", ".gif", ".bmp", ".ico"};
            boolean flag = false;

            // 如果满足后缀名单，返回true
            for (String s : SuffixSafe) {
                if (Suffix.toLowerCase().equals(s)) {
                    flag = true;
                    break;
                }
            }

            System.out.println("[*] 尝试上传文件：" + fileName);

            if (!flag) {
                redirectAttributes.addFlashAttribute("message",
                        "只允许上传图片，[.jpg, .png, .jpeg, .gif, .bmp, .ico]");
            } else {
                Files.write(path, bytes);
                redirectAttributes.addFlashAttribute("message",
                        "上传文件成功：" + path + "");
            }

        } catch (Exception e) {
            return e.toString();
        }
        return "redirect:upload";
    }

}
