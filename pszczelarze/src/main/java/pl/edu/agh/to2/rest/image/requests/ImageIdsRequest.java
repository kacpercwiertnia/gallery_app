package pl.edu.agh.to2.rest.image.requests;

import java.net.URI;
import java.net.http.HttpRequest;

public class ImageIdsRequest {
    public ImageIdsRequest(){

    }
    public HttpRequest build(){
        return HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/image"))
                .header("Content-Type", "application/json")
                .GET()
                .build();
    }
}
