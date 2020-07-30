package shared;

import constant.Constants;
import org.json.JSONObject;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.util.Properties;
import java.util.UUID;

public class Shared {
    public static JSONObject getBodyAsJSON(HttpServletRequest request) throws Exception {
        StringBuilder result = new StringBuilder("");
        BufferedReader reader = null;
        try {
            reader = request.getReader();
            while(true) {
                String line = reader.readLine();
                if(line==null) break;
                result.append(line).append('\n');
            }
            return new JSONObject(result.toString());
        }
        catch (Exception e){
            throw new Exception("Parse Error");
        }
    }

    public static void initializeResponse(HttpServletResponse response) {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Request-Headers", "*");
        response.setHeader("Access-Control-Max-Age", "1728000");
    }

    public static void sendEmail(String receiver, String subject, String text) {
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.smtp.host", "smtp.gmail.com");
        properties.setProperty("mail.smtp.port", "587");

        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Constants.EMAIL, Constants.EMAIL_PASSWORD);
            }
        });
        MimeMessage mimeMessage = new MimeMessage(session);
        try {
            mimeMessage.setFrom(new InternetAddress(Constants.EMAIL));
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
            mimeMessage.setSubject(subject);
            mimeMessage.setText(text);
            Transport.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
