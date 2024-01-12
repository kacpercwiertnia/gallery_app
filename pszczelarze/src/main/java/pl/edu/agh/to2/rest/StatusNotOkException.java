package pl.edu.agh.to2.rest;

public class StatusNotOkException extends Exception{
    public StatusNotOkException (String message){
        super(message);
    }
}
