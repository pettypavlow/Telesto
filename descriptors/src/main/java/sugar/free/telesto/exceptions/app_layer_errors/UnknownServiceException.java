package sugar.free.telesto.exceptions.app_layer_errors;

public class UnknownServiceException extends AppLayerErrorException {

    private static final long serialVersionUID = 1;

    public UnknownServiceException(int errorCode) {
        super(errorCode);
    }
}
