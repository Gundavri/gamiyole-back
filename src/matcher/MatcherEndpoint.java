package matcher;

import auth.Auth;
import constant.Constants;
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
        value = "/match/{token}",
        decoders = MessageDecoder.class,
        encoders = MessageEncoder.class
)
public class MatcherEndpoint {

    private Session session;
    private static Map<String, Message> wamyolebi = new HashMap<>(), gamyolebi = new HashMap<>();
    private static final Set<MatcherEndpoint> matcherEndpoints = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException, EncodeException {
        if(!checkAuth(session, token, new Message())) return;
        this.session = session;
        matcherEndpoints.add(this);
        Message message = new Message();
        message.setContent("Please send information about you");
        this.session.getBasicRemote().sendObject(message);
        System.out.println("Session opened!");
    }

    @OnMessage
    public void onMessage(Session session, Message message, @PathParam("token") String token) throws IOException, EncodeException {
        if(!checkAuth(session, token, message)) return;
        if(message.isGamiyole()) {
            gamyolebi.put(session.getId(), message);
            gamyoli(session);
        } else {
            wamyolebi.put(session.getId(), message);
            wamyoli(session);
        }

        System.out.println(token);
        System.out.println(message);
    }

    @OnClose
    public void onClose(Session session) throws IOException, EncodeException {
//        chatEndpoints.remove(this);
//        Message message = new Message();
//        message.setFrom(users.get(session.getId()));
//        message.setContent("disconnected!");
//        broadcast(message);
        System.out.println("Session closed! Don't match him to others.");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {

    }

    private void gamyoli(Session session) {
        for (String wamyolisSession:
             wamyolebi.keySet()) {

        }
    }

    private void wamyoli(Session session) {

    }

    private boolean checkAuth(Session session, String token, Message message) throws IOException, EncodeException {
        try {
            String jwt = token;
            jwt = jwt.substring(jwt.indexOf(" ") + 1);
            JSONObject obj = Auth.compareJWT(jwt);
            message.setEmail(obj.getString("email"));
            return true;
        } catch (Exception e) {
            Message message1 = new Message();
            message1.setContent("Auth failed!");
            session.getBasicRemote().sendObject(message1);
            session.close();
            return false;
        }
    }

}
