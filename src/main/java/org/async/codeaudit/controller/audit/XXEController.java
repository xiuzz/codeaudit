package org.async.codeaudit.controller.audit;

import lombok.extern.slf4j.Slf4j;
import org.async.codeaudit.common.R;
import org.async.codeaudit.service.XXEService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
@Slf4j
@RestController
@RequestMapping("/xxe")
public class XXEController {
    @Resource
    private XXEService xxeService;
    @PostMapping("/code")
    public R<String> code(MultipartFile file){
        return  null;
    }
}
