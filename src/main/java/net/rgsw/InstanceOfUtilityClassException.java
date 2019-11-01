package net.rgsw;

public class InstanceOfUtilityClassException extends RuntimeException {
    private final String msg;

    public InstanceOfUtilityClassException() {
        msg = String.format( "Instance of utility class %s", getStackTrace()[ 0 ].getClassName() );
    }

    public InstanceOfUtilityClassException( String message ) {
        super( message );
        msg = message;
    }

    @Override
    public String getMessage() {
        return msg;
    }
}
