package sugar.free.telesto.exceptions.app_layer_errors;

public class MaximumNumberOfBolusTypeAlreadyRunningException extends AppLayerErrorException {

    private static final long serialVersionUID = 1;

    public MaximumNumberOfBolusTypeAlreadyRunningException(int errorCode) {
        super(errorCode);
    }
}
