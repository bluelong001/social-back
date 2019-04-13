package me.study.social.rest;

import me.study.social.bean.*;
import me.study.social.repository.ChatContentRepository;
import me.study.social.repository.ChatRelationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chat")
public class ChatController {
    private static Logger logger = LoggerFactory.getLogger(ChatController.class);

    @Resource
    ChatContentRepository chatContentRepository;
    @Resource
    ChatRelationRepository chatRelationRepository;

    @PostMapping("/chatwith")
    public Rest addChatWith(@RequestBody Map body, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Integer chatWithId = (Integer) body.get("chatWithId");
        Integer userId = (Integer) body.get("user_id");
        ChatContent content = ChatContent.builder()
                .chatWithId(chatWithId)
                .userId(userId)
                .content(String.valueOf(body.get("content")))
                .unread(false)
                .build();
        content = chatContentRepository.save(content);
        ChatRelation chatRelation = chatRelationRepository.findFirstByUserAIdAndUserBId(chatWithId, userId);
        if (chatRelation == null)
            chatRelation = chatRelationRepository.findFirstByUserBIdAndUserAId(chatWithId, userId);
        if (chatRelation != null) {
            if (!StringUtils.isEmpty(chatRelation.getChatContentIdArr())) {
                List<Integer> arr = Arrays.asList(Integer.parseInt(String.valueOf(chatRelation.getChatContentIdArr().split(";"))));
                arr.remove(content.get_id());

//           chatContentRepository.saveAndFlush()
            } else {
                ChatRelation relation = ChatRelation.builder()
                        .userAId(userId)
                        .userBId(chatWithId)
                        .chatContentIdArr(String.valueOf(content.get_id()))
                        .build();
            }
            return Rest.builder().code(0).set("data", content);
        }

        return Rest.builder().code(0).set("data", ChatContent.builder().build());
    }

    @PostMapping("/clearchatunread")
    public Rest clearChatUnread(@RequestBody Map body, HttpServletRequest request) {
        HttpSession session = request.getSession();

        return Rest.builder();
    }

    @GetMapping("/chatwith")
    public Rest listChatWith(@RequestBody Map body, HttpServletRequest request) {
        HttpSession session = request.getSession();

        return Rest.builder().code(1).msg("暂无聊天记录");
    }

    @GetMapping("/chatlist")
    public Rest chatList(@RequestParam Map param, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Integer userId = (Integer.parseInt(String.valueOf(param.get("user_id"))));
        List<ChatRelation> list = chatRelationRepository.findAllByUserAIdOrUserBId(userId, userId);
        return Rest.builder().msg("登录成功").set("chatList", Collections.emptyList());
    }
}
