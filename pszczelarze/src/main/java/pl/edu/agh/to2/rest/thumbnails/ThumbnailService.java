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
    public static JSONArray getThumbnailsRequest(List<Integer> ids, String size) throws StatusNotOkException{
        try{
            var request = new ThumbnailsRequest(ids, size).build();
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
