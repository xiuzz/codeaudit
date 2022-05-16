package org.async.codeaudit.controller;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jodah.expiringmap.ExpiringMap;
import org.async.codeaudit.common.R;
import org.async.codeaudit.entiy.User;
import org.async.codeaudit.service.UserService;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * 用户控制功能
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
@Resource
    private UserService userService;

    /**
     * 用户登录功能
     * @param httpServletRequest
     * @param user
     * @return
     */
   @PostMapping(value = "/login")
    public R<User> userLogin(HttpServletRequest httpServletRequest,@RequestBody User user){
       //密码md5加密
       String password=DigestUtils.md5DigestAsHex(user.getPassword().getBytes(StandardCharsets.UTF_8));
       //根据用户名查询数据库
       LambdaQueryWrapper<User> lambdaQueryWrapper=new LambdaQueryWrapper<>();
       lambdaQueryWrapper.eq(User::getUserName,user.getUserName());
       User bool = userService.getOne(lambdaQueryWrapper);
       if(bool==null)return  R.error("用户名错误");
       //比对密码
       if(!bool.getPassword().equals(password))return  R.error("密码错误");
   /* String re = UUID.randomUUID().toString();
    expiringMap.put(re,user.getUid());
    log.info(user.toString());*/
       httpServletRequest.getSession().setAttribute("user",user.getUid());
    return R.success(bool);
    }
    /**
     * 用户注册功能
     * @param user
     * @return
     */
    @PostMapping("/resister")
    public R<String> InsertUser(@RequestBody User user){
        log.info("创建用户功能{}",user.toString());
        //判断用户是否存在
        LambdaQueryWrapper<User> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUserName,user.getUserName());
        User boolUser = userService.getOne(lambdaQueryWrapper);
        if(boolUser!=null){
            return  R.error("改用户名已存在");
        }
        //给用户密码加密
       user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes(StandardCharsets.UTF_8)));
        user.setCreateTime(LocalDateTime.now());
        userService.save(user);
        return R.success("注册成功");
    }
    @PutMapping("/update")
    public R<String> updateUser(@RequestBody User user,HttpServletRequest httpServletRequest){//修改用户信息
         user.setUid((Long)httpServletRequest.getSession().getAttribute("user"));
         userService.updateById(user);
        return  R.success("修改成功");
    }

    /**
     * 忘记密码功能
     * 暂时注销
     * @param UID
     * @param password
     * @return
     */
   /*@RequestMapping("/forgetPassWord")
    public Code forget(String UID,String password){
        User user = userMapper.queryUserByUid(UID);
        user.setPassword(password);
        userMapper.updateUser(user);
        return new Code(1,"成功");
    }
   */
}
