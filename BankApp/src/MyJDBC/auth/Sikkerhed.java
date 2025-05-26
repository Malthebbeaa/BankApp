package MyJDBC.auth;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class Sikkerhed {
    /**
     *
     * @return en tilfældigt salt på 128 bits, til brug til hashing
     */
    public static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16]; // 128 bits
        random.nextBytes(salt);
        return salt;
    }

    /**
     *
     * @param inputPassword
     * @param salt
     * @return Genererer et bytes som er en hashed værdi: inputPassword + salt, som returnes som en String
     * @throws NoSuchAlgorithmException
     */
    public static String hashPassword(String inputPassword, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(salt);
        byte[] passwordhash = digest.digest(inputPassword.getBytes());
        String passwordHashBase64 = Base64.getEncoder().encodeToString(passwordhash);

        return passwordHashBase64;
    }
}
