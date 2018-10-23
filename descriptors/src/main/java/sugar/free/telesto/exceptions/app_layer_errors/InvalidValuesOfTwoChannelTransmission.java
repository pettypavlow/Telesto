package sugar.free.telesto.exceptions.app_layer_errors;

public class InvalidValuesOfTwoChannelTransmission extends AppLayerErrorException {

    private static final long serialVersionUID = 1;

    public InvalidValuesOfTwoChannelTransmission(int errorCode) {
        super(errorCode);
    }
}
