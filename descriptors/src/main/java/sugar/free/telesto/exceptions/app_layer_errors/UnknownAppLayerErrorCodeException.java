package sugar.free.telesto.exceptions.app_layer_errors;

public class UnknownAppLayerErrorCodeException extends AppLayerErrorException {

    private static final long serialVersionUID = 1;

    public UnknownAppLayerErrorCodeException(int errorCode) {
        super(errorCode);
    }
}
