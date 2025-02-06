package hellofx;

import backend.models.User;
import cryptomanager.Crypto;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.worm.CRUD;

public class HelloFX extends Application {

    @Override
    public void start(Stage stage) {
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        Label l = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
        Scene scene = new Scene(new StackPane(l), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        try{
            Crypto cr = new Crypto("1235");
            String t = cr.hashPassword("123445");
            System.out.println(": "+t);
            CRUD.changeDB("this.db");
            CRUD<User> userCrud = new CRUD<>(User.class);
            User user = new User("this", "that", "12345", "not this", null);
            userCrud.save(user);
        }
        catch(Exception e)
        {
            System.out.println("Exceotion: "+e);
        }
      
        launch();
    }

}
