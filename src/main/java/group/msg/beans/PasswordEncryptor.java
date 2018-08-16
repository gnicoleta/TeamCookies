package group.msg.beans;

import javax.xml.bind.DatatypeConverter;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class PasswordEncryptor implements Serializable {


    public String passwordEncryption(String password) {

        try {
            try {
                MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                messageDigest.update(password.getBytes());
                byte[] digest = messageDigest.digest();
                return DatatypeConverter.printHexBinary(digest).toUpperCase();

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return password;


    }
}
