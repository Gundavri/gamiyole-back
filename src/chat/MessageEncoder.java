package chat;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import java.io.IOException;

public class MessageEncoder implements Encoder.Text<chat.Message> {
    @Override
    public String encode(Message message) throws EncodeException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(message);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
        //do nothing
    }

    @Override
    public void destroy() {
        //do nothing
    }
}
