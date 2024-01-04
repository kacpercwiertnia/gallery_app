package pl.edu.agh.to2.image;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class OriginalImageController{
    @FXML
    private ImageView originalImage;

    public void initialize(Image img){
        originalImage.setImage(img);
    }
}
