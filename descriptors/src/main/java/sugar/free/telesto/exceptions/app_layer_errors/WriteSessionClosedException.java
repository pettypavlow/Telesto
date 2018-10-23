package sugar.free.telesto.exceptions.app_layer_errors;

public class WriteSessionClosedException extends AppLayerErrorException {

    private static final long serialVersionUID = 1;

    public WriteSessionClosedException(int errorCode) {
        super(errorCode);
    }
}
