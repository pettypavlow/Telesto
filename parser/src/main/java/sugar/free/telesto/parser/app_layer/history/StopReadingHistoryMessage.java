package sugar.free.telesto.parser.app_layer.history;

import sugar.free.telesto.parser.app_layer.AppLayerMessage;
import sugar.free.telesto.parser.app_layer.MessagePriority;
import sugar.free.telesto.parser.app_layer.Service;

public class StopReadingHistoryMessage extends AppLayerMessage {

    public StopReadingHistoryMessage() {
        super(MessagePriority.NORMAL, false, false, Service.HISTORY);
    }
}
