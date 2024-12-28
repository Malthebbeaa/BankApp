package gui.actions;

import MyJDBC.MyJDBC;
import application.model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.math.BigDecimal;

public class DepostitWindow extends Stage {
    private User user;
    private TextField amountTxf;
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
        Button depositButton = new Button("Deposit");
        pane.add(depositButton,0,2);
        depositButton.setOnAction(event -> {
            depositAction();
        });

    }

    private void depositAction(){
        int userId = MyJDBC.getUserId(user.getUsername());
        MyJDBC.deposit(userId, new BigDecimal(Integer.valueOf(amountTxf.getText())));
        user.setCurrentBalance(user.getCurrentBalance().add(new BigDecimal(Integer.valueOf(amountTxf.getText()))));
        close();
    }
}
