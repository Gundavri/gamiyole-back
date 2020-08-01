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
import java.util.*;
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
    private static Map<String, Set<String>> acceptedGamyolebi = new HashMap<>(), declinedGamyolebi = new HashMap<>();
    private static Map<String, String> sessionIdsByEmail = new HashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException, EncodeException {
        if(!checkAuth(session, token, new Message())) return;
        this.session = session;
        matcherEndpoints.add(this);
        System.out.println("Session opened!");
       // fill();
    }

    @OnMessage
    public void onMessage(Session session, Message message, @PathParam("token") String token) throws IOException, EncodeException {
        if(!checkAuth(session, token, message)) return;
        if(!sessionIdsByEmail.containsKey(message.getEmail())) {
            sessionIdsByEmail.put(message.getEmail(), session.getId());
        }
        if(message.getDestination()!=null) {
            try {
                setLatLng(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(message.getContent() != null) {
            JSONObject contentObj = new JSONObject(message.getContent());
            if (contentObj.has("key")) {
                if (contentObj.getString("key").equals("Accapted Gamyoli")) {
                    if (!acceptedGamyolebi.containsKey(session.getId())) {
                        acceptedGamyolebi.put(session.getId(), new HashSet<>());
                    }
                    acceptedGamyolebi.get(session.getId()).add(sessionIdsByEmail.get(contentObj.getString("email")));
                    for (MatcherEndpoint me : matcherEndpoints) {
                        synchronized (me) {
                            Session ses = me.session;
                            if(gamyolebi.containsKey(ses.getId()) && ses.getId().equals(sessionIdsByEmail.get(contentObj.getString("email")))) {
                                JSONObject obj = new JSONObject();
                                JSONObject cont = new JSONObject(wamyolebi.get(session.getId()));
                                cont.put("email", wamyolebi.get(session.getId()).getEmail());
                                obj.put("chosenBy", cont);
                                obj.put("isWamyole", true);
                                Message msg = new Message();
                                msg.setContent(obj.toString());
                                ses.getBasicRemote().sendObject(msg);
                                return;
                            }
                        }
                    }
                } else if (contentObj.getString("key").equals("Declined Gamyoli")) {
                    if (!declinedGamyolebi.containsKey(session.getId())) {
                        declinedGamyolebi.put(session.getId(), new HashSet<>());
                    }
                    declinedGamyolebi.get(session.getId()).add(sessionIdsByEmail.get(contentObj.getString("email")));
                } else if (contentObj.getString("key").equals("Accepted Wamyoli")) {
                    Message msg = new Message();
                    JSONObject cont = new JSONObject();
                    cont.put("timeToChat", "YES");
                    msg.setContent(cont.toString());
                    session.getBasicRemote().sendObject(msg);
                    String sesId = sessionIdsByEmail.get(contentObj.getString("email"));
                    for (MatcherEndpoint me : matcherEndpoints) {
                        synchronized (me) {
                            Session ses = me.session;
                            if(ses.getId().equals(sesId)) {
                                ses.getBasicRemote().sendObject(msg);
                                return;
                            }
                        }
                    }
                }
            }
        }

        if(message.isGamiyole()) {
            if(!gamyolebi.containsKey(session.getId()))
                gamyolebi.put(session.getId(), message);
            gamyoli(session);
        } else {
            if(!wamyolebi.containsKey(session.getId()))
                wamyolebi.put(session.getId(), message);
            wamyoli(session);
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException, EncodeException {
        matcherEndpoints.remove(this);
        if(wamyolebi.containsKey(session.getId())) {
            wamyolebi.remove(session.getId());
        }
        if(gamyolebi.containsKey(session.getId())) {
            gamyolebi.remove(session.getId());
            for (MatcherEndpoint me : matcherEndpoints) {
                synchronized (me) {
                    Session ses = me.session;
                    if (wamyolebi.keySet().contains(ses.getId())) {
                        try {
                            sendAllGamyoli(ses);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        System.out.println("Session closed! Don't match him to others.");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {

    }

    private void gamyoli(Session session) throws IOException, EncodeException {
        for (MatcherEndpoint me : matcherEndpoints) {
            synchronized (me) {
                Session ses = me.session;
                if (wamyolebi.keySet().contains(ses.getId())) {
                    try {
                        sendAllGamyoli(ses);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void wamyoli(Session session) throws IOException, EncodeException {
        sendAllGamyoli(session);
    }

    private void sendAllGamyoli(Session session) throws IOException, EncodeException {
        Object[] toSend = gamyolebi.values().toArray();
        toSend = Arrays.stream(toSend).filter(x -> (
                ((Message) x).isFromUni() && wamyolebi.get(session.getId()).isFromUni())
                || (!((Message) x).isFromUni() && !wamyolebi.get(session.getId()).isFromUni())).toArray();
        JSONObject obj = new JSONObject();
        obj.put("arr", toSend);
        obj.put("isGamyoleebi", true);
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
