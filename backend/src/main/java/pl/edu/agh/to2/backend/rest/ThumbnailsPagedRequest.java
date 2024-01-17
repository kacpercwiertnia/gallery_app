package pl.edu.agh.to2.backend.rest;

import pl.edu.agh.to2.backend.thumbnail.ThumbnailSize;

import java.util.Objects;

public record ThumbnailsPagedRequest(String path, ThumbnailSize size, Integer page, Integer offset) { //TODO: check if passes with integer
    public ThumbnailsPagedRequest{
        Objects.requireNonNull(path, "path not given");
        Objects.requireNonNull(size, "size not given");
        Objects.requireNonNull(page, "page not given");
        Objects.requireNonNull(offset, "page size not given");
    }//TODO: constraints if decided to leave it as post
}
