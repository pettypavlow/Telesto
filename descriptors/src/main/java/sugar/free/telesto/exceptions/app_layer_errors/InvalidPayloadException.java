package sugar.free.telesto.exceptions.app_layer_errors;

public class InvalidPayloadException extends AppLayerErrorException {

    private static final long serialVersionUID = 1;

    public InvalidPayloadException(int errorCode) {
        super(errorCode);
    }
}
