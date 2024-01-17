package pl.edu.agh.to2.rest.image.requests;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.List;

public class PostImageRequest {
    private final List<String> images;

    public PostImageRequest(List<String> images){
        this.images = images;
    }
    public HttpRequest build(){
        var body = new StringBuilder();

        for(String image : images){
            body.append("\"" + image + "\",");
        }
        body.deleteCharAt(body.length()-1);

        return HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/image"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"images\":["+body.toString()+"]}"))
                .build();
    }

}
