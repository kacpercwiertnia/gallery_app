package pl.edu.agh.to2.rest.thumbnails.requests;

import pl.edu.agh.to2.rest.BodyBuilder;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.List;

public class ThumbnailsRequest {
    private final String path;
    private final String size;
    private final int page;
    private final int offset;
    public ThumbnailsRequest(String path, String size, int page, int offset){
        this.path = path;
        this.size = size;
        this.page = page;
        this.offset = offset;
    }

    public HttpRequest build(){
        var bodyBuilder = new BodyBuilder();

        var body = bodyBuilder
                .openBody()
                .addFirstKeyWithValue("path", path)
                .addNextKey("size", size)
                .addNextKey("page", page)
                .addNextKey("offset", offset)
                .closeAndGetBody();

        return HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/thumbnails/paged"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
    }

    private String addQuotes(String text){
        return "\"" + text + "\"";
    }
}
