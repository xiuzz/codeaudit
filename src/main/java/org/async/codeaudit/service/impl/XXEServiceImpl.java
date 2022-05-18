package org.async.codeaudit.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.async.codeaudit.service.XXEService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/xxe")
@RestController
public class XXEServiceImpl implements XXEService {

}
