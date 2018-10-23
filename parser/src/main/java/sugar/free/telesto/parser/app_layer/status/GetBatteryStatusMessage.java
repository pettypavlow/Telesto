package sugar.free.telesto.parser.app_layer.status;

import sugar.free.telesto.descriptors.BatteryStatus;
import sugar.free.telesto.parser.app_layer.AppLayerMessage;
import sugar.free.telesto.parser.app_layer.MessagePriority;
import sugar.free.telesto.parser.app_layer.Service;
import sugar.free.telesto.parser.ids.BatteryTypeIDs;
import sugar.free.telesto.parser.ids.SymbolStatusIDs;
import sugar.free.telesto.parser.utils.ByteBuf;

public class GetBatteryStatusMessage extends AppLayerMessage {

    private BatteryStatus batteryStatus;

    public GetBatteryStatusMessage() {
        super(MessagePriority.NORMAL, false, false, Service.STATUS);
    }

    @Override
    protected void parse(ByteBuf byteBuf) {
        batteryStatus = new BatteryStatus();
        batteryStatus.setBatteryType(BatteryTypeIDs.IDS.getA(byteBuf.readUInt16LE()));
        batteryStatus.setBatteryAmount(byteBuf.readUInt16LE());
        batteryStatus.setSymbolStatus(SymbolStatusIDs.IDS.getA(byteBuf.readUInt16LE()));
    }

    public BatteryStatus getBatteryStatus() {
        return this.batteryStatus;
    }
}
