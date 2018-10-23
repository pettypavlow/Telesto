package sugar.free.telesto.parser.app_layer.remote_control;

import sugar.free.telesto.parser.app_layer.AppLayerMessage;
import sugar.free.telesto.parser.app_layer.MessagePriority;
import sugar.free.telesto.parser.app_layer.Service;
import sugar.free.telesto.parser.utils.ByteBuf;

public class ConfirmAlertMessage extends AppLayerMessage {

    private int alertID;

    public ConfirmAlertMessage() {
        super(MessagePriority.NORMAL, false, true, Service.REMOTE_CONTROL);
    }

    @Override
    protected ByteBuf getData() {
        ByteBuf byteBuf = new ByteBuf(2);
        byteBuf.putUInt16LE(alertID);
        return byteBuf;
    }

    public void setAlertID(int alertID) {
        this.alertID = alertID;
    }
}
