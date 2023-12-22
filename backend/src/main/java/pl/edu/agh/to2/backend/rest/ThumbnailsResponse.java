package pl.edu.agh.to2.backend.rest;

import java.util.List;

public record ThumbnailsResponse (List<ThumbnailDto> thumbnails, String message){
}
