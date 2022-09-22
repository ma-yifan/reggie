package com.mayifan.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mayifan.common.R;
import com.mayifan.common.SMSUtils;
import com.mayifan.common.ValidateCodeUtils;
import com.mayifan.pojo.User;
import com.mayifan.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {


    @Resource
    private UserService userService;
    @PostMapping("/sendMsg")
    public void sendMsg(@RequestBody User user, HttpServletRequest request) {
        //获取电话号码
        String phoneNumber = user.getPhone();
        //生成6位的随机数
        String code = ValidateCodeUtils.generateValidateCode(6).toString();//以字符串的形式好存储
        log.info("用户的电话是{}",phoneNumber);
        log.info("验证码是{}",code);
        //发送验证码
//        SMSUtils.sendMessage("阿里云短信测试","SMS_154950909",phoneNumber,code);
        //将验证码存在session中，并设置保存时间
        HttpSession session = request.getSession();
        session.setAttribute(phoneNumber,code);
        session.setMaxInactiveInterval(60*15);
    }

    /*
    RequestBody也可以标注Map
     */
    @PostMapping("/login")
    public R<String> login(@RequestBody Map map,HttpSession session){
        //从session域中获取code进行比对
        String phoneNumber = map.get("phone").toString();
        String code = map.get("code").toString();
        String codeInSession = session.getAttribute(phoneNumber).toString();
        if (codeInSession != null && codeInSession.equals(code)) {
            //登录成功，新用户则进行注册添加
            User select_user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getPhone, phoneNumber));
            if (select_user == null) {
                User user = new User();
                user.setPhone(phoneNumber);
                userService.save(user);
                session.setAttribute("user",user.getId());
                return R.success("登录成功");
            }
            session.setAttribute("user",select_user.getId());
            return R.success("登录成功");
        }
        return R.error("登录失败！");
    }

    @PostMapping("/loginout")
    public R<String> loginOut(HttpSession seesion){
        seesion.removeAttribute("user");
        return R.success("退出成功！");
    }
}
