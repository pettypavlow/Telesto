package sugar.free.telesto.exceptions.app_layer_errors;

public class InvalidConfigBlockLengthException extends AppLayerErrorException {

    private static final long serialVersionUID = 1;

    public InvalidConfigBlockLengthException(int errorCode) {
        super(errorCode);
    }
}
