package matcher;

import auth.Auth;
import constant.Constants;
import org.json.JSONObject;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
        System.out.println("Session opened!");
        fill();
    }

    @OnMessage
    public void onMessage(Session session, Message message, @PathParam("token") String token) throws IOException, EncodeException {
        if(!checkAuth(session, token, message)) return;
        System.out.println(message);
        try {
            setLatLng(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private void wamyoli(Session session) throws IOException, EncodeException {
        Object[] toSend = gamyolebi.values().toArray();
        JSONObject obj = new JSONObject();
        obj.put("arr", toSend);
        Message message = new Message();
        message.setContent(obj.toString());
        session.getBasicRemote().sendObject(message);
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

    private void setLatLng(Message message) throws IOException {
        String place = message.getDestination();
        URL url = new URL(Constants.GOOGLE_API_BASE_URL + "place/findplacefromtext/json?inputtype=textquery&fields=geometry&input=" +
                URLEncoder.encode(place, StandardCharsets.UTF_8.toString()) + "&key=" + Constants.GOOGLE_API_KEY);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "UTF-8"));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();

        con.disconnect();

        JSONObject coordObj = new JSONObject(content.toString()).getJSONArray("candidates")
                                    .getJSONObject(0).getJSONObject("geometry").getJSONObject("location");

        message.setLat(coordObj.get("lat").toString());
        message.setLng(coordObj.get("lng").toString());
    }

    private void fill() {
        gamyolebi.put("1", new Message());
        gamyolebi.put("5", new Message());
        gamyolebi.put("4", new Message());
        gamyolebi.put("3", new Message());
        gamyolebi.put("2", new Message());
    }

}
