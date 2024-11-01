package org.main.main;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.web.HTMLEditor;
import java.io.IOException;
import java.util.Optional;
import org.main.backend.Operation;
import org.main.backend.models.User;
import org.main.cryptomanager.Crypto;
import org.main.frontend.ui.AppController;
import javafx.util.Duration;
import org.main.frontend.ui.MainMenu;
import org.worm.CRUD;

public class App extends Application {
    private static Operation op;
    private Stage stage;
    private final HTMLEditor garbage = new HTMLEditor();
    private FXMLLoader loader;
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
    app.initResources(this);
    Screen screen = Screen.getPrimary();
    Rectangle2D bounds = screen.getVisualBounds();

    primary.setX(0);
    primary.setY(0);
    primary.setWidth(bounds.getWidth());
    primary.setHeight(bounds.getHeight());

    Scene scene = new Scene(root,primary.getWidth(),primary.getHeight());
    System.out.println("X: "+primary.getX()+" Y: "+primary.getY());
    primary.setScene(scene);
    primary.setTitle("Sign Up");
    // primary.setResizable(false);

    primary.setMaximized(true);
    primary.show();
    }

    public static void main(String[] args) throws IOException {
        // op = Operation.initConnection();
        launch(args);
    }

    private static boolean userExist()
    {
        try
        {
            CRUD<User> crudUser = new CRUD<>(User.class);
            return crudUser.findAll().size()>0;

        }
        catch(Exception e)
        {
            return false;
        }
    }
     public void showMessage(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);  // Alert type is INFORMATION
        alert.initModality(Modality.WINDOW_MODAL);
        alert.setTitle(title);                           // Title of the pop-up
        alert.setHeaderText(null);                       // Optional header, can be null
        alert.setContentText(message);                   // Message content
        alert.initOwner(stage); 
        alert.setOnShown(e -> {
                Platform.runLater(() -> {

            alert.setX(this.stage.getX() + (this.stage.getWidth() / 2+alert.getWidth())/2);
            alert.setY(this.stage.getY() + (this.stage.getHeight() / 2-alert.getHeight()/2));
            System.out.println("ALERT X: "+alert.getX()+" ALERT Y: "+alert.getY());
            System.out.println("ALERT X: "+alert.getX()+" ALERT Y: "+alert.getY());
        });

        });
        // System.out.println("X: "+(this.stage.getX() + (this.stage.getWidth() - alert.getWidth()) / 2)+" Y: "+stage.getY());

        alert.showAndWait();  // Display the alert and wait for user to close it
     
    }
    public void changeToAnotherScene(FXMLLoader loader) throws Exception {
        // FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/main/frontend/ui/other_scene.fxml"));
            Parent anotherRoot = loader.load();

            FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), this.stage.getScene().getRoot());
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);

            // Set an action to switch the scene after fade out is complete
            fadeOut.setOnFinished(event -> {
                Scene newScene = new Scene(anotherRoot,this.stage.getWidth(),this.stage.getHeight());
                this.stage.setScene(newScene);

                // Create a fade in transition for the new scene
                FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), newScene.getRoot());
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();
            });

            fadeOut.play();
            loader = null;
    }
    public void changeToAnotherScene(String loaderPath) throws Exception {
            loader = new FXMLLoader(getClass().getResource(loaderPath));
            Parent anotherRoot = loader.load();

            FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), this.stage.getScene().getRoot());
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);

            // Set an action to switch the scene after fade out is complete
            fadeOut.setOnFinished(event -> {
                Scene newScene = new Scene(anotherRoot,this.stage.getWidth(),this.stage.getHeight());
                this.stage.setScene(newScene);

                // Create a fade in transition for the new scene
                FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), newScene.getRoot());
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();
            });

            fadeOut.play();
            loader = null;
    }
    public int showYesNoDialog(String title,String message,String text) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.initModality(Modality.WINDOW_MODAL); // Make it modal to the owner
        alert.initOwner(stage); 
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.setContentText(text);

        // Set the button types (Yes and No)
        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(yesButton, noButton, cancelButton);

        // Show the dialog and wait for the user's response
        Optional<ButtonType> result = alert.showAndWait();

        // Check the response
        if (result.isPresent()) {
            if (result.get() == yesButton) {
                return 1;
            } else if (result.get() == noButton) {
                System.out.println("User chose No");
                return 0;
            } else {
                System.out.println("User chose Cancel");
                return 2;
                
            }
        }
        return 2;
    }
    public Task<FXMLLoader> loadAndWait()
    {
         try
            {
                
                Task<FXMLLoader> loadSceneTask = new Task<>() {
                @Override
                protected FXMLLoader call() throws Exception {
                    // Load the new memory screen (scene creation)
                    Thread.sleep(1500);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/main/frontend/fxml/memory.fxml"));       
                    return loader;
                }};
                return loadSceneTask;
                
            }
            catch(Exception e)
            {
                System.err.println("EX: "+e);
                System.err.println("UNABLE TO LOAD A SCENE: "+e);
                return null;
            }
    }

    public void loadMainMenuScreen(Crypto cr)
    {
         try
            {
                this.loader = new FXMLLoader(getClass().getResource("/org/main/frontend/fxml/mainMenu.fxml"));
                changeToAnotherScene(loader);
                MainMenu controller = loader.getController();
                if(controller!=null)
                controller.initResources(this, cr);
            }
            catch(Exception e)
            {
                System.err.println("EX: "+e);
                System.err.println("UNABLE TO LOAD A SCENE: "+e);
            }
    }

    public void clipImageToCircle(ImageView imageView,double radius)
    {
        imageView.setFitWidth(radius*2);
        imageView.setFitHeight(radius*2);
        imageView.setPreserveRatio(true); // Maintain aspect ratio
        imageView.setSmooth(true); // Smooth rendering for the image
        Circle clip = new Circle(radius); // Half of width/height to achieve a circle
        imageView.setClip(clip); // Set the clip for the ImageView

        // Center the clip on the ImageView
        clip.centerXProperty().bind(imageView.fitWidthProperty().divide(2));
        clip.centerYProperty().bind(imageView.fitHeightProperty().divide(2));   
    }
    public String getGreeting()
    {
        return "Hello";
    }
}
