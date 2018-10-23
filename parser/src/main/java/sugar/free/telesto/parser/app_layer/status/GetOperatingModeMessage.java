package sugar.free.telesto.parser.app_layer.status;

import sugar.free.telesto.descriptors.OperatingMode;
import sugar.free.telesto.parser.app_layer.AppLayerMessage;
import sugar.free.telesto.parser.app_layer.MessagePriority;
import sugar.free.telesto.parser.app_layer.Service;
import sugar.free.telesto.parser.ids.OperatingModeIDs;
import sugar.free.telesto.parser.utils.ByteBuf;

public class GetOperatingModeMessage extends AppLayerMessage {

    private OperatingMode operatingMode;

    public GetOperatingModeMessage() {
        super(MessagePriority.NORMAL, true, false, Service.STATUS);
    }

    @Override
    protected void parse(ByteBuf byteBuf) {
        this.operatingMode = OperatingModeIDs.IDS.getA(byteBuf.readUInt16LE());
    }

    public OperatingMode getOperatingMode() {
        return this.operatingMode;
    }
}
