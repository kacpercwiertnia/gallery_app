package pl.edu.agh.to2.rest;

import pl.edu.agh.to2.Main;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ImageIdsRequest {
    private HttpRequest request;
    public ImageIdsRequest(){

    }
    public void build(){
        this.request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/image"))
                .header("Content-Type", "application/json")
                .GET()
                .build();
    }

    public HttpResponse<String> getResponse() {
        HttpResponse<String> response = null;

        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            Main.log.info(e.getMessage());
        } catch (InterruptedException e) {
            Main.log.info(e.getMessage());
        }

        return response;
    }
}
