package sugar.free.telesto.exceptions.app_layer_errors;

public class StepCountOutOfRangeException extends AppLayerErrorException {

    private static final long serialVersionUID = 1;

    public StepCountOutOfRangeException(int errorCode) {
        super(errorCode);
    }
}
