package MyJDBC.GET;

import application.model.Konto;
import application.model.Transaction;
import application.model.User;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;

public class GETRequests {
    public static int getUserId(String username){
        try {
            int user_Id = 0;
            Connection minConnection = DriverManager
                    .getConnection("jdbc:sqlserver://localhost;databaseName=bankdb;user=sa;password=MyStrongPass123;");

            minConnection.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
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

    public static ArrayList<Transaction> pastTransactions(int userId){
        ArrayList<Transaction> pastTransactions = new ArrayList<>();
        try {
            Connection minConnection = DriverManager
                    .getConnection("jdbc:sqlserver://localhost;databaseName=bankdb;user=sa;password=MyStrongPass123;");

            minConnection.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
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

    public static ArrayList<User> getUsers(){
        try {
            ArrayList<User> users = new ArrayList<>();
            Connection minConnection = DriverManager
                    .getConnection("jdbc:sqlserver://localhost;databaseName=bankdb;user=sa;password=MyStrongPass123;");

            minConnection.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
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

            minConnection.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
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

            minConnection.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
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

}
