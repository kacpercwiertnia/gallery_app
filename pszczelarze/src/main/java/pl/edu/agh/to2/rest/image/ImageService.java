package pl.edu.agh.to2.rest.image;

import org.json.JSONArray;
import org.json.JSONObject;
import pl.edu.agh.to2.rest.AbstractService;
import pl.edu.agh.to2.rest.StatusNotOkException;
import pl.edu.agh.to2.rest.image.requests.GetImageRequest;
import pl.edu.agh.to2.rest.image.requests.GetTotalImagesInDirectoryRequest;
import pl.edu.agh.to2.rest.image.requests.ImageIdsRequest;
import pl.edu.agh.to2.rest.image.requests.PostImageRequest;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.List;

public class ImageService extends AbstractService {
    public static String getImage (int id) throws StatusNotOkException, RuntimeException{
        var request = new GetImageRequest(id).build();
        try{
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            checkIfOk(response);
            return new JSONObject(response.body()).getString("image");
        }
        catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public static JSONArray getImageIds() throws StatusNotOkException{
        var request = new ImageIdsRequest().build();
        try{
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            checkIfOk(response);
            JSONObject jsonObject = new JSONObject(response.body());
            return (JSONArray) jsonObject.get("imagesIds");
        }
        catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public static void postImage(List<String> images) throws StatusNotOkException{
        var request = new PostImageRequest(images).build();
        try{
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            checkIfOk(response);
        }
        catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public static Integer getTotalImagesInDirectory(String path) throws StatusNotOkException{
        var request = new GetTotalImagesInDirectoryRequest(path).build();
        try{
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            checkIfOk(response);
            var jsonObject = new JSONObject(response.body());
            return (Integer) jsonObject.get("total");
        }catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }
}
