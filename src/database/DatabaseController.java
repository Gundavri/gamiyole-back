package database;

import constant.Constants;
import models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseController {
    public static final String DB_SERVER_IP = "127.0.0.1";
    public static final String DB_DATABASE_NAME = "Gamiyole"; // Put your DB name
    public static final String DB_USERNAME = "root"; // Put your username
    public static final String DB_PASSWORD = "password"; // Put your password

    private Connection con;
    private static DatabaseController dbInstance;

    private DatabaseController () {
        this.getConnection();
    }

    public static DatabaseController getInstance() {
        if(dbInstance == null) dbInstance = new DatabaseController();
        return dbInstance;
    }

    private void getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.con = DriverManager
                    .getConnection("jdbc:mysql://" + DB_SERVER_IP + "/" + DB_DATABASE_NAME, DB_USERNAME, DB_PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public User getUserFromDB(String email) throws Exception {
        String query = "select * from USER as u where u.email = ?";
        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            List<User> users = transformToUser(rs);
            if(users.size() == 0) {
                throw new Exception(Constants.USER_NOT_FOUND_MSG);
            }
            return users.get(0);
        } catch (SQLException e) {
            throw new Exception(Constants.INTERNAL_SERVER_ERROR_MSG);
        }
    }

    public void insertUser(User user) throws Exception {
        String query = "insert into USER(name, password, email, surname) values (?, ?, ?, ?);";
        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, user.getName());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getSurname());
            int res = ps.executeUpdate();
        } catch (SQLException e) {
            throw new Exception(Constants.USER_FOUND_MSG);
        }
    }

    public void deleteUser(String email) throws Exception {
        String query = "delete from USER as u where u.email = ?;";
        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, email);
            int res = ps.executeUpdate();
            if(res == 0) throw new Exception(Constants.USER_NOT_FOUND_MSG);
        } catch (SQLException e) {
            throw new Exception(Constants.INTERNAL_SERVER_ERROR_MSG);
        }
    }

    private List<User> transformToUser(ResultSet rs) throws SQLException {
        List<User> list = new ArrayList<>();
        while(rs.next()){
          User u = new User();
          u.setEmail(rs.getString(User.EMAIL_COLUMN));
          u.setName(rs.getString(User.NAME_COLUMN));
          u.setSurname(rs.getString(User.SURNAME_COLUMN));
          u.setPassword(rs.getString(User.PASSWORD_COLUMN));
          list.add(u);
        }
        return list;
    }

}
