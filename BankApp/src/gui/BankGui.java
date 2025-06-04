package gui;

import application.model.User;
import gui.actions.*;
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
    private GridPane pane;
    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        stage.setTitle("Banking Page");
        pane = new GridPane();
        pane.setPrefSize(420, 600);
        initContent();

        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
    }

    private void initContent() {
        pane.setHgap(10);
        pane.setVgap(10);
        pane.setPadding(new Insets(20));
        pane.setAlignment(Pos.CENTER);


        Label username = new Label("Welcome " + user.getUsername());
        username.setFont(new Font(24));
        pane.add(username, 0,0);

        balanceLbl = new Label("Current Balance: $" + user.getTotalBalance());
        balanceLbl.setFont(new Font(18));
        pane.add(balanceLbl,0,1);


        Button kontiButton = createActionButtonWithTitleAndPosition("Accounts", 2);
        kontiButton.setOnAction(event -> {
            KontiWindow kontiWindow = new KontiWindow("Konti", user);
            kontiWindow.showAndWait();
        });

        Button depositButton = createActionButtonWithTitleAndPosition("Deposit", 3);
        depositButton.setOnAction(event -> {
            DepostitWindow depostitWindow = new DepostitWindow("Deposit", user);
            depostitWindow.showAndWait();
            updateBalance();
        });

        Button withdrawButton = createActionButtonWithTitleAndPosition("Withdraw", 4);
        withdrawButton.setOnAction(event -> {
            WithdrawWindow withdrawWindow = new WithdrawWindow("Withdraw", user);
            withdrawWindow.showAndWait();
            updateBalance();
        });

        Button pastTransactionsButton = createActionButtonWithTitleAndPosition("Past Transactions", 5);
        pastTransactionsButton.setOnAction(event -> {
            try {
                PastTransactionWindow pastTransactionWindow = new PastTransactionWindow("PastTrans", user);
                pastTransactionWindow.showAndWait();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        Button transferButton = createActionButtonWithTitleAndPosition("Transfer", 6);
        transferButton.setOnAction(event -> {
            TransferWindow transferWindow = new TransferWindow("Transfer", user);
            transferWindow.showAndWait();
            updateBalance();
        });

        Button createAccButton = createActionButtonWithTitleAndPosition("Create Account", 7);
        createAccButton.setOnAction(event -> {
            CreateAccountWindow createAccountWindow = new CreateAccountWindow("Create Account", user);
            createAccountWindow.showAndWait();
            updateBalance();
        });

        Button logoutButton = createActionButtonWithTitleAndPosition("Logout", 9);
        logoutButton.setOnAction(event -> {
            LoginGui loginGui = new LoginGui();
            try {
                loginGui.start(stage);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private Button createActionButtonWithTitleAndPosition(String title, int position) {
        Button button = new Button(title);
        pane.add(button,0,position);
        button.setPrefWidth(250);
        button.setPrefHeight(50);
        button.setFont(new Font("Arial", 24));

        return button;
    }

    public void updateBalance(){
        balanceLbl.setText("Current Balance: $" + user.getTotalBalance());
    }
    public void setUser(User user){
        this.user = user;
    }
}
