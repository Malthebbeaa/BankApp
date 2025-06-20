package gui.actions;

import MyJDBC.PUT_POST.PUTAndPOSTRequests;
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

public class DepostitWindow extends Stage {
    private User user;
    private TextField amountTxf;
    private ComboBox<Konto> kontoComboBox;

    public DepostitWindow(String titel, User user){
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
        Label amountLbl = new Label("Amount:");
        pane.add(amountLbl, 0,0);
        amountTxf = new TextField();
        pane.add(amountTxf, 0,1);


        Label kontoLbl = new Label("Account:");
        pane.add(kontoLbl, 0,2);
        ObservableList kontolist = FXCollections.observableArrayList();
        for (Konto konto : user.getKonti()) {
            kontolist.add(konto);
        }
        kontoComboBox = new ComboBox<>(kontolist);
        pane.add(kontoComboBox, 0,3);

        Button depositButton = new Button("Deposit");
        pane.add(depositButton,0,4);
        depositButton.setOnAction(event -> {
            depositAction();
        });

    }

    private void depositAction(){
        int userId = user.getUserId();
        Konto konto = kontoComboBox.getValue();
        BigDecimal depositAmount = new BigDecimal(Integer.valueOf(amountTxf.getText()));
        PUTAndPOSTRequests.deposit(userId, depositAmount, konto);

        PUTAndPOSTRequests.updateKontoObject(konto);
        close();
    }
}
