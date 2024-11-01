plugins {
    id("application")
    id("org.openjfx.javafxplugin") version "0.1.0"
    id("com.github.johnrengelman.shadow") version "7.1.0"
}

repositories {
    mavenCentral()
}

javafx {
    version = "21.0.4"
    modules = listOf("javafx.controls", "javafx.fxml", "javafx.web")
}

dependencies {
    testImplementation(libs.junit)
    implementation(files("libs/org.worm.jar"))
    implementation("com.google.guava:guava:31.1-jre")
    implementation("org.xerial:sqlite-jdbc:3.42.0.0")
    implementation("org.mindrot:jbcrypt:0.4")
    implementation("com.google.code.gson:gson:2.10.1")
}

application {
    mainClass.set("org.main.main.App")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "org.main.main.App"
    }
}

tasks.shadowJar {
    manifest {
        attributes["Main-Class"] = "org.main.main.App" 
    }
}

