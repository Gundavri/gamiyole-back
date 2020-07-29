package models;

public class User {
    public static final String EMAIL_COLUMN = "email";
    public static final String NAME_COLUMN = "name";
    public static final String SURNAME_COLUMN = "surname";
    public static final String PASSWORD_COLUMN = "password";
    public static final String PHONE_COLUMN = "phone";
    public static final String IMG_BLOB = "img";
    public static final String AGE_COLUMN = "age";

    private String name, surname, email, password, phone;
    private int age;

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

    public String getPhone() { return phone; }

    public void setPhone(String phone) { this.phone = phone; }

    public int getAge() { return age; }

    public void setAge(int age) { this.age = age; }

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
