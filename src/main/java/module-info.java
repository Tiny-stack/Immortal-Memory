module main {
    requires javafx.controls;
    requires javafx.web;
    requires javafx.fxml;
    // requires java.sql;
    requires org.worm;
    // requires org.mindrot.jbcrypt;
    // requires jBCrypt;
    // requires org.mindrot.jbcrypt;
    // requires java.crypto; // For encryption (AES, Cipher, etc.)
    // requires spring.security.crypto;
    requires java.logging;
    // requires com.gluonhq;
    requires com.gluonhq.richtextarea;
     opens frontend.ui to javafx.fxml;
    // requires java.sql;
    // requires commons.logging;
    exports hellofx;
    exports backend.models;
    exports cryptomanager;
    exports frontend.effects;
    exports frontend.ui;
    exports main;
    exports org.mindrot.jbcrypt;
}