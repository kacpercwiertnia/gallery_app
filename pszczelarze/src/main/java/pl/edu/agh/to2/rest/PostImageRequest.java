package pl.edu.agh.to2.rest;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class PostImageRequest {
    private List<String> images;
    private HttpRequest request;

    public PostImageRequest(List<String> images){
        this.images = images;
    }
    public void build(){
        var body = new StringBuilder();

        for(String image : images){
            body.append("\"" + image + "\",");
        }
        body.deleteCharAt(body.length()-1);

        this.request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/image"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"images\":["+body.toString()+"]}"))
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
