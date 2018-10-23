package sugar.free.telesto.exceptions.app_layer_errors;

public class ImplausiblePortionLengthValueException extends AppLayerErrorException {

    private static final long serialVersionUID = 1;

    public ImplausiblePortionLengthValueException(int errorCode) {
        super(errorCode);
    }
}
