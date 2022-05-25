package org.async.codeaudit.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.async.codeaudit.common.R;
import org.async.codeaudit.entiy.Result;
import org.async.codeaudit.service.ResultService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
@Slf4j
@RestController
@RequestMapping("result")
public class ResultController {
    @Resource
    private ResultService resultService;
    //直接发送请求就可以了，不需要携带参数，直接通过session存的uid查
    @GetMapping("/get")
    public R<List<Result>> getByUId(HttpServletRequest request){
        Long user =(Long)request.getSession().getAttribute("user");
        LambdaQueryWrapper<Result> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Result::getUid,user);
        List<Result> list = resultService.list(lambdaQueryWrapper);
        log.info("查询到的结果集{}",list);
        return R.success(list);
    }
    @PostMapping("/set")
   public R<String> setByUId(@RequestBody Result result,HttpServletRequest request){
          result.setSendTime(LocalDateTime.now());
        long id = Thread.currentThread().getId();
        log.info(result.toString());
        Long uid =(Long)request.getSession().getAttribute("user");
        result.setUid(uid);
        log.info(result.toString());
        resultService.save(result);
       return R.success("接收成功");
    }
}
