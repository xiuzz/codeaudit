package org.async.codeaudit.controller.audit;

import org.async.codeaudit.common.R;
import org.async.codeaudit.service.DeserializeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import javax.annotation.Resource;
import java.io.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * 反序列漏洞
 */
@RestController
@RequestMapping("/deserialize")
public class DeserializeController {
    @Resource
    private DeserializeService deserializeService;
    /**
     * 反序列化漏洞
     * 代码审计：java序列化的数据一般会以标记(ac ed 00 05)开头，base64编码后的特征为rO0AB
     * 反序列化函数调用点：
     * ObjectInputStream.readObject
     * ObjectInputStream.readUnshared
     * XMLDecoder.readObject
     * Yaml.load
     * XStream.fromXML
     * ObjectMapper.readValue
     * JSON.parseObject
     */
    @PostMapping("/code")
   public R<String> code(MultipartFile file){
       return  null;
   }
        @GetMapping("study/test")
        public String test() {
            String obj = "hello world!";
            try {
                // 创建一个包含对象进行反序列化信息的”object”数据文件
                FileOutputStream fos = new FileOutputStream("object");
                ObjectOutputStream os = new ObjectOutputStream(fos);

                // writeObject()方法将obj对象写入object文件
                os.writeObject(obj);
                os.close();
                // 从文件中反序列化obj对象
                FileInputStream fis = new FileInputStream("object");
                ObjectInputStream ois = new ObjectInputStream(fis);

                // 恢复对象
                String obj2 = (String) ois.readObject();
                ois.close();
                return obj2;

            } catch (Exception e) {
                return e.toString();
            }
        }

        /**
         * @vul readObject，读取输入流,并转换对象。ObjectInputStream.readObject() 方法的作用正是从一个源输入流中读取字节序列，再把它们反序列化为一个对象。如果此时ObjectInputStream对象的初始化参数来自外部请求输入参数则基本可以确定存在反序列化漏洞
         * @poc http://127.0.0.1:8888/Deserialize/readObject?base64=payload
         * payload：java -jar ysoserial-0.0.6-SNAPSHOT-BETA-all.jar CommonsCollections5 "open -a Calculator" | base64
         */
        @RequestMapping("study/readObject")
        public String readObject(String base64) {
            try {
                System.out.println("[vul] 执行反序列化");
                BASE64Decoder decoder = new BASE64Decoder();

                // 坑：读取的base64内容，加号会被转空格
                base64 = base64.replace(" ", "+");

                byte[] bytes = decoder.decodeBuffer(base64);

                // 将字节转为输入流
                ByteArrayInputStream stream = new ByteArrayInputStream(bytes);

                // 反序列化流，将序列化的原始数据恢复为对象
                ObjectInputStream in = new ObjectInputStream(stream);
                in.readObject();
                in.close();
                return "反序列化漏洞";
            } catch (Exception e) {
                return "需生成Payload：java -jar ysoserial-0.0.6-SNAPSHOT-BETA-all.jar CommonsCollections5 \"open -a Calculator\" | base64";
            }
        }

        /**
         * Apache Commons IO的ValidatingObjectInputStream。使用ValidatingObjectInputStream来校验反序列化的类
         *
         * @safe accept方法来实现反序列化类白/黑名单控制

        @RequestMapping("/safe")
        public String safe(String base64) {
            try {
                System.out.println("[safe] 执行反序列化");
                BASE64Decoder decoder = new BASE64Decoder();

                // 坑：读取的base64内容，加号会被转空格
                base64 = base64.replace(" ", "+");

                byte[] bytes = decoder.decodeBuffer(base64);

                // 将字节转为输入流
                ByteArrayInputStream stream = new ByteArrayInputStream(bytes);

                // 使用 ValidatingObjectInputStream
                ValidatingObjectInputStream ois = new ValidatingObjectInputStream(stream);

                // 只允许反序列化Student class
                ois.accept(Student.class);
                Object obj = ois.readObject();

                return "ValidatingObjectInputStream";
            } catch (Exception e) {
                return e.toString();
            }
        }
*/
        /**
         * @safe 禁止 JVM 执行外部命令 Runtime.exec，通过扩展 SecurityManager 可以实现。
         */

        /**
         * @vul Java RMI Registry 反序列化漏洞(<=jdk8u111)
         * 检测：java -cp ysoserial.jar ysoserial.exploit.RMIRegistryExploit ip 9999 JRMPClient "rmi.c3blgk.dnslog.cn"
         */
        @RequestMapping("study/rmi")
        public String rmi() {
            try {
                Registry registry = LocateRegistry.createRegistry(9999);
                System.out.println("[+] RMI Listening: 9999");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "开启RMI监听，端口：9999";
        }

}
