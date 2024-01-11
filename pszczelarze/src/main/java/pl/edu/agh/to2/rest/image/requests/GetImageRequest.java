package pl.edu.agh.to2.rest.image.requests;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GetImageRequest {
    private int imageId;

    public GetImageRequest(int id){
        this.imageId = id;
    }
    public HttpRequest build(){
        return HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/image/"+this.imageId))
                .header("Content-Type", "application/json")
                .GET()
                .build();
    }
}
