package pl.edu.agh.to2.rest.image.requests;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.List;
import java.util.Map;

public class PostImageRequest {
    private final Map<String, List<String>> images;

    public PostImageRequest(Map<String, List<String>> images){
        this.images = images;
    }
    public HttpRequest build(){
        var body = new StringBuilder();

        for (Map.Entry<String, List<String>> entry: images.entrySet()){
            body.append("{\"path\":\""+entry.getKey()+"\", \"images\": [");
            for (String image: entry.getValue()){
                body.append("\"" + image + "\",");
            }
            body.deleteCharAt(body.length() - 1 );
            body.append("]},");
        }
        body.deleteCharAt(body.length()-1);

        return HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/image"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"images\":["+body.toString()+"]}"))
                .build();
    }

}
