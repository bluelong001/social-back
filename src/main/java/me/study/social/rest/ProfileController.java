package me.study.social.rest;

import me.study.social.bean.Rest;
import me.study.social.bean.UserInfo;
import me.study.social.repository.UserInfoRepository;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    @Resource
    UserInfoRepository userInfoRepository;
    @GetMapping
    public Rest profile(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Integer _id;
        if (null!=(_id = (Integer) session.getAttribute("_id")));
        else
            return Rest.builder().code(1);
        UserInfo user = userInfoRepository.getOne(_id);
        return Rest.builder().set("userInfo", user);
    }

    @PostMapping("/us")
    public Rest us(@RequestBody Map param){
        UserInfo userInfo = userInfoRepository.findFirstByUsername((String) param.get("username"));
        userInfo.setSignature((String) param.get("newval"));
        userInfoRepository.saveAndFlush(userInfo);
        return Rest.builder().code(0).msg("修改成功");
    }

    @GetMapping("/userInfo")
    public UserInfo userInfo(String name){
        return userInfoRepository.findFirstByUsername(name);
    }
}
