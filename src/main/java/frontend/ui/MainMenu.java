package frontend.ui;



import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;

import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.HTMLEditor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

import java.sql.SQLException;

// import backend.Operation;
import cryptomanager.Crypto;
import frontend.effects.ParticleSystem;
import main.App;
// import org.main.backend.records.Memory;
// import org.main.backend.records.Ressponse;
import org.worm.CRUD;
import backend.models.Memory;
import backend.models.User;


public class MainMenu {

    // private App ui;
    // private Crypto cr;
    // private Operation op;
    private CRUD<Memory> memo;
    // @FXML
    // private Pane particlePane;
    @FXML
    private Canvas canvas;
    @FXML 
    private StackPane root;
    // private HTMLEditor ht = new HTMLEditor();
    // private final HTMLEditor unusedEditor = new HTMLEditor(); //force the html editor to load here to prevent lag in next scene

    private ImageView pr = new ImageView(new Image(getClass().getResourceAsStream("/org/main/frontend/icons/loader.gif")));
    public void initResources() throws SQLException
    {
        // this.ui = ui;
        // this.cr = cr;
        this.memo = new CRUD<>(Memory.class);
        ParticleSystem particleSystem = new ParticleSystem(canvas.getGraphicsContext2D(),canvas.getWidth(),canvas.getHeight(),new Timeline());
        particleSystem.addParticle(200,100,100);
        
            // pr.setVisible(false);

    }
        
        // gc.fillRoundRect(5, 5, 20, 20, 50, 50);

    
    private static String removeBodyContent(String html) {
        // Regex to find <body ...>...</body> and replace its content
        return html.replaceAll("(?i)(<body[^>]*>)([\\s\\S]*?)(</body>)", "$1$3");
    }

    @FXML
    private void newMemo() {
            // ProgressIndicator pr = new ProgressIndicator();
            
            // pr.setFitWidth(400);
            // pr.setFitHeight(400);
            // pr.setPreserveRatio(true); // Maintain aspect ratio
            // pr.setSmooth(true); // Smooth rendering for the image

            // // Create a Circle for clipping
            // Circle clip = new Circle(200); // Half of width/height to achieve a circle
            // pr.setClip(clip); // Set the clip for the ImageView

            // // Center the clip on the ImageView
            // clip.centerXProperty().bind(pr.fitWidthProperty().divide(2));
            // clip.centerYProperty().bind(pr.fitHeightProperty().divide(2));
            // root.getChildren().add(pr);
           Task<FXMLLoader> task = Shared.ui.loadAndWait();
           if(task==null)
           {
                System.out.println("Task is null");
                return;
           }
           task.setOnSucceeded(e->{
                // FXMLLoader loader = task.getValue();
                try
                {
                    Shared.ui.changeToAnotherScene((FXMLLoader)task.getValue());
                    // MemoryController memo = loader.getController();
                    if(memo!=null)
                        ((MemoryController)((FXMLLoader)task.getValue()).getController()).initResources();
                }
                catch(Exception ex)
                {
                    System.out.println("Exception Raised Here");
                    System.out.println(ex);
                }
            });
            new Thread(task).start();


    }
    private boolean showProgressBar()
    {
        pr.setVisible(true);
        return true;
    }
    @FXML
    private void allMemo()
    {
        // pr.setVisible(!pr.isVisible());
    }
}
