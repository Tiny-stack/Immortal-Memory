package org.main.frontend.ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.web.HTMLEditor;

public class Helper {
    public static String extractBodyContent(String html) {
        Pattern pattern = Pattern.compile("(?s)<body.*?>(.*?)</body>");
        Matcher matcher = pattern.matcher(html);
        return matcher.find() ? matcher.group(1) : "";
    }
    public static void setBodyContent(String newBodyContent,HTMLEditor htmlEditor) {
        String fullHtmlContent = htmlEditor.getHtmlText();
        Pattern pattern = Pattern.compile("(?s)(<body.*?>)(.*?)(</body>)");
        Matcher matcher = pattern.matcher(fullHtmlContent);

        if (matcher.find()) {
            // Replace the existing body content with the new body content
            String updatedHtml = matcher.replaceFirst("$1" + Matcher.quoteReplacement(newBodyContent) + "$3");
            htmlEditor.setHtmlText(updatedHtml);
        }
    }
    public static void clipImageToCircle(ImageView imageView,double radius)
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
}
