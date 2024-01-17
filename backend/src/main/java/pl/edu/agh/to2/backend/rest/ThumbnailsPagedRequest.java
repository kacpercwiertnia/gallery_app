package pl.edu.agh.to2.backend.rest;

import pl.edu.agh.to2.backend.thumbnail.ThumbnailSize;

public record ThumbnailsPagedRequest(String path, ThumbnailSize size, int page, int offset) { //TODO: constraints if decided to leave it as post
}
