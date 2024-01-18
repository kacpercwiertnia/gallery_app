package pl.edu.agh.to2.gallery;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.JSONObject;
import pl.edu.agh.to2.Main;
import pl.edu.agh.to2.directories.ZipHandler;
import pl.edu.agh.to2.image.OriginalImageController;
import pl.edu.agh.to2.rest.StatusNotOkException;
import pl.edu.agh.to2.rest.directories.DirectoryService;
import pl.edu.agh.to2.rest.image.ImageService;
import pl.edu.agh.to2.rest.thumbnails.ThumbnailService;
import pl.edu.agh.to2.thumbnails.ThumbnailSize;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GalleryControler {
    @FXML
    private GridPane thumbnailGrid;
    @FXML
    private ComboBox<ThumbnailSize> sizeSelect;
    @FXML
    private Label uploadImagesLabel;
    @FXML
    private Button prevPage;
    @FXML
    private Button nextPage;
    @FXML
    private Label pageNumber;
    private final Map<String, List<String>> uploadedImages;
    @FXML
    private HBox dirTree;
    private String placeholderUrl = "placeholder_small.gif";
    private final Thread scheduler;
    private int thumbnailsPerRow = 10;
    private final ZipHandler zipHandler;

    //paging stuff
    private int currentPage = 0;
    private String currentPath = "/";
    private final List<ImageView> freePlaceholders;
    private final List<Integer> currentImages;
    private int currentImagesOnPage = 0;

    public GalleryControler() {
        this.scheduler = new PollingScheduler(this);
        this.scheduler.start();
        this.uploadedImages = new HashMap<>();
        this.freePlaceholders = new ArrayList<>();
        this.currentImages = new ArrayList<>();
        this.zipHandler = new ZipHandler();
    }

    @FXML
    public void initialize() {
        ObservableList<ThumbnailSize> thumbnailSizes = FXCollections.observableList(Arrays.stream(ThumbnailSize.values()).toList());
        sizeSelect.setItems(thumbnailSizes);
        sizeSelect.setValue(ThumbnailSize.SMALL);
        setPageChangeComponents();
        buildDirectoryTree();
    }

    @FXML
    public void uploadImageButtonClicked(ActionEvent actionEvent) {
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Images and zip", "*.jpeg", "*.jpg", "*.png", "*.zip");
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(filter);
        File file = fileChooser.showOpenDialog(null);
        if (file == null) return;
        try {
            if (zipHandler.checkIfZip(file)) {
                Map<String, List<String>> imageMap = zipHandler.getImagesFromZip(file);

                for (Map.Entry<String, List<String>> entry : imageMap.entrySet()) {
                    String fullPath = currentPath;
                    if(!entry.getKey().isEmpty()){
                        fullPath = currentPath.equals("/") ? "/" + entry.getKey() : currentPath + "/" + entry.getKey();
                        fullPath =fullPath.replaceAll(" ","_");
                    }
                    if (uploadedImages.containsKey(fullPath)) {
                        uploadedImages.get(fullPath).addAll(entry.getValue());
                    } else {
                        uploadedImages.put(fullPath, entry.getValue());
                    }
                }
            } else {
                byte[] fileContent = Files.readAllBytes(file.toPath());
                String stringImage = Base64.getEncoder().encodeToString(fileContent);
                List<String> images = uploadedImages.getOrDefault(currentPath, new ArrayList<>());
                images.add(stringImage);
                uploadedImages.put(currentPath,images);
            }
            uploadImagesLabel.setText("Wybrane obrazki: " + uploadedImages.values().stream().mapToInt(List::size).sum());
            setPageChangeComponents();
        } catch (IOException e) {
            Main.log.warning("Failed to load images: " + e.getMessage());
        }
    }

    @FXML
    public void thumbnailSizeChanged(ActionEvent event) {
        currentPage = 0;
        clearImages();

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
        setPageChangeComponents();
    }

    @FXML
    public void clearUploadedImages(ActionEvent actionEvent) {
        uploadedImages.clear();
        uploadImagesLabel.setText("");
    }

    @FXML
    public void goToPrevPage(ActionEvent actionEvent) {

        currentPage -= 1;
        setPageChangeComponents();
        clearImages();
        refreshThumbnailsLists();
    }

    @FXML
    public void goToNextPage(ActionEvent actionEvent) {
        currentPage += 1;
        setPageChangeComponents();
        clearImages();
        refreshThumbnailsLists();
    }

    public void sendUploadedImages(ActionEvent actionEvent) {
        if (uploadedImages.isEmpty()) return;
        try {
            ImageService.postImage(uploadedImages);
            buildDirectoryTree();
        } catch (StatusNotOkException ex) {
            Main.log.warning("Request for posting an image failed. Reason: " + ex.getMessage());
        }
        uploadedImages.clear();
        uploadImagesLabel.setText("");
    }

    public void refreshThumbnailsLists() {
        try {
            var total = ImageService.getTotalImagesInDirectory(currentPath);

            var totalForPage = getTotalForCurrentPage(total);

            if (totalForPage != currentImagesOnPage) {
                createPlaceholders(currentImagesOnPage, totalForPage); //assumption that totalForPage is always smaller than currentImagesOnPage
                currentImagesOnPage = totalForPage;
            }

            if (currentImages.size() == currentImagesOnPage) {
                return;
            }

            setPageChangeComponents();

            var thumbnails = ThumbnailService.getThumbnailsRequest(currentPath,
                    sizeSelect.getValue(),
                    currentPage,
                    getPageSize());

            for (int i = 0; i < thumbnails.length(); i++) {
                JSONObject thumbnail = thumbnails.getJSONObject(i);
                int id = thumbnail.getInt("imageId");

                if (currentImages.contains(id)) {
                    continue;
                }

                String image = thumbnail.getString("basedImage");
                boolean status = thumbnail.getBoolean("isCorrect");

                if (status) {
                    InputStream is = Base64.getDecoder().wrap(new ByteArrayInputStream(image.getBytes()));
                    ImageView imageView = freePlaceholders.remove(0);
                    imageView.setImage(new Image(is));
                    imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                        seeOriginalImage(event, id);
                    });
                    currentImages.add(id);
                } else {
                    File file2 = new File("src/main/resources/images/failed_" + sizeSelect.getValue() + ".png");
                    ImageView placeholder = freePlaceholders.remove(0);
                    placeholder.setImage(new Image(file2.toURI().toString()));
                    currentImages.add(id);
                }
            }
        } catch (StatusNotOkException ex) {
            Main.log.warning("Request for refreshing thumbnail list failed. Reason: " + ex.getMessage());
        }
    }

    private int getTotalForCurrentPage(int total) {
        var pageSize = getPageSize();
        return Math.min(total - pageSize * currentPage, getPageSize());
    }

    private void createPlaceholders(int start, int end) {
        for (int i = start; i < end; i++) {
            File file = new File("src/main/resources/images/" + placeholderUrl);
            Image placeholderImage = new Image(file.toURI().toString());
            ImageView imageView = new ImageView(placeholderImage);
            freePlaceholders.add(imageView);
            thumbnailGrid.add(imageView, i % thumbnailsPerRow, i / thumbnailsPerRow);
        }
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

    private boolean checkIfNextPageButtonVisible() {
        try {
            var total = ImageService.getTotalImagesInDirectory(currentPath);
            return total > (currentPage + 1) * getPageSize();
        } catch (StatusNotOkException ex) {
            Main.log.info(ex.getMessage());
            return false;
        }
    }

    private void setPageChangeComponents() {
        nextPage.setVisible(checkIfNextPageButtonVisible());
        pageNumber.setText("Strona " + (currentPage + 1));
        prevPage.setVisible(!(currentPage == 0));
    }

    private void clearImages() {
        currentImages.clear();
        currentImagesOnPage = 0;
        freePlaceholders.clear();
        thumbnailGrid.getChildren().clear();
        thumbnailGrid.getStyleClass().clear();
        thumbnailGrid.getStyleClass().add(sizeSelect.getValue().toString());
    }

    private int getPageSize() {
        return switch (sizeSelect.getValue()) {
            case SMALL -> 100;
            case MEDIUM -> 16;
            case LARGE -> 9;
        };
    }

    private void buildDirectoryTree(){
        var subdirectories = DirectoryService.getSubdirectories(currentPath);
        dirTree.getChildren().clear();

        var parent = currentPath.substring(0, currentPath.lastIndexOf("/")+1);

        if(!currentPath.equals("/")){
            if(parent.equals("/"))
                dirTree.getChildren().add(buildDirectory(parent, true));
            else
                dirTree.getChildren().add(buildDirectory(parent.substring(0, parent.length()-1), true));
        }

        subdirectories.forEach(subdirectory -> {
            dirTree.getChildren().add(buildDirectory(subdirectory, false));
        });
    }

    private VBox buildDirectory(String subdirectoryPath, boolean isPrev){
        VBox dir = new VBox();
        dir.getStyleClass().add("dir");
        dir.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleDirectoryEnter(event, subdirectoryPath));

        ImageView imageView = new ImageView();
        imageView.getStyleClass().add("dirImage");

        File file = new File("src/main/resources/images/directory.png");
        Image image = new Image(file.toURI().toString());

        var subdirectoryName = "..";

        if(!isPrev)
            subdirectoryName = subdirectoryPath.substring(subdirectoryPath.lastIndexOf('/'));

        Label label = new Label(subdirectoryName);

        imageView.setImage(image);
        dir.getChildren().addAll(imageView, label);

        return dir;
    }

    private void handleDirectoryEnter(MouseEvent event, String directoryPath){
        currentPath = directoryPath;
        currentPage = 0;
        clearImages();
        refreshThumbnailsLists();
        setPageChangeComponents();
        buildDirectoryTree();
    }
}
