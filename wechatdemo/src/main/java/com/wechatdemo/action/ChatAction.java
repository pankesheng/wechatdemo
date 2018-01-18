package com.wechatdemo.action;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
 
import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.apache.log4j.Logger;
import com.wechatdemo.common.GetHttpSessionConfigurator;

/**
 * @author pks
 * @version 2017年12月8日
 */
@ServerEndpoint(value = "/action/websocket/chat",configurator=GetHttpSessionConfigurator.class)
public class ChatAction{
private final static Logger log = Logger.getLogger(ChatAction.class);
    private static final Set<ChatAction> onlineUsers = new CopyOnWriteArraySet<ChatAction>();
    private String nickname;
    private Session session;
    private HttpSession httpSession;
   
    @OnOpen
    public void start(Session session, EndpointConfig config) {
        this.session = session;
        this.httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        this.nickname=(String) httpSession.getAttribute("login_info");
        onlineUsers.add(this);
        String message = String.format("* %s %s", nickname, " from websocket 上线了...");
        broadcast(message);
    }
 
    @OnClose
    public void end(Session session) {
        onlineUsers.remove(this);
        String message = String.format("* %s %s", nickname, " from websocket 已经离开...");
        broadcast(message);
    }
 
    @OnMessage
    public void incoming(String message, EndpointConfig config) {
        // Never trust the client
        String filteredMessage = String.format("%s: %s",nickname, message.toString());
        broadcast(filteredMessage);
    }
 
    @OnError
    public void onError(Throwable t) throws Throwable {
        log.error("错误: " + t.toString(), t);
    }
 
    private static void broadcast(String msg) {
        for (ChatAction client : onlineUsers) {
            try {
                synchronized (client) {
                    client.session.getBasicRemote().sendText(msg);
                }
            } catch (IOException e) {
                log.debug("错误: 消息发送失败!", e);
                onlineUsers.remove(client);
                try {
                    client.session.close();
                } catch (IOException e1) {
                    // Ignore
                }
                String message = String.format("* %s %s", client.nickname, " from websocket 已经离开...");
                broadcast(message);
            }
        }
    }
}
