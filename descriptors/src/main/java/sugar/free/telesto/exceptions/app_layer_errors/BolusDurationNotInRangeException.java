package sugar.free.telesto.exceptions.app_layer_errors;

public class BolusDurationNotInRangeException extends AppLayerErrorException {

    private static final long serialVersionUID = 1;

    public BolusDurationNotInRangeException(int errorCode) {
        super(errorCode);
    }
}
