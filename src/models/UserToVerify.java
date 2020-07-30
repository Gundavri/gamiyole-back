package models;

public class UserToVerify {
    public static final String EMAIL_COLUMN = "email";
    public static final String NAME_COLUMN = "name";
    public static final String SURNAME_COLUMN = "surname";
    public static final String PASSWORD_COLUMN = "password";
    public static final String RANDOM_HASH_COLUMN = "random_hash";

    private String name, surname, email, password, random_hash;

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

    public String getRandom_hash() {
        return random_hash;
    }

    public void setRandom_hash(String random_hash) {
        this.random_hash = random_hash;
    }

    @Override
    public boolean equals(Object obj) {
        UserToVerify u = (UserToVerify) obj;
        return  u == this ||
                (u.getName().equals(name) &&
                        u.getSurname().equals(surname) &&
                        u.getEmail().equals(email) &&
                        u.getPassword().equals(password) &&
                        u.getRandom_hash().equals(random_hash));
    }
}
