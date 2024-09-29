package org.main.frontend.ui;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

import org.main.App;
import org.main.backend.Operation;
import org.main.cryptomanager.Crypto;
import org.main.backend.records.Ressponse;

public class AppController {
    private App ui;
    private Crypto cr;
    private Operation op;

    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextArea memoryEditor;
    @FXML
    private TextField memoryTitle;

    @FXML
    private Label welcomeLabel;

    @FXML
    private void handleSignup() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        System.out.println("Signup clicked");
        this.cr = new Crypto(password);
        password = cr.hashPassword(password);
        long signUpTimestamp = System.currentTimeMillis();
        String encryptedTimeStamp = cr.encryptTimestamp(signUpTimestamp);
        Ressponse re = op.signUp(username, "Hello", password, "I am a programmer", encryptedTimeStamp);
        if(re.status)
        {
            System.err.println("success");
            ui.showMessage("SUCCESS", "Sign Up successful! Kindly remember your password");
            try
            {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/main/frontend/fxml/login.fxml"));
                ui.changeToAnotherScene(loader);
                AppController app = loader.getController();
                app.initResources(ui, op);
            }
            catch(Exception e)
            {
                System.err.println("UNABLE TO LOAD A SCENE");
            }
        }
        // Handle signup logic here
    }
    @FXML
    private void handleLogin() {
        String password = passwordField.getText();
        System.out.println("LOGIN clicked");
        this.cr = new Crypto(password);
        System.err.println(op.login());
        if(cr.checkPassword(password, op.login()))
        {
            
            ui.showMessage("LOGIN SUCCESS FUL", "Welcome "+op.getUserName()+"!");
            try
            {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/main/frontend/fxml/memory.fxml"));
                ui.changeToAnotherScene(loader);
                MemoryController memo = loader.getController();
                boolean res = cr.setSecretkey(op.getSignUpDate());
                if(res)
                    memo.initResources(ui, op,cr);
                else
                    System.out.println("Unable to generate the secretKey");
            }
            catch(Exception e)
            {
                System.err.println("EX: "+e);
                System.err.println("UNABLE TO LOAD A SCENE");
            }

        }
        else
        {
            ui.showMessage("AUTHENTICATION FAILED", "Wrong Password, Try Again!");
        }
        // Handle signup logic here
    }
    public void initResources(App ui,Operation op)
    {
        String userName = op.getUserName();
        try
        {
            if(userName.length()>0)
                this.welcomeLabel.setText("Welcome "+op.getUserName());
        }
        catch(Exception e)
        {

        }
        this.ui = ui;
        this.op = op;
        System.out.println("OP: "+op);
    }
    public void initResources(App ui,Operation op,Crypto cr)
    {
        String userName = op.getUserName();
        this.ui = ui;
        this.op = op;
        this.cr = cr;
        System.out.println("OP: "+op);
    }
    
}
