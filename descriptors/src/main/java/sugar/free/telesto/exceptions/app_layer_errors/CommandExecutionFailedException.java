package sugar.free.telesto.exceptions.app_layer_errors;

public class CommandExecutionFailedException extends AppLayerErrorException {

    private static final long serialVersionUID = 1;

    public CommandExecutionFailedException(int errorCode) {
        super(errorCode);
    }
}
