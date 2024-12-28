package application.model;

import java.math.BigDecimal;

public class User {
    private int userId;
    private String username;
    private String password;
    private BigDecimal currentBalance;


    public User(int userId, String username, String password, BigDecimal currentBalance){
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.currentBalance = currentBalance;
    }

    public void setUserId(int userId){this.userId = userId;}
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }

    public int getUserId() {
        return userId;
    }
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }
}
