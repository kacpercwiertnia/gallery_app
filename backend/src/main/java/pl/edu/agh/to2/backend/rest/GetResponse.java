package pl.edu.agh.to2.backend.rest;

import java.io.Serializable;
import java.util.List;

public class GetResponse implements Serializable {
    private final String[] images;

    public GetResponse(List<String> encodedImages) {
        this.images = encodedImages.toArray(new String[0]);
    }

    public String[] getResponse() {
        return images;
    }
}
