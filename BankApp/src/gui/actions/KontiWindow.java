package gui.actions;

import application.model.Konto;
import application.model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class KontiWindow extends Stage {
    private User user;

    public KontiWindow(String titel, User user){
        this.user = user;
        setTitle(titel);
        GridPane pane = new GridPane();
        pane.setPrefSize(450, 400);
        initContent(pane);

        Scene scene = new Scene(pane);
        setScene(scene);
    }

    private void initContent(GridPane pane) {
        //todo styling
        pane.setHgap(10);
        pane.setVgap(10);
        pane.setPadding(new Insets(20));
        pane.setAlignment(Pos.CENTER);
        Label kontiLbl = new Label( user.getUsername()+ "'s Konti");
        kontiLbl.setFont(new Font(24));
        pane.add(kontiLbl, 0,0, 3, 1);

        Label kontoNrLbl = new Label("KontoNr");
        kontoNrLbl.setFont(new Font(18));
        Label regNrLbl = new Label("RegNr");
        regNrLbl.setFont(new Font(18));
        Label saldolbl = new Label("Saldo");
        saldolbl.setFont(new Font(18));
        pane.add(kontoNrLbl, 0, 1);
        pane.add(regNrLbl, 1,1);
        pane.add(saldolbl, 2, 1);
        int i = 2;
        for (Konto konto : user.getKonti()) {
            Label kontoNr = new Label(konto.getKontoNr());
            Label regNr = new Label(konto.getRegNr());
            Label saldo = new Label(konto.getSaldo().toString() + " kr");
            pane.add(kontoNr, 0, i);
            pane.add(regNr, 1, i);
            pane.add(saldo, 2, i);
            i++;
        }
    }
}
