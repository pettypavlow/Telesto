package sugar.free.telesto.exceptions.app_layer_errors;

public class ReadingHistoryAlreadyStartedException extends AppLayerErrorException {

    private static final long serialVersionUID = 1;

    public ReadingHistoryAlreadyStartedException(int errorCode) {
        super(errorCode);
    }
}
