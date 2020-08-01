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
import java.util.Date;

@WebServlet(name = "LoginServlet", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        JSONObject jsonObject, resObject = new JSONObject();
        DatabaseController dbController = DatabaseController.getInstance();

        Shared.initializeResponse(response);

        try {
            jsonObject = Shared.getBodyAsJSON(request);
        } catch (Exception e) {
            resObject.put("error", Constants.PARSE_ERROR_MSG);
            response.setStatus(400);
            response.getWriter().println(resObject.toString());
            return;
        }

        final String
                email = jsonObject.getString("email"),
                password = jsonObject.getString("password");

        String error = parametersError(email, password);
        if (error != null) {
            resObject.put("error", error);
            response.setStatus(400);
            response.getWriter().println(resObject.toString());
            return;
        }

        String hashedPassword = "";
        try {
            User user = dbController.getUserFromDB(email);
            if(user == null) throw new Exception(Constants.USER_NOT_FOUND_MSG);
            hashedPassword = user.getPassword();
        } catch (Exception e) {
            if (e.getMessage().equals(Constants.USER_NOT_FOUND_MSG)) {
                response.setStatus(404);
                resObject.put("error", Constants.USER_NOT_FOUND_MSG);
            }
            else if(e.getMessage().equals(Constants.INTERNAL_SERVER_ERROR_MSG)){
                response.setStatus(500);
                resObject.put("error", Constants.INTERNAL_SERVER_ERROR_MSG);
            }
            response.getWriter().println(resObject.toString());
            return;
        }

        if(!Auth.comparePassword(hashedPassword, password)) {
            resObject.put("error", Constants.USER_NOT_FOUND_MSG);
            response.setStatus(404);
            response.getWriter().println(resObject.toString());
            return;
        }

        String token = "";
        try {
            token = Auth.generateJWT(email, new Date().getTime() + Constants.JWT_MINUTES * 60 * 1000);
        } catch (Exception e) {
            resObject.put("error", Constants.INTERNAL_SERVER_ERROR_MSG);
            response.setStatus(500);
            response.getWriter().println(resObject.toString());
            return;
        }

        resObject.put("token", token);
        response.getWriter().println(resObject.toString());
    }

    // Check parameters validity and returns error if it exists
    private String parametersError(String email, String password) {
        if(email.length() < 6 || email.length() > 64) {
            return "Email length is invalid 'length'=" + email.length();
        }
        if(password.length() < 6 || password.length() > 64) {
            return "Password length is invalid 'length'=" + password.length();
        }
        return null;
    }
}
