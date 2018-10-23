package sugar.free.telesto.exceptions;

public abstract class OrbitalException extends Exception {

    private static final long serialVersionUID = 1;

    public OrbitalException() {
        super();
    }

    public OrbitalException(String message) {
        super(message);
    }
}
