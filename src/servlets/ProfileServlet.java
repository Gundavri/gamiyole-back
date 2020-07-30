package servlets;

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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
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
}
