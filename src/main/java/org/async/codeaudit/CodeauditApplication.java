package org.async.codeaudit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@Slf4j
@SpringBootApplication
@ServletComponentScan
public class CodeauditApplication {

    public static void main(String[] args) {
        SpringApplication.run(CodeauditApplication.class, args);
        log.info(" 启动成功");
    }

}
