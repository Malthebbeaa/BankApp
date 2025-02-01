package gui.loginAndReg;

import MyJDBC.MyJDBC;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class RegisterGui extends Application {
    private Stage stage;
    private TextField usernameTxf;
    private PasswordField passwordField, reTypePasswordField;
    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        stage.setTitle("Register Page");
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


        Label registerLbl = new Label("Register");
        pane.add(registerLbl, 0,0);
        registerLbl.setFont(new Font(24));
        GridPane.setHalignment(registerLbl, HPos.CENTER);
        registerLbl.setPadding(new Insets(50));

        Label usernamelbl = new Label("Username:");
        pane.add(usernamelbl, 0,1);
        usernamelbl.setFont(new Font(18));
        usernameTxf = new TextField();
        pane.add(usernameTxf, 0,2);

        Label passwordlbl = new Label("Password:");
        pane.add(passwordlbl,0,3);
        passwordlbl.setFont(new Font(18));
        passwordField = new PasswordField();
        pane.add(passwordField, 0,4);

        Label reTypePasswordlbl = new Label("Retype Password:");
        pane.add(reTypePasswordlbl,0,5);
        reTypePasswordlbl.setFont(new Font(18));
        reTypePasswordField = new PasswordField();
        pane.add(reTypePasswordField, 0,6);


        Button registerButton = new Button("Register");
        pane.add(registerButton,0,7);
        registerButton.setOnAction(event -> {
            String username = usernameTxf.getText();
            String password = passwordField.getText();
            String reTypePassword = reTypePasswordField.getText();

            if (checkPasswords(password, reTypePassword) && username.length() > 0){
                if (MyJDBC.registerUser(username, password)){
                    Alert alertSuccesful = new Alert(Alert.AlertType.INFORMATION);
                    alertSuccesful.setHeaderText("Succesfully Registered");
                    alertSuccesful.setContentText("You have registered succesfully. OK to go to login page");
                    alertSuccesful.showAndWait();

                    LoginGui loginGui = new LoginGui();
                    try {
                        loginGui.start(stage);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Alert alertRegister = new Alert(Alert.AlertType.ERROR);
                    alertRegister.setHeaderText("Invalid Input");
                    alertRegister.setContentText("Username is already taken");
                    alertRegister.showAndWait();
                }
            } else {
                Alert alertPassword = new Alert(Alert.AlertType.INFORMATION);
                alertPassword.setHeaderText("Wrong password");
                alertPassword.setContentText("Write the same password twice");
                alertPassword.showAndWait();
            }
        });
        registerButton.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHalignment(registerButton, HPos.CENTER);

        Label registerLink = new Label("Already have an account? Login here!");
        registerLink.setStyle("-fx-text-fill: blue; -fx-underline: true;");
        registerLink.setOnMouseClicked(event -> {
            LoginGui loginGui = new LoginGui();
            try {
                loginGui.start(stage);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        pane.add(registerLink, 0,8);
    }

    public boolean checkPasswords(String password, String passwordRetype){
        return password.equals(passwordRetype);
    }
}
