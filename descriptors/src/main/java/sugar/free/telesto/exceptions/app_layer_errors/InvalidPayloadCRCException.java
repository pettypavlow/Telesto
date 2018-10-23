package sugar.free.telesto.exceptions.app_layer_errors;

public class InvalidPayloadCRCException extends AppLayerErrorException {

    private static final long serialVersionUID = 1;

    public InvalidPayloadCRCException(int errorCode) {
        super(errorCode);
    }
}
