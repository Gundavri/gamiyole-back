package servlets;

import auth.Auth;
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
import java.util.Date;
import java.util.UUID;

@WebServlet(name = "RegisterServlet", urlPatterns = "/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String jwt = req.getHeader("Authorization");
//        jwt = jwt.substring(jwt.indexOf(" ") + 1);
    }

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
                name = jsonObject.getString("name"),
                surname = jsonObject.getString("surname"),
                email = jsonObject.getString("email"),
                password = jsonObject.getString("password");

        String error = parametersError(name, surname, email, password);
        if(error != null) {
            resObject.put("error", error);
            response.setStatus(400);
            response.getWriter().println(resObject.toString());
            return;
        }

        String hashedPassword = Auth.hashPassword(password);
        String randomHash = UUID.randomUUID().toString();
        try {
            User userInDB = dbController.getUserFromDB(email);
            if(userInDB != null) throw new Exception();
            UserToVerify userToVerify = new UserToVerify();
            userToVerify.setEmail(email);
            userToVerify.setName(name);
            userToVerify.setSurname(surname);
            userToVerify.setPassword(hashedPassword);
            userToVerify.setRandom_hash(randomHash);
            dbController.insertUserToVerify(userToVerify);
        } catch (Exception e) {
            resObject.put("error", Constants.USER_FOUND_MSG);
            response.setStatus(400);
            response.getWriter().println(resObject.toString());
            return;
        }

        String token = "";
        try {
//            token = Auth.generateJWT(email, new Date().getTime() + Constants.JWT_MINUTES * 60 * 1000);
              Shared.sendEmail(email,
                      "GAMIYOLE VERIFICATION",
                        "Please click this link to verify your account: " + Constants.FRONT_BASE_URL + "verify?hash=" + randomHash);
        } catch (Exception e) {
            resObject.put("error", Constants.INTERNAL_SERVER_ERROR_MSG);
            response.setStatus(500);
            response.getWriter().println(resObject.toString());
            return;
        }

        resObject.put("message", "Confirm email");
        response.getWriter().println(resObject.toString());
    }

    // Check parameters validity and returns error if it exists
    private String parametersError(String name, String surname, String email, String password) {
        if(name.length() < 2 || name.length() > 32) {
            return "Name length is invalid 'length'=" + name.length();
        }
        if(surname.length() < 2 || surname.length() > 32) {
            return "Surname length is invalid 'length'=" + surname.length();
        }
        if(email.length() < 6 || email.length() > 64) {
            return "Email length is invalid 'length'=" + email.length();
        }
        if(password.length() < 6 || password.length() > 64) {
            return "Password length is invalid 'length'=" + password.length();
        }
        return null;
    }
}
