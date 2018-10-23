package sugar.free.telesto.exceptions.app_layer_errors;

public class ServiceIncompatibleException extends AppLayerErrorException {

    private static final long serialVersionUID = 1;

    public ServiceIncompatibleException(int errorCode) {
        super(errorCode);
    }
}
