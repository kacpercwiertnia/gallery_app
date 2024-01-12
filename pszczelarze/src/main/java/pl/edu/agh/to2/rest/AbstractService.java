package pl.edu.agh.to2.rest;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;

public abstract class AbstractService {
    protected static final HttpClient client = HttpClient.newHttpClient();

    protected static <T> void checkIfOk(HttpResponse<T> response) throws StatusNotOkException{
        if (response.statusCode() != 200){
            throw new StatusNotOkException(String.valueOf(response.statusCode()));
        }
    }
}
