package ru.itmo.common.authorization;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class User {

    private final String login;
    private final String password;
    private String salt;
    public User(String login, String password) throws NoSuchAlgorithmException {
        this.login = login;
        salt = setSalt();
        this.password = secure_password(password, salt);
    }
    public User(String login, String password, String salt) throws NoSuchAlgorithmException{
        this.login = login;
        this.password = password;
        this.salt = salt;
    }
    public String setSalt(){
        byte[] array = new byte[7]; // length is bounded by 7
        new Random().nextBytes(array);
        return new String(array, StandardCharsets.UTF_8);
    }
    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getSalt() {
        return salt;
    }

    public static String secure_password(String password, String salt) throws NoSuchAlgorithmException{
        MessageDigest md = MessageDigest.getInstance("MD5");
        String pepper = "*63&^mVLC(#";
        password = pepper + password + salt;

        md.update(password.getBytes());
        byte[] digest = md.digest();
        return DatatypeConverter.printHexBinary(digest).toUpperCase();
    }
}
