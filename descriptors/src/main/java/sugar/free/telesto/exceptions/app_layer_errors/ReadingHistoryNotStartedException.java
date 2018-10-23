package sugar.free.telesto.exceptions.app_layer_errors;

public class ReadingHistoryNotStartedException extends AppLayerErrorException {

    private static final long serialVersionUID = 1;

    public ReadingHistoryNotStartedException(int errorCode) {
        super(errorCode);
    }
}
