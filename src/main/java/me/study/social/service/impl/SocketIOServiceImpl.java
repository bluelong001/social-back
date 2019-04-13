package me.study.social.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import com.corundumstudio.socketio.annotation.OnConnect;
import me.study.social.bean.IdToId;
import me.study.social.repository.IdToIdRepository;
import me.study.social.service.SocketIOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.util.StringUtils;

@Service(value = "socketIOService")
public class SocketIOServiceImpl implements SocketIOService {

    // 用来存已连接的客户端
    private static Map<String, SocketIOClient> clientMap = new ConcurrentHashMap<>();

    @Autowired
    private SocketIOServer socketIOServer;

    @Resource
    public IdToIdRepository idToIdRepository;

    /**
     * Spring IoC容器创建之后，在加载SocketIOServiceImpl Bean之后启动
     *
     * @throws Exception
     */
    @PostConstruct
    private void autoStartup() throws Exception {
        start();
    }

    /**
     * Spring IoC容器在销毁SocketIOServiceImpl Bean之前关闭,避免重启项目服务端口占用问题
     *
     * @throws Exception
     */
    @PreDestroy
    private void autoStop() throws Exception {
        stop();
    }

    @Override
    public void start() {
        // 监听客户端连接
        socketIOServer.addConnectListener(client -> {
            String loginUserNum = getParamsByClient(client);
            if (loginUserNum != null) {
                clientMap.put(loginUserNum, client);
            }
        });

        // 监听客户端断开连接
        socketIOServer.addDisconnectListener(client -> {
            String loginUserNum = getParamsByClient(client);
            if (loginUserNum != null) {
                clientMap.remove(loginUserNum);
                client.disconnect();
            }
        });

        // 处理自定义的事件，与连接监听类似
        socketIOServer.addEventListener("login", String.class, (client, data, ackSender) -> {
            IdToId o = idToIdRepository.findFirstByUsername(data);
            if (o != null)
                o.setSocketid(client.getSessionId().toString());
            else
                o = IdToId.builder()
                        .socketid(client.getSessionId().toString())
                        .username(data)
                        .build();
            idToIdRepository.saveAndFlush(o);
            clientMap.put(data, client);
        });

        socketIOServer.addEventListener("comment", String.class, (client, data, ackSender) -> {
            IdToId o = idToIdRepository.findFirstByUsername(data);
            pushMessageToUser(o);
        });

        socketIOServer.addEventListener("chat", Map.class, (client, data, ackSender) -> {
            pushMessageToUser(data);
        });
        socketIOServer.start();
    }

    @Override
    public void stop() {
        if (socketIOServer != null) {
            socketIOServer.stop();
            socketIOServer = null;
        }
    }

//
    @Override
    public void pushMessageToUser(IdToId idToId) {
        String loginUserNum = idToId.getUsername();
            SocketIOClient client = clientMap.get(loginUserNum);
            if (client != null)
                client.sendEvent("receiveComment");

    }

    @Override
    public void pushMessageToUser(Map map ) {
        String loginUserNum = (String) map.get("to_user");
            SocketIOClient client = clientMap.get(loginUserNum);
            if (client != null)
                client.sendEvent("receiveMsg", map);

    }
    /**
     * 此方法为获取client连接中的参数，可根据需求更改
     *
     * @param client
     * @return
     */
    private String getParamsByClient(SocketIOClient client) {
        // 从请求的连接中拿出参数（这里的loginUserNum必须是唯一标识）
        Map<String, List<String>> params = client.getHandshakeData().getUrlParams();
        List<String> list = params.get("loginUserNum");
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
}