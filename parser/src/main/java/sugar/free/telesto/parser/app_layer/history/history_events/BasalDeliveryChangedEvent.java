package sugar.free.telesto.parser.app_layer.history.history_events;

import sugar.free.telesto.parser.utils.ByteBuf;

public class BasalDeliveryChangedEvent extends HistoryEvent {

    private double oldBasalRate;
    private double newBasalRate;

    @Override
    public void parse(ByteBuf byteBuf) {
        oldBasalRate = byteBuf.readUInt32Decimal1000();
        newBasalRate = byteBuf.readUInt32Decimal1000();
    }

    public double getOldBasalRate() {
        return oldBasalRate;
    }

    public double getNewBasalRate() {
        return newBasalRate;
    }
}
