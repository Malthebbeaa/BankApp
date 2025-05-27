package MyJDBC.auth;

import application.model.Konto;
import application.model.User;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class Auth {
    private static Connection minConnection;
    static {
        try {
            minConnection = DriverManager.getConnection("jdbc:sqlserver://localhost;databaseName=bankdb;user=sa;password=MyStrongPass123;");
            System.out.println("Database connected!");
        } catch (SQLException e) {
            System.err.println("Connection failed!");
            e.printStackTrace(); // giver dig den rigtige fejlmeddelelse
        }
    }

    /**
     *
     * @param inputUsername
     * @param inputPassword
     *
     * genererer en unik salt, der benyttes til at hashe inputpassword
     * inputUsername, det nye hashede password og den nye salt sendes dernæst videre til generering
     * @throws NoSuchAlgorithmException
     */
    public static boolean opretBankuserAction(String inputUsername, String inputPassword) throws NoSuchAlgorithmException {
        if (inputUsername.length() == 0 || inputPassword.length() == 0) {
            throw new RuntimeException("Username eller password skal udfyldes");
        } else if (inputUsername.equals(null) || inputPassword.equals(null)) {
            throw new RuntimeException("Username eller password må ikke være null");
        }

        byte[] salt = Sikkerhed.generateSalt();
        String passwordHashBase64 = Sikkerhed.hashPassword(inputPassword, salt);

        return generateBankuser(inputUsername, passwordHashBase64, salt);
    }

    /**
     *
     * @param inputUsername
     * @param hashedpassword
     * @param salt
     *
     * generer en række i Bruger tabellen i databasen,
     * med det indtastede username, det hashede password og den salt der er benyttet til hashing
     */
    private static boolean generateBankuser(String inputUsername, String hashedpassword, byte[] salt) {
        try {
            String sql = "exec createUser ?, ?, ?";
            PreparedStatement prestmt = minConnection.prepareStatement(sql);
            prestmt.clearParameters();

            prestmt.setString(1, inputUsername);
            prestmt.setString(2, hashedpassword);
            prestmt.setBytes(3, salt);
            int result = prestmt.executeUpdate();

            return result == 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param inputUsername
     * @param inputPassword
     * @return user med tilhørende konti
     * @throws NoSuchAlgorithmException, hvis brugeren ikke indtaster oplysninger korrekt
     */
    public static User loginAction(String inputUsername, String inputPassword) throws NoSuchAlgorithmException {
        if (inputUsername.length() == 0 || inputPassword.length() == 0) {
            throw new RuntimeException("Username eller password skal udfyldes");
        } else if (inputUsername.equals(null) || inputPassword.equals(null)) {
            throw new RuntimeException("Username eller password må ikke være null");
        }

        if (authenticate(inputUsername, inputPassword)) {
            User user = getUser(inputUsername);
            addKontiTilBruger(user);
            return user;
        } else  {
            return null;
        }
    }

    /**
     *
     * @param inputUsername
     * @param inputPassword
     * @return returnerer true hvis der findes en bruger, hvor det fundne hashedpassword
     * er magentil det hashedpassword der genereres ud fra inputPasseword og den fundne salt på brugeren
     */
    private static boolean authenticate(String inputUsername, String inputPassword) {
        try {
            String resultPasswordHash = null;
            byte[] resultSalt = null;
            minConnection = DriverManager.getConnection("jdbc:sqlserver://localhost;databaseName=bankdb;user=sa;password=MyStrongPass123;");

            String sql = "SELECT user_password, salt FROM BankUser WHERE username = ?";
            PreparedStatement prestmt = minConnection.prepareStatement(sql);
            prestmt.setString(1, inputUsername);

            ResultSet result = prestmt.executeQuery();

            if (result.next()) {
                resultPasswordHash = result.getString("user_password");
                resultSalt = result.getBytes("salt");
            } else {
                System.out.println("Bruger findes ikke");
                return false;
            }

            String inputHash = Sikkerhed.hashPassword(inputPassword, resultSalt);

            return inputHash.equals(resultPasswordHash);
        } catch (SQLException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static User getUser(String inputUsername) {
        try {
            String sql = "SELECT user_id FROM BankUser WHERE username = ?";
            PreparedStatement prestmt = minConnection.prepareStatement(sql);
            prestmt.setString(1, inputUsername);

            int userId;

            ResultSet result = prestmt.executeQuery();

            if (result.next()) {
                userId = result.getInt("user_id");
            } else {
                System.out.println("Bruger findes ikke");
                return null;
            }

            return new User(userId, inputUsername);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private static void addKontiTilBruger(User user) {
        try {
            String sql = "SELECT * FROM Konto WHERE user_id = ?";
            PreparedStatement prestmt = minConnection.prepareStatement(sql);
            prestmt.setInt(1, user.getUserId());

            ResultSet result = prestmt.executeQuery();

            while (result.next()) {
                int userId = result.getInt("user_id");
                String kontoNr = result.getString("kontoNr");
                String regNr = result.getString("regNr");
                BigDecimal saldo = result.getBigDecimal("saldo");
                String kontotype = result.getString("kontotype");

                Konto konto = new Konto(userId, kontoNr, regNr, saldo, kontotype);
                user.addKonto(konto);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateUserInformation(User user) {
        try {
            String sql = "SELECT * FROM Konto WHERE user_id = ?";

            PreparedStatement prestmt = minConnection.prepareStatement(sql);
            prestmt.setInt(1, user.getUserId());

            ResultSet res = prestmt.executeQuery();

            while (res.next()) {

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
