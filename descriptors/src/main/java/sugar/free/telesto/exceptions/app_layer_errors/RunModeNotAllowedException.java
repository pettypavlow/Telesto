package sugar.free.telesto.exceptions.app_layer_errors;

public class RunModeNotAllowedException extends AppLayerErrorException {

    private static final long serialVersionUID = 1;

    public RunModeNotAllowedException(int errorCode) {
        super(errorCode);
    }
}
