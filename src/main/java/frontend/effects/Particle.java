package frontend.effects;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Particle {
    private double x;
    private double y;
    private double velocityX;
    private double velocityY;
    private double life;
    private double maxLife;
    private double maxX = 100;
    private double maxY = 100;

    public Particle(double x, double y) {
        this.x = x;
        this.y = y;
        this.velocityX = (Math.random() - 0.5) * 2;
        this.velocityY = (Math.random() - 0.5) * 2;
        this.maxLife = 100 + Math.random() * 50; // Random life duration
        this.life = maxLife;
    }

    public Particle(double x, double y,double maxX,double maxY) {
        this.x = x;
        this.y = y;
        this.maxX = maxX;
        this.maxY = maxY;
        this.velocityX = (Math.random() - 0.5) * 2;
        this.velocityY = (Math.random() - 0.5) * 2;
        this.maxLife = 100 + Math.random() * 50; // Random life duration
        this.life = maxLife;
    }

    public void update() {
        if(x+velocityX<0)
            velocityX*=-1;
        else if(x+velocityX>=maxX)
            velocityX*=-1;
        if(y+velocityY<0)
            velocityY*=-1;
        else if(y+velocityY>=maxY)
            velocityY*=-1;
        x += velocityX;
        y += velocityY;
       
        // life -= 1;
    }

    public boolean isDead() {
        return false;
    }

    public void draw(GraphicsContext gc) {
        // double alpha = life / maxLife; // Fade out effect
        // gc.setGlobalAlpha(alpha);
        // gc.setFill(Color.YELLOW); // Particle color
        // gc.fillOval(x, y, 5, 5); // Draw the particle
        // gc.setGlobalAlpha(1.0); // Reset alpha
        gc.setFill(Color.rgb(255, 255, 100, 0.5)); // Soft yellow with lower opacity
        gc.fillOval(x - 2.5, y - 2.5, 5, 5); // Larger size for the glow

        // Inner circle (core)
        gc.setFill(Color.rgb(255, 255, 100, 1.0)); // Bright yellow with full opacity
        gc.fillOval(x - 1, y - 1, 2, 2); // Smaller size for the core

    }
}