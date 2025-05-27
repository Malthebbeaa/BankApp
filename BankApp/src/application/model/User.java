package application.model;

import java.math.BigDecimal;
import java.util.ArrayList;

public class User {
    private int userId;
    private String username;

    private ArrayList<Konto> konti;


    public User(int userId, String username){
        this.userId = userId;
        this.username = username;
        this.konti = new ArrayList<>();
    }

    public void addKonto(Konto konto) {
        if (!konti.contains(konto)) {
            konti.add(konto);
        }
    }
    public ArrayList<Konto> getKonti() {
        return konti;
    }

    public BigDecimal getTotalBalance() {
        BigDecimal totalBalance = new BigDecimal(0);

        for(Konto konto : konti) {
            totalBalance = totalBalance.add(konto.getSaldo());
        }

        return totalBalance;
    }

    public void setUserId(int userId){this.userId = userId;}
    public void setUsername(String username) {
        this.username = username;
    }


    public int getUserId() {
        return userId;
    }
    public String getUsername() {
        return username;
    }


    @Override
    public String toString() {
        return "ID = " + userId + ", username = " + username;
    }
}
