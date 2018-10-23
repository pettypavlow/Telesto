package sugar.free.telesto.exceptions.app_layer_errors;

public class WrongStateException extends AppLayerErrorException {

    private static final long serialVersionUID = 1;

    public WrongStateException(int errorCode) {
        super(errorCode);
    }
}
