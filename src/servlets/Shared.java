package servlets;

import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;

public class Shared {
    public static JSONObject getBodyAsJSON(HttpServletRequest request) throws Exception {
        StringBuilder result = new StringBuilder("");
        BufferedReader reader = null;
        try {
            reader = request.getReader();
            while(true) {
                String line = reader.readLine();
                if(line==null) break;
                result.append(line).append('\n');
            }
            return new JSONObject(result.toString());
        }
        catch (Exception e){
            throw new Exception("Parse Error");
        }
    }
}
