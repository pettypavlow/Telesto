package sugar.free.telesto.exceptions.app_layer_errors;

public class NoActiveTBRToChangeException extends AppLayerErrorException {

    private static final long serialVersionUID = 1;

    public NoActiveTBRToChangeException(int errorCode) {
        super(errorCode);
    }
}
