package sugar.free.telesto.exceptions.app_layer_errors;

public class CustomBolusNotConfiguredException extends AppLayerErrorException {

    private static final long serialVersionUID = 1;

    public CustomBolusNotConfiguredException(int errorCode) {
        super(errorCode);
    }
}
