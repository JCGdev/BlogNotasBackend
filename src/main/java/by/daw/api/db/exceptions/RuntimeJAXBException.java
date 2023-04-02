package by.daw.api.db.exceptions;

public class RuntimeJAXBException extends RuntimeException {
    public RuntimeJAXBException() {
    }

    public RuntimeJAXBException(String message) {
        super(message);
    }

    public RuntimeJAXBException(String message, Throwable cause) {
        super(message, cause);
    }

    public RuntimeJAXBException(Throwable cause) {
        super(cause);
    }

    public RuntimeJAXBException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
