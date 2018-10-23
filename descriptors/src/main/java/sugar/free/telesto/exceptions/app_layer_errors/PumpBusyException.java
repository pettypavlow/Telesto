package sugar.free.telesto.exceptions.app_layer_errors;

public class PumpBusyException extends AppLayerErrorException {

    private static final long serialVersionUID = 1;

    public PumpBusyException(int errorCode) {
        super(errorCode);
    }
}
