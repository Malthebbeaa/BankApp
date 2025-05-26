package application.model;

import java.math.BigDecimal;

public class Konto {
    /*
    user_id int FOREIGN KEY REFERENCES BankUser(user_id),
    kontoNr char(7),
    regNr char(4),
    saldo DECIMAL(10,2),
    kontoType VARCHAR(20),
    PRIMARY KEY (kontoNr, regNr)
     */
    private int owner_id;
    private String kontoNr;
    private String regNr;
    private BigDecimal saldo;
    private String kontoType;

    public Konto(int owner_id, String kontoNr, String regNr, BigDecimal saldo, String kontoType) {
        this.owner_id = owner_id;
        this.kontoNr = kontoNr;
        this.regNr = regNr;
        this.saldo = saldo;
        this.kontoType = kontoType;
    };

    @Override
    public String toString() {
        return "Konto{" +
                "owner_id=" + owner_id +
                ", kontoNr='" + kontoNr + '\'' +
                ", regNr='" + regNr + '\'' +
                ", saldo=" + saldo +
                ", kontoType='" + kontoType + '\'' +
                '}';
    }

    public void setOwner_id(int owner_id) {
        this.owner_id = owner_id;
    }

    public void setKontoNr(String kontoNr) {
        this.kontoNr = kontoNr;
    }

    public void setRegNr(String regNr) {
        this.regNr = regNr;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public void setKontoType(String kontoType) {
        this.kontoType = kontoType;
    }

    public int getOwner_id() {
        return owner_id;
    }

    public String getKontoNr() {
        return kontoNr;
    }

    public String getRegNr() {
        return regNr;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public String getKontoType() {
        return kontoType;
    }
}
