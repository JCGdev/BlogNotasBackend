package by.daw.api.db.exceptions;

public class UserDoesNotExistException extends RuntimeException{

    public UserDoesNotExistException(){
        super();
    }
    public UserDoesNotExistException(String msg) {
        super(msg);
    }

}
