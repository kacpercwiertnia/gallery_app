package pl.edu.agh.to2.backend.rest;

import java.util.Objects;

public record SendImagesRequest(String[] images) {
    public SendImagesRequest{
        Objects.requireNonNull(images, "list of images cannot be null");
    }
}
