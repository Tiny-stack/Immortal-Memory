// /*
//  * This source file was generated by the Gradle 'init' task
//  */
// package org.main;
// import java.util.*;
// import org.main.backend.Operation;

// import javafx.application.Application;
// import javafx.scene.Scene;
// import javafx.scene.control.Label;
// import javafx.scene.control.TextArea;
// import javafx.stage.Stage;
// import javafx.scene.control.Label;
// import java.io.BufferedReader;
// import java.io.InputStreamReader;
// import java.io.Reader;
// import java.io.IOException;
// import org.main.cryptomanager.Crypto;
// import org.main.backend.records.Ressponse;
// // import org.main.backend.Operation;

// public class App extends Application{
//     public String getGreeting() {
//         return "Hello World!";
//     }
//     @Override
//     public void start(Stage stage) {
//         Label label = new Label("Hello, JavaFX!");
//         TextArea textArea = new TextArea();
//         Scene scene = new Scene(label, 400, 300);
//         stage.setScene(scene);
//         stage.setTitle("Digital Personal Diary");
    
//         stage.show();
//     }
//     public static void main(String[] args) throws IOException {
//         System.out.println(new App().getGreeting());
//         Operation op = Operation.initConnection();
//         Scanner sc = new Scanner(System.in);
//         if(op==null)
//         {
//             System.out.println("FAILD");
//             return;
//         }
//         // op.insertRecord("hello","I am here",5);
//         System.out.println("Reading: "+op.readMemories(5,5,"id"));
//         // launch(args);
//         // BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//         // String userName = op.getUserName();
//         // String password = "12,34,5";
//         // String encPass = "";
//         // if(userName.equals(""))
//         // {
//         //     userName = "tiny";
//         //     password = "12,34,5";
//         // }
//         // else
//         // {
//         //     System.out.print("Welcome! "+userName);
//         //     encPass = op.login();
            
//         // }
//         // System.out.println("Enter Your Password: ");
        
//         // Crypto cr = new Crypto(password);
//         // System.out.println("encPass: "+encPass);
//         // if(!encPass.equals(""))
//         // {
//         //     if(cr.checkPassword(password, encPass))
//         //         System.err.println("PASS Matched");
//         //     else
//         //         System.out.println("NOt matched");
//         // }


//         // password = cr.hashPassword(password);
//         // long signUpTimestamp = System.currentTimeMillis();  // Use current timestamp as example
//         // String encDate = cr.encryptTimestamp(signUpTimestamp);
//         // System.out.println(op.signUp(userName,"tiny",password,"Iam tinty",encDate));
//         // System.out.println("Enter a text: ");
//         // String text = br.readLine();
//         // String encText = cr.encryptText(text);
//         // System.out.println("Encrypted text: "+encText);
//         // op.insertRecord(encText, "nice", 0);
//         // String dec = cr.decryptText(encText);
//         // System.err.println("Decrypted text: "+dec);

//         // Operation op = Operation.initConnection();

        





        
//     }
// }
package org.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;

import org.main.backend.Operation;
import org.main.frontend.ui.AppController;
import org.main.backend.victororm.*;

public class App extends Application {
    private static Operation op;
    private Stage stage;
    @Override
    public void start(Stage primary) throws IOException {
        this.stage = primary;
    FXMLLoader loader;
    if(userExist())
        loader = new FXMLLoader(getClass().getResource("/org/main/frontend/fxml/login.fxml"));
    else
    {
        loader = new FXMLLoader(getClass().getResource("/org/main/frontend/fxml/signup.fxml"));
    }

    Parent root = loader.load();
    AppController app = loader.getController();
    app.initResources(this, op);
   
    Scene scene = new Scene(root, 300, 200);
    primary.setScene(scene);
    primary.setTitle("Sign Up");
    primary.show();
    }

    // public static void main(String[] args) throws IOException {
    //     op = Operation.initConnection();
    //     launch(args);
    // }

    public static void main(String[] args) throws SQLException
	{
		CRUD<User> userCrud = new CRUD<>(User.class);
		// User u = new User("Vishwajeet Rauniyar","12,3,4","09/10/2001","I am Tiny",null);
        // u.setId(1l);
		// User u2 = new User("Tiny","123","09/10/2001","I am his friend",u);
       

		// userCrud.save(u2);
		// System.out.println("Saved");
        // System.out.println("u1: "+u);
        // System.out.println("u2: "+u2);
        System.out.println(userCrud.findById(7));
        System.out.println(userCrud.delete(6));
	}
    private static boolean userExist()
    {
        return !op.getUserName().equals("");
    }
     public void showMessage(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);  // Alert type is INFORMATION
        alert.setTitle(title);                           // Title of the pop-up
        alert.setHeaderText(null);                       // Optional header, can be null
        alert.setContentText(message);                   // Message content

        alert.showAndWait();  // Display the alert and wait for user to close it
    }
    public void changeToAnotherScene(FXMLLoader loader) throws Exception {
        // FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/main/frontend/ui/other_scene.fxml"));
            Parent anotherRoot = loader.load();
            this.stage.setScene(new Scene(anotherRoot, 400, 300)); // Example of scene switching
    }

}