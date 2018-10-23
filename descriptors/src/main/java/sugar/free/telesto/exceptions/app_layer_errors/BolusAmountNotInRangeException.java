package sugar.free.telesto.exceptions.app_layer_errors;

public class BolusAmountNotInRangeException extends AppLayerErrorException {

    private static final long serialVersionUID = 1;

    public BolusAmountNotInRangeException(int errorCode) {
        super(errorCode);
    }
}
