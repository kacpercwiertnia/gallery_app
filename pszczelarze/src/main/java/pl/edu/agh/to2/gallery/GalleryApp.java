package pl.edu.agh.to2.gallery;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import pl.edu.agh.to2.Main;

import java.io.IOException;

public class GalleryApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            var loader = new FXMLLoader();
            loader.setLocation(GalleryApp.class.getClassLoader().getResource("gallery.fxml"));
            BorderPane rootLayout = loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.setOnCloseRequest(e -> {
                Platform.exit();
                System.exit(0);
            });
            primaryStage.show();
        } catch (IOException e) {
            Main.log.info(e.getMessage());
        }
    }
}
