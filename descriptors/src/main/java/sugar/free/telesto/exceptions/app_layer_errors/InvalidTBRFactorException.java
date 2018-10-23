package sugar.free.telesto.exceptions.app_layer_errors;

public class InvalidTBRFactorException extends AppLayerErrorException {

    private static final long serialVersionUID = 1;

    public InvalidTBRFactorException(int errorCode) {
        super(errorCode);
    }
}
