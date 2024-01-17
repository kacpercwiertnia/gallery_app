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
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.JSONObject;
import pl.edu.agh.to2.Main;
import pl.edu.agh.to2.directories.ZipHandler;
import pl.edu.agh.to2.image.OriginalImageController;
import pl.edu.agh.to2.rest.StatusNotOkException;
import pl.edu.agh.to2.rest.image.ImageService;
import pl.edu.agh.to2.rest.thumbnails.ThumbnailService;
import pl.edu.agh.to2.thumbnails.CashedThumbnails;
import pl.edu.agh.to2.thumbnails.ThumbnailSize;

import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
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
    private Map<Integer, ImageView> selectedThumbnails;
    private List<Integer> waitingIds;
    private final List<String> uploadedImages;
    private String placeholderUrl = "placeholder_small.gif";
    private final Thread scheduler;
    private int thumbnailsPerRow = 10;
    private ZipHandler zipHandler;

    public GalleryControler() {
        this.scheduler = new PollingScheduler(this);
        this.scheduler.start();
        this.thumbnails = new CashedThumbnails();
        this.selectedThumbnails = thumbnails.getThumbnails(ThumbnailSize.SMALL);
        this.waitingIds = thumbnails.getWaitingImagesIds(ThumbnailSize.SMALL);
        this.uploadedImages = new ArrayList<>();
        this.zipHandler = new ZipHandler();
    }

    @FXML
    public void initialize() {
        ObservableList<ThumbnailSize> thumbnailSizes = FXCollections.observableList(Arrays.stream(ThumbnailSize.values()).toList());
        sizeSelect.setItems(thumbnailSizes);
        sizeSelect.setValue(ThumbnailSize.SMALL);
    }

    @FXML
    public void uploadImageButtonClicked(ActionEvent actionEvent) {
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Images and zip", "*.jpeg", "*.jpg", "*.png", "*.zip");
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(filter);
        File file = fileChooser.showOpenDialog(null);

        if(zipHandler.checkIfZip(file)){
            try {
                Map<String, List<String>> imageMap = zipHandler.getImagesFromZip(file);

                for (Map.Entry<String, List<String>> entry : imageMap.entrySet()) {
                    uploadedImages.addAll(entry.getValue());
                }

                uploadImagesLabel.setText("Wybrane obrazki: " + uploadedImages.size());
            } catch (IOException e) {
                Main.log.info("Failed to load zip: " + e.getMessage());
            }
        }else if(file != null) {
            try {
                byte[] fileContent = Files.readAllBytes(file.toPath());
                String stringImage = Base64.getEncoder().encodeToString(fileContent);
                uploadedImages.add(stringImage);
                uploadImagesLabel.setText("Wybrane obrazki: " + uploadedImages.size());
            } catch (IOException e) {
                Main.log.info(e.getMessage());
            }
        }
    }

    @FXML
    public void thumbnailSizeChanged(ActionEvent event) {
        thumbnailGrid.getChildren().clear();
        thumbnailGrid.getStyleClass().clear();
        this.waitingIds = thumbnails.getWaitingImagesIds(sizeSelect.getValue());
        this.selectedThumbnails = thumbnails.getThumbnails(sizeSelect.getValue());
        thumbnailGrid.getStyleClass().add(sizeSelect.getValue().toString());

        switch (sizeSelect.getValue()) {
            case SMALL -> {
                placeholderUrl = "placeholder_small.gif";
                thumbnailsPerRow = 10;
            }
            case MEDIUM -> {
                placeholderUrl = "placeholder_medium.gif";
                thumbnailsPerRow = 4;
            }
            case LARGE -> {
                placeholderUrl = "placeholder_large.gif";
                thumbnailsPerRow = 3;
            }
        }

        refreshThumbnailsLists();
        redrawThumbnailGrid();
    }

    @FXML
    public void clearUploadedImages(ActionEvent actionEvent) {
        uploadedImages.clear();
        uploadImagesLabel.setText("");
    }

    public void sendUploadedImages(ActionEvent actionEvent) {
        if (uploadedImages.isEmpty()) return;
        try {
            ImageService.postImage(uploadedImages);
            refreshIdsLists();
        } catch (StatusNotOkException ex) {
            Main.log.warning("Request for posting an image failed. Reason: " + ex.getMessage());
        }
        uploadedImages.clear();
        uploadImagesLabel.setText("");
    }

    public void refreshIdsLists() {
        try {
            var imagesIds = ImageService.getImageIds();

            for (int i = 0; i < imagesIds.length(); i++) {
                int id = imagesIds.getInt(i);

                if (!selectedThumbnails.containsKey(id) && !waitingIds.contains(id)) {
                    waitingIds.add(id);
                    File file2 = new File("src/main/resources/images/" + placeholderUrl);
                    Image image = new Image(file2.toURI().toString());
                    ImageView imageView = new ImageView(image);
                    selectedThumbnails.put(id, imageView);
                    thumbnailGrid.add(selectedThumbnails.get(id), (selectedThumbnails.size() - 1) % thumbnailsPerRow, (selectedThumbnails.size() - 1) / thumbnailsPerRow);
                }
            }
        } catch (StatusNotOkException ex) {
            Main.log.warning("Request for refreshing id list failed. Reason: " + ex.getMessage());
        }

    }

    public void refreshThumbnailsLists() {
        refreshIdsLists();

        if (waitingIds.isEmpty()) return;
        try {
            var thumbnails = ThumbnailService.getThumbnailsRequest(waitingIds, sizeSelect.getValue().toString());

            for (int i = 0; i < thumbnails.length(); i++) {
                JSONObject thumbnail = thumbnails.getJSONObject(i);
                int id = thumbnail.getInt("imageId");
                String image = thumbnail.getString("basedImage");
                boolean status = thumbnail.getBoolean("isCorrect");

                if (status) {
                    waitingIds.remove(Integer.valueOf(id));
                    InputStream is = Base64.getDecoder().wrap(new ByteArrayInputStream(image.getBytes()));
                    ImageView imageView = selectedThumbnails.get(id);
                    imageView.setImage(new Image(is));
                    imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                        seeOriginalImage(event, id);
                    });
                } else {
                    waitingIds.remove(Integer.valueOf(id));
                    File file2 = new File("src/main/resources/images/failed_" + sizeSelect.getValue() + ".png");
                    ImageView placeholder = selectedThumbnails.get(id);
                    placeholder.setImage(new Image(file2.toURI().toString()));
                }
            }
        } catch (StatusNotOkException ex) {
            Main.log.warning("Request for refreshing thumbnail list failed. Reason: " + ex.getMessage());
        }

    }

    private void redrawThumbnailGrid() {
        selectedThumbnails.forEach((K, V) -> {
            if (!thumbnailGrid.getChildren().contains(V)) {
                thumbnailGrid.getChildren().add(V);
            }
        });
    }

    private void seeOriginalImage(MouseEvent event, int imageId) {
        try {
            var encodedImage = ImageService.getImage(imageId);
            Image image;
            byte[] byteImage = Base64.getDecoder().decode(encodedImage);
            try (InputStream is = new BufferedInputStream(new ByteArrayInputStream(byteImage))) {
                image = new Image(is);
            }
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("originalImage.fxml"));
            Parent root = loader.load();
            OriginalImageController controller = loader.getController();

            Scene scene = new Scene(root, image.getWidth(), image.getHeight());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            controller.initialize(image);

        } catch (StatusNotOkException ex) {
            Main.log.warning("Request for getting original message failed, reason: " + ex.getMessage());
        } catch (IOException e) {
            Main.log.warning("Failed to load FXML file: " + e.getMessage());
        }
    }
}
