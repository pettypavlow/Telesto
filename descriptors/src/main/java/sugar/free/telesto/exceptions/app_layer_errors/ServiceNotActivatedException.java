package sugar.free.telesto.exceptions.app_layer_errors;

public class ServiceNotActivatedException extends AppLayerErrorException {

    private static final long serialVersionUID = 1;

    public ServiceNotActivatedException(int errorCode) {
        super(errorCode);
    }
}
