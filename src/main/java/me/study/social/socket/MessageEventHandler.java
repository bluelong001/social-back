package me.study.social.socket;


import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import io.netty.channel.unix.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 消息事件处理器
 *
 * @version 1.0
 * @since 2018/1015
 */
@Component
public class MessageEventHandler {

    private final SocketIOServer server;
//    private final ApiService apiService;

    private static final Logger logger = LoggerFactory.getLogger(MessageEventHandler.class);

    @Autowired
    public MessageEventHandler(SocketIOServer server) {
        this.server = server;
    }

    //添加connect事件，当客户端发起连接时调用
    @OnConnect
    public void onConnect(SocketIOClient client) {
        if (client != null) {
            String imei = client.getHandshakeData().getSingleUrlParam("imei");
            String applicationId = client.getHandshakeData().getSingleUrlParam("appid");
            logger.info("连接成功, applicationId=" + applicationId + ", imei=" + imei +
                    ", sessionId=" + client.getSessionId().toString());
            client.joinRoom(applicationId);
            // 更新POS监控状态为在线
//            ReportParam param = new ReportParam();
//            param.setImei(imei);
//            apiService.updateJustState(param, client.getSessionId().toString(), 1);
        } else {
            logger.error("客户端为空");
        }
    }

    //添加@OnDisconnect事件，客户端断开连接时调用，刷新客户端信息
    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        String imei = client.getHandshakeData().getSingleUrlParam("imei");
        logger.info("客户端断开连接, imei=" + imei + ", sessionId=" + client.getSessionId().toString());
        // 更新POS监控状态为离线
//        ReportParam param = new ReportParam();
//        param.setImei(imei);
//        apiService.updateJustState(param, "", 2);
        client.disconnect();
    }

//    // 消息接收入口
//    @OnEvent(value = Socket.EVENT_MESSAGE)
//    public void onEvent(SocketIOClient client, AckRequest ackRequest, Object data) {
//        logger.info("接收到客户端消息");
//        if (ackRequest.isAckRequested()) {
//            // send ack response with data to client
//            ackRequest.sendAckData("服务器回答Socket.EVENT_MESSAGE", "好的");
//        }
//    }
//
//    // 广播消息接收入口
//    @OnEvent(value = "broadcast")
//    public void onBroadcast(SocketIOClient client, AckRequest ackRequest, Object data) {
//        logger.info("接收到广播消息");
//        // 房间广播消息
//        String room = client.getHandshakeData().getSingleUrlParam("appid");
//        server.getRoomOperations(room).sendEvent("broadcast", "广播啦啦啦啦");
//    }
//
//    /**
//     * 报告地址接口
//     *
//     * @param client     客户端
//     * @param ackRequest 回执消息
//     * @param param      报告地址参数
//     */
//    @OnEvent(value = "doReport")
//    public void onDoReport(SocketIOClient client, AckRequest ackRequest, ReportParam param) {
//        logger.info("报告地址接口 start....");
//        BaseResponse result = postReport(param);
//        ackRequest.sendAckData(result);
//    }
//
//    /*----------------------------------------下面是私有方法-------------------------------------*/
//    private BaseResponse postReport(ReportParam param) {
//        BaseResponse result = new BaseResponse();
//        int r = apiService.report(param);
//        if (r > 0) {
//            result.setSuccess(true);
//            result.setMsg("报告地址成功");
//        } else {
//            result.setSuccess(false);
//            result.setMsg("该POS机还没有入网，报告地址失败。");
//        }
//        return result;
//    }
}