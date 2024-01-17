package pl.edu.agh.to2.rest.image.requests;

import java.net.URI;
import java.net.http.HttpRequest;

public class GetTotalImagesInDirectoryRequest {
    private final String path;

    public GetTotalImagesInDirectoryRequest(String path){
        this.path = path;
    }

    public HttpRequest build(){
        return HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/image/total?path=" + path))
                .header("Content-Type", "application/json")
                .GET()
                .build();
    }
}
