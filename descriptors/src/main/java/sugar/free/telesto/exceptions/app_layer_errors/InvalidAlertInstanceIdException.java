package sugar.free.telesto.exceptions.app_layer_errors;

public class InvalidAlertInstanceIdException extends AppLayerErrorException {

    private static final long serialVersionUID = 1;

    public InvalidAlertInstanceIdException(int errorCode) {
        super(errorCode);
    }
}
