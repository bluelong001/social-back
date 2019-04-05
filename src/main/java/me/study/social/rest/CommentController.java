package me.study.social.rest;

import me.study.social.bean.ChatContent;
import me.study.social.bean.Password;
import me.study.social.bean.Rest;
import me.study.social.bean.UserInfo;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @PostMapping
    public Rest main( HttpServletRequest request) {

        return Rest.builder().msg("登录成功").set("userInfo", UserInfo.builder().build());
    }

    @GetMapping("/getcom")
    public Rest getCom(@RequestBody Password password, HttpServletRequest request) {
        HttpSession session = request.getSession();

        return Rest.builder().msg("登录成功").set("userInfo", UserInfo.builder().username(password.getUsername()).build());
    }

    @GetMapping("/clearcomunread")
    public Rest clearComUnread(String username, HttpServletRequest request) {
        HttpSession session = request.getSession();

        return Rest.builder().msg("登录成功");
    }

    @GetMapping("/getmsg")
    public Rest getMsg(String username, HttpServletRequest request) {
        HttpSession session = request.getSession();

        return Rest.builder().set("result", UserInfo.builder().build());
    }
}
