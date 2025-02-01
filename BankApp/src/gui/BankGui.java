package gui;

import application.model.User;
import gui.actions.DepostitWindow;
import gui.actions.PastTransactionWindow;
import gui.actions.TransferWindow;
import gui.actions.WithdrawWindow;
import gui.loginAndReg.LoginGui;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.SQLException;

public class BankGui extends Application {
    private Stage stage;
    private User user;
    private TextField balance;
    private Label balanceLbl;
    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        stage.setTitle("Banking Page");
        GridPane pane = new GridPane();
        pane.setPrefSize(420, 600);
        initContent(pane);

        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
    }

    private void initContent(GridPane pane) {
        pane.setHgap(10);
        pane.setVgap(10);
        pane.setPadding(new Insets(20));
        pane.setAlignment(Pos.CENTER);


        Label username = new Label("Welcome " + user.getUsername());
        username.setFont(new Font(24));
        pane.add(username, 0,0);

        balanceLbl = new Label("Current Balance: $" + user.getCurrentBalance());
        balanceLbl.setFont(new Font(18));
        pane.add(balanceLbl,0,1);


        Button depositButton = new Button("Deposit");
        pane.add(depositButton,0,2);
        depositButton.setPrefWidth(250);
        depositButton.setPrefHeight(50);
        depositButton.setFont(new Font("Arial", 24));

        depositButton.setOnAction(event -> {
            DepostitWindow depostitWindow = new DepostitWindow("Deposit", user);
            depostitWindow.showAndWait();
            updateBalance();
        });

        Button withdrawButton = new Button("Withdraw");
        pane.add(withdrawButton,0,3);
        withdrawButton.setPrefWidth(250);
        withdrawButton.setPrefHeight(50);
        withdrawButton.setFont(new Font("Arial", 24));

        withdrawButton.setOnAction(event -> {
            WithdrawWindow withdrawWindow = new WithdrawWindow("Withdraw", user);
            withdrawWindow.showAndWait();
            updateBalance();
        });

        Button pastTransactionsButton = new Button("Past Transactions");
        pane.add(pastTransactionsButton,0,4);
        pastTransactionsButton.setPrefWidth(250);
        pastTransactionsButton.setPrefHeight(50);
        pastTransactionsButton.setFont(new Font("Arial", 24));
        pastTransactionsButton.setOnAction(event -> {
            try {
                PastTransactionWindow pastTransactionWindow = new PastTransactionWindow("PastTrans", user);
                pastTransactionWindow.showAndWait();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        Button transferButton = new Button("Transfer");
        pane.add(transferButton,0,5);
        transferButton.setPrefWidth(250);
        transferButton.setPrefHeight(50);
        transferButton.setFont(new Font("Arial", 24));
        transferButton.setOnAction(event -> {
            TransferWindow transferWindow = new TransferWindow("Transfer", user);
            transferWindow.showAndWait();
            updateBalance();
        });

        Button logoutButton = new Button("Logout");
        pane.add(logoutButton,0,7);
        logoutButton.setPrefWidth(250);
        logoutButton.setPrefHeight(50);
        logoutButton.setFont(new Font("Arial", 24));
        logoutButton.setOnAction(event -> {
            LoginGui loginGui = new LoginGui();
            try {
                loginGui.start(stage);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void updateBalance(){
        balanceLbl.setText("Current Balance: $" + user.getCurrentBalance());
        //balance.clear();
        //balance.setText(String.valueOf(user.getCurrentBalance()));
    }
    public void setUser(User user){
        this.user = user;
    }
}
