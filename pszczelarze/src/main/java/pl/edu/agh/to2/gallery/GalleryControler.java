package pl.edu.agh.to2.gallery;

import javafx.fxml.FXML;

import javafx.event.ActionEvent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.util.Base64;


public class GalleryControler {
    @FXML
    private GridPane thumbnailGrid;
    @FXML
    private ChoiceBox sizeSelect;
    @FXML
    public void uploadImageButtonClicked(ActionEvent actionEvent){
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        String size = sizeSelect.getValue().toString();

        try {
            byte[] fileContent = Files.readAllBytes(file.toPath());
            String stringImage = Base64.getEncoder().encodeToString(fileContent);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/image"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString("{\"images\":[\""+stringImage+"\"]}"))
                    .build();

            HttpResponse<String> response = null;
            System.out.println(request.bodyPublisher());
            try {
                response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(response.body());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
