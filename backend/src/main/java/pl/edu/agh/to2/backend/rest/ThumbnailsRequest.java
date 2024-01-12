package pl.edu.agh.to2.backend.rest;

import pl.edu.agh.to2.backend.thumbnail.ThumbnailSize;

import java.util.List;
import java.util.Objects;

public record ThumbnailsRequest(List<Integer> imagesIds, ThumbnailSize size){
    public ThumbnailsRequest {
        Objects.requireNonNull(imagesIds, "List cannot be null");
        Objects.requireNonNull(size, "Size cannot be null");
    }
}