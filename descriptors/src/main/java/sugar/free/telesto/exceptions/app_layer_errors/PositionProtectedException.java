package sugar.free.telesto.exceptions.app_layer_errors;

public class PositionProtectedException extends AppLayerErrorException {

    private static final long serialVersionUID = 1;

    public PositionProtectedException(int errorCode) {
        super(errorCode);
    }
}
