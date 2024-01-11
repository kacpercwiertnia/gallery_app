package pl.edu.agh.to2.rest.thumbnails.requests;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class ThumbnailsRequest {
    private final List<Integer> ids;
    private final String size;
    public ThumbnailsRequest(List<Integer> ids, String size){
        this.ids = ids;
        this.size = size;
    }

    public HttpRequest build(){
        StringBuilder body = new StringBuilder("{\"imagesIds\":[");
        for(Integer id: ids){
            body.append(id.toString()).append(",");
        }
        body.deleteCharAt(body.length()-1);
        body.append("],\"size\":\"").append(size).append("\"}");

        return HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/thumbnails"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                .build();
    }
}
