package gui.actions;

import MyJDBC.MyJDBC;
import application.model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.math.BigDecimal;

public class WithdrawWindow extends Stage {
    private User user;
    private TextField amountTxf;
    public WithdrawWindow(String titel, User user){
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
        amountTxf = new TextField();
        pane.add(amountTxf, 0,0);
        Button withdrawButton = new Button("Withdraw");
        pane.add(withdrawButton,0,1);
        withdrawButton.setOnAction(event -> {
            withdrawAction();
        });

    }

    private void withdrawAction(){
        int userId = MyJDBC.getUserId(user.getUsername());
        MyJDBC.withdraw(userId, new BigDecimal(Integer.valueOf(amountTxf.getText())), "", "");
        //user.setCurrentBalance(user.getCurrentBalance().subtract(new BigDecimal(Integer.valueOf(amountTxf.getText()))));
        close();
    }
}
