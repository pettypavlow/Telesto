package sugar.free.telesto.exceptions.app_layer_errors;

import sugar.free.telesto.exceptions.AppLayerException;

public abstract class AppLayerErrorException extends AppLayerException {

    private static final long serialVersionUID = 1;

    private int errorCode;

    public AppLayerErrorException(int errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        return "Error code: " + errorCode;
    }
}
