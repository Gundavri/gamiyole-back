package models;

public class User {
    public static final String EMAIL_COLUMN = "email";
    public static final String NAME_COLUMN = "name";
    public static final String SURNAME_COLUMN = "surname";
    public static final String PASSWORD_COLUMN = "password";

    private String name, surname, email, password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object obj) {
        User u = (User) obj;
        return  u == this ||
                (u.getName().equals(name) &&
                u.getSurname().equals(surname) &&
                u.getEmail().equals(email) &&
                u.getPassword().equals(password));
    }
}
