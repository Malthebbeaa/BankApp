package application.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void addKonto() {
        //Arrange
        User user = new User(1, "test");
        Konto konto = new Konto(1, "1234567", "1234", new BigDecimal(200), "TestKonto");
        assertFalse(user.getKonti().contains(konto));
        //Act
        user.addKonto(konto);

        //Assert
        assertTrue(user.getKonti().contains(konto));
    }

    @Test
    void getKonti() {
        //Arrange
        User user = new User(1, "test");
        Konto konto = new Konto(1, "1234567", "1234", new BigDecimal(200), "TestKonto");
        Konto konto1 = new Konto(1, "2345678", "2345", new BigDecimal(200), "TestKonto");
        //Act
        user.addKonto(konto);
        user.addKonto(konto1);

        //Assert
        assertTrue(user.getKonti().contains(konto) && user.getKonti().contains(konto1));
    }

    @Test
    void getTotalBalance() {
        //Arrange
        User user = new User(1, "test");
        BigDecimal initialSaldo = new BigDecimal(200);
        Konto konto = new Konto(1, "1234567", "1234", initialSaldo, "TestKonto");
        //Act
        user.addKonto(konto);
        BigDecimal saldo = user.getTotalBalance();

        //Assert
        assertEquals(initialSaldo, saldo);
    }
}