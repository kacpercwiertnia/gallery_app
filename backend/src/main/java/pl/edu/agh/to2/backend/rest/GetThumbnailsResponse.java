package pl.edu.agh.to2.backend.rest;

import java.io.Serializable;
import java.util.List;

public class GetThumbnailsResponse implements Serializable {
    private final int allImages;
    private final String[] images;

    public GetThumbnailsResponse(List<String> encodedImages, int allImages) {
        this.allImages = allImages;
        this.images = encodedImages.toArray(new String[0]);
    }

    public int getAllImages() {
        return allImages;
    }

    public String[] getResponse() {
        return images;
    }
}
