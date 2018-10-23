package sugar.free.telesto.exceptions.app_layer_errors;

public class ServiceCommandNotAvailableException extends AppLayerErrorException {

    private static final long serialVersionUID = 1;

    public ServiceCommandNotAvailableException(int errorCode) {
        super(errorCode);
    }
}
