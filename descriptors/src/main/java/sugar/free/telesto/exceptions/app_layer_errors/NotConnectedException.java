package sugar.free.telesto.exceptions.app_layer_errors;

public class NotConnectedException extends AppLayerErrorException {

    private static final long serialVersionUID = 1;

    public NotConnectedException(int errorCode) {
        super(errorCode);
    }
}
