package me.study.social.rest;

import me.study.social.bean.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/chat")
public class ChatController {
    private static Logger logger = LoggerFactory.getLogger(ChatController.class);
    @PostMapping("/chatwith")
    public Rest addChatWith(@RequestBody ChatContent body, HttpServletRequest request){
        HttpSession session = request.getSession();

        return Rest.builder().code(0).set("data", ChatContent.builder().build());
    }
    @PostMapping("/clearchatunread")
    public Rest clearChatUnread(@RequestBody Map body, HttpServletRequest request){
        HttpSession session = request.getSession();

        return Rest.builder();
    }
    @GetMapping("/chatwith")
    public Rest listChatWith(@RequestBody Map body, HttpServletRequest request){
        HttpSession session = request.getSession();

        return Rest.builder().code(1).msg("暂无聊天记录");
    }
    @GetMapping("/chatlist")
    public Rest chatList(@RequestParam Map param, HttpServletRequest request){
        HttpSession session = request.getSession();
        return Rest.builder().msg("登录成功").set("chatList", Collections.emptyList());
    }
}
