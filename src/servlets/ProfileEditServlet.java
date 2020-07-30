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

@WebServlet(name = "ProfileEditServlet", urlPatterns = "/profile-edit")
public class ProfileEditServlet extends HttpServlet {
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
                User tmpUser = dbController.getUserFromDB(obj.getString("email"));

                String newName = jsonObject.getString("name");
                if(newName != null) tmpUser.setName(newName);
                String newSurname = jsonObject.getString("surname");
                if(newSurname != null) tmpUser.setSurname(newSurname);
                String phone = jsonObject.getString("phone");
                if(newSurname != null) tmpUser.setPhone(phone);

                dbController.updateUser(tmpUser);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        response.getWriter().println(resObj.toString());

    }
}
