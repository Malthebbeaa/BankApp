package MyJDBC.PUT_POST;

import MyJDBC.GET.GETRequests;
import application.model.Konto;
import application.model.User;

import java.math.BigDecimal;
import java.sql.*;

public class PUTAndPOSTRequests {
    public static boolean deposit(int userId, BigDecimal amount, Konto konto){
        try {
            Connection minConnection = DriverManager
                    .getConnection("jdbc:sqlserver://localhost;databaseName=bankdb;user=sa;password=MyStrongPass123;");

            minConnection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
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

            minConnection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
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

            minConnection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
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


    public static void updateKontoObject(Konto konto) {
        try {
            Connection minConnection = DriverManager
                    .getConnection("jdbc:sqlserver://localhost;databaseName=bankdb;user=sa;password=MyStrongPass123;");


            minConnection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
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


    public static Konto createKonto(User user, String kontoType, double initialSaldo) {
        try {
            Connection minConnection = DriverManager
                    .getConnection("jdbc:sqlserver://localhost;databaseName=bankdb;user=sa;password=MyStrongPass123;");

            minConnection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            CallableStatement callStmt = minConnection.prepareCall("{call createAccount(?, ?, ?)}");
            callStmt.setInt(1, user.getUserId());
            callStmt.setBigDecimal(2, new BigDecimal(initialSaldo));
            callStmt.setString(3, kontoType);

            boolean hasResults = callStmt.execute();

            if (hasResults) {
                ResultSet res = callStmt.getResultSet();
                if (res.next()) {
                    int userId = res.getInt("user_id");
                    String kNr = res.getString("kontoNr");
                    String rNr = res.getString("regNr");
                    BigDecimal saldo = res.getBigDecimal("saldo");
                    String kontotype = res.getString("kontotype");

                    Konto konto = new Konto(userId, kNr, rNr, saldo, kontotype);
                    user.addKonto(konto);
                    return konto;
                }
            }

            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
