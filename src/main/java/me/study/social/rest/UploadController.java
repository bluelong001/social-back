package me.study.social.rest;

import me.study.social.bean.Pyq;
import me.study.social.bean.Rest;
import me.study.social.bean.UserInfo;
import me.study.social.repository.PyqRepository;
import me.study.social.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/upload")
public class UploadController {
    @Value("${project-path}")
    private String projectPath;
    @Resource
    PyqRepository pyqRepository;

    @Resource
    UserInfoRepository userInfoRepository;

    @PostMapping("/uploadAvater")
    public Rest uploadAvater(@RequestParam Map param, @RequestParam("avater") MultipartFile avater, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Integer _id = (Integer) session.getAttribute("_id");
        UserInfo userInfo = userInfoRepository.findById(_id).orElse(null);
        String fileName;
        if (userInfo.getAvater().equals("default.jpg"))
            fileName = UUID.randomUUID().toString().replaceAll("-", "") + ".jpg";
        else
            fileName = userInfo.getAvater();
        try {
            File to = new File(projectPath, fileName);
            if (!to.getParentFile().exists())
                to.getParentFile().mkdirs();
            Files.copy(avater.getInputStream(), to.toPath());

        } catch (IOException e) {
            e.printStackTrace();
        }

        userInfo.setAvater(fileName);
        userInfoRepository.save(userInfo);
        return Rest.builder().code(0).msg("上传成功");
    }

    @PostMapping("/uploadFile")
    public Rest uploadFile(@RequestParam Map param, @RequestParam("file2") List<MultipartFile> file2) {
        List<String> pimg = new ArrayList<>();
        file2.forEach((file) -> {
            try {
                String fileName = UUID.randomUUID().toString().replaceAll("-", "") + ".jpg";
                File to = new File(projectPath, fileName);
                if (!to.getParentFile().exists())
                    to.getParentFile().mkdirs();
                Files.copy(file.getInputStream(), to.toPath());
                pimg.add(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Pyq pyq = Pyq.builder()
                .content((String) param.get("content"))
                .writerId(Integer.parseInt((String) param.get("username")))
                .pimgArr(String.join(";", pimg))
                .build();
        pyqRepository.save(pyq);
        return Rest.builder().code(0).msg("上传成功");
    }

}
