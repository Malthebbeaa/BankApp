package gui.actions;

import MyJDBC.MyJDBC;
import application.model.Konto;
import application.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.math.BigDecimal;

public class TransferWindow extends Stage {
    private User userFrom;
    private User userTo;
    private TextField userToIdTxf, amountTxf, toKontoNr, toRegNr;
    private BigDecimal amount;
    private ComboBox<Konto> kontoComboBox;

    public TransferWindow(String titel, User userFrom){
        this.userFrom = userFrom;
        setTitle(titel);
        GridPane pane = new GridPane();
        pane.setPrefSize(450, 400);
        initContent(pane);

        Scene scene = new Scene(pane);
        setScene(scene);
    }

    private void initContent(GridPane pane) {
        pane.setHgap(10);
        pane.setVgap(10);
        pane.setPadding(new Insets(20));
        pane.setAlignment(Pos.CENTER);

        Label fromKontoLbl = new Label("From Account");
        pane.add(fromKontoLbl, 0,0);

        ObservableList kontolist = FXCollections.observableArrayList();
        for (Konto konto : userFrom.getKonti()) {
            kontolist.add(konto);
        }
        kontoComboBox = new ComboBox<>(kontolist);
        pane.add(kontoComboBox, 0,1);

        Label amountlbl = new Label("Amount for transfer");
        pane.add(amountlbl, 0,2);
        amountTxf = new TextField();
        pane.add(amountTxf, 0,3);


        Label toKontonrLbl = new Label("Receiving Account Number: ");
        pane.add(toKontonrLbl, 0, 5);
        toKontoNr = new TextField();
        pane.add(toKontoNr, 0,6);

        Label toRegNrLbl = new Label("Receiving Registration Number: ");
        pane.add(toRegNrLbl, 0, 7);
        toRegNr = new TextField();


        pane.add(toRegNr, 0,8);

        Button transferButton = new Button("Transfer");
        transferButton.setOnAction(event -> {
            transferAction();
            //userFrom.setCurrentBalance(userFrom.getCurrentBalance().subtract(amount));
            close();
        });
        pane.add(transferButton,0,10);
    }

    public boolean transferAction(){
        amount = BigDecimal.valueOf(Integer.valueOf(amountTxf.getText()));
        Konto fromKonto = kontoComboBox.getValue();

        String toKNR = toKontoNr.getText();
        String toRNR = toRegNr.getText();
        Konto toKonto = MyJDBC.getKontoFromKontoNrAndRegNr(toKNR, toRNR);

        return MyJDBC.transfer(fromKonto, toKonto, amount);
    }
}
