package top.pi1grim.ea.component;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
@ServerEndpoint("/api/v3/websocket/{id}")
public class WebSocketServer {
    /**静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。*/
    private static int onlineCount = 0;
    /**concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。*/
    private static final ConcurrentHashMap<Long,WebSocketServer> WEB_SOCKET_MAP = new ConcurrentHashMap<>();
    /**与某个客户端的连接会话，需要通过它来给客户端发送数据*/
    private Session session;
    /**
     * 接收userId
     */
    private Long id;

    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(Session session,@PathParam("id") Long id) {
        this.session = session;
        this.id = id;
        if(WEB_SOCKET_MAP.containsKey(id)){
            WEB_SOCKET_MAP.remove(id);
            WEB_SOCKET_MAP.put(id, this);
            //加入set中
        }else{
            WEB_SOCKET_MAP.put(id, this);
            //加入set中
            addOnlineCount();
            //在线数加1
        }

        log.info("用户连接: " + id + ",当前在线人数为: " + getOnlineCount());

        try {
            sendMessage("INFO"); //TODO
        } catch (IOException e) {
            log.info("用户:" + id + ",网络异常");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if(WEB_SOCKET_MAP.containsKey(id)){
            WEB_SOCKET_MAP.remove(id);
            subOnlineCount();
        }
        log.info("用户退出: " + id + ",当前在线人数为: " + getOnlineCount());
    }

    /**
     * @param error
     */
    @OnError
    public void onError(RuntimeException error) {
        log.error("用户错误: " + id + ",原因: " + error.getMessage(), error);
    }
    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }


    /**
     * 发送自定义消息
     */
    public static void sendInfo(String message, Long id) throws IOException {

        if (Objects.nonNull(id) && WEB_SOCKET_MAP.containsKey(id)) {
            WEB_SOCKET_MAP.get(id).sendMessage(message);
        } else {
            log.info("用户" + id + ",不在线！");
        }
    }


    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }
}
