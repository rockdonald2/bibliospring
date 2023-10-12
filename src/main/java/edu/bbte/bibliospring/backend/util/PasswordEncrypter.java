package edu.bbte.bibliospring.backend.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordEncrypter {

    private static final String PASSWORD_ALGORITHM;
    private static final String PASSWORD_ENCODING;

    static {
        PASSWORD_ALGORITHM = PropertyProvider.getProperty("password.algorithm").orElse("SHA1");
        PASSWORD_ENCODING = PropertyProvider.getProperty("password.encoding").orElse("UTF8");
    }

    public String hashPassword(String plainPassword, String plainSalt) throws SecurityException {
        try {
            MessageDigest algorithm = MessageDigest.getInstance(PASSWORD_ALGORITHM);
            byte[] bytes = (plainPassword + plainSalt).getBytes(PASSWORD_ENCODING);

            algorithm.reset();
            algorithm.update(bytes);

            return toHexString(algorithm.digest());
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new SecurityException("Failed to encrypt string", e);
        }
    }

    private String toHexString(byte[] bytes) {
        StringBuilder buffer = new StringBuilder();

        for (byte i : bytes) {
            String hex = Integer.toHexString(0xFF & i);

            if (hex.length() == 1) {
                buffer.append('0');
            }

            buffer.append(hex);
        }

        return buffer.toString();
    }

}
