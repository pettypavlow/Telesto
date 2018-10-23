package sugar.free.telesto.parser.app_layer.history;

import java.util.ArrayList;
import java.util.List;

import sugar.free.telesto.parser.app_layer.AppLayerMessage;
import sugar.free.telesto.parser.app_layer.MessagePriority;
import sugar.free.telesto.parser.app_layer.Service;
import sugar.free.telesto.parser.app_layer.history.history_events.HistoryEvent;
import sugar.free.telesto.parser.utils.ByteBuf;

public class ReadHistoryEventsMessage extends AppLayerMessage {

    private List<HistoryEvent> historyEvents;

    public ReadHistoryEventsMessage() {
        super(MessagePriority.NORMAL, true, false, Service.HISTORY);
    }

    @Override
    protected void parse(ByteBuf byteBuf) throws Exception {
        historyEvents = new ArrayList<>();
        byteBuf.shift(2);
        int frameCount = byteBuf.readUInt16LE();
        for (int i = 0; i < frameCount; i++) {
            int length = byteBuf.readUInt16LE();
            historyEvents.add(HistoryEvent.deserialize(ByteBuf.from(byteBuf.readBytes(length))));
        }
    }

    public List<HistoryEvent> getHistoryEvents() {
        return historyEvents;
    }
}
