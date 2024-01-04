package pl.edu.agh.to2.thumbnails;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CashedThumbnails {
    private final Map<Integer, ImageView> smallThumbnails;
    private final Map<Integer,ImageView> mediumThumbnails;
    private final Map<Integer,ImageView> largeThumbnails;
    private final List<Integer> waitingSmallImagesIds;
    private final List<Integer> waitingMediumImagesIds;
    private final List<Integer> waitingLargeImagesIds;

    public CashedThumbnails(){
        this.smallThumbnails = new HashMap<>();
        this.mediumThumbnails = new HashMap<>();
        this.largeThumbnails = new HashMap<>();
        this.waitingSmallImagesIds = new ArrayList<>();
        this.waitingMediumImagesIds = new ArrayList<>();
        this.waitingLargeImagesIds = new ArrayList<>();
    }

    public Map<Integer, ImageView> getThumbnails(ThumbnailSize size){
        return switch(size){
            case SMALL -> smallThumbnails;
            case MEDIUM -> mediumThumbnails;
            case LARGE -> largeThumbnails;
        };
    }

    public List<Integer> getWaitingImagesIds(ThumbnailSize size){
        return switch(size){
            case SMALL -> waitingSmallImagesIds;
            case MEDIUM -> waitingMediumImagesIds;
            case LARGE -> waitingLargeImagesIds;
            default -> null;
        };
    }
}
