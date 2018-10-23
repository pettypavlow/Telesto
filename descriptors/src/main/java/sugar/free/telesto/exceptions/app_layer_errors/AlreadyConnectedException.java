package sugar.free.telesto.exceptions.app_layer_errors;

public class AlreadyConnectedException extends AppLayerErrorException {

    private static final long serialVersionUID = 1;

    public AlreadyConnectedException(int errorCode) {
        super(errorCode);
    }
}
