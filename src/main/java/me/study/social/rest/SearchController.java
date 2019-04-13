package me.study.social.rest;

import me.study.social.bean.Pyq;
import me.study.social.bean.Rest;
import me.study.social.bean.UserInfo;
import me.study.social.repository.PyqRepository;
import me.study.social.repository.UserInfoRepository;
import me.study.social.service.SocketIOService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("search")
public class SearchController {

    @Resource
    UserInfoRepository userInfoRepository;

    @Resource
    PyqRepository pyqRepository;
    @Resource
    SocketIOService socketIOService;

    @GetMapping
    private Rest search(String searchText){
//        socketIOService
        UserInfo user=userInfoRepository.findFirstByUsername(searchText);
        List<Pyq> pyqList = pyqRepository.findAllByContentLike(searchText);
        return Rest.builder().code(0).set("user",user).set("pyqList",pyqList);
    }
}
