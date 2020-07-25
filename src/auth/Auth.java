package auth;

import constant.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

public class Auth {
    public static String generateJWT(String email, long date) throws Exception {
        try {
            return Jwts.builder()
                    .setExpiration(new Date(date))
                    .claim("email", email)
                    .signWith(SignatureAlgorithm.HS256, Constants.JWT_SECRET.getBytes("UTF-8"))
                    .compact();
        } catch (Exception e) {
            throw e;
        }
    }

    public static JSONObject compareJWT(String jwt) throws Exception {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(Constants.JWT_SECRET.getBytes("UTF-8"))
                    .parseClaimsJws(jwt);
            String email = (String) claims.getBody().get("email");
            JSONObject res = new JSONObject();
            res.put("email", email);
            return res;
        } catch (Exception e) {
            throw e;
        }
    }

    // Hashes password using bcrypt algorithm
    public static String hashPassword(String password) {
        String salt = BCrypt.gensalt(Constants.SALT_AMOUNT);
        String hashed_password = BCrypt.hashpw(password, salt);
        return hashed_password;
    }

    // Compares password using bcrypt algorithm
    public static boolean comparePassword(String hashedPassword, String password) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}
