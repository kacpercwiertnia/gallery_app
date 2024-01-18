package pl.edu.agh.to2.rest.directories.responses;

import java.util.List;

public record GetSubdirectoriesResponse(String parent, List<String> subdirectories) {
}
