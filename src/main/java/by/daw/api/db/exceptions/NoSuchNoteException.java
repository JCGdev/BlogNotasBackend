package by.daw.api.db.exceptions;

public class NoSuchNoteException extends RuntimeException{
    public NoSuchNoteException() {
    }

    public NoSuchNoteException(String message) {
        super(message);
    }

    public NoSuchNoteException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchNoteException(Throwable cause) {
        super(cause);
    }

    public NoSuchNoteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
