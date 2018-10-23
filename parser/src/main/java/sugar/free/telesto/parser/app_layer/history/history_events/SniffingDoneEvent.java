package sugar.free.telesto.parser.app_layer.history.history_events;

import sugar.free.telesto.parser.utils.ByteBuf;

public class SniffingDoneEvent extends HistoryEvent {

    private double amount;

    @Override
    public void parse(ByteBuf byteBuf) {
        amount = byteBuf.readUInt16Decimal();
    }

    public double getAmount() {
        return amount;
    }
}
