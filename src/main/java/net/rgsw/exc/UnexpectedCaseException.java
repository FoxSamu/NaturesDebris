package net.rgsw.exc;

public class UnexpectedCaseException extends RuntimeException {
    public UnexpectedCaseException() {
    }

    public UnexpectedCaseException( String message ) {
        super( message );
    }

    public UnexpectedCaseException( String message, Throwable cause ) {
        super( message, cause );
    }

    public UnexpectedCaseException( Throwable cause ) {
        super( cause );
    }

    public UnexpectedCaseException( String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace ) {
        super( message, cause, enableSuppression, writableStackTrace );
    }
}
