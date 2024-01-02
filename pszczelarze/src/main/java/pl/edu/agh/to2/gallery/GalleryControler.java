package pl.edu.agh.to2.gallery;

import javafx.fxml.FXML;

import javafx.event.ActionEvent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import org.json.JSONArray;
import org.json.JSONObject;
import pl.edu.agh.to2.Main;
import pl.edu.agh.to2.rest.ImageIdsRequest;
import pl.edu.agh.to2.rest.ImageRequest;
import pl.edu.agh.to2.rest.ThumbnailsRequest;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;


public class GalleryControler {
    @FXML
    private GridPane thumbnailGrid;
    @FXML
    private ChoiceBox sizeSelect;
    private final List<ImageView> waitingThumbnails = new ArrayList<>();
    private final List<ImageView> colectedThumbnails= new ArrayList<>();
    private final List<Integer> waitingIds = new ArrayList<>();
    private final List<Integer> collectedIds = new ArrayList<>();
    private int numOfImages = 0;

    @FXML
    public void uploadImageButtonClicked(ActionEvent actionEvent){
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        String size = sizeSelect.getValue().toString();

        try {
            byte[] fileContent = Files.readAllBytes(file.toPath());
            String stringImage = Base64.getEncoder().encodeToString(fileContent);
            ImageRequest imageRequest = new ImageRequest(stringImage);
            imageRequest.build();

            HttpResponse<String> response = imageRequest.getResponse();

            if(response != null && response.statusCode() == 200){
                File file2 = new File("src/main/resources/images/placeholder_small.gif");
                Image image = new Image(file2.toURI().toString());
                ImageView imageView = new ImageView(image);
                waitingThumbnails.add(imageView);
                thumbnailGrid.add(imageView, numOfImages%4, numOfImages/4);
                numOfImages += 1;
                refreshIdsLists();
            }

        } catch (IOException e) {
            Main.log.info(e.getMessage());
        }
    }

    public void refreshIdsLists(){
        ImageIdsRequest imageIdsRequest = new ImageIdsRequest();
        imageIdsRequest.build();

        HttpResponse<String> response = imageIdsRequest.getResponse();

        if(response != null && response.statusCode() == 200){
            JSONObject jsonObject = new JSONObject(response.body());
            JSONArray imagesIds = (JSONArray) jsonObject.get("imagesIds");

            for(int i = 0; i < imagesIds.length(); i++){
                int id = imagesIds.getInt(i);

                if(!collectedIds.contains(id) && !waitingIds.contains(id)){
                    waitingIds.add(id);
                }
            }
        }
    }

    @FXML
    public void refreshThumbnailsLists(){
        refreshIdsLists();
        ThumbnailsRequest thumbnailsRequest = new ThumbnailsRequest(waitingIds, sizeSelect.getValue().toString());
        thumbnailsRequest.build();

        HttpResponse<String> response = thumbnailsRequest.getResponse();

        if(response != null && response.statusCode() == 200){
            JSONObject jsonObject = new JSONObject(response.body());
            JSONArray thumbnails = (JSONArray) jsonObject.get("thumbnails");

            for(int i = 0; i < thumbnails.length(); i++){
                JSONObject thumbnail = thumbnails.getJSONObject(i);
                int id = thumbnail.getInt("imageId");
                String image = thumbnail.getString("basedImage");
                boolean status = thumbnail.getBoolean("isCorrect");

                if(status){
                    collectedIds.add(id);
                    waitingIds.remove(Integer.valueOf(id));

                    InputStream is = Base64.getDecoder().wrap(new ByteArrayInputStream(image.getBytes()));

                    ImageView imageView = waitingThumbnails.remove(0);
                    imageView.setImage(new Image(is));

                    colectedThumbnails.add(imageView);
                }

            }
        }
    }
}
