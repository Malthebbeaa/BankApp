package MyJDBC.PUT_POST;

import MyJDBC.GET.GETRequests;
import application.model.Konto;
import application.model.User;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;

public class PUTAndPOSTRequests {
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

    public static boolean transfer(Konto fromKonto, Konto toKonto, BigDecimal amount){
        try {
            Connection minConnection = DriverManager
                    .getConnection("jdbc:sqlserver://localhost;databaseName=bankdb;user=sa;password=MyStrongPass123;");

            PreparedStatement preparedStatement = minConnection.prepareStatement("EXEC transferAction ?, ?, ?, ?, ?, ?, ?");

            int fromUserId = GETRequests.getUserIdFromKontoAndReg(fromKonto.getKontoNr(), fromKonto.getRegNr());
            int toUserId = GETRequests.getUserIdFromKontoAndReg(toKonto.getKontoNr(), toKonto.getRegNr());

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
