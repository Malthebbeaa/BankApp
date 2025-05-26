package gui.actions;

import MyJDBC.MyJDBC;
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
    private TextField userToIdTxf, amountTxf;
    private BigDecimal amount;
    private ComboBox<User> userComboBox;

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

        Label amountlbl = new Label("Amount for transfer");
        pane.add(amountlbl, 0,0);
        amountTxf = new TextField();
        pane.add(amountTxf, 1,0);

        ObservableList usersList = FXCollections.observableArrayList();
        for (User user : MyJDBC.getUsers()) {
            if (user.getUserId() != userFrom.getUserId()){
                usersList.add(user);
            }
        }

        Label userToLbl = new Label("Receiving user: ");
        pane.add(userToLbl, 0,1);
        userComboBox = new ComboBox<>(usersList);
        pane.add(userComboBox,1,1);

        Button transferButton = new Button("Transfer");
        transferButton.setOnAction(event -> {
            transferAction();
            //userFrom.setCurrentBalance(userFrom.getCurrentBalance().subtract(amount));
            close();
        });
        pane.add(transferButton,0,2);
    }

    public boolean transferAction(){
        int userToId = userComboBox.getValue().getUserId();
        int userFromId = MyJDBC.getUserId(userFrom.getUsername());
        amount = BigDecimal.valueOf(Integer.valueOf(amountTxf.getText()));

        return MyJDBC.transfer(userToId, userFromId,"","","","", amount);
    }
}
