package pl.edu.agh.to2.rest.thumbnails;

import org.json.JSONArray;
import org.json.JSONObject;
import pl.edu.agh.to2.rest.AbstractService;
import pl.edu.agh.to2.rest.StatusNotOkException;
import pl.edu.agh.to2.rest.thumbnails.requests.ThumbnailsRequest;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.List;

public class ThumbnailService extends AbstractService {
    public static JSONArray getThumbnailsRequest(String path, String size, int page, int offset) throws StatusNotOkException{ //TODO: change size to enum
        try{
            var request = new ThumbnailsRequest(path, size, page, offset).build();
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
