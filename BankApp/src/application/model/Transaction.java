package application.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

public class Transaction {
    private int userId;
    private String transactionType;
    private BigDecimal transactionAmount;
    private Date date;

    public Transaction(int userId, String transactionType, BigDecimal transactionAmount, Date date){
        this.userId = userId;
        this.transactionType = transactionType;
        this.transactionAmount = transactionAmount;
        this.date = date;
    }

    public void setUser(int userId) {
        this.userId = userId;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getUser() {
        return userId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public Date getDate() {
        return date;
    }
}
