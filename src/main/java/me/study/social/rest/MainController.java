package me.study.social.rest;

import me.study.social.bean.Password;
import me.study.social.bean.Rest;
import me.study.social.bean.UserInfo;
import me.study.social.repository.PasswordRepository;
import me.study.social.repository.UserInfoRepository;
import org.springframework.data.domain.Example;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class MainController {
    @Resource
    UserInfoRepository userInfoRepository;
    @Resource
    PasswordRepository passwordRepository;

    @PostMapping("/user/login")
    public Rest login(@RequestBody Password password, HttpServletRequest request) {
        if (StringUtils.isEmpty(password.getUsername()) || StringUtils.isEmpty(password.getPassword()))
            return Rest.builder().code(1).msg("用户名或密码不能为空");
        if (passwordRepository.findAll(Example.of(password)).size() <= 0)
            return Rest.builder().msg("用户名不存在或密码错误");
        UserInfo user = userInfoRepository.findFirstByUsername(password.getUsername());
        HttpSession session = request.getSession();
        session.setAttribute("_id", user.get_id());

        return Rest.builder().msg("登录成功").set("userInfo", user);
    }


    @PostMapping("/user/register")
    public Rest register(@RequestBody Password password, HttpServletRequest request) {
        if (StringUtils.isEmpty(password.getUsername())) return Rest.builder().code(1).msg("用户名不能为空");
        if (StringUtils.isEmpty(password.getPassword())) return Rest.builder().code(2).msg("密码不能为空");
        if (!password.getRepassword().equals(password.getPassword())) return Rest.builder().code(3).msg("两次密码必须一致");
        if (passwordRepository.findAll(Example.of(password)).size() > 0) return Rest.builder().code(4).msg("用户已存在");
        passwordRepository.save(password);
        UserInfo userInfo = UserInfo.builder().username(password.getUsername()).build();
        userInfoRepository.save(userInfo);
        return Rest.builder().msg("注册成功");
    }

    @GetMapping("/user/logout")
    public Rest logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute("userInfo");

        return Rest.builder().msg("登出成功");
    }
}
