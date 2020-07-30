package servlets;

import constant.Constants;
import database.DatabaseController;
import models.User;
import models.UserToVerify;
import org.json.JSONObject;
import shared.Shared;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "VerificationServlet", urlPatterns = "/verification")
public class VerificationServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DatabaseController dbController = DatabaseController.getInstance();
        JSONObject resObject = new JSONObject();
        Shared.initializeResponse(response);

        String hash = request.getParameter("hash");
        System.out.println(hash);


        try {
            UserToVerify userToVerify = dbController.getUserToVerifyFromDB(hash);
            if(userToVerify == null) throw new Exception(Constants.USER_NOT_FOUND_MSG);
            User newUser = new User();
            newUser.setName(userToVerify.getName());
            newUser.setSurname(userToVerify.getSurname());
            newUser.setEmail(userToVerify.getEmail());
            newUser.setPassword(userToVerify.getPassword());
            dbController.deleteUserToVerify(userToVerify.getRandom_hash());
            dbController.insertUser(newUser);
        } catch (Exception e) {
            resObject.put("error", e.getMessage().length() == 0 ? Constants.INTERNAL_SERVER_ERROR_MSG : e.getMessage());
            response.setStatus(e.getMessage().length() == 0 ? 500 : 404);
            response.getWriter().println(resObject.toString());
            return;
        }
        response.setStatus(200);
        resObject.put("message", "Account has successfuly been verified");
        response.getWriter().println(resObject.toString());
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
