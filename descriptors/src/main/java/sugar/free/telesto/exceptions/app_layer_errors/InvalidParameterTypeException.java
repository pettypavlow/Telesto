package sugar.free.telesto.exceptions.app_layer_errors;

public class InvalidParameterTypeException extends AppLayerErrorException {

    private static final long serialVersionUID = 1;

    public InvalidParameterTypeException(int errorCode) {
        super(errorCode);
    }
}
