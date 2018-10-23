package sugar.free.telesto.parser.app_layer.history.history_events;

import sugar.free.telesto.descriptors.OperatingMode;
import sugar.free.telesto.parser.ids.OperatingModeIDs;
import sugar.free.telesto.parser.utils.ByteBuf;

public class OperatingModeChangedEvent extends HistoryEvent {

    private OperatingMode oldValue;
    private OperatingMode newValue;

    @Override
    public void parse(ByteBuf byteBuf) {
        oldValue = OperatingModeIDs.IDS.getA(byteBuf.readUInt16LE());
        newValue = OperatingModeIDs.IDS.getA(byteBuf.readUInt16LE());
    }


    public OperatingMode getOldValue() {
        return oldValue;
    }

    public OperatingMode getNewValue() {
        return newValue;
    }
}
