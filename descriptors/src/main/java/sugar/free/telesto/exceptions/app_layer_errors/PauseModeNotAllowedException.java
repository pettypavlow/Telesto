package sugar.free.telesto.exceptions.app_layer_errors;

public class PauseModeNotAllowedException extends AppLayerErrorException {

    private static final long serialVersionUID = 1;

    public PauseModeNotAllowedException(int errorCode) {
        super(errorCode);
    }
}
