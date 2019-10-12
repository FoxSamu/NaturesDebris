package net.rgsw;

public class UselessOperationException extends RuntimeException {
    public UselessOperationException() {
    }

    public UselessOperationException( String message ) {
        super( message );
    }

    public UselessOperationException( String message, Throwable cause ) {
        super( message, cause );
    }

    public UselessOperationException( Throwable cause ) {
        super( cause );
    }

    public UselessOperationException( String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace ) {
        super( message, cause, enableSuppression, writableStackTrace );
    }
}
