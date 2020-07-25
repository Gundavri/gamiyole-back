package database;

import constant.Constants;
import models.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DatabaseControllerTest {
    @Test
    void test1() {
        final String name = "mixo", surname = "mixoshvili", email = "mixo@mixo.mixo", password = "viri123";
        DatabaseController dbController = DatabaseController.getInstance();
        User u = new User();
        u.setName(name);
        u.setSurname(surname);
        u.setEmail(email);
        u.setPassword(password);
        try {
            dbController.insertUser(u);
            User userFromDB = dbController.getUserFromDB(u.getEmail());
            assertTrue(userFromDB.equals(u));
            dbController.deleteUser(u.getEmail());
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    void getUserFromDB1() {
        DatabaseController dbController = DatabaseController.getInstance();
        try {
            User u = dbController.getUserFromDB("bgugu17@freeuni.edu.ge");
            assertTrue(false);
        } catch (Exception e) {
            assertEquals(Constants.USER_NOT_FOUND_MSG, e.getMessage());
        }
    }

    @Test
    void insertUser1() {
        final String name = "mixo", surname = "mixoshvili", email = "mixo@mixo.mixo", password = "viri123";
        DatabaseController dbController = DatabaseController.getInstance();
        User u = new User();
        u.setName(name);
        u.setSurname(surname);
        u.setEmail(email);
        u.setPassword(password);
        try {
            dbController.insertUser(u);
            dbController.insertUser(u);
            assertTrue(false);
        } catch (Exception e) {
            assertEquals(Constants.USER_FOUND_MSG, e.getMessage());
            try {
                dbController.deleteUser(u.getEmail());
                assertTrue(true);
            } catch (Exception ex) {
                assertTrue(false);
            }
        }
    }

    @Test
    void deleteUser1() {
        DatabaseController dbController = DatabaseController.getInstance();
        try {
            dbController.deleteUser("bgugu17@freeuni.edu.ge");
            assertTrue(false);
        } catch (Exception e) {
            assertEquals(Constants.USER_NOT_FOUND_MSG, e.getMessage());
        }
    }
}