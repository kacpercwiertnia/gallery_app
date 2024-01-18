package pl.edu.agh.to2.rest.directories.requests;

import pl.edu.agh.to2.rest.BodyBuilder;

import java.net.URI;
import java.net.http.HttpRequest;

public class GetSubdirectoriesRequest {
    private String parentDirectory;

    public GetSubdirectoriesRequest(String parentDirectory){
        this.parentDirectory = parentDirectory;
    }
    public HttpRequest build(){
        var bodyBuilder = new BodyBuilder();

        var body = bodyBuilder
                .openBody()
                .addFirstKeyWithValue("parent", parentDirectory)
                .closeAndGetBody();

        return HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/directories"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
    }
}
