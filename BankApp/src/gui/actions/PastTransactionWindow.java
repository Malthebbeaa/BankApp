package gui.actions;

import MyJDBC.GET.GETRequests;
import application.model.Transaction;
import application.model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.ArrayList;

public class PastTransactionWindow extends Stage {
    private User user;
    private TextArea pastTransactionsArea = new TextArea();
    public PastTransactionWindow(String titel, User user) throws SQLException {
        this.user = user;
        setTitle(titel);
        GridPane pane = new GridPane();
        pane.setPrefSize(450, 400);
        initContent(pane);

        ScrollPane scrollPane = new ScrollPane(pane);

        Scene scene = new Scene(scrollPane);
        setScene(scene);
    }

    private void initContent(GridPane pane) throws SQLException {
        pane.setHgap(50);
        pane.setVgap(10);
        pane.setPadding(new Insets(20));
        pane.setAlignment(Pos.CENTER);

        Label dateLbl = new Label("Date");
        dateLbl.setFont(new Font(18));
        Label typeLbl = new Label("Transaction type");
        typeLbl.setFont(new Font(18));
        Label amountLbl = new Label("Amount");
        amountLbl.setFont(new Font(18));


        pane.add(dateLbl, 0, 0);
        pane.add(typeLbl, 1, 0);
        pane.add(amountLbl, 2, 0);

        int userId = GETRequests.getUserId(user.getUsername());
        ArrayList<Transaction> pastTransactions = GETRequests.pastTransactions(userId);

        for (int i = 0; i < pastTransactions.size(); i++) {
            Transaction transaction = pastTransactions.get(i);

            Label date = new Label(transaction.getDate() + "");
            date.setFont(new Font(12));
            Label type = new Label(transaction.getTransactionType());
            type.setFont(new Font(12));
            Label amount = new Label("$"+transaction.getTransactionAmount());
            amount.setFont(new Font(12));


            pane.add(date, 0, i + 1);
            pane.add(type, 1, i + 1);
            pane.add(amount, 2, i + 1);
        }

    }

}
