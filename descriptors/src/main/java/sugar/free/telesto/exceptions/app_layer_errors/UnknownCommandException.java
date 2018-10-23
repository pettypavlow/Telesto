package sugar.free.telesto.exceptions.app_layer_errors;

public class UnknownCommandException extends AppLayerErrorException {

    private static final long serialVersionUID = 1;

    public UnknownCommandException(int errorCode) {
        super(errorCode);
    }
}
