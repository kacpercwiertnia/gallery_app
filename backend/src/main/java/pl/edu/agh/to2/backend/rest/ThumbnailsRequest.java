package pl.edu.agh.to2.backend.rest;

import pl.edu.agh.to2.backend.thumbnail.ThumbnailSize;

import java.util.Objects;

public record ThumbnailsRequest(String path, ThumbnailSize size, Integer page, Integer offset) { //TODO: check if passes with integer
    public ThumbnailsRequest {
        Objects.requireNonNull(path, "path not given");
        Objects.requireNonNull(size, "size not given");
        Objects.requireNonNull(page, "page not given");
        Objects.requireNonNull(offset, "page size not given");
    }
}
