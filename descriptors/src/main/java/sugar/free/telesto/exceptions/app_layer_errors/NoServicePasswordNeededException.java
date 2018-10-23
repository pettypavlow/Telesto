package sugar.free.telesto.exceptions.app_layer_errors;

public class NoServicePasswordNeededException extends AppLayerErrorException {

    private static final long serialVersionUID = 1;

    public NoServicePasswordNeededException(int errorCode) {
        super(errorCode);
    }
}
