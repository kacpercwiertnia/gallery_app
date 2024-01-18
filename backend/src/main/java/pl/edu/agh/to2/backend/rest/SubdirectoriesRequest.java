package pl.edu.agh.to2.backend.rest;

import java.util.Objects;

public record SubdirectoriesRequest(String parent) {
    public SubdirectoriesRequest{
        Objects.requireNonNull(parent, "parent not given");
    }
}
