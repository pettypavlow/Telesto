package sugar.free.telesto.parser.app_layer.remote_control;

import sugar.free.telesto.parser.app_layer.AppLayerMessage;
import sugar.free.telesto.parser.app_layer.MessagePriority;
import sugar.free.telesto.parser.app_layer.Service;

public class CancelTBRMessage extends AppLayerMessage {
    public CancelTBRMessage() {
        super(MessagePriority.HIGHER, false, false, Service.REMOTE_CONTROL);
    }
}
