package pl.edu.agh.to2.backend.model;

import java.io.Serializable;
public class GetResponse implements Serializable {
    private String response;
    public GetResponse(String message){
        this.response = message;
    }

    public String getResponse() {
        return response;
    }
}
