package servlets;

import auth.Auth;
import org.json.JSONObject;
import shared.Shared;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "CheckJWTServlet", urlPatterns = "/validate-token")
public class CheckJWTServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONObject resObj = new JSONObject();
        Shared.initializeResponse(response);
        try {
            String jwt = request.getParameter("token");
            jwt = jwt.substring(jwt.indexOf(" ") + 1);
            JSONObject obj = Auth.compareJWT(jwt);
            resObj.put("result", "success");
            response.setStatus(200);
        } catch (Exception e) {
            resObj.put("error", "Auth failed");
            response.setStatus(401);
        }
        response.getWriter().println(resObj.toString());
    }
}
