package sugar.free.telesto.exceptions.app_layer_errors;

public class BolusTypeAndParameterMismatchException extends AppLayerErrorException {

    private static final long serialVersionUID = 1;

    public BolusTypeAndParameterMismatchException(int errorCode) {
        super(errorCode);
    }
}
