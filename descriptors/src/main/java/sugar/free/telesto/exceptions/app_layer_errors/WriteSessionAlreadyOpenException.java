package sugar.free.telesto.exceptions.app_layer_errors;

public class WriteSessionAlreadyOpenException extends AppLayerErrorException {

    private static final long serialVersionUID = 1;

    public WriteSessionAlreadyOpenException(int errorCode) {
        super(errorCode);
    }
}
