package sugar.free.telesto.exceptions.app_layer_errors;

public class NotReferencedException extends AppLayerErrorException {

    private static final long serialVersionUID = 1;

    public NotReferencedException(int errorCode) {
        super(errorCode);
    }
}
