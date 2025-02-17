package servlets;

import auth.Auth;
import constant.Constants;
import database.DatabaseController;
import models.User;
import org.json.JSONObject;
import shared.Shared;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "ProfileServlet", urlPatterns = "/profile")
public class ProfileServlet extends HttpServlet {
    // for other users profile
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
        JSONObject resObj = new JSONObject();
        DatabaseController dbController = DatabaseController.getInstance();

        Shared.initializeResponse(response);
        String email = request.getParameter("email");
        User user;
        try {
            user = dbController.getUserFromDB(email);
            if(user == null) throw new Exception(Constants.USER_NOT_FOUND_MSG);
        } catch (Exception e) {
            if (e.getMessage().equals(Constants.USER_NOT_FOUND_MSG)) {
                response.setStatus(404);
                resObj.put("error", Constants.USER_NOT_FOUND_MSG);
            }
            else if(e.getMessage().equals(Constants.INTERNAL_SERVER_ERROR_MSG)){
                response.setStatus(500);
                resObj.put("error", Constants.INTERNAL_SERVER_ERROR_MSG);
            }
            response.getWriter().println(resObj.toString());
            return;
        }
        JSONObject userJSON = new JSONObject();
        userJSON.put("name", user.getName());
        userJSON.put("surname", user.getSurname());
        userJSON.put("email", user.getEmail());
        userJSON.put("phone", user.getPhone());
        userJSON.put("age", user.getAge());

        resObj.put("user", userJSON);
        response.getWriter().println(resObj.toString());
    }

    // For current users profile
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
        JSONObject resObj = new JSONObject(), jsonObject = new JSONObject();
        DatabaseController dbController = DatabaseController.getInstance();

        Shared.initializeResponse(response);
        try {
            jsonObject = Shared.getBodyAsJSON(request);
        } catch (Exception e) {
            resObj.put("error", Constants.PARSE_ERROR_MSG);
            response.setStatus(400);
            response.getWriter().println(resObj.toString());
            return;
        }
        String JWT = jsonObject.getString("token");

        if(JWT != null){
            try {
                JSONObject obj = Auth.compareJWT(JWT);
                User user = dbController.getUserFromDB(obj.getString("email"));

                JSONObject userJSON = new JSONObject();
                userJSON.put("name", user.getName());
                userJSON.put("surname", user.getSurname());
                userJSON.put("email", user.getEmail());
                userJSON.put("phone", user.getPhone());
                userJSON.put("age", user.getAge());

                resObj.put("user", userJSON);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        response.getWriter().println(resObj.toString());

    }
}
