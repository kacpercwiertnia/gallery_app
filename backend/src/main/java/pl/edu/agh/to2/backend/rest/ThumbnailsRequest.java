package pl.edu.agh.to2.backend.rest;

import pl.edu.agh.to2.backend.thumbnail.ThumbnailSize;

import java.util.List;

public record ThumbnailsRequest(List<Integer> imagesIds, ThumbnailSize size){} //sa adnotacje na walidowanie tego ogarnij jakies size, notnull itd