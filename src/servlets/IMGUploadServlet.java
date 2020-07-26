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

@WebServlet(name = "IMGUploadServlet", urlPatterns = "/upload-img")
@MultipartConfig(maxFileSize = 16177215) // upload file's size up to 16MB
public class IMGUploadServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        DatabaseController dbController = DatabaseController.getInstance();
        InputStream inputStream = null;

        String JWT = request.getParameter("token");
        Part filePart = request.getPart("img");
        if (filePart != null) {
            // prints out some information for debugging
            System.out.println(filePart.getName());
            System.out.println(filePart.getSize());
            System.out.println(filePart.getContentType());

            // obtains input stream of the upload file
            inputStream = filePart.getInputStream();
            try {
                JSONObject obj = Auth.compareJWT(JWT);
                dbController.insertIMG(obj.getString("email"), inputStream);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
