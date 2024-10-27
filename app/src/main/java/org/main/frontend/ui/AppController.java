package org.main.frontend.ui;


import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.control.Label;
import java.sql.SQLException;
import org.main.App;
import org.main.backend.Operation;
import org.main.backend.models.User;
import org.main.cryptomanager.Crypto;
import org.main.backend.records.Ressponse;
import org.worm.CRUD;
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
    private ImageView profilePic;

    @FXML
    private Label welcomeLabel;
    @FXML
    private ImageView loader;
    @FXML
    private AnchorPane root;
    // @FXML
    // private ImageView pr = new ImageView(new Image(getClass().getResourceAsStream("/org/main/frontend/icons/loader.gif")));
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
            boolean res = cr.setSecretkey(u.getDob());
            if(res)
                showloaderAndAlert("LOGIN SUCCESSfFUL", "Welcome "+u.getName()+"!");
            else
                System.out.println("ERRRRRIIIIIRRRRR");
            // ui.showMessage();

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
            Helper.clipImageToCircle(profilePic,100);
            loader.setImage(new Image(getClass().getResourceAsStream("/org/main/frontend/icons/loader.gif")));
            Helper.clipImageToCircle(loader, 100);
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
    private void showloaderAndAlert(String title,String message)
    {

        loader.setVisible(true);
        // root.getChildren().add(loader);
        Task<Void> loadSceneTask = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    // Load the new memory screen (scene creation)
                    Thread.sleep(1500); //if use quickly enters the password and stage is not fully loaded yet Alert box will not be position properly to fix this add this delay it will ensure that stage is loaded peroperly before showing the dialog
                    return null;
                }};
        loadSceneTask.setOnSucceeded(e->{
            ui.showMessage(title, message);
            try
            {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/main/frontend/fxml/mainMenu.fxml"));
                ui.changeToAnotherScene(loader);
                MainMenu mainMenu = loader.getController();
                if(mainMenu!=null)
                    mainMenu.initResources(ui,cr);
                else
                    System.out.println("Unable to generate the secretKey");
            }
            catch(Exception ex)
            {
                System.err.println("EX: "+ex);
                System.err.println("UNABLE TO LOAD A SCENE: "+e);
            }
        });
        new Thread(loadSceneTask).start();
                
    }   
}
