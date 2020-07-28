package servlets;

import auth.Auth;
import constant.Constants;
import database.DatabaseController;
import org.json.JSONObject;
import shared.Shared;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;

@WebServlet(name = "IMGToFrontServlet", urlPatterns = "/get-img")
public class IMGToFrontServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        DatabaseController dbController = DatabaseController.getInstance();
        JSONObject resObject = new JSONObject();

        String JWT = request.getParameter("token");

        try {
            JSONObject obj = Auth.compareJWT(JWT);
            String imgStr = dbController.getIMG(obj.getString("email"));
            resObject.put("img", imgStr);
            response.getWriter().println(imgStr);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
