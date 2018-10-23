package sugar.free.telesto.exceptions.app_layer_errors;

public class NotAllowedToAccessPositionZeroException extends AppLayerErrorException {

    private static final long serialVersionUID = 1;

    public NotAllowedToAccessPositionZeroException(int errorCode) {
        super(errorCode);
    }
}
