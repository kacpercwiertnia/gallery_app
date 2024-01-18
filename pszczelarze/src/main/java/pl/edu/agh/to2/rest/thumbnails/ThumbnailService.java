package pl.edu.agh.to2.rest.thumbnails;

import org.json.JSONArray;
import org.json.JSONObject;
import pl.edu.agh.to2.rest.AbstractService;
import pl.edu.agh.to2.rest.StatusNotOkException;
import pl.edu.agh.to2.rest.thumbnails.requests.ThumbnailsRequest;
import pl.edu.agh.to2.thumbnails.ThumbnailSize;

import java.net.http.HttpResponse;

public class ThumbnailService extends AbstractService {
    public static JSONArray getThumbnailsRequest(String path, ThumbnailSize size, int page, int offset) throws StatusNotOkException{
        try{
            var request = new ThumbnailsRequest(path, size.toString(), page, offset).build();
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            checkIfOk(response);

            JSONObject jsonObject = new JSONObject(response.body());
            return (JSONArray) jsonObject.get("thumbnails");
        }
        catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }
}
