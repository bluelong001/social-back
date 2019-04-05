package me.study.social.rest;

import me.study.social.bean.Pyq;
import me.study.social.bean.Rest;
import me.study.social.bean.UserInfo;
import me.study.social.repository.PyqRepository;
import me.study.social.repository.UserInfoRepository;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("pyq")
public class PyqController {
    @Resource
    PyqRepository pyqRepository;
    @Resource
    UserInfoRepository userInfoRepository;

    @GetMapping
    public Rest pyq(@RequestParam Map param, HttpServletRequest request){
        HttpSession session=request.getSession();
        Integer _id = (Integer) session.getAttribute("_id");
        List<Pyq> pyqList=pyqRepository.findAllByWriterId(_id);
        UserInfo userInfo = userInfoRepository.getOne(_id);
        pyqList.forEach((pyq)-> {
            pyq.setPimg(Arrays.asList(pyq.getPimgArr().split(";")));
            pyq.setWriter(userInfo);
            pyq.setComments(Collections.emptyList());
        });
        return Rest.builder().set("data", pyqList);
    }

    @PostMapping("/delpyq")
    public Rest delPyq(@RequestBody Map param){
        pyqRepository.deleteById((Integer) param.get("id"));
        return Rest.builder().msg("删除成功");
    }
}
