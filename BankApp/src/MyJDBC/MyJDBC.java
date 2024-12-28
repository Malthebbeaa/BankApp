package MyJDBC;

import application.model.Transaction;
import application.model.User;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;

public class MyJDBC {
    public static User validateUserLogin(String username, String password){
        try {
            Connection minConnection = DriverManager
                    .getConnection("jdbc:sqlserver://localhost;databaseName=BankingDatabase;user=sa;password=MyStrongPass123;");

            PreparedStatement preparedStatement = minConnection.prepareStatement("SELECT * FROM BankUser WHERE username = ? AND user_password = ?");

            preparedStatement.clearParameters();
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet res = preparedStatement.executeQuery();

            while (res.next()){
                int id = res.getInt("id");

                BigDecimal currentBalance = res.getBigDecimal("currentBalance");

                return new User(id,username, password, currentBalance);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static boolean registerUser(String username, String password){
        try {
            if (!checkUser(username)){
                Connection minConnection = DriverManager
                        .getConnection("jdbc:sqlserver://localhost;databaseName=BankingDatabase;user=sa;password=MyStrongPass123;");

                String sql = "INSERT INTO BankUser(username, user_password) VALUES (?, ?)";

                PreparedStatement preparedStatement = minConnection.prepareStatement(sql);

                preparedStatement.clearParameters();
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);

                return preparedStatement.executeUpdate() == 1;

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public static boolean checkUser(String username){
        try {
            Connection minConnection = DriverManager
                    .getConnection("jdbc:sqlserver://localhost;databaseName=BankingDatabase;user=sa;password=MyStrongPass123;");

            String sql = "SELECT * FROM BankUser WHERE username = ?";

            PreparedStatement preparedStatement = minConnection.prepareStatement(sql);

            preparedStatement.clearParameters();
            preparedStatement.setString(1, username);

            ResultSet res = preparedStatement.executeQuery();

            if (!res.next()){
                return false;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public static int getUserId(String username){
        try {
            int user_Id = 0;
            Connection minConnection = DriverManager
                    .getConnection("jdbc:sqlserver://localhost;databaseName=BankingDatabase;user=sa;password=MyStrongPass123;");

            PreparedStatement preparedStatement = minConnection.prepareStatement("SELECT id FROM BankUser WHERE username = ?");

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

    public static boolean deposit(int userId, BigDecimal amount){
        try {
            Connection minConnection = DriverManager
                    .getConnection("jdbc:sqlserver://localhost;databaseName=BankingDatabase;user=sa;password=MyStrongPass123;");

            PreparedStatement preparedStatement = minConnection.prepareStatement("EXEC deposit ?, ?");

            preparedStatement.clearParameters();
            preparedStatement.setInt(1, userId);
            preparedStatement.setBigDecimal(2, amount);

            return preparedStatement.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean withdraw(int userId, BigDecimal amount){
        try {
            Connection minConnection = DriverManager
                    .getConnection("jdbc:sqlserver://localhost;databaseName=BankingDatabase;user=sa;password=MyStrongPass123;");

            PreparedStatement preparedStatement = minConnection.prepareStatement("EXEC withdraw ?, ?");

            preparedStatement.clearParameters();
            preparedStatement.setInt(1, userId);
            preparedStatement.setBigDecimal(2, amount);


            return preparedStatement.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<Transaction> pastTransactions(int userId){
        ArrayList<Transaction> pastTransactions = new ArrayList<>();
        try {
            Connection minConnection = DriverManager
                    .getConnection("jdbc:sqlserver://localhost;databaseName=BankingDatabase;user=sa;password=MyStrongPass123;");

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

    public static boolean transfer(int userIdTo, int userIdFrom, BigDecimal amount){
        try {
            Connection minConnection = DriverManager
                    .getConnection("jdbc:sqlserver://localhost;databaseName=BankingDatabase;user=sa;password=MyStrongPass123;");

            PreparedStatement preparedStatement = minConnection.prepareStatement("EXEC transferAction ?, ?, ?");

            preparedStatement.clearParameters();
            preparedStatement.setInt(1, userIdFrom);
            preparedStatement.setInt(2, userIdTo);
            preparedStatement.setBigDecimal(3, amount);

            return preparedStatement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
