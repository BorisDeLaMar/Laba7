package ru.itmo.common.authorization;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User {

    private final String login;
    private final String password;

    public User(String login, String password) throws NoSuchAlgorithmException {
        this.login = login;
        this.password = secure_password(password);
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
    public static String secure_password(String password) throws NoSuchAlgorithmException{
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        return DatatypeConverter.printHexBinary(digest).toUpperCase();
    }
}
