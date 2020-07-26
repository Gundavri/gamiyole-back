package shared;

import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    public static void initializeResponse(HttpServletResponse response) {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Request-Headers", "*");
        response.setHeader("Access-Control-Max-Age", "1728000");
    }
}
