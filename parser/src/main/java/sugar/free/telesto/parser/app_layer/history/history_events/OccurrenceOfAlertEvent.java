package sugar.free.telesto.parser.app_layer.history.history_events;

import sugar.free.telesto.descriptors.AlertType;
import sugar.free.telesto.parser.ids.AlertTypeIncrementalIDs;
import sugar.free.telesto.parser.utils.ByteBuf;

public abstract class OccurrenceOfAlertEvent extends HistoryEvent {

    private AlertType alertType;
    private int alertID;

    @Override
    public void parse(ByteBuf byteBuf) {
        alertType = AlertTypeIncrementalIDs.IDS.getA(byteBuf.readUInt16LE());
        alertID = byteBuf.readUInt16LE();
    }

    public AlertType getAlertType() {
        return alertType;
    }

    public int getAlertID() {
        return alertID;
    }
}
