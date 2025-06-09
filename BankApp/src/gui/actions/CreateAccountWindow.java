package gui.actions;

import MyJDBC.PUT_POST.PUTAndPOSTRequests;
import application.model.Konto;
import application.model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class CreateAccountWindow extends Stage {
    private User user;
    private TextField kontoTypeTxf, initialSaldoTxf;

    public CreateAccountWindow(String titel, User user){
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

        Label kontoTypeLbl = new Label("Account Type");
        pane.add(kontoTypeLbl, 0,0);
        kontoTypeTxf = new TextField();
        pane.add(kontoTypeTxf, 0, 1);

        Label saldolbl = new Label("Initial Saldo");
        pane.add(saldolbl, 0, 2);
        initialSaldoTxf = new TextField();
        pane.add(initialSaldoTxf, 0, 3);

        Button createAccountButton = new Button("Create Account");
        createAccountButton.setOnAction(event -> {
            Konto createdKonto = createAccount();
        });
        pane.add(createAccountButton,0,5);
    }

    private Konto createAccount() {
        String kontoType = kontoTypeTxf.getText();
        double initalSaldo = Double.valueOf(initialSaldoTxf.getText());

        Konto konto = PUTAndPOSTRequests.createKonto(user, kontoType, initalSaldo);

        clearFields();
        return konto;
    }

    private void clearFields(){
        kontoTypeTxf.clear();
        initialSaldoTxf.clear();
    }
}
