package sugar.free.telesto.parser.app_layer.remote_control;

import sugar.free.telesto.descriptors.OperatingMode;
import sugar.free.telesto.parser.app_layer.AppLayerMessage;
import sugar.free.telesto.parser.app_layer.MessagePriority;
import sugar.free.telesto.parser.app_layer.Service;
import sugar.free.telesto.parser.ids.OperatingModeIDs;
import sugar.free.telesto.parser.utils.ByteBuf;

public class SetOperatingModeMessage extends AppLayerMessage {

    private OperatingMode operatingMode;

    public SetOperatingModeMessage() {
        super(MessagePriority.HIGHEST, false, true, Service.REMOTE_CONTROL);
    }

    @Override
    protected ByteBuf getData() {
        ByteBuf byteBuf = new ByteBuf(2);
        byteBuf.putUInt16LE(OperatingModeIDs.IDS.getB(operatingMode));
        return byteBuf;
    }

    public void setOperatingMode(OperatingMode operatingMode) {
        this.operatingMode = operatingMode;
    }
}
