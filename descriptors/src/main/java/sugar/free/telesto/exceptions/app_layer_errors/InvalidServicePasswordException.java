package sugar.free.telesto.exceptions.app_layer_errors;

public class InvalidServicePasswordException extends AppLayerErrorException {

    private static final long serialVersionUID = 1;

    public InvalidServicePasswordException(int errorCode) {
        super(errorCode);
    }
}
