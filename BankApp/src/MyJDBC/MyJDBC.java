package MyJDBC;

import application.model.Konto;
import application.model.Transaction;
import application.model.User;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;

public class MyJDBC {
    public static int getUserId(String username){
        try {
            int user_Id = 0;
            Connection minConnection = DriverManager
                    .getConnection("jdbc:sqlserver://localhost;databaseName=bankdb;user=sa;password=MyStrongPass123;");

            PreparedStatement preparedStatement = minConnection.prepareStatement("SELECT user_id FROM BankUser WHERE username = ?");

            preparedStatement.clearParameters();
            preparedStatement.setString(1, username);
            ResultSet res = preparedStatement.executeQuery();

            while (res.next()){
                user_Id = res.getInt(1);
            }
            return user_Id;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean deposit(int userId, BigDecimal amount, Konto konto){
        try {
            Connection minConnection = DriverManager
                    .getConnection("jdbc:sqlserver://localhost;databaseName=bankdb;user=sa;password=MyStrongPass123;");

            PreparedStatement preparedStatement = minConnection.prepareStatement("EXEC deposit ?, ?, ?, ?");

            preparedStatement.clearParameters();
            preparedStatement.setInt(1, userId);
            preparedStatement.setBigDecimal(2, amount);
            preparedStatement.setString(3, konto.getKontoNr());
            preparedStatement.setString(4, konto.getRegNr());

            return preparedStatement.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean withdraw(int userId, BigDecimal amount, Konto konto){
        try {
            Connection minConnection = DriverManager
                    .getConnection("jdbc:sqlserver://localhost;databaseName=bankdb;user=sa;password=MyStrongPass123;");

            PreparedStatement preparedStatement = minConnection.prepareStatement("EXEC withdraw ?, ?, ?, ?");

            preparedStatement.clearParameters();
            preparedStatement.setInt(1, userId);
            preparedStatement.setBigDecimal(2, amount);
            preparedStatement.setString(3, konto.getKontoNr());
            preparedStatement.setString(4, konto.getRegNr());

            return preparedStatement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<Transaction> pastTransactions(int userId){
        ArrayList<Transaction> pastTransactions = new ArrayList<>();
        try {
            Connection minConnection = DriverManager
                    .getConnection("jdbc:sqlserver://localhost;databaseName=bankdb;user=sa;password=MyStrongPass123;");

            PreparedStatement preparedStatement = minConnection.prepareStatement("EXEC pastTransactions ?");

            preparedStatement.clearParameters();
            preparedStatement.setInt(1, userId);

            ResultSet res = preparedStatement.executeQuery();

            while (res.next()){
                Transaction transaction = new Transaction(userId, res.getString(2), res.getBigDecimal(3),res.getDate(4));
                pastTransactions.add(transaction);
            }

            return pastTransactions;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean transfer(Konto fromKonto, Konto toKonto, BigDecimal amount){
        try {
            Connection minConnection = DriverManager
                    .getConnection("jdbc:sqlserver://localhost;databaseName=bankdb;user=sa;password=MyStrongPass123;");

            PreparedStatement preparedStatement = minConnection.prepareStatement("EXEC transferAction ?, ?, ?, ?, ?, ?, ?");

            int fromUserId = getUserIdFromKontoAndReg(fromKonto.getKontoNr(), fromKonto.getRegNr());
            int toUserId = getUserIdFromKontoAndReg(toKonto.getKontoNr(), toKonto.getRegNr());

            preparedStatement.clearParameters();
            preparedStatement.setInt(1, fromUserId);
            preparedStatement.setInt(2, toUserId);
            preparedStatement.setBigDecimal(3, amount);
            preparedStatement.setString(4, fromKonto.getKontoNr());
            preparedStatement.setString(5, fromKonto.getRegNr());
            preparedStatement.setString(6, toKonto.getKontoNr());
            preparedStatement.setString(7, toKonto.getRegNr());

            return preparedStatement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<User> getUsers(){
        try {
            ArrayList<User> users = new ArrayList<>();
            Connection minConnection = DriverManager
                    .getConnection("jdbc:sqlserver://localhost;databaseName=bankdb;user=sa;password=MyStrongPass123;");

            PreparedStatement preparedStatement = minConnection.prepareStatement("SELECT * FROM BankUser");
            ResultSet res = preparedStatement.executeQuery();

            while (res.next()){
                User user = new User(res.getInt(1), res.getString(2));
                users.add(user);
            }

            return users;
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public static int getUserIdFromKontoAndReg(String kontoNr, String regNr) {
        try {
            Connection minConnection = DriverManager
                    .getConnection("jdbc:sqlserver://localhost;databaseName=bankdb;user=sa;password=MyStrongPass123;");

            PreparedStatement preparedStatement = minConnection.prepareStatement("exec getUserIdFromKontoAndReg ?, ?");

            preparedStatement.clearParameters();
            preparedStatement.setString(1, kontoNr);
            preparedStatement.setString(2, regNr);
            ResultSet res = preparedStatement.executeQuery();

            if (res.next()) {
                return res.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Konto getKontoFromKontoNrAndRegNr(String kontoNr, String regNr) {
        try {
            Connection minConnection = DriverManager
                    .getConnection("jdbc:sqlserver://localhost;databaseName=bankdb;user=sa;password=MyStrongPass123;");


            String sql = "SELECT * FROM Konto WHERE kontoNr = ? AND regNr = ?";
            PreparedStatement prestmt = minConnection.prepareStatement(sql);
            prestmt.setString(1, kontoNr);
            prestmt.setString(2, regNr);

            ResultSet result = prestmt.executeQuery();

            if (result.next()) {
                int userId = result.getInt("user_id");
                String kNr = result.getString("kontoNr");
                String rNr = result.getString("regNr");
                BigDecimal saldo = result.getBigDecimal("saldo");
                String kontotype = result.getString("kontotype");

                Konto konto = new Konto(userId, kNr, rNr, saldo, kontotype);
                return konto;
            }
        } catch (SQLException e) {

        }
        return null;
    }

    public static void updateSingleKonto(Konto konto) {
        try {
            Connection minConnection = DriverManager
                    .getConnection("jdbc:sqlserver://localhost;databaseName=bankdb;user=sa;password=MyStrongPass123;");


            String sql = "SELECT saldo FROM Konto WHERE kontoNr = ? AND regNr = ? AND user_id = ?";
            PreparedStatement prestmt = minConnection.prepareStatement(sql);

            prestmt.setString(1, konto.getKontoNr());
            prestmt.setString(2, konto.getRegNr());
            prestmt.setInt(3, konto.getOwner_id());

            ResultSet res = prestmt.executeQuery();

            if (res.next()) {
                BigDecimal newSaldo = res.getBigDecimal(1);

                konto.setSaldo(newSaldo);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Konto createKonto(User user,String kontoType, double initialSaldo) {
        try {
            Connection minConnection = DriverManager
                    .getConnection("jdbc:sqlserver://localhost;databaseName=bankdb;user=sa;password=MyStrongPass123;");


            String sql = "exec createAccount ?, ?, ?; select * from inserted";
            PreparedStatement prestmt = minConnection.prepareStatement(sql);

            prestmt.setInt(1, user.getUserId());
            prestmt.setBigDecimal(2, new BigDecimal(initialSaldo));
            prestmt.setString(3, kontoType);


            ResultSet res = prestmt.executeQuery();

            if (res.next()) {
                int userId = res.getInt("user_id");
                String kNr = res.getString("kontoNr");
                String rNr = res.getString("regNr");
                BigDecimal saldo = res.getBigDecimal("saldo");
                String kontotype = res.getString("kontotype");

                Konto konto = new Konto(userId, kNr, rNr, saldo, kontotype);
                user.addKonto(konto);
                return konto;
            } else  {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
