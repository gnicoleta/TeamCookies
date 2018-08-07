package group.msg.beans;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordEncryptor {
    public String passwordEncryption(String password){

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(password.getBytes());
            byte[] digest=messageDigest.digest();
            return DatatypeConverter.printHexBinary(digest).toUpperCase();

        }catch (NoSuchAlgorithmException e){

        }
        return password;


    }
}
