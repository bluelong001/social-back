package me.study.social.rest;

import me.study.social.bean.*;
import me.study.social.repository.CommentRepository;
import me.study.social.repository.PyqRepository;
import me.study.social.repository.UserInfoRepository;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Resource
    CommentRepository commentRepository;
    @Resource
    UserInfoRepository userInfoRepository;
    @Resource
    PyqRepository pyqRepository;

    @PostMapping
    public Rest main(@RequestBody Map body, HttpServletRequest request) {
        Comment comment = Comment.builder()
                .content((String) body.get("content"))
                .from((String) body.get("from"))
                .to(StringUtils.isEmpty((String) body.get("to")) ? "" : (String) body.get("to"))
                .pyqId((Integer) body.get("pyq"))
                .writer((String) body.get("writer"))
                .build();
        comment = commentRepository.save(comment);
        return Rest.builder().msg("评论成功").code(0);
    }

    @GetMapping("/getcom")
    public Rest getCom(String username, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Integer _id;
        if (null != (_id = (Integer) session.getAttribute("_id"))) ;
        else return null;
        UserInfo userA = userInfoRepository.findById(_id).get();
        List<Comment> commentList = new ArrayList<>();
        commentList.addAll(commentRepository.findAllByFromAndTo(userA.getUsername(), username));
        commentList.addAll(commentRepository.findAllByToAndFrom(userA.getUsername(), username));
        commentList.forEach(comment -> {
            Pyq pyq = pyqRepository.findById(comment.getPyqId()).orElse(null);
            comment.setPyq(pyq.getContent());
            if (!StringUtils.isEmpty(pyq.getPimgArr()))
                comment.setFooterimg(Arrays.asList(pyq.getPimgArr().split(";")).get(0));
            else
                comment.setFooterimg(userInfoRepository.findById(pyq.getWriterId()).get().getAvater());
            comment.setHeaderimg(userA.getAvater());
        });
        return Rest.builder().code(0).set("com", commentList);
    }

    @GetMapping("/clearcomunread")
    public Rest clearComUnread(String username, HttpServletRequest request) {
        HttpSession session = request.getSession();
        List<Comment> list = commentRepository.findAllByTo(username);
        list.forEach(comment -> comment.setToisn(false));
        commentRepository.saveAll(list);
        return Rest.builder().code(0);
    }

    @GetMapping("/getmsg")
    public Rest getMsg(String username, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Integer _id;
        if (null != (_id = (Integer) session.getAttribute("_id"))) ;
        else return null;
        UserInfo userA = userInfoRepository.findById(_id).get();
        List<Comment> commentList = new ArrayList<>();
        commentList.addAll(commentRepository.findAllByFromAndTo(userA.getUsername(), username));
        commentList.addAll(commentRepository.findAllByToAndFrom(userA.getUsername(), username));
        return Rest.builder().set("result",commentList);
    }
}
