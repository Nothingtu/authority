package connectionPool;

public class ConnectionNotFoundException extends RuntimeException {

    public ConnectionNotFoundException(){}
    public ConnectionNotFoundException(String message){
        super(message);
    }
}
