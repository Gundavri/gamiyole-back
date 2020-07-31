package chat;


import auth.Auth;
import org.json.JSONObject;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(
        value = "/chat/{username}/{token}",
        decoders = chat.MessageDecoder.class,
        encoders = chat.MessageEncoder.class
)
public class ChatEndpoint {

    private Session session;
    private static final Set<ChatEndpoint> chatEndpoints = new CopyOnWriteArraySet<>();
    private static Map<String, String> users = new HashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username, @PathParam("token") String token) throws IOException, EncodeException {
        System.out.println("name: " + username + " \ntoken: " + token);
        Message message = new Message();
        if(!checkAuth(session, token, message)) return;
        this.session = session;
        chatEndpoints.add(this);
        users.put(session.getId(), message.getFrom());
        message.setContent("Connected!");
        message.setTo(username);
        sendMessageToOneUser(message);
    }

    @OnMessage
    public void onMessage(Session session, Message message, @PathParam("username") String username, @PathParam("token") String token) throws IOException, EncodeException {
        if(!checkAuth(session, token, message)) return;
        sendMessageToOneUser(message);
    }

    @OnClose
    public void onClose(Session session, @PathParam("username") String username) throws IOException, EncodeException {
        chatEndpoints.remove(this);
        Message message = new Message();
        message.setFrom(users.get(session.getId()));
        message.setContent("disconnected!");
        message.setTo(username);
        sendMessageToOneUser(message);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {

    }


    private static void sendMessageToOneUser(Message message) throws IOException, EncodeException {
        for (ChatEndpoint endpoint : chatEndpoints) {
            synchronized (endpoint) {
                if (endpoint.session.getId().equals(getSessionId(message.getTo()))) {
                    endpoint.session.getBasicRemote().sendObject(message);
                }
            }
        }
    }

    private static String getSessionId(String to) {
        if (users.containsValue(to)) {
            for (String sessionId : users.keySet()) {
                if (users.get(sessionId).equals(to)) {
                    return sessionId;
                }
            }
        }
        return null;
    }

    private boolean checkAuth(Session session, String token, Message message) throws IOException, EncodeException {
        try {
            String jwt = token;
            jwt = jwt.substring(jwt.indexOf(" ") + 1);
            JSONObject obj = Auth.compareJWT(jwt);
            System.out.println(obj.getString("email"));
            message.setFrom(obj.getString("email"));
            return true;
        } catch (Exception e) {
            matcher.Message message1 = new matcher.Message();
            message1.setContent("Auth failed!");
            session.getBasicRemote().sendObject(message1);
            session.close();
            return false;
        }
    }

}
