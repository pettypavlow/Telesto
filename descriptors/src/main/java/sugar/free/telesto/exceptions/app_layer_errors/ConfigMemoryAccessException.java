package sugar.free.telesto.exceptions.app_layer_errors;

public class ConfigMemoryAccessException extends AppLayerErrorException {

    private static final long serialVersionUID = 1;

    public ConfigMemoryAccessException(int errorCode) {
        super(errorCode);
    }
}
