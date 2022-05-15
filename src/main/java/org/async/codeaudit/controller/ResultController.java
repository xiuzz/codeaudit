package org.async.codeaudit.controller;

import org.async.codeaudit.common.R;
import org.async.codeaudit.entiy.Result;
import org.async.codeaudit.service.ResultService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class ResultController {
    @Resource
    private ResultService resultService;
    @RequestMapping("/user/getResult/{start}")
    public R<List<Result>> getByUId(String uid, @PathVariable("start") int start){
        return resultService.getByUId(uid,start);
    }
}
