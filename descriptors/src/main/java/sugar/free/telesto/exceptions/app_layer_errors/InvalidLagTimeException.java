package sugar.free.telesto.exceptions.app_layer_errors;

public class InvalidLagTimeException extends AppLayerErrorException {

    private static final long serialVersionUID = 1;

    public InvalidLagTimeException(int errorCode) {
        super(errorCode);
    }
}
