package org.main.frontend.ui;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import java.sql.SQLException;
import org.main.App;
import org.main.backend.Operation;
import org.main.backend.models.User;
import org.main.cryptomanager.Crypto;
import org.main.backend.records.Ressponse;
import org.main.backend.victororm.CRUD;
// import org.main.backend.models.User;;


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
    private void handleSignup() throws SQLException{
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        System.out.println("Signup clicked");
        this.cr = new Crypto(password);
        password = cr.hashPassword(password);
        long signUpTimestamp = System.currentTimeMillis();
        String encryptedTimeStamp = cr.encryptTimestamp(signUpTimestamp);
        // Ressponse re = op.signUp(username, "Hello", password, "I am a programmer", encryptedTimeStamp);
        Integer id = new CRUD<>(User.class).save(new User(username,encryptedTimeStamp,password,email,null));
        if(id>0)
        {
            System.err.println("success");
            ui.showMessage("SUCCESS", "Sign Up successful! Kindly remember your password");
            try
            {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/main/frontend/fxml/login.fxml"));
                ui.changeToAnotherScene(loader);
                AppController app = loader.getController();
                app.initResources(ui);
            }
            catch(Exception e)
            {
                System.err.println("UNABLE TO LOAD A SCENE");
            }
        }
        // Handle signup logic here
    }
    @FXML
    private void handleLogin() throws SQLException{
        String password = passwordField.getText();
        System.out.println("LOGIN clicked");
        this.cr = new Crypto(password);
        // System.err.println(op.login());
        User u = new CRUD<>(User.class).findAll(1,0).get(0);
        if(u==null)
            throw new SQLException("ERROR");
        if(cr.checkPassword(password, u.getPassword()))
        {
            
            ui.showMessage("LOGIN SUCCESSfFUL", "Welcome "+u.getName()+"!");
            try
            {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/main/frontend/fxml/memory.fxml"));
                ui.changeToAnotherScene(loader);
                MemoryController memo = loader.getController();
                boolean res = cr.setSecretkey(u.getDob());
                if(res)
                    memo.initResources(ui,cr);
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
    public void initResources(App ui)
    {
        try
        {
            String userName = new CRUD<>(User.class).findAll().get(0).getName();
            if(userName.length()>0)
                this.welcomeLabel.setText("Welcome "+userName);
        }
        catch(Exception e)
        {

        }
        this.ui = ui;
    }
    public void initResources(App ui,Crypto cr)
    {
        this.ui = ui;
        this.cr = cr;
    }
    
}
