package by.daw.api.db.exceptions;

public class UserAlreadyExistsException extends RuntimeException{
    public UserAlreadyExistsException() {
        super();
    }
    public UserAlreadyExistsException(String msg){
        super(msg);
    }
}
