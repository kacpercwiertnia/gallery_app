package pl.edu.agh.to2.rest.image.requests;

import pl.edu.agh.to2.Main;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

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
