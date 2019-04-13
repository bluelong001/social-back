package me.study.social.rest;

import me.study.social.bean.*;
import me.study.social.repository.ChatContentRepository;
import me.study.social.repository.ChatRelationRepository;
import me.study.social.repository.UserInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/chat")
public class ChatController {
    private static Logger logger = LoggerFactory.getLogger(ChatController.class);

    @Resource
    UserInfoRepository userInfoRepository;
    @Resource
    ChatContentRepository chatContentRepository;
    @Resource
    ChatRelationRepository chatRelationRepository;

    @PostMapping("/chatwith")
    public Rest addChatWith(@RequestBody Map body, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Rest rest = Rest.builder();
        Integer chatWithId = (Integer) body.get("chatWithId");
        Integer userId = (Integer) body.get("user_id");
        if (chatWithId == userId) {
            ChatContent newContent = ChatContent.builder()
                    .chatWithId(chatWithId)
                    .userId(userId)
                    .content(String.valueOf(body.get("content")))
                    .unread(false)
                    .build();
            newContent = chatContentRepository.save(newContent);
            ChatRelation chatRelation = chatRelationRepository.findFirstByUserAIdAndUserBId(chatWithId, userId);
            if (chatRelation == null)
                chatRelation = chatRelationRepository.findFirstByUserBIdAndUserAId(chatWithId, userId);
            if (chatRelation != null) {
                if (!StringUtils.isEmpty(chatRelation.getChatContentIdArr())) {
                    String idArr = chatRelation.getChatContentIdArr();
                    idArr += ";" + newContent.get_id();
                    chatRelation.setChatContentIdArr(idArr);
                    chatRelationRepository.saveAndFlush(chatRelation);
                    return rest.code(0).set("data", newContent);
                }
            }
            return rest.code(0).set("data", newContent);

        }

        ChatContent content = ChatContent.builder()
                .chatWithId(chatWithId)
                .userId(userId)
                .content(String.valueOf(body.get("content")))
                .build();
        content = chatContentRepository.saveAndFlush(content);
        ChatRelation chatRelation = chatRelationRepository.findFirstByUserAIdAndUserBId(chatWithId, userId);
        if (chatRelation == null)
            chatRelation = chatRelationRepository.findFirstByUserBIdAndUserAId(chatWithId, userId);
        if (chatRelation != null) {
            if (!StringUtils.isEmpty(chatRelation.getChatContentIdArr())) {
                String idArr = chatRelation.getChatContentIdArr();
                idArr += ";" + content.get_id();
                chatRelation.setChatContentIdArr(idArr);
                chatRelationRepository.saveAndFlush(chatRelation);
                rest.code(0).set("data", content);
            }
        } else {
            ChatRelation relation = ChatRelation.builder()
                    .userAId(userId)
                    .userBId(chatWithId)
                    .chatContentIdArr(String.valueOf(content.get_id()))
                    .build();
            chatRelationRepository.saveAndFlush(relation);
            rest.code(0).set("data", content);
        }

        return rest;


    }

    @PostMapping("/clearchatunread")
    public Rest clearChatUnread(@RequestBody Map body, HttpServletRequest request) {
        HttpSession session = request.getSession();
        List<ChatContent> list = chatContentRepository.findAllByUserIdAndChatWithIdAndUnread((Integer) body.get("to"), (Integer) body.get("from"), true);
        list.forEach(chatContent -> chatContent.setUnread(false));
        chatContentRepository.saveAll(list);
        return Rest.builder();
    }

    @GetMapping("/chatwith")
    public Rest listChatWith(Integer user_id, Integer chatWith, HttpServletRequest request) {
        HttpSession session = request.getSession();
        ChatRelation relation = chatRelationRepository.findFirstByUserAIdAndUserBId(user_id, chatWith);
        if (relation == null) relation = chatRelationRepository.findFirstByUserAIdAndUserBId(chatWith, user_id);
        if (null == relation) return Rest.builder().code(1).msg("暂无聊天记录");
        List<String> ids = Arrays.asList(relation.getChatContentIdArr().split(";"));
        if (ids.size() <= 0) return Rest.builder().code(1).msg("暂无聊天记录");
        List<ChatContent> chatContents = new ArrayList<>();
        ids.forEach(id -> {
            ChatContent content = chatContentRepository.findById(Integer.parseInt(id)).orElse(null);
            content.setUser_id(userInfoRepository.findById(content.getUserId()).orElse(null));
            content.setChatWith(userInfoRepository.findById(content.getChatWithId()).orElse(null));
            chatContents.add(content);
        });
        Collections.sort(chatContents);
        relation.setChatContent(chatContents);
        return Rest.builder().code(0).set("chatList", chatContents);
    }

    @GetMapping("/chatlist")
    public Rest chatList(Integer user_id, HttpServletRequest request) {
        HttpSession session = request.getSession();
        List<Map<String, Object>> data = new ArrayList<>();
        List<ChatRelation> list = chatRelationRepository.findAllByUserAIdOrUserBId(user_id, user_id);
        list.forEach(relation -> {
            List<String> ids = Arrays.asList(relation.getChatContentIdArr().split(";"));
            AtomicInteger time = new AtomicInteger();
            ChatContent content1 = chatContentRepository.findById(Integer.parseInt(ids.get(0))).orElse(null);
            Map<String, Object> map = new HashMap<>();
            Integer chatWithId = content1.getChatWithId() == user_id ? content1.getUserId() : content1.getChatWithId();
            map.put("chatWith", userInfoRepository.findById(chatWithId));
            map.put("addTime", content1.getAddTime());
            map.put("content", content1.getContent());
            ids.forEach(id -> {
                ChatContent content = chatContentRepository.findById(Integer.parseInt(id)).orElse(null);
                if (content.getChatWithId() == user_id)
                    time.getAndIncrement();
                map.put("unread", time.get());
            });
            data.add(map);
        });
        return Rest.builder().code(0).set("chatList", data);
    }
}
