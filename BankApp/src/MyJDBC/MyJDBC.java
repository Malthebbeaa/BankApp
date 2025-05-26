package MyJDBC;

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

    public static boolean deposit(int userId, BigDecimal amount, String kontoNr, String regNr){
        try {
            Connection minConnection = DriverManager
                    .getConnection("jdbc:sqlserver://localhost;databaseName=bankdb;user=sa;password=MyStrongPass123;");

            PreparedStatement preparedStatement = minConnection.prepareStatement("EXEC deposit ?, ?, ?, ?");

            preparedStatement.clearParameters();
            preparedStatement.setInt(1, userId);
            preparedStatement.setBigDecimal(2, amount);
            preparedStatement.setString(3, kontoNr);
            preparedStatement.setString(4, regNr);

            return preparedStatement.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean withdraw(int userId, BigDecimal amount, String kontoNr, String regNr){
        try {
            Connection minConnection = DriverManager
                    .getConnection("jdbc:sqlserver://localhost;databaseName=bankdb;user=sa;password=MyStrongPass123;");

            PreparedStatement preparedStatement = minConnection.prepareStatement("EXEC withdraw ?, ?, ?, ?");

            preparedStatement.clearParameters();
            preparedStatement.setInt(1, userId);
            preparedStatement.setBigDecimal(2, amount);
            preparedStatement.setString(3, kontoNr);
            preparedStatement.setString(4, regNr);

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

    public static boolean transfer(int userIdTo, int userIdFrom, String fromKontoNr, String fromRegNr, String toKontoNr, String toRegNr, BigDecimal amount){
        try {
            Connection minConnection = DriverManager
                    .getConnection("jdbc:sqlserver://localhost;databaseName=bankdb;user=sa;password=MyStrongPass123;");

            PreparedStatement preparedStatement = minConnection.prepareStatement("EXEC transferAction ?, ?, ?, ?, ?, ?, ?");

            preparedStatement.clearParameters();
            preparedStatement.setInt(1, userIdFrom);
            preparedStatement.setInt(2, userIdTo);
            preparedStatement.setBigDecimal(3, amount);
            preparedStatement.setString(4, fromKontoNr);
            preparedStatement.setString(5, fromRegNr);
            preparedStatement.setString(6, toKontoNr);
            preparedStatement.setString(7, toRegNr);

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
}
