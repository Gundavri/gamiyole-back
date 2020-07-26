package servlets;

import auth.Auth;
import constant.Constants;
import org.json.JSONObject;
import shared.Shared;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@WebServlet(name = "DestinationAutoCompleteServlet", urlPatterns = "/destination-autocomplete")
public class DestinationAutoCompleteServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Shared.initializeResponse(response);
        JSONObject resObj = new JSONObject();

        try {
            String jwt = request.getParameter("token");
            jwt = jwt.substring(jwt.indexOf(" ") + 1);
            JSONObject obj = Auth.compareJWT(jwt);
            response.setStatus(200);
        } catch (Exception e) {
            resObj.put("error", "Auth failed");
            response.setStatus(401);
            response.getWriter().println(resObj.toString());
            return;
        }

        URL url = new URL(Constants.GOOGLE_API_BASE_URL + "place/autocomplete/json?input=" +
                    request.getParameter("place") + "&key=" + Constants.GOOGLE_API_KEY);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();

        con.disconnect();
        response.getWriter().println(content);
    }
}
