package gui.loginAndReg;

import MyJDBC.MyJDBC;
import application.model.User;
import gui.BankGui;
import gui.loginAndReg.RegisterGui;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class LoginGui extends Application {
    private Stage stage;
    private User user;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        stage.setTitle("Login Page");
        GridPane pane = new GridPane();
        pane.setPrefSize(420, 600);
        initContent(pane);

        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
    }

    private void initContent(GridPane pane) {
        pane.setHgap(15);
        pane.setVgap(15);
        pane.setPadding(new Insets(20));
        pane.setAlignment(Pos.CENTER);

        Label bankingLbl = new Label("Banking Application");
        bankingLbl.setFont(new Font(32));
        pane.add(bankingLbl, 0,0);
        GridPane.setHalignment(bankingLbl, HPos.CENTER);
        bankingLbl.setPadding(new Insets(50));

        Label usernamelbl = new Label("Username:");
        usernamelbl.setFont(new Font(18));
        pane.add(usernamelbl, 0,1);
        TextField usernameTxf = new TextField();
        pane.add(usernameTxf, 0,2);

        Label passwordlbl = new Label("Password:");
        passwordlbl.setFont(new Font(18));
        pane.add(passwordlbl,0,3);
        PasswordField passwordField = new PasswordField();
        pane.add(passwordField, 0,4);


        Button loginButton = new Button("Login");
        loginButton.setMaxWidth(Double.MAX_VALUE); // Make button fill the column width
        GridPane.setHalignment(loginButton, HPos.CENTER); // Align center
        pane.add(loginButton,0,5);
        loginButton.setOnAction(event -> {
            String username = usernameTxf.getText();
            String password = passwordField.getText();
            user = MyJDBC.validateUserLogin(username, password);

            if (user != null){
                BankGui bankGui = new BankGui();
                bankGui.setUser(user);
                try {
                    bankGui.start(stage);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
                errorAlert.setHeaderText("No user found");
                errorAlert.setContentText("Username or password is wrong");
                errorAlert.showAndWait();
            }
        });


        Label registerLink = new Label("Don't have an account? Register here!");
        registerLink.setStyle("-fx-text-fill: blue; -fx-underline: true;");
        registerLink.setOnMouseClicked(event -> {
            RegisterGui registerGui = new RegisterGui();
            try {
                registerGui.start(stage);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        pane.add(registerLink, 0,6);

    }

    public User getUser() {
        return user;
    }
}
