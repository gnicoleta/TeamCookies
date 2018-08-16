package group.msg.beans;

import group.msg.logger.LoggerProducer;

import javax.inject.Inject;
import javax.xml.bind.DatatypeConverter;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.logging.Logger;

public class PasswordEncryptor implements Serializable {

    @Inject
    private Logger logger;

    public String passwordEncryption(String password) {

        try {
            try {
                MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                messageDigest.update(password.getBytes());
                byte[] digest = messageDigest.digest();
                return DatatypeConverter.printHexBinary(digest).toUpperCase();

            } catch (NoSuchAlgorithmException e) {
                logger.info(Arrays.toString(e.getStackTrace()));
            }
        }catch (Exception e){
            logger.info(Arrays.toString(e.getStackTrace()));
        }
        return password;


    }
}
