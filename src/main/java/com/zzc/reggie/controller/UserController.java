package com.zzc.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zzc.reggie.common.R;
import com.zzc.reggie.entity.User;
import com.zzc.reggie.service.UserService;
import com.zzc.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author: 赵智超
 * @date: 2023/07/03/20:45
 * @Description:
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private  UserService userService;
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
        log.info("{}", user);
        String phone = user.getPhone();
        if (StringUtils.isNotEmpty(phone)) {
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            session.setAttribute("code",code);
            return R.success(code);
        }

        return R.error("短信发送失败");
    }


    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session) {
        log.info("{}",map);
        String phone = map.get("phone").toString();
        String code = map.get("code").toString();
        Object codeSession = session.getAttribute("code");
        if (codeSession!=null && codeSession.equals(code)){
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);
            User user = userService.getOne(queryWrapper);
            if (user==null){
                user =new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user",user.getPhone());
            return R.success(user);
        }
        return R.error("登录失败");
    }
}
