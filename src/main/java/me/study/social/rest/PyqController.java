package me.study.social.rest;

import me.study.social.bean.Comment;
import me.study.social.bean.Pyq;
import me.study.social.bean.Rest;
import me.study.social.bean.UserInfo;
import me.study.social.repository.CommentRepository;
import me.study.social.repository.PyqRepository;
import me.study.social.repository.UserInfoRepository;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@RestController
@RequestMapping("pyq")
public class PyqController {
    @Resource
    PyqRepository pyqRepository;
    @Resource
    UserInfoRepository userInfoRepository;
    @Resource
    CommentRepository commentRepository;

    @GetMapping
    public Rest pyq(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Integer _id = (Integer) session.getAttribute("_id");
        List<Pyq> pyqList =new ArrayList<>();
                pyqList=pyqRepository.findAll();
        UserInfo userInfo = userInfoRepository.getOne(_id);
        pyqList.forEach((pyq) -> {
            pyq.setPimg(Arrays.asList(pyq.getPimgArr().split(";")));
            pyq.setWriter(userInfoRepository.findById(pyq.getWriterId()).orElse(null));
            List<Comment> list = commentRepository.findAllByPyqId(pyq.get_id());
            list.forEach(comment -> {
                comment.setReply(Collections.emptyList());
//                        comment.setReply();
            });
            pyq.setComments(list);
        });
        Collections.sort(pyqList);
        return Rest.builder().set("data", pyqList).set("userInfo", userInfo);
    }

    @PostMapping("/delpyq")
    public Rest delPyq(@RequestBody Map param) {
        pyqRepository.deleteById((Integer) param.get("id"));
        return Rest.builder().msg("删除成功");
    }

    @GetMapping("/tPyqList")
    public List<Pyq> toPyqList(Integer id) {
        List<Pyq> pyqList = pyqRepository.findAllByWriterId(id);
        pyqList.forEach(pyq -> {
            pyq.setPimg(Arrays.asList(pyq.getPimgArr().split(";")));
            pyq.setWriter(userInfoRepository.getOne(id));
            pyq.setComments(Collections.emptyList());
        });
        return pyqList;


    }
}
