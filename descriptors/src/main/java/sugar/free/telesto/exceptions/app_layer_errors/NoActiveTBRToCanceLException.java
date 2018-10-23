package sugar.free.telesto.exceptions.app_layer_errors;

public class NoActiveTBRToCanceLException extends AppLayerErrorException {

    private static final long serialVersionUID = 1;

    public NoActiveTBRToCanceLException(int errorCode) {
        super(errorCode);
    }
}
