package pl.edu.agh.to2.rest;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GetImageRequest {
    private int imageId;
    private HttpRequest request;

    public GetImageRequest(int id){
        this.imageId = id;
    }
    public void build(){
        this.request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/image/"+this.imageId))
                .header("Content-Type", "application/json")
                .GET()
                .build();
    }
    public HttpResponse<String> getResponse(){
        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return response;
    }
}
