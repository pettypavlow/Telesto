package sugar.free.telesto.exceptions.app_layer_errors;

public class InvalidDurationPresetException extends AppLayerErrorException {

    private static final long serialVersionUID = 1;

    public InvalidDurationPresetException(int errorCode) {
        super(errorCode);
    }
}
