package auth;

import static org.junit.jupiter.api.Assertions.*;

import constant.Constants;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.*;
import java.util.concurrent.TimeUnit;

class AuthTest {

    @Test
    void generateCompareJWT1() {
        final String email = "asdasd@asd.com";
        try {
            String jwt = Auth.generateJWT(email, new Date().getTime() + Constants.JWT_MINUTES * 60 * 1000);
            JSONObject obj = Auth.compareJWT(jwt);
            assertEquals(obj.getString("email"), email);
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    void generateCompareJWT2() {
        final String email = "";
        try {
            String jwt = Auth.generateJWT(email, new Date().getTime() + Constants.JWT_MINUTES * 60 * 1000);
            JSONObject obj = Auth.compareJWT(jwt);
            assertEquals(obj.getString("email"), email);
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    void generateCompareJWT3() {
        final String email = "";
        final int delay = 1;
        try {
            String jwt = Auth.generateJWT(email, new Date().getTime() + delay*1000);
            wait(delay*1000);
            JSONObject obj = Auth.compareJWT(jwt);
            assertTrue(false);
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    void hashComparePassword1() {
        final String password = "asdasd123";
        String hp = Auth.hashPassword(password);
        assertTrue(Auth.comparePassword(hp, password));
    }

    @Test
    void hashComparePassword2() {
        final String password = "";
        String hp = Auth.hashPassword(password);
        assertTrue(Auth.comparePassword(hp, password));
    }
}