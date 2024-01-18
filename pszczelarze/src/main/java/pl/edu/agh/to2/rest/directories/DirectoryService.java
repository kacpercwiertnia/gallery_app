package pl.edu.agh.to2.rest.directories;

import org.json.JSONObject;
import pl.edu.agh.to2.rest.AbstractService;
import pl.edu.agh.to2.rest.directories.requests.GetSubdirectoriesRequest;
import pl.edu.agh.to2.rest.directories.responses.GetSubdirectoriesResponse;

import java.net.http.HttpResponse;
import java.util.List;

public class DirectoryService extends AbstractService {
    public static List<String> getSubdirectories(String parentDirectory){
        var request = new GetSubdirectoriesRequest(parentDirectory).build();
        try{
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            checkIfOk(response);

            return new JSONObject(response.body()).getJSONArray("subdirectories").toList().stream().map(Object::toString).toList();
        }
        catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }
}
