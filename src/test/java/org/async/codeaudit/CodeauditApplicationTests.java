package org.async.codeaudit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
class CodeauditApplicationTests {

    @Test
    void reg() {
        Pattern pattern = Pattern.compile("Runtime\\.getRuntime\\(\\)\\.exec\\((.+)\\);");
        Matcher matcher = pattern.matcher("String test = \";echo 1 > 1.txt\";\n" +
                "        String Command = \"ping 127.0.0.1\" + test;\n" +
                "\n" +
                "        try {\n" +
                "            Runtime.getRuntime().exec(Command);\n" +
                "        } catch (IOException e) {\n" +
                "            e.printStackTrace();\n" +
                "        }\n" +
                "        response.addHeader(\"Content-Length\", \"\" + file.length());\n" +
                "        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());\n" +
                "        response.setContentType(\"application/octet-stream\");\n" +
                "        toClient.write(buffer);\n" +
                "        toClient.flush();\n" +
                "        toClient.close();\n" +
                "        return \"下载文件成功：\" + filePath;\n" +
                "                    ");
        System.out.println(matcher.find());
        String group = matcher.group();
        String substring = group.substring("Runtime.getRuntime().exec(".length(),group.lastIndexOf(")"));
        Pattern compile = Pattern.compile("String " + substring);
        System.out.println(substring);
        Matcher matcher1 = compile.matcher("\"String test = \\\";echo 1 > 1.txt\\\";\\n\" +\n" +
                "                \"        String Command = \\\"ping 127.0.0.1\\\" + test;\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"        try {\\n\" +\n" +
                "                \"            Runtime.getRuntime().exec(Command);\\n\" +\n" +
                "                \"        } catch (IOException e) {\\n\" +\n" +
                "                \"            e.printStackTrace();\\n\" +\n" +
                "                \"        }\\n\" +\n" +
                "                \"        response.addHeader(\\\"Content-Length\\\", \\\"\\\" + file.length());\\n\" +\n" +
                "                \"        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());\\n\" +\n" +
                "                \"        response.setContentType(\\\"application/octet-stream\\\");\\n\" +\n" +
                "                \"        toClient.write(buffer);\\n\" +\n" +
                "                \"        toClient.flush();\\n\" +\n" +
                "                \"        toClient.close();\\n\" +\n" +
                "                \"        return \\\"下载文件成功：\\\" + filePath;\\n\" +\n" +
                "                \"                    \"");
        System.out.println(matcher1.find());

    }
 @Test
    void encode(){
     System.out.println(DigestUtils.md5DigestAsHex("123456".getBytes(StandardCharsets.UTF_8)));
 }
}
