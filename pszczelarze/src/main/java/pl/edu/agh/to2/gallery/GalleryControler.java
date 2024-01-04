package pl.edu.agh.to2.gallery;

import javafx.fxml.FXML;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
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
import java.util.Base64;
import java.util.List;
import java.util.Map;

public class GalleryControler {
    @FXML
    private GridPane thumbnailGrid;
    @FXML
    private ChoiceBox sizeSelect;
    private final CashedThumbnails thumbnails;
    private Map<Integer,ImageView> selectedThumbnails;
    private List<Integer> waitingIds;
    private int numOfImages = 0;
    private String placeholderUrl = "placeholder_small.gif";
    private Thread scheduler;

    public GalleryControler(){
        this.scheduler = new PollingScheduler(this);
        this.scheduler.start();
        this.thumbnails = new CashedThumbnails();
        this.selectedThumbnails = thumbnails.getThumbnails(ThumbnailSize.SMALL);
        this.waitingIds = thumbnails.getWaitingImagesIds(ThumbnailSize.SMALL);
    }

    @FXML
    public void uploadImageButtonClicked(ActionEvent actionEvent){
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        String size = sizeSelect.getValue().toString();

        try {
            byte[] fileContent = Files.readAllBytes(file.toPath());
            String stringImage = Base64.getEncoder().encodeToString(fileContent);
            PostImageRequest postImageRequest = new PostImageRequest(stringImage);
            postImageRequest.build();

            HttpResponse<String> response = postImageRequest.getResponse();

            if(response != null && response.statusCode() == 200){
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
        switch(sizeSelect.getValue().toString()){
            case "SMALL":
                this.waitingIds = thumbnails.getWaitingImagesIds(ThumbnailSize.SMALL);
                this.selectedThumbnails = thumbnails.getThumbnails(ThumbnailSize.SMALL);
                placeholderUrl = "placeholder_small.gif";
                break;
            case "MEDIUM":
                this.waitingIds = thumbnails.getWaitingImagesIds(ThumbnailSize.MEDIUM);
                this.selectedThumbnails = thumbnails.getThumbnails(ThumbnailSize.MEDIUM);
                placeholderUrl = "placeholder_medium.gif";
                break;
            case "LARGE":
                this.waitingIds = thumbnails.getWaitingImagesIds(ThumbnailSize.LARGE);
                this.selectedThumbnails = thumbnails.getThumbnails(ThumbnailSize.LARGE);
                placeholderUrl = "placeholder_large.gif";
                break;
            default:
                break;
        }
        numOfImages = 0;
        refreshThumbnailsLists();
        redrawThumbnailGrid();
    }

    private void redrawThumbnailGrid(){
        selectedThumbnails.forEach((K,V)->{
            if (!thumbnailGrid.getChildren().contains(V)) {
                thumbnailGrid.getChildren().add(V);
                numOfImages++;
            }
        });
    }

    private void seeOriginalImage(MouseEvent event, int imageId) {
        try{
            GetImageRequest request = new GetImageRequest(imageId);
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
            e.printStackTrace();
            Main.log.warning("Failed to load FXML file." );
        }

    }

}
