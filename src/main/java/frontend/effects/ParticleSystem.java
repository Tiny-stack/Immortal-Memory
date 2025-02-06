package frontend.effects;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
public class ParticleSystem  {
    private List<Particle> particles;
    private Canvas canvas;
    private AnimationTimer animationTimer;
    private double width;
    private double height;
    private Timeline timeline;

    public ParticleSystem(GraphicsContext gc,double width, double height,Timeline timeline) {
        this.particles = new ArrayList<>();
        this.width = width;
        this.height = height;
        this.timeline = timeline;
        // animationTimer = new AnimationTimer() {
        //     @Override
        //     public void handle(long now) {
        //         updateParticles();
        //         drawParticles(gc);
        //     }
        // };
        KeyFrame keyFrame = new KeyFrame(
            Duration.seconds(.017), // Duration of the key frame
            event -> {
               updateParticles();
               drawParticles(gc);
            }
        );
        timeline.getKeyFrames().add(keyFrame);

        // Set the timeline to repeat indefinitely
        timeline.setCycleCount(Timeline.INDEFINITE);

        // Start the animation
        timeline.play();
        // animationTimer.start();
    }

    public void addParticle() {
        particles.add(new Particle(this.width/2, this.height/2,this.width,this.height));
    }
    public void addParticle(double x, double y,int count) {

        for(int i = 0;i<count;i++)
            this.addParticle();
    }

    private void updateParticles() {
        Iterator<Particle> it = particles.iterator();
        while (it.hasNext()) {
            Particle p = it.next();
            p.update();
            if (p.isDead()) {
                it.remove(); // Remove dead particles
            }
        }
    }

    private void drawParticles(GraphicsContext gc) {
        // Clear canvas
        gc.clearRect(0, 0, this.width, this.height);

        // Draw particles
        for (Particle p : particles) {
            p.draw(gc);
        }
    }

    // Stops the particle animation
    public void stop() {
        animationTimer.stop();
    }
}
