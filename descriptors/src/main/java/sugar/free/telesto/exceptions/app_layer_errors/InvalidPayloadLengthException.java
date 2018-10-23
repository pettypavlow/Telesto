package sugar.free.telesto.exceptions.app_layer_errors;

public class InvalidPayloadLengthException extends AppLayerErrorException {

    private static final long serialVersionUID = 1;

    public InvalidPayloadLengthException(int errorCode) {
        super(errorCode);
    }
}
