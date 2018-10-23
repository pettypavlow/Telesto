package sugar.free.telesto.parser.app_layer.remote_control;

import sugar.free.telesto.parser.app_layer.AppLayerMessage;
import sugar.free.telesto.parser.app_layer.MessagePriority;
import sugar.free.telesto.parser.app_layer.Service;
import sugar.free.telesto.parser.utils.ByteBuf;

public class CancelBolusMessage extends AppLayerMessage {

    private int bolusID;

    public CancelBolusMessage() {
        super(MessagePriority.HIGHEST, false, true, Service.REMOTE_CONTROL);
    }

    @Override
    protected ByteBuf getData() {
        ByteBuf byteBuf = new ByteBuf(2);
        byteBuf.putUInt16LE(bolusID);
        return byteBuf;
    }

    public void setBolusID(int bolusID) {
        this.bolusID = bolusID;
    }
}
