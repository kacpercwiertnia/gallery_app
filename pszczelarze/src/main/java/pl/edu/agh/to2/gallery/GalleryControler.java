package pl.edu.agh.to2.gallery;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import pl.edu.agh.to2.Main;
import pl.edu.agh.to2.image.OriginalImageController;
import pl.edu.agh.to2.rest.GetImageRequest;
import pl.edu.agh.to2.rest.ImageIdsRequest;
import pl.edu.agh.to2.rest.PostImageRequest;
import pl.edu.agh.to2.rest.ThumbnailsRequest;
import pl.edu.agh.to2.thumbnails.CashedThumbnails;
import pl.edu.agh.to2.thumbnails.ThumbnailSize;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;

public class GalleryControler {
    @FXML
    private GridPane thumbnailGrid;
    @FXML
    private ComboBox<ThumbnailSize> sizeSelect;
    @FXML
    private Label uploadImagesLabel;
    private final CashedThumbnails thumbnails;
    private Map<Integer,ImageView> selectedThumbnails;
    private List<Integer> waitingIds;
    private List<String> uploadedImages;
    private String placeholderUrl = "placeholder_small.gif";
    private Thread scheduler;

    public GalleryControler(){
        this.scheduler = new PollingScheduler(this);
        this.scheduler.start();
        this.thumbnails = new CashedThumbnails();
        this.selectedThumbnails = thumbnails.getThumbnails(ThumbnailSize.SMALL);
        this.waitingIds = thumbnails.getWaitingImagesIds(ThumbnailSize.SMALL);
        this.uploadedImages = new ArrayList<>();
    }

    @FXML
    public void uploadImageButtonClicked(ActionEvent actionEvent){
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);

        try {
            byte[] fileContent = Files.readAllBytes(file.toPath());
            String stringImage = Base64.getEncoder().encodeToString(fileContent);
            uploadedImages.add(stringImage);
            uploadImagesLabel.setText("Wybrane obrazki: " + uploadedImages.size());
        } catch (IOException e) {
            Main.log.info(e.getMessage());
        }
    }

    public void sendUploadedImages(ActionEvent actionEvent){
        PostImageRequest postImageRequest = new PostImageRequest(uploadedImages);
        postImageRequest.build();

        HttpResponse<String> response = postImageRequest.getResponse();

        if(response != null && response.statusCode() == 200){
            refreshIdsLists();
        }
        uploadedImages.clear();
        uploadImagesLabel.setText("");
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

                if(!selectedThumbnails.containsKey(id) && !waitingIds.contains(id)){
                    waitingIds.add(id);
                    File file2 = new File("src/main/resources/images/"+placeholderUrl);
                    Image image = new Image(file2.toURI().toString());
                    ImageView imageView = new ImageView(image);
                    selectedThumbnails.put(id, imageView);
                    thumbnailGrid.add(selectedThumbnails.get(id), (selectedThumbnails.size()-1)%4, (selectedThumbnails.size()-1)/4);
                }
            }
        }
    }

    public void refreshThumbnailsLists(){
        refreshIdsLists();

        if (waitingIds.isEmpty()) return;

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
                    waitingIds.remove(Integer.valueOf(id));
                    InputStream is = Base64.getDecoder().wrap(new ByteArrayInputStream(image.getBytes()));
                    ImageView imageView = selectedThumbnails.get(id);
                    imageView.setImage(new Image(is));
                    imageView.addEventHandler(MouseEvent.MOUSE_CLICKED,event->{
                        seeOriginalImage(event,id);
                    });
                }
            }
        }
    }

    @FXML
    public void thumbnailSizeChanged(ActionEvent event){
        thumbnailGrid.getChildren().clear();
        this.waitingIds = thumbnails.getWaitingImagesIds(sizeSelect.getValue());
        this.selectedThumbnails = thumbnails.getThumbnails(sizeSelect.getValue());

        switch(sizeSelect.getValue()){
            case SMALL -> placeholderUrl = "placeholder_small.gif";
            case MEDIUM -> placeholderUrl = "placeholder_medium.gif";
            case LARGE -> placeholderUrl = "placeholder_large.gif";
        }

        refreshThumbnailsLists();
        redrawThumbnailGrid();
    }

    private void redrawThumbnailGrid(){
        selectedThumbnails.forEach((K,V)->{
            if (!thumbnailGrid.getChildren().contains(V)) {
                thumbnailGrid.getChildren().add(V);
            }
        });
    }

    private void seeOriginalImage(MouseEvent event, int imageId) {
        try{
            var request = new GetImageRequest(imageId);
            request.build();

            HttpResponse<String> response = request.getResponse();
            if (response.statusCode()==200){
                Image image;
                String encodedImage = new JSONObject(response.body()).getString("image");
                byte[] byteImage = Base64.getDecoder().decode(encodedImage);
                try (InputStream is = new BufferedInputStream(new ByteArrayInputStream(byteImage))){
                    image = new Image(is);
                }
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("originalImage.fxml"));
                Parent root = loader.load();
                OriginalImageController controller = loader.getController();

                Scene scene = new Scene(root,image.getWidth(),image.getHeight());
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
                controller.initialize(image);
            }
        } catch (IOException e){
            Main.log.warning("Failed to load FXML file: " + e.getMessage() );
        }

    }

    @FXML
    public void initialize(){
        ObservableList<ThumbnailSize> thumbnailSizes = FXCollections.observableList(Arrays.stream(ThumbnailSize.values()).toList());
        sizeSelect.setItems(thumbnailSizes);
        sizeSelect.setValue(ThumbnailSize.SMALL);
    }

}
