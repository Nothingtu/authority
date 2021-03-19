package mvc.Exception;

public class RequestNotFoundException  extends RuntimeException{
    public RequestNotFoundException(){}
    public RequestNotFoundException(String message){
        super(message);
    }
}
