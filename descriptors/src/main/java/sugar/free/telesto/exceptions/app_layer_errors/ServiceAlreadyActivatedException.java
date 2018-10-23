package sugar.free.telesto.exceptions.app_layer_errors;

public class ServiceAlreadyActivatedException extends AppLayerErrorException {

    private static final long serialVersionUID = 1;

    public ServiceAlreadyActivatedException(int errorCode) {
        super(errorCode);
    }
}
